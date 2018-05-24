package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class ExamInputModel(
        val type: String = "",
        val data: String = "",
        val phase: String = "",
        val location: String = "",
        val statement: String = "",
        @JsonProperty("created_by")
        val createdBy: String = "",
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now()
)