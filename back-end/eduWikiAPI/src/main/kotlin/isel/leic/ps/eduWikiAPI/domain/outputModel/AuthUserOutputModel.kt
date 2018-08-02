package isel.leic.ps.eduWikiAPI.domain.outputModel.single

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
        val gender: String = "",
        val version: Int = 0,
        @JsonProperty("privilege")
        val userPrivilege: String = "",
        @JsonProperty("reputation")
        val userReputation: Int = 0
)