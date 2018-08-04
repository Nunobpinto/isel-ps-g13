package isel.leic.ps.eduWikiAPI.domain.outputModel.reports

import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseReportOutputModel(
        val reportId: Int = -1,
        val courseId: Int = 0,
        val fullName: String? = null,
        val shortName: String? = null,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)