package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class ExamVersionOutputModel (
        val version: Int = 1,
        val examId: Int = 0,
        val sheet: UUID = UUID.randomUUID(),
        val dueDate: LocalDate = LocalDate.now(),
        val type: ExamType,
        val phase: String = "",
        val location: String = "",
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val sheetId: UUID
)