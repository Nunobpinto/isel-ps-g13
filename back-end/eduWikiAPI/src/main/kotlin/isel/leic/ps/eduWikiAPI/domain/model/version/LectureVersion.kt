package isel.leic.ps.eduWikiAPI.domain.model.version

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate

data class LectureVersion (
        val lectureId: Int = 0,
        val createdBy: String = "",
        val version: Int = 0,
        val weekDay: DayOfWeek,
        val begins: LocalDate = LocalDate.now(),
        val duration: Duration = Duration.ZERO,
        val location: String = ""
)