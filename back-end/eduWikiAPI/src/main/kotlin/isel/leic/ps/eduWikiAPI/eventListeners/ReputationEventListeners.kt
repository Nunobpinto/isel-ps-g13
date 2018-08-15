package isel.leic.ps.eduWikiAPI.eventListeners

import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole
import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.eventListeners.events.*
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.ReputationUpdateException
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ReputationEventListeners {

    companion object {
        const val MIN_ALLOWED_POINTS = 1
        const val VOTE_UP_POINTS = 2
        const val VOTE_DOWN_POINTS = - 2
        const val VOTER_UP_POINTS = 1
        const val VOTER_DOWN_POINTS = - 1
        const val APPROVED_RESOURCE_POINTS = 10
        const val REJECTED_RESOURCE_POINTS = - 5
        const val APPROVED_REPORT_POINTS = 5
        const val REJECTED_REPORT_POINTS = - 5
    }

    @Autowired
    lateinit var reputationDAO: ReputationDAO

    @Async
    @Transactional
    @TransactionalEventListener
    fun handleResourceUpdatedEvent(event: ResourceUpdatedEvent) {
        // Log update
        reputationDAO.registerActionLog(
                event.user,
                ActionType.ALTER,
                event.entity,
                event.logId,
                event.timestamp
        )
    }

    @Async
    @Transactional
    @TransactionalEventListener
    fun handleResourceDeletedEvent(event: ResourceDeletedEvent) {
        // Log Deletion
        reputationDAO.registerActionLog(
                event.user,
                ActionType.DELETE,
                event.entity,
                event.logId,
                event.timestamp
        )
    }

    @Async
    @Transactional
    @TransactionalEventListener
    fun handleResourceCreatedEvent(event: ResourceCreatedEvent) {
        // Log creation
        reputationDAO.registerActionLog(
                event.creator,
                ActionType.CREATE,
                event.entity,
                event.logId,
                event.timestamp
        )
    }

    @Async
    @Transactional
    @TransactionalEventListener
    fun handleResourceApprovedEventOnUpdate(event: ResourceApprovedEvent) {

        // Log Approval
        val adminApprovalActionLog = reputationDAO.registerActionLog(
                event.administrator,
                event.adminAction,
                event.approvedEntity,
                event.approvedLogId,
                event.timestamp
        )
        // Log effect of that approval
        val creatorActionlog = reputationDAO.registerActionLog(
                event.creator,
                event.resultingAction,
                event.newEntity,
                event.newLogId,
                event.timestamp
        )

        // Modify creator's reputation
        val pointsGiven: Int = when(event.adminAction) {
            ActionType.REJECT_STAGE -> REJECTED_RESOURCE_POINTS
            ActionType.REJECT_REPORT -> REJECTED_REPORT_POINTS
            ActionType.APPROVE_STAGE -> APPROVED_RESOURCE_POINTS
            ActionType.APPROVE_REPORT -> APPROVED_REPORT_POINTS
            else -> throw ReputationUpdateException("Bad adminAction, it can't be ${event.adminAction}", event)
        }
        changeUserReputation(creatorActionlog.actionId, event.creator, event.administrator, pointsGiven, event)

        // Affect the reputation of users that interacted with this resource
        reputationDAO.getActionLogsByResource(event.approvedEntity, event.approvedLogId)
                .filter { it.actionType == ActionType.VOTE_UP || it.actionType == ActionType.VOTE_DOWN }
                .forEach {
                    changeUserReputation(
                            adminApprovalActionLog.actionId,
                            it.user,
                            event.administrator,
                            if(it.actionType == ActionType.VOTE_DOWN) VOTER_DOWN_POINTS else VOTER_UP_POINTS,
                            event
                    )
                }
    }

    @Async
    @Transactional
    @TransactionalEventListener
    fun handleResourceRejectedEvent(event: ResourceRejectedEvent) {

        // Log Rejection
        val rejectedActionLog = reputationDAO.registerActionLog(
                event.administrator,
                event.action,
                event.rejectedEntity,
                event.rejectedLogId,
                event.timestamp
        )

        // Modify creator's reputation
        val pointsGiven = if(event.action == ActionType.REJECT_REPORT) REJECTED_REPORT_POINTS else REJECTED_RESOURCE_POINTS
        changeUserReputation(rejectedActionLog.actionId, event.creator, event.administrator, pointsGiven, event)

        // Affect the reputation of users that interacted with this resource
        reputationDAO.getActionLogsByResource(event.rejectedEntity, event.rejectedLogId)
                .filter { it.actionType == ActionType.VOTE_UP || it.actionType == ActionType.VOTE_DOWN }
                .forEach {
                    changeUserReputation(
                            rejectedActionLog.actionId,
                            it.user,
                            event.administrator,
                            if(it.actionType == ActionType.VOTE_DOWN) VOTER_UP_POINTS else VOTER_DOWN_POINTS,
                            event
                    )
                }
    }

    @Async
    @Transactional
    @TransactionalEventListener
    fun handleVoteOnResourceEvent(event: VoteOnResourceEvent) {
        //TODO CHECK IF VOTING ON SELF OR ALREADY VOTED

        // Log action
        val actionLog = reputationDAO.registerActionLog(
                event.voter,
                if(event.vote == Vote.Up) ActionType.VOTE_UP else ActionType.VOTE_DOWN,
                event.entity,
                event.logId,
                event.timestamp
        )

        // Change creator's reputation based on vote
        val pointsGiven = if(actionLog.actionType == ActionType.VOTE_UP) VOTE_UP_POINTS else VOTE_DOWN_POINTS
        changeUserReputation(actionLog.actionId, event.owner, event.voter, pointsGiven, event)
    }

    private fun changeUserReputation(actionId: Int, owner: String, givenBy: String, pointsGiven: Int, event: Any) {
        // Get owner's reputation details
        val ownerRepDetails = reputationDAO.getUserReputationDetails(owner)
                .orElseThrow { ReputationUpdateException("Could not get $owner reputation details", event) }

        // Log reputation change
        reputationDAO.registerReputationLog(
                ownerRepDetails.user,
                ownerRepDetails.repId,
                pointsGiven,
                givenBy,
                actionId
        )

        val newPoints = ownerRepDetails.points + pointsGiven
        val roles = ReputationRole.values().filter { it.hierarchyLevel > 0 }

        when {
        // If reputation is lower than minimum value, reset it
            newPoints < MIN_ALLOWED_POINTS -> {
                val firstRole = roles.last()
                ownerRepDetails.role = firstRole.name
                ownerRepDetails.points = firstRole.minPoints
            }
        // If reputation has reached peak, stop increasing
            newPoints > roles.first().maxPoints -> {
                val maxRole = roles.first()
                ownerRepDetails.role = maxRole.name
                ownerRepDetails.points = maxRole.maxPoints
            }
        // Resolve new role if that's the case
            else -> {
                val newRole = roles.find { newPoints <= it.maxPoints && newPoints >= it.minPoints }
                        ?: throw ReputationUpdateException("Error finding new role for reputation $newPoints", event)
                ownerRepDetails.role = newRole.name
                ownerRepDetails.points = newPoints
            }
        }
        reputationDAO.updateUserReputation(ownerRepDetails)
    }
}