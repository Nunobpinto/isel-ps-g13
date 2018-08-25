package isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging

import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


data class ExamStageOutputModel (
        val stagedId: Int = 0,
        val version: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val type: ExamType,
        val phase: String = "",
        val location: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val sheetId: UUID? = UUID.randomUUID()
)