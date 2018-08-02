package isel.leic.ps.eduWikiAPI.domain.outputModel.reports

import java.sql.Timestamp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

data class LectureReportOutputModel (
        val reportId: Int = -1,
        val lectureId: Int = 0,
        val weekDay: DayOfWeek? = null,
        val begins: LocalDate? = null,
        val duration: Int? = null,
        val location: String? = null,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)