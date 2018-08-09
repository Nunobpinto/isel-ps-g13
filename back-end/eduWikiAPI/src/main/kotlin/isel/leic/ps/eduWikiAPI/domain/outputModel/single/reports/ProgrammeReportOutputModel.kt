package isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports

import java.sql.Timestamp
import java.time.LocalDateTime

data class ProgrammeReportOutputModel(
        val reportId: Int = -1,
        val programmeId: Int = 0,
        val fullName: String? = null,
        val shortName: String? = null,
        val academicDegree: String? = null,
        val totalCredits: Int? = null,
        val duration: Int? = null,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)