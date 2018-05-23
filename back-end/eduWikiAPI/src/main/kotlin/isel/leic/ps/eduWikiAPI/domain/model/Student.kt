package isel.leic.ps.eduWikiAPI.domain.model

import java.sql.Timestamp

data class Student (
        val username: String = "",
        val givenName: String = "",
        val familyName: String = "",
        val personalEmail: String = "",
        val organizationEmail: String = "",
        val gender: String = "",
        val version: Int = 0,
        val userPrivilege: String = "",
        val userReputation: Int = 0
)