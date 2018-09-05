package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import java.sql.Timestamp
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

data class LectureOutputModel(
        val lectureId: Int = 0,
        val className: String = "",
        val lecturedTerm: String = "",
        val courseShortName: String = "",
        val version: Int = 0,
        val createdBy: String = "",
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        val begins: LocalTime = LocalTime.now(),
        val duration: Duration = Duration.ZERO,
        val location: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)
