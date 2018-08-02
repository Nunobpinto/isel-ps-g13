package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.Year

data class TermOutputModel (
        val id: Int = 0,
        val shortName: String = "",
        val year: Year = Year.now(),
        val type: String = "",
        val version: Int = 0,
        @JsonProperty("createdBy")
        val username: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)