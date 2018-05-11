package isel.leic.ps.eduWikiAPI.domain.model;

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate

data class Lecture(
        val id: Int = -1,
        val version: Int = 1,
        val votes: Int = 1,
        val createdBy: String = "",
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        val begins: LocalDate = LocalDate.now(),
        val duration: Duration = Duration.ZERO,
        val location: String = ""
)
