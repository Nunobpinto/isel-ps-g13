package isel.leic.ps.eduWikiAPI.domain.outputModel.reports

import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
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