package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import java.sql.Timestamp
import java.time.LocalDateTime

data class ProgrammeVersionOutputModel (
        val version: Int = 1,
        val programmeId: Int = 0,
        val fullName: String = "",
        val shortName: String = "",
        val academicDegree: String = "",
        val totalCredits: Int = 0,
        val duration: Int = 0,
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)