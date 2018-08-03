package isel.leic.ps.eduWikiAPI.domain.outputModel.reports

import java.sql.Timestamp
import java.time.LocalDateTime

data class ClassReportOutputModel(
        val reportId: Int = -1,
        val classId: Int = 0,
        val className: String? = null,
        val termId: Int = 0,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)