package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import java.util.*
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class ExamOutputModel (
        val examId: Int = 0,
        val version: Int = 1,
        val votes: Int = 0,
        val createdBy: String = "",
        val sheetId: UUID? = UUID.randomUUID(),
        val dueDate: LocalDate = LocalDate.now(),
        val type: ExamType,
        val phase: String = "",
        val location: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)