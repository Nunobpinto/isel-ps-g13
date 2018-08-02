package isel.leic.ps.eduWikiAPI.domain.outputModel.reports

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class ExamReportOutputModel (
        val reportId: Int = -1,
        val examId: Int = 0,
        val sheet: String? = null,
        val dueDate: LocalDate? = null,
        val type: String? = null,
        val phase: String? = null,
        val location: String? = null,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)