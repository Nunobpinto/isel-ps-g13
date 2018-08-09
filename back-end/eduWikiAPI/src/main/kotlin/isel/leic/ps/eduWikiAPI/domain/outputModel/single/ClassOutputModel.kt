package isel.leic.ps.eduWikiAPI.domain.outputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDateTime

data class ClassOutputModel(
        val classId: Int = 0,
        val version: Int = 0,
        @JsonProperty("createdBy")
        val username: String = "",
        val className: String = "",
        val termId: Int = 0,
        val lecturedTerm: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)