package isel.leic.ps.eduWikiAPI.domain.model.report

import java.sql.Timestamp
import java.time.LocalDate

data class HomeworkReport (
        val reportId: Int = 0,
        val classMiscUnitId: Int = 0,
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp
)