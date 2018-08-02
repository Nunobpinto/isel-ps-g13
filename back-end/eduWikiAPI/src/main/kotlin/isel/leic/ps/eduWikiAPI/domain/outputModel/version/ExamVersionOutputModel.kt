package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class ExamVersionOutputModel (
        val version: Int = 1,
        val examId: Int = 0,
        val sheet: String = "", //TODO
        val dueDate: LocalDate = LocalDate.now(),
        val type: String = "",
        val phase: String = "",
        val location: String = "",
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)