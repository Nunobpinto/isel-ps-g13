package isel.leic.ps.eduWikiAPI.domain.model.report

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

data class CourseReport(
        @ColumnName(COURSE_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_FULL_NAME)
        val fullName: String? = null,
        @ColumnName(COURSE_SHORT_NAME)
        val shortName: String? = null,
        @ColumnName(COURSE_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(COURSE_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)