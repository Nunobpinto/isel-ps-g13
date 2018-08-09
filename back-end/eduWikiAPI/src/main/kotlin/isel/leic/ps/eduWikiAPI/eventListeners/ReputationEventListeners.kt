package isel.leic.ps.eduWikiAPI.eventListeners

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.eventListeners.events.*
import isel.leic.ps.eduWikiAPI.exceptions.ReputationUpdateException
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOJdbi
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

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
    lateinit var jdbi: Jdbi

    @Async
    @EventListener
    fun handleResourceUpdatedEvent(event: ResourceUpdatedEvent) {
        // Log update
        jdbi.useExtension<ReputationDAOJdbi, Exception>(ReputationDAOJdbi::class.java) {
            it.registerActionLog(
                    event.user,
                    ActionType.ALTER,
                    event.entity,
                    event.logId,
                    event.timestamp
            )
        }
    }

    @Async
    @EventListener
    fun handleResourceDeletedEvent(event: ResourceDeletedEvent) {
        jdbi.useExtension<ReputationDAOJdbi, Exception>(ReputationDAOJdbi::class.java) {
            // Log Deletion
            it.registerActionLog(
                    event.user,
                    ActionType.DELETE,
                    event.entity,
                    event.logId,
                    event.timestamp
            )
        }
    }

    @Async
    @EventListener
    fun handleResourceCreatedEvent(event: ResourceCreatedEvent) {
        jdbi.useExtension<ReputationDAOJdbi, Exception>(ReputationDAOJdbi::class.java) {
            // Log creation
            it.registerActionLog(
                    event.creator,
                    ActionType.CREATE,
                    event.entity,
                    event.logId,
                    event.timestamp
            )
        }
    }

    @Async
    @EventListener
    fun handleResourceApprovedEvent(event: ResourceApprovedEvent) {
        jdbi.useTransaction<Exception> {
            val reputationDAO = it.attach(ReputationDAOJdbi::class.java)

            // Log Approval
            val adminApprovalActionLog = reputationDAO.registerActionLog(
                    event.administrator,
                    event.action,
                    event.approvedEntity,
                    event.approvedLogId,
                    event.timestamp
            )
            // Log effect of that approval
            val creatorActionlog = reputationDAO.registerActionLog(
                    event.creator,
                    if(event.action == ActionType.APPROVE_REPORT) ActionType.ALTER else ActionType.CREATE,
                    event.newEntity,
                    event.newLogId,
                    event.timestamp
            )

            // Modify creator's reputation
            val pointsGiven = if(event.action == ActionType.APPROVE_REPORT) APPROVED_REPORT_POINTS else APPROVED_RESOURCE_POINTS
            changeUserReputation(reputationDAO, creatorActionlog.actionId, event.creator, event.administrator, pointsGiven, event)

            // Affect the reputation of users that interacted with this resource
            reputationDAO.getActionLogsByResource(event.approvedEntity, event.approvedLogId)
                    .filter { it.actionType == ActionType.VOTE_UP || it.actionType == ActionType.VOTE_DOWN }
                    .forEach {
                        changeUserReputation(
                                reputationDAO,
                                adminApprovalActionLog.actionId,
                                it.user,
                                event.administrator,
                                if(it.actionType == ActionType.VOTE_DOWN) VOTER_DOWN_POINTS else VOTER_UP_POINTS,
                                event
                        )
                    }
        }
    }

    @Async
    @EventListener
    fun handleResourceRejectedEvent(event: ResourceRejectedEvent) {
        jdbi.useTransaction<Exception> {
            val reputationDAO = it.attach(ReputationDAOJdbi::class.java)

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
            changeUserReputation(reputationDAO, rejectedActionLog.actionId, event.creator, event.administrator, pointsGiven, event)

            // Affect the reputation of users that interacted with this resource
            reputationDAO.getActionLogsByResource(event.rejectedEntity, event.rejectedLogId)
                    .filter { it.actionType == ActionType.VOTE_UP || it.actionType == ActionType.VOTE_DOWN }
                    .forEach {
                        changeUserReputation(
                                reputationDAO,
                                rejectedActionLog.actionId,
                                it.user,
                                event.administrator,
                                if(it.actionType == ActionType.VOTE_DOWN) VOTER_UP_POINTS else VOTER_DOWN_POINTS,
                                event
                        )
                    }
        }
    }

    @Async
    @EventListener
    fun handleVoteOnResourceEvent(event: VoteOnResourceEvent) {
        jdbi.useTransaction<Exception> {
            //TODO CHECK IF VOTING ON SELF OR ALREADY VOTED
            val reputationDAO = it.attach(ReputationDAOJdbi::class.java)

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
            changeUserReputation(reputationDAO, actionLog.actionId, event.owner, event.voter, pointsGiven, event)
        }
    }

    private fun changeUserReputation(reputationDAO: ReputationDAOJdbi, actionId: Int, owner: String, givenBy: String, pointsGiven: Int, event: Any) {
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
        val roles = reputationDAO.getAllReputationRoles().filter { it.hierarchyLevel > 0 }

        when {
        // If reputation is lower than minimum value, reset it
            newPoints < MIN_ALLOWED_POINTS -> {
                val firstRole = roles.last()
                ownerRepDetails.role = firstRole.reputationRoleId
                ownerRepDetails.points = firstRole.minPoints
            }
        // If reputation has reached peak, stop increasing
            newPoints > roles.first().maxPoints -> {
                val maxRole = roles.first()
                ownerRepDetails.role = maxRole.reputationRoleId
                ownerRepDetails.points = maxRole.maxPoints
            }
        // Resolve new role if that's the case
            else -> {
                val newRole = roles.find { newPoints <= it.maxPoints && newPoints >= it.minPoints }
                        ?: throw ReputationUpdateException("Error finding new role for reputation $newPoints", event)
                ownerRepDetails.role = newRole.reputationRoleId
                ownerRepDetails.points = newPoints
            }
        }
        reputationDAO.updateUserReputation(ownerRepDetails)
    }
}