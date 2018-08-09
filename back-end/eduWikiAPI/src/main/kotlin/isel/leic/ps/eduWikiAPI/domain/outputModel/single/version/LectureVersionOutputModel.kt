package isel.leic.ps.eduWikiAPI.domain.outputModel.single.version

import java.sql.Timestamp
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

data class LectureVersionOutputModel (
        val version: Int = 1,
        val lectureId: Int = 0,
        val createdBy: String = "",
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        val begins: LocalDate = LocalDate.now(),
        val duration: Duration = Duration.ZERO,
        val location: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)