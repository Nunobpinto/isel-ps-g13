package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class HomeworkOutputModel (
        val homeworkId: Int = 0,
        val version: Int = 0,
        val createdBy: String = "",
        val homeworkName: String = "",
        val sheetId: UUID? = null,
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val votes: Int = 0,
        val className: String = "",
        val lecturedTerm: String = "",
        val courseShortName: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)