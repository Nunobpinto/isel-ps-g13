package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDate

class WorkAssignmentInputModel(
        @JsonProperty("created_by")
        val createdBy: String,
        val sheet: String = "", //TODO
        val supplement: String? = null,
        @JsonProperty("due_date")
        val dueDate: LocalDate = LocalDate.now(),
        val individual: Boolean = false,
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean = false,
        @JsonProperty("multipleDeliveries")
        val multipleDeliveries: Boolean = false,
        @JsonProperty("requires_report")
        val requiresReport: Boolean = false
)