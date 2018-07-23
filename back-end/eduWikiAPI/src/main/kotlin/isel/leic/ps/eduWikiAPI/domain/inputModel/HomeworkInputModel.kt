package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class HomeworkInputModel (
        val sheet: String = "", //TODO
        @JsonProperty("due_date")
        val dueDate: LocalDate = LocalDate.now(),
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean = false,
        @JsonProperty("multipleDeliveries")
        val multipleDeliveries: Boolean = false,
        @JsonProperty("created_by")
        val createdBy: String = ""
)