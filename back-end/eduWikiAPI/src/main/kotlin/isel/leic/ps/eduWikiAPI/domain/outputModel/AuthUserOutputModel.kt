package isel.leic.ps.eduWikiAPI.domain.outputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDateTime

data class AuthUserOutputModel (
        val username: String = "",
        val givenName: String = "",
        val familyName: String = "",
        val personalEmail: String = "",
        val organizationEmail: String = "",
        @JsonProperty("privilege")
        val userPrivilege: String = "",
        @JsonProperty("reputation")
        val userReputation: Int = 0
)