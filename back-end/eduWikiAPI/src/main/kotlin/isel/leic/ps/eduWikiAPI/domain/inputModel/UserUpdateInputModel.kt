package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

data class UserUpdateInputModel(
        @JsonProperty("family_name")
        val familyName: String? = null,
        @JsonProperty("given_name")
        val givenName: String? = null
)
