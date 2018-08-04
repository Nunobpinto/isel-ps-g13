package isel.leic.ps.eduWikiAPI.domain.outputModel

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthUserOutputModel (
        val username: String = "",
        val givenName: String = "",
        val familyName: String = "",
        val personalEmail: String = "",
        val organizationEmail: String = "",
        val confirmed: Boolean
)