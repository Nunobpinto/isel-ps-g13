package isel.leic.ps.eduWikiAPI.domain.outputModel.staging

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDateTime

data class ClassStageOutputModel(
        val stagedId: Int = 0,
        val version: Int = 0,
        @JsonProperty("createdBy")
        val username: String = "",
        val fullName: String = "",
        val lecturedTerm: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)