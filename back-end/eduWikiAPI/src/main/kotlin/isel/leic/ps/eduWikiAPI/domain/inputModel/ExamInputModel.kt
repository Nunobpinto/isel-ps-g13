package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class ExamInputModel(
        val sheet: String = "", //TODO
        @JsonProperty("due_date")
        val dueDate: LocalDate = LocalDate.now(),
        @JsonProperty("exam_type")
        val type: String = "",
        val phase: String = "",
        val location: String = "",
        @JsonProperty("created_by")
        val createdBy: String = ""
)