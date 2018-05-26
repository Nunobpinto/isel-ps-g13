package isel.leic.ps.eduWikiAPI.domain.inputModel

import java.sql.Timestamp
import java.time.LocalDate

class WorkAssignmentInputModel(
        val createdBy: String,
        val sheet: String? = null,
        val supplement: String? = null,
        val dueDate: LocalDate? = LocalDate.now(),
        val individual: Boolean? = false,
        val lateDelivery: Boolean? = false,
        val multipleDeliveries: Boolean? = false,
        val requiresReport: Boolean? = false,
        val timestamp: Timestamp = Timestamp(1)
)