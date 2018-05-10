package isel.leic.ps.eduWikiAPI.domain.model.version

import java.sql.Timestamp
import java.time.LocalDate

data class ExamVersion (
        val exameId: Int = 0,
        val sheet: String = "", //TODO
        val dueDate: LocalDate = LocalDate.now(),
        val type: String = "",
        val phase: String = "",
        val location: String = "",
        val createdBy: String = "",
        val version: Int = 0,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(0)
)