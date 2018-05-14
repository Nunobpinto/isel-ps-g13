package isel.leic.ps.eduWikiAPI.domain.model.staging

import java.sql.Timestamp
import java.time.LocalDate

data class ExamStage (
        val exameId: Int = 0,
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val type: String = "",
        val phase: String = "",
        val location: String = "",
        val createdBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(0)
)