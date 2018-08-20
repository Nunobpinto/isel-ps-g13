package isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class WorkAssignmentReportOutputModel (
        val reportId: Int = -1,
        val workAssignmentId: Int = 0,
        val phase: String? = null,
        val sheetId: UUID? = null,
        val supplementId: UUID? = null,
        val dueDate: LocalDate? = null,
        val individual: Boolean? = null,
        val lateDelivery: Boolean? = null,
        val multipleDeliveries: Boolean? = null,
        val requiresReport: Boolean? = null,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)