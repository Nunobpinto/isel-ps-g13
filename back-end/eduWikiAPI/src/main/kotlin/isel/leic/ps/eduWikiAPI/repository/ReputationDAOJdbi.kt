package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.ReputationMatcher
import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_TABLE
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_USERNAME
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.sql.Timestamp
import java.util.*

interface ReputationDAOJdbi : ReputationDAO {

    companion object {
        // TABLE NAMES
        const val ACTION_LOG_TABLE = "action_log"
        const val REPUTATION_LOG_TABLE = "reputation_log"
        const val REPUTATION_TABLE = "reputation"
        const val REPUTATION_ROLE_TABLE = "reputation_role"
        const val REPUTATION_MATCHER_TABLE = "reputation_matcher"
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
        const val REPUTATION_ROLE_ID = "reputation_role_id"
        const val REPUTATION_ROLE_MAX_POINTS = "max_points"
        const val REPUTATION_ROLE_MIN_POINTS = "min_points"
        const val REPUTATION_ROLE_HIERARCHY_LEVEL = "hierarchy_level"
        const val REPUTATION_MATCHER_URI = "uri_match"
        const val REPUTATION_MATCHER_ID = "reputation_role_id"
    }

    @SqlQuery(
            "SELECT " +
                    "rr.$REPUTATION_ROLE_ID, " +
                    "rr.$REPUTATION_ROLE_MAX_POINTS, " +
                    "rr.$REPUTATION_ROLE_MIN_POINTS, " +
                    "rr.$REPUTATION_ROLE_HIERARCHY_LEVEL " +
                    "FROM $USER_TABLE as u " +
                    "INNER JOIN $REPUTATION_TABLE as r ON r.$REPUTATION_USER = u.$USER_USERNAME " +
                    "INNER JOIN $REPUTATION_ROLE_TABLE as rr ON r.$REPUTATION_ROLE = rr.$REPUTATION_ROLE_ID " +
                    "WHERE u.$USER_USERNAME = :username"
    )
    override fun getReputationRoleOfUser(username: String): Optional<ReputationRole>

    @SqlQuery("SELECT * FROM $REPUTATION_ROLE_TABLE ORDER BY $REPUTATION_ROLE_HIERARCHY_LEVEL DESC")
    override fun getAllReputationRoles(): List<ReputationRole>

    @SqlQuery("SELECT * FROM $REPUTATION_MATCHER_TABLE")
    override fun getReputationMatchers(): List<ReputationMatcher>

    @SqlUpdate(
            "INSERT INTO $REPUTATION_TABLE (" +
                    "$REPUTATION_POINTS," +
                    "$REPUTATION_ROLE," +
                    REPUTATION_USER +
                    ") VALUES (" +
                    ":reputation.reputationPoints," +
                    ":reputation.reputationRole," +
                    ":reputation.username" +
                    ")")
    @GetGeneratedKeys
    override fun createUserReputation(reputation: Reputation): Reputation

    @SqlQuery("SELECT * FROM $REPUTATION_ROLE_TABLE WHERE $REPUTATION_ROLE_HIERARCHY_LEVEL = :level")
    override fun getRoleByHierarchyLevel(level: Int): Optional<ReputationRole>

    @SqlUpdate("UPDATE $REPUTATION_TABLE " +
                "SET $REPUTATION_ROLE = :role, " +
                 "$REPUTATION_POINTS = :reputationPoints " +
                "WHERE $REPUTATION_USER = :username")
    override fun updateUserRole(username: String, reputationPoints: Int, role: String): Int

    @SqlUpdate(
            "INSERT INTO $ACTION_LOG_TABLE (" +
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

    @SqlQuery("SELECT * FROM $REPUTATION_TABLE WHERE $REPUTATION_USER = :user")
    override fun getUserReputationDetails(user: String): Optional<ReputationDetails>

    @SqlUpdate(
            "INSERT INTO $REPUTATION_LOG_TABLE (" +
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
            "UPDATE $REPUTATION_TABLE SET " +
                    "$REPUTATION_ROLE = :reputationDetails.role, " +
                    "$REPUTATION_POINTS = :reputationDetails.points " +
                    "WHERE $REPUTATION_USER = :reputationDetails.user AND $REPUTATION_ID = :reputationDetails.repId"
    )
    override fun updateUserReputation(reputationDetails: ReputationDetails): Int

    @SqlQuery(
            "SELECT * FROM $ACTION_LOG_TABLE " +
                    "WHERE $ACTION_LOG_ENTITY = :approvedEntity AND $ACTION_LOG_LOG_ID = :approvedLogId"
    )
    override fun getActionLogsByResource(approvedEntity: String, approvedLogId: Int): List<ActionLog>

}






















