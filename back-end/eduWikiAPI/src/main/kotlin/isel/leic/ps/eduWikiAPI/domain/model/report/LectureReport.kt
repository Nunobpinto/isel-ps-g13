package isel.leic.ps.eduWikiAPI.domain.model.report

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate

data class LectureReport (
     val reportId: Int = 0,
     val classMiscUnitId: Int = 0,
     val weekDay: DayOfWeek = DayOfWeek.MONDAY,
     val begins: LocalDate = LocalDate.now(),
     val duration: Duration = Duration.ZERO,
     val location: String = "",
     val madeBy: String = "",
     val votes: Int = 0
)