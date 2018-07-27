package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_FAMILY_NAME
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_GIVEN_NAME
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_ORG_EMAIL
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_PASSWORD
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_PERSONAL_EMAIL
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_USERNAME
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class User (
        @ColumnName(USER_USERNAME)
        val username: String = "",
        @ColumnName(USER_PASSWORD)
        val password: String = "",
        @ColumnName(USER_GIVEN_NAME)
        val givenName: String = "",
        @ColumnName(USER_FAMILY_NAME)
        val familyName: String = "",
        @ColumnName(USER_PERSONAL_EMAIL)
        val personalEmail: String = "",
        @ColumnName(USER_ORG_EMAIL)
        val organizationEmail: String = "",
        val userPrivilege: String = "",
        val userReputation: Int = 0
)