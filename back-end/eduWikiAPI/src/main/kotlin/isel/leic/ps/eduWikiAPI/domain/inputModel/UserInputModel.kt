package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class UserInputModel (
        val username: String,
        @JsonProperty("family_name")
        val familyName: String? = null,
        @JsonProperty("personal_email")
        val personalEmail: String? = null,
        @JsonProperty("given_name")
        val givenName: String? = null,
        @JsonProperty("organization_email")
        val organizationEmail: String? = null,
        val password: String? = null
)