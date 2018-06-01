package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class HomeworkReportInputModel (
        val sheet: String? = null,
        @JsonProperty("due_date")
        val dueDate: String? =null,
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean? =null,
        @JsonProperty("multiple_deliveries")
        val multipleDeliveries: Boolean? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)