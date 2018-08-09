package isel.leic.ps.eduWikiAPI.domain.inputModel

import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import java.time.LocalDate

class ExamInputModel(
        val dueDate: LocalDate = LocalDate.now(),
        val type: ExamType,
        val phase: String = "",
        val location: String = "",
        val createdBy: String = ""
)