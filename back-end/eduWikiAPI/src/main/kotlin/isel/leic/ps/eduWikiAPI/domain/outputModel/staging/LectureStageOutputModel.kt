package isel.leic.ps.eduWikiAPI.domain.outputModel.staging

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

data class LectureOutputModel (
    val stagedId: Int = 0,
    val version: Int = 0,
    @JsonProperty("createdBy")
    val username: String = "",
    val weekDay: DayOfWeek = DayOfWeek.MONDAY,
    val begins: LocalDate = LocalDate.now(),
    val duration: Duration = Duration.ZERO,
    val location: String = "",
    val votes: Int = 0,
    val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)
