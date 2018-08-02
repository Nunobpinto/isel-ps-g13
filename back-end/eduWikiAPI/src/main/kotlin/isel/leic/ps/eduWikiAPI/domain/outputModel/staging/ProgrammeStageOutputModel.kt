package isel.leic.ps.eduWikiAPI.domain.outputModel.staging

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDateTime

class ProgrammeStageOutputModel(
        val stagedId: Int = 0,
        val version: Int = 0,
        @JsonProperty("createdBy")
        val username: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val academicDegree: String = "",
        val totalCredits: Int = 0,
        val duration: Duration = Duration.ZERO,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)