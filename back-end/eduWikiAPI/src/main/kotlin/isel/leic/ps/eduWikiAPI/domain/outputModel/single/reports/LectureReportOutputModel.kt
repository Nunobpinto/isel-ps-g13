package isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports

import java.sql.Time
import java.sql.Timestamp
import java.time.*

data class LectureReportOutputModel (
        val reportId: Int = -1,
        val className: String = "",
        val lecturedTerm: String = "",
        val courseShortName: String = "",
        val lectureId: Int = 0,
        val weekDay: DayOfWeek? = null,
        val begins: LocalTime? = null,
        val duration: Duration? = null,
        val location: String? = null,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)