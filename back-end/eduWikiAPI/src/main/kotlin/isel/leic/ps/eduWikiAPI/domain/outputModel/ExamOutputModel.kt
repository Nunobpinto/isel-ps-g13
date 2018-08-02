package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime


data class ExamOutputModel (
        val examId: Int = 0,
        val version: Int = 0,
        @JsonProperty("createdBy")
        val username: String = "",
        val fullName: String = "",
        val sheet: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val type: String = "",
        val phase: String = "",
        val location: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)