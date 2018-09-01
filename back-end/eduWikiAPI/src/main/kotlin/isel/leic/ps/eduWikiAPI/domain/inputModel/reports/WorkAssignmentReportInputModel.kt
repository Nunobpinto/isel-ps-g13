package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.*

class WorkAssignmentReportInputModel (
        @JsonProperty("work_assignment_id")
        val workAssignmentId: Int = 0,
        val sheetId: UUID = UUID.randomUUID(),
        val phase: String? = null,
        val supplementId: UUID = UUID.randomUUID(),
        @JsonProperty("due_date")
        val dueDate: LocalDate? = null,
        val individual: Boolean? = null,
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean? = null,
        @JsonProperty("multiple_deliveries")
        val multipleDeliveries: Boolean? = null,
        @JsonProperty("requires_report")
        val requiresReport: Boolean? = null
)