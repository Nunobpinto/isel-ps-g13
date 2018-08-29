package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class UserInputModel (
        val username: String,
        @JsonProperty("family_name")
        val familyName: String = "",
        @JsonProperty("given_name")
        val givenName: String = "",
        val email: String = "",
        val password: String = ""
)