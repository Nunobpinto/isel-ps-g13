package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class HomeworkVersionOutputModel(
        @JsonProperty("createdBy")
        val username: String = "",
        val version: Int = 1,
        val homeworkId: Int = 0,
        val sheetId: UUID = UUID.randomUUID(),
        val dueDate: LocalDate = LocalDate.now(),
        val lateDelivery: Boolean = false,
        val multipleDeliveries: Boolean = false,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)