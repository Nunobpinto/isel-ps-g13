package isel.leic.ps.eduWikiAPI.domain.model.report

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class CourseClassReport (
        @ColumnName("report_id")
        val reportId: Int = 0,
        @ColumnName("votes")
        val votes: Int = 0,
        @ColumnName("course_id")
        val courseId: Int = -1,
        @ColumnName("term_id")
        val termId: Int = -1,
        @ColumnName("timestamp")
        val timestamp: Timestamp = Timestamp(1),
        @ColumnName("reported_by")
        val reportedBy: String = ""
)