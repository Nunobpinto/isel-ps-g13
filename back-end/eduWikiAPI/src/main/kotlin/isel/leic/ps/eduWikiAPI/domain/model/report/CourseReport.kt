package isel.leic.ps.eduWikiAPI.domain.model.report

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class CourseReport(
        @ColumnName("report_id")
        val reportId: Int = 0,
        @ColumnName("course_id")
        val courseId: Int = 0,
        @ColumnName("programme_id")
        val programmeId: Int = 0,
        @ColumnName("course_full_name")
        val courseFullName: String? = null,
        @ColumnName("course_short_name")
        val courseShortName: String? = null,
        @ColumnName("reported_by")
        val reportedBy: String = "",
        @ColumnName("votes")
        val votes: Int = 0,
        @ColumnName("timestamp")
        val timestamp: Timestamp = Timestamp(1)
)