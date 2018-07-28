package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.repository.interfaces.UserDAO
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.*

interface UserDAOJdbi : UserDAO {

    companion object {
        // TABLE NAMES
        const val USER_TABLE = "user_account"
        const val USER_REPORT_TABLE = "user_report"
        // FIELDS
        const val USER_USERNAME = "user_username"
        const val USER_PASSWORD = "user_password"
        const val USER_GIVEN_NAME = "user_given_name"
        const val USER_FAMILY_NAME = "user_family_name"
        const val USER_PERSONAL_EMAIL = "user_personal_email"
        const val USER_ORG_EMAIL = "user_organization_email"

    }

    @SqlQuery("SELECT * FROM $USER_TABLE where $USER_USERNAME = :username")
    override fun getUser(username: String): Optional<User>

    @SqlUpdate("INSERT INTO $USER_TABLE(" +
            "$USER_USERNAME," +
            "$USER_PASSWORD," +
            "$USER_GIVEN_NAME," +
            "$USER_FAMILY_NAME," +
            "$USER_PERSONAL_EMAIL," +
            USER_ORG_EMAIL +
            ") VALUES ( " +
            ":user.username," +
            ":user.password," +
            ":user.givenName," +
            ":user.familyName," +
            ":user.personalEmail," +
            ":user.organizationEmail " +
            ")")
    @GetGeneratedKeys
    override fun createUser(user: User): User
}