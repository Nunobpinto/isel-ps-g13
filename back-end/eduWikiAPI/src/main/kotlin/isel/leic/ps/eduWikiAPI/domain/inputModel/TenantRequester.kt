package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

data class TenantRequester(
        @JsonProperty("username")
        val username: String,
        @JsonProperty("family_name")
        val familyName: String,
        @JsonProperty("given_name")
        val givenName: String,
        val email: String,
        @JsonProperty("principal")
        val principal: Boolean
)