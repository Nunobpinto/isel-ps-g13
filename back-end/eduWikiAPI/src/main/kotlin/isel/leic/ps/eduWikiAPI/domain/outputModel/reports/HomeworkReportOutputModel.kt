package isel.leic.ps.eduWikiAPI.domain.outputModel.reports

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class HomeworkReportOutputModel (
        val reportId: Int = -1,
        val homeworkId: Int = 0,
        val sheetId: UUID? = null,
        val dueDate: LocalDate? = null,
        val lateDelivery: Boolean? = null,
        val multipleDeliveries: Boolean? = null,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)