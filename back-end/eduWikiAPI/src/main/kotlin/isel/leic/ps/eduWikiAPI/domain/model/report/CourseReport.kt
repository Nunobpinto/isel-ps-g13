package isel.leic.ps.eduWikiAPI.domain.model.report

import java.sql.Timestamp

data class CourseReport (
        val reportId: Int = 0,
        val courseId: Int = 0,
        val courseFullName: String? = "",
        val courseShortName: String? = "",
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(1)
)