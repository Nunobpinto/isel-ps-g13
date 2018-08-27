package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.*

class HomeworkReportInputModel (
        val sheetId: UUID? = null,
        @JsonProperty("due_date")
        val dueDate: LocalDate? =null,
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean? =null,
        @JsonProperty("multiple_deliveries")
        val multipleDeliveries: Boolean? = null,
        @JsonProperty("homework_name")
        val homeworkName: String? = null
)