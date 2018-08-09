package isel.leic.ps.eduWikiAPI.domain.outputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseProgrammeOutputModel (
        val courseId: Int = 0,
        val organizationId: Int = 0,
        val programmeId: Int = 0,
        val version: Int = 0,
        @JsonProperty( "createdBy")
        val username: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val lecturedTerm: String = "",
        val optional: Boolean = false,
        val credits: Int = 0,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)