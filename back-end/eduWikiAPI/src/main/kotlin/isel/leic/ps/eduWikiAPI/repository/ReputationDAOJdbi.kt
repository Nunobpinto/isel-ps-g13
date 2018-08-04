package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.ReputationMatcher
import isel.leic.ps.eduWikiAPI.domain.model.Reputation
import isel.leic.ps.eduWikiAPI.domain.model.ReputationRole
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_TABLE
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_USERNAME
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.*

interface ReputationDAOJdbi : ReputationDAO {

    companion object {
        // TABLE NAMES
        const val REPUTATION_TABLE = "reputation"
        const val REPUTATION_ROLE_TABLE = "reputation_role"
        const val REPUTATION_MATCHER_TABLE = "reputation_matcher"
        // FIELDS
        const val REPUTATION_ID = "reputation_id"
        const val REPUTATION_POINTS = "reputation_points"
        const val REPUTATION_USER = "user_username"
        const val REPUTATION_ROLE = "reputation_role"
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
    override fun saveNewUser(reputation: Reputation): Reputation

    @SqlQuery("SELECT * FROM $REPUTATION_ROLE_TABLE WHERE $REPUTATION_ROLE_HIERARCHY_LEVEL = :level")
    override fun getRoleByHierarchyLevel(level: Int): Optional<ReputationRole>

    @SqlUpdate("UPDATE $REPUTATION_TABLE " +
                "SET $REPUTATION_ROLE = :role, " +
                 "$REPUTATION_POINTS = :reputationPoints " +
                "WHERE $REPUTATION_USER = :username")
    override fun changeRole(username: String, reputationPoints: Int, role: String): Int

}