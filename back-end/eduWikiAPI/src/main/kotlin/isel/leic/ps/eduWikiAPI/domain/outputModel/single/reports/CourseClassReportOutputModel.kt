package isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports

import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseClassReportOutputModel(
        val reportId: Int = -1,
        val courseClassId: Int = 0,
        val classId: Int? = null,
        val votes: Int = 0,
        val courseId: Int? = null,
        val termId: Int = 0,
        val reportedBy: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val deletePermanently: Boolean = false,
        val courseShortName: String? = null,
        val termShortName: String = "",
        val className: String? = null
)