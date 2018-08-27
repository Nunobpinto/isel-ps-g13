package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import java.time.LocalDate

class ExamInputModel(
        @JsonProperty("due_date")
        val dueDate: LocalDate = LocalDate.now(),
        val type: String,
        val phase: String = "",
        val location: String = ""
)