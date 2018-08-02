package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import com.fasterxml.jackson.annotation.JsonProperty

data class UserOutputModel (
        val username: String = "",
        @JsonProperty("reputation")
        val userReputation: Int = 0
)