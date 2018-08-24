package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.model.*
import java.sql.Timestamp
import java.util.*

interface ReputationDAO {

    fun getReputationRoleOfUser(username: String): Optional<String>

    fun getUserReputationDetails(user: String): Optional<Reputation>

    fun createUserReputation(reputation: Reputation): Reputation

    fun updateUserRole(username: String, reputationPoints: Int, role: String): Reputation

    fun updateUserReputation(reputationDetails: Reputation): Int

    fun registerReputationLog(user: String, reputationId: Int, pointsGiven: Int, givenBy: String, actionId: Int): ReputationLog

    fun getActionLogsByResource(approvedEntity: String, approvedLogId: Int): List<ActionLog>

    fun getActionLogsByUserAndResource(user: String, approvedEntity: String, approvedLogId: Int): List<ActionLog>

    fun getActionLogsByUser(username: String): List<ActionLog>

    fun registerActionLog(user: String, action: ActionType, entity: String, logId: Int, timestamp: Timestamp): ActionLog

    fun getReputationLogsByUser(username: String): List<ReputationLog>

    fun getActionLogById(repActionId: Int): Optional<ActionLog>
}