package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_CREDITS
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_LECTURED_TERM
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_OPTIONAL
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseProgrammeReport (
        @ColumnName(COURSE_PROGRAMME_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(COURSE_PROGRAMME_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_LECTURED_TERM)
        val lecturedTerm: String? = null,
        @ColumnName(COURSE_PROGRAMME_OPTIONAL)
        val optional: Boolean? = null,
        @ColumnName(COURSE_PROGRAMME_CREDITS)
        val credits: Int? = null,
        @ColumnName(COURSE_PROGRAMME_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        @ColumnName(COURSE_PROGRAMME_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(COURSE_PROGRAMME_VOTES)
        val votes: Int = 0
)