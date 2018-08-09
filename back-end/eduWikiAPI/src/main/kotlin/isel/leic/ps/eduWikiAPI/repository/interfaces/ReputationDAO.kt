package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.ReputationMatcher
import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.model.*
import java.sql.Timestamp
import java.util.*

interface ReputationDAO {

    fun getReputationRoleOfUser(username: String): Optional<ReputationRole>

    fun getAllReputationRoles(): List<ReputationRole>

    fun getReputationMatchers(): List<ReputationMatcher>

    fun getUserReputationDetails(user: String): Optional<ReputationDetails>

    fun getRoleByHierarchyLevel(level: Int): Optional<ReputationRole>

    fun createUserReputation(reputation: Reputation): Reputation

    fun updateUserRole(username: String, reputationPoints: Int, role: String): Int

    fun updateUserReputation(reputationDetails: ReputationDetails): Int

    fun registerActionLog(user: String, action: ActionType, entity: String, logId: Int, timestamp: Timestamp): ActionLog

    fun registerReputationLog(user: String, reputationId: Int, pointsGiven: Int, givenBy: String, actionId: Int): ReputationLog

    fun getActionLogsByResource(approvedEntity: String, approvedLogId: Int): List<ActionLog>
}