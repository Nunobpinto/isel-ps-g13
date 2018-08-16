package isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDateTime

class ProgrammeStageOutputModel(
        val stagedId: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val academicDegree: String = "",
        val totalCredits: Int = 0,
        val duration: Int = 0,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)