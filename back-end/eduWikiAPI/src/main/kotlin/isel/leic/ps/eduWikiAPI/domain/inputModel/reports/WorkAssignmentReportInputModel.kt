package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class WorkAssignmentReportInputModel (
        @JsonProperty("work_assignment_id")
        val workAssignmentId: Int = 0,
        val sheet: String? = null,
        val supplement: String? = null,
        @JsonProperty("due_date")
        val dueDate: LocalDate? = null,
        val individual: Boolean? = null,
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean? = null,
        @JsonProperty("multipleDeliveries")
        val multipleDeliveries: Boolean? = null,
        @JsonProperty("requires_report")
        val requiresReport: Boolean? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)