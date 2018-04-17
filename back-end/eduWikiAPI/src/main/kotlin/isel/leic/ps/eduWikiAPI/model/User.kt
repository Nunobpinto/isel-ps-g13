package isel.leic.ps.eduWikiAPI.model

data class User (
        val username: String,
        val familyName: String,
        val givenName: String,
        val personalEmail: String,
        val organizationEmail: String,
        val userPrivilege: String,
        val userReputation: Int,
        val password: String
)