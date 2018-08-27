package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.*

class HomeworkInputModel (
        @JsonProperty("due_date")
        val dueDate: LocalDate = LocalDate.now(),
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean = false,
        @JsonProperty("multiple_deliveries")
        val multipleDeliveries: Boolean = false,
        @JsonProperty("homework_name")
        val homeworkName: String = ""
)