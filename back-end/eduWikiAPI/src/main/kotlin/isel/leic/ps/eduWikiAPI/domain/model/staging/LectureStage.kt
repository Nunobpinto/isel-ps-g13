package isel.leic.ps.eduWikiAPI.domain.model.staging

import java.sql.Timestamp
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate

data class LectureStage (
        val classMiscUnitId: Int = 0,
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        val begins: LocalDate = LocalDate.now(),
        val duration: Duration = Duration.ZERO,
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp(0),
        val votes: Int = 1,
        val location: String = ""
)