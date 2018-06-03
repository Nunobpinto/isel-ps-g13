package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_CREDITS
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_LECTURED_TERM
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_OPTIONAL
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_PROG_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class CourseProgrammeReport (
        @ColumnName(COURSE_PROG_REPORT_ID)
        val reportId: Int = 0,
        @ColumnName(COURSE_PROG_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_PROG_PROG_ID)
        val programmeId: Int = 0,
        @ColumnName(COURSE_PROG_LECTURED_TERM)
        val lecturedTerm: String? = null,
        @ColumnName(COURSE_PROG_OPTIONAL)
        val optional: Boolean? = null,
        @ColumnName(COURSE_PROG_CREDITS)
        val credits: Int? = null,
        @ColumnName(COURSE_PROG_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(1),
        @ColumnName(COURSE_PROG_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(COURSE_PROG_VOTES)
        val votes: Int = 0
)