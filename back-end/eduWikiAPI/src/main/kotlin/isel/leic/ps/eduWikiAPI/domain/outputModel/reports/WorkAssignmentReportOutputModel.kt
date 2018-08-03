package isel.leic.ps.eduWikiAPI.domain.outputModel.reports

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkAssignmentReportOutputModel (
        val reportId: Int = -1,
        val workAssignmentId: Int = 0,
        val sheet: String? = null, //TODO ficheiros
        val supplement: String? = null, //TODO ficheiros
        val dueDate: LocalDate? = null,
        val individual: Boolean? = null,
        val lateDelivery: Boolean? = null,
        val multipleDeliveries: Boolean? = null,
        val requiresReport: Boolean? = null,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)