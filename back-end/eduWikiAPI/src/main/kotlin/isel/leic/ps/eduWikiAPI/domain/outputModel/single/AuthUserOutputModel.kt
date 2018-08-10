package isel.leic.ps.eduWikiAPI.domain.outputModel


data class AuthUserOutputModel (
        val username: String = "",
        val givenName: String = "",
        val familyName: String = "",
        val personalEmail: String = "",
        val organizationEmail: String = "",
        val confirmed: Boolean
)