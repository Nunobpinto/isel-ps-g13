package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class ExamInputModel(
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val type: String = "",
        val phase: String = "",
        val location: String = "",
        @JsonProperty("created_by")
        val createdBy: String = ""
)