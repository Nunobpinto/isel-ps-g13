package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class UserInputModel (
        val username: String,
        @JsonProperty("family_name")
        val familyName: String = "",
        @JsonProperty("personal_email")
        val personalEmail: String = "",
        @JsonProperty("given_name")
        val givenName: String = "",
        @JsonProperty("organization_email")
        val organizationEmail: String = "",
        val password: String = ""
)