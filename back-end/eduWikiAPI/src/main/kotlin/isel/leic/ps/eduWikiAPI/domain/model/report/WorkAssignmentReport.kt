package isel.leic.ps.eduWikiAPI.domain.model.report

import java.time.LocalDate

data class WorkAssignmentReport (
        val reportId: Int = 0,
        val courseMiscUnitId: Int = 0,
        val sheet: String = "", //TODO
        val supplement: String = "", //TODO
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false,
        val madeBy: String = "",
        val votes: Int = 0
)