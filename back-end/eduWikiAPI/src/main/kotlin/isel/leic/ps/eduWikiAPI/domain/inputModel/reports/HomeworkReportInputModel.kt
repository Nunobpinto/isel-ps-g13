package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class HomeworkReportInputModel (
        @JsonProperty("homework_id")
        val homeworkId: Int = 0,
        val sheet: String? = null,
        @JsonProperty("due_date")
        val dueDate: LocalDate? =null,
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean? =null,
        @JsonProperty("multipleDeliveries")
        val multipleDeliveries: Boolean? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)