package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Time
import java.sql.Timestamp
import java.time.*

data class LectureOutputModel(
        val lectureId: Int = 0,
        val version: Int = 0,
        val createdBy: String = "",
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        val begins: LocalTime = LocalTime.now(),
        val duration: Duration = Duration.ZERO,
        val location: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)
