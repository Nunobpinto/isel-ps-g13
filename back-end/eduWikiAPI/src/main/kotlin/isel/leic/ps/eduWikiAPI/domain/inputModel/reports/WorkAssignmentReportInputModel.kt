package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class WorkAssignmentReportInputModel (
        val sheet: String? = null,
        val supplement: String? = null,
        @JsonProperty("due_date")
        val dueDate: LocalDate? = LocalDate.now(),
        val individual: Boolean? = false,
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean? = false,
        @JsonProperty("multiple_deliveries")
        val multipleDeliveries: Boolean? = false,
        @JsonProperty("requires_report")
        val requiresReport: Boolean? = false,
        @JsonProperty("reported_by")
        val reportedBy: String
)