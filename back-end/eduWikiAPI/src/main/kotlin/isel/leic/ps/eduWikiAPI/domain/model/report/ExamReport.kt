package isel.leic.ps.eduWikiAPI.domain.model.report

import java.sql.Timestamp
import java.time.LocalDate

data class ExamReport (
        val reportId: Int = 0,
        val courseMiscUnitId: Int = 0,
        val sheet: String? = null,
        val dueDate: LocalDate? = LocalDate.now(),
        val type: String = "",
        val phase: String? = null,
        val location: String? = null,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(1)
)