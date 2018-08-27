package isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class HomeworkStageOutputModel (
        val stagedId: Int = 0,
        val version: Int = 0,
        val homeworkName: String = "",
        val createdBy: String = "",
        val sheetId: UUID? = UUID.randomUUID(),
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)