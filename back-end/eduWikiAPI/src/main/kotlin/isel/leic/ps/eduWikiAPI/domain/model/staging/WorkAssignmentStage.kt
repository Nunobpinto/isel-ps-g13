package isel.leic.ps.eduWikiAPI.domain.model.staging

import java.sql.Timestamp
import java.time.LocalDate

data class WorkAssignmentStage (
        val workAssignmentId: Int = 0,
        val votes: Int = 0,
        val createdBy: String = "",
        val sheet: String = "", //TODO
        val supplement: String = "", //TODO
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val requiresReport: Boolean = false,
        val timestamp: Timestamp = Timestamp(0)
)