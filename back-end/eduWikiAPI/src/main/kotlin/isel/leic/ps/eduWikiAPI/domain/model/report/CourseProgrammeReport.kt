package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_CREDITS
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_DELETE_FLAG
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_LECTURED_TERM
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_OPTIONAL
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseProgrammeReport (
        @ColumnName(COURSE_PROGRAMME_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(COURSE_PROGRAMME_REPORT_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_REPORT_PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_REPORT_LECTURED_TERM)
        val lecturedTerm: String? = null,
        @ColumnName(COURSE_PROGRAMME_REPORT_OPTIONAL)
        val optional: Boolean? = null,
        @ColumnName(COURSE_PROGRAMME_REPORT_CREDITS)
        val credits: Int? = null,
        @ColumnName(COURSE_PROGRAMME_REPORT_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        @ColumnName(COURSE_PROGRAMME_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(COURSE_PROGRAMME_REPORT_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_PROGRAMME_REPORT_DELETE_FLAG)
        val deleteFlag: Boolean,
        @ColumnName(COURSE_PROGRAMME_REPORT_LOG_ID)
        val logId: Int = 0
)