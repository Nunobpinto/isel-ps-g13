package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDate

class WorkAssignmentInputModel(
        @JsonProperty("created_by")
        val createdBy: String,
        val sheet: String = "",
        val supplement: String = "",
        @JsonProperty("due_date")
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean = false,
        @JsonProperty("multiple_deliveries")
        val multipleDeliveries: Boolean = false,
        @JsonProperty("requires_report")
        val requiresReport: Boolean = false,
        val timestamp: Timestamp = Timestamp(1)
)