package isel.leic.ps.eduWikiAPI.domain.model;

import java.time.LocalDate

data class WorkAssignment (
        val id: Int = -1,
        val version: Int = 1,
        val votes: Int = 1,
        val createdBy: String = "",
        val sheet: String = "", //TODO
        val supplement: String = "", //TODO
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false
)
