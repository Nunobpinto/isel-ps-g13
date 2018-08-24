package isel.leic.ps.eduWikiAPI.domain.inputModel

import java.time.LocalDate
import java.util.*

class WorkAssignmentInputModel(
        val dueDate: LocalDate = LocalDate.now(),
        val phase: String = "",
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false
)