package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import isel.leic.ps.eduWikiAPI.repository.UserDAOImpl.Companion.USER_TABLE
import isel.leic.ps.eduWikiAPI.repository.UserDAOImpl.Companion.USER_USERNAME
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
class ReputationDAOImpl : ReputationDAO {

    companion object {
        // TABLE NAMES
        const val ACTION_LOG_TABLE = "action_log"
        const val REPUTATION_LOG_TABLE = "reputation_log"
        const val REPUTATION_TABLE = "reputation"
        // FIELDS
        const val ACTION_LOG_ID = "action_id"
        const val ACTION_LOG_USER = "user_username"
        const val ACTION_LOG_ACTION = "action"
        const val ACTION_LOG_ENTITY = "entity"
        const val ACTION_LOG_LOG_ID = "log_id"
        const val ACTION_LOG_TIMESTAMP = "time_stamp"
        const val REPUTATION_LOG_ID = "reputation_log_id"
        const val REPUTATION_LOG_ACTION = "reputation_log_action"
        const val REPUTATION_LOG_GIVEN_BY = "reputation_log_given_by"
        const val REPUTATION_LOG_POINTS = "reputation_log_points"
        const val REPUTATION_LOG_REP_ID = "reputation_id"
        const val REPUTATION_LOG_USER = "user_username"
        const val REPUTATION_ID = "reputation_id"
        const val REPUTATION_POINTS = "points"
        const val REPUTATION_USER = "user_username"
        const val REPUTATION_ROLE = "role"
    }

    @Qualifier("MainJdbi")
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getReputationRoleOfUser(username: String): Optional<String> =
            jdbi.open().attach(ReputationDAOJdbi::class.java).getReputationRoleOfUser(username)

    override fun getUserReputationDetails(user: String): Optional<Reputation> =
            jdbi.open().attach(ReputationDAOJdbi::class.java).getUserReputationDetails(user)

    override fun createUserReputation(reputation: Reputation): Reputation =
            jdbi.open().attach(ReputationDAOJdbi::class.java).createUserReputation(reputation)

    override fun updateUserRole(username: String, reputationPoints: Int, role: String): Reputation =
            jdbi.open().attach(ReputationDAOJdbi::class.java).updateUserRole(username, reputationPoints, role)

    override fun updateUserReputation(reputationDetails: Reputation): Int =
            jdbi.open().attach(ReputationDAOJdbi::class.java).updateUserReputation(reputationDetails)

    override fun registerActionLog(user: String, action: ActionType, entity: String, logId: Int, timestamp: Timestamp): ActionLog =
            jdbi.open().attach(ReputationDAOJdbi::class.java).registerActionLog(user, action, entity, logId, timestamp)

    override fun registerReputationLog(user: String, reputationId: Int, pointsGiven: Int, givenBy: String, actionId: Int): ReputationLog =
            jdbi.open().attach(ReputationDAOJdbi::class.java).registerReputationLog(user, reputationId, pointsGiven, givenBy, actionId)

    override fun getActionLogsByResource(approvedEntity: String, approvedLogId: Int): List<ActionLog> =
            jdbi.open().attach(ReputationDAOJdbi::class.java).getActionLogsByResource(approvedEntity, approvedLogId)

    override fun getActionLogsByUserAndResource(user: String, approvedEntity: String, approvedLogId: Int): List<ActionLog> =
            jdbi.open().attach(ReputationDAOJdbi::class.java).getActionLogsByUserAndResource(user, approvedEntity, approvedLogId)

    override fun getActionLogsByUser(username: String): List<ActionLog> =
            jdbi.open().attach(ReputationDAOJdbi::class.java).getActionLogsByUser(username)

    override fun getReputationLogsByUser(username: String): List<ReputationLog> =
            jdbi.open().attach(ReputationDAOJdbi::class.java).getReputationLogsByUser(username)

    override fun getActionLogById(repActionId: Int): Optional<ActionLog> =
            jdbi.open().attach(ReputationDAOJdbi::class.java).getActionLogById(repActionId)

    internal interface ReputationDAOJdbi : ReputationDAO {
        @SqlQuery(
                "SELECT R.$REPUTATION_ROLE " +
                        "FROM :schema.$USER_TABLE as U " +
                        "INNER JOIN :schema.$REPUTATION_TABLE as R ON R.$REPUTATION_USER = U.$USER_USERNAME " +
                        "WHERE U.$USER_USERNAME = :username"
        )
        override fun getReputationRoleOfUser(username: String): Optional<String>

        @SqlUpdate(
                "INSERT INTO :schema.$REPUTATION_TABLE (" +
                        "$REPUTATION_POINTS," +
                        "$REPUTATION_ROLE," +
                        REPUTATION_USER +
                        ") VALUES (" +
                        ":reputation.points," +
                        ":reputation.role," +
                        ":reputation.username" +
                        ")")
        @GetGeneratedKeys
        override fun createUserReputation(reputation: Reputation): Reputation

        @SqlUpdate(
                "UPDATE :schema.$REPUTATION_TABLE " +
                        "SET $REPUTATION_ROLE = :role, " +
                        "$REPUTATION_POINTS = :reputationPoints " +
                        "WHERE $REPUTATION_USER = :username"
        )
        @GetGeneratedKeys
        override fun updateUserRole(username: String, reputationPoints: Int, role: String): Reputation

        @SqlUpdate(
                "INSERT INTO :schema.$ACTION_LOG_TABLE (" +
                        "$ACTION_LOG_USER," +
                        "$ACTION_LOG_ACTION," +
                        "$ACTION_LOG_ENTITY," +
                        "$ACTION_LOG_LOG_ID," +
                        "$ACTION_LOG_TIMESTAMP)" +
                        "VALUES (" +
                        ":user, :action, :entity, :logId, :timestamp)"
        )
        @GetGeneratedKeys
        override fun registerActionLog(user: String, action: ActionType, entity: String, logId: Int, timestamp: Timestamp): ActionLog

        @SqlQuery("SELECT * FROM :schema.$REPUTATION_TABLE WHERE $REPUTATION_USER = :user")
        override fun getUserReputationDetails(user: String): Optional<Reputation>

        @SqlUpdate(
                "INSERT INTO :schema.$REPUTATION_LOG_TABLE (" +
                        "$REPUTATION_LOG_ACTION, " +
                        "$REPUTATION_LOG_GIVEN_BY, " +
                        "$REPUTATION_LOG_POINTS, " +
                        "$REPUTATION_LOG_REP_ID, " +
                        "$REPUTATION_LOG_USER)" +
                        "VALUES (" +
                        ":actionId, :givenBy, :pointsGiven, :reputationId, :user)"
        )
        @GetGeneratedKeys
        override fun registerReputationLog(user: String, reputationId: Int, pointsGiven: Int, givenBy: String, actionId: Int): ReputationLog

        @SqlUpdate(
                "UPDATE :schema.$REPUTATION_TABLE SET " +
                        "$REPUTATION_ROLE = :reputationDetails.role, " +
                        "$REPUTATION_POINTS = :reputationDetails.points " +
                        "WHERE $REPUTATION_USER = :reputationDetails.username AND $REPUTATION_ID = :reputationDetails.reputationId"
        )
        override fun updateUserReputation(reputationDetails: Reputation): Int

        @SqlQuery(
                "SELECT * FROM :schema.$ACTION_LOG_TABLE " +
                        "WHERE $ACTION_LOG_ENTITY = :approvedEntity AND $ACTION_LOG_LOG_ID = :approvedLogId"
        )
        override fun getActionLogsByResource(approvedEntity: String, approvedLogId: Int): List<ActionLog>

        @SqlQuery(
                "SELECT * FROM :schema.$ACTION_LOG_TABLE " +
                        "WHERE $ACTION_LOG_ENTITY = :approvedEntity AND $ACTION_LOG_LOG_ID = :approvedLogId AND $ACTION_LOG_USER = :user"
        )
        override fun getActionLogsByUserAndResource(user: String, approvedEntity: String, approvedLogId: Int): List<ActionLog>

        @SqlQuery("SELECT * FROM :schema.$ACTION_LOG_TABLE WHERE $ACTION_LOG_USER = :username")
        override fun getActionLogsByUser(username: String): List<ActionLog>

        @SqlQuery("SELECT * FROM :schema.$REPUTATION_LOG_TABLE WHERE $REPUTATION_LOG_USER = :username")
        override fun getReputationLogsByUser(username: String): List<ReputationLog>

        @SqlQuery("SELECT * FROM :schema.$ACTION_LOG_TABLE WHERE $ACTION_LOG_ID = :repActionId")
        override fun getActionLogById(repActionId: Int): Optional<ActionLog>

    }

}






















