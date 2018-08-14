package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDateTime

data class ProgrammeOutputModel(
        val programmeId: Int = 0,
        val version: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val academicDegree: String = "",
        val totalCredits: Int = 0,
        val duration: Int = 0,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)