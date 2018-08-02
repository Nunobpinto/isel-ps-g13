package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkAssignmentVersionOutputModel (
        val version: Int = 1,
        val workAssignmentId: Int = 0,
        val sheet: String = "", //TODO
        val supplement: String? = null, //TODO
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false,
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)