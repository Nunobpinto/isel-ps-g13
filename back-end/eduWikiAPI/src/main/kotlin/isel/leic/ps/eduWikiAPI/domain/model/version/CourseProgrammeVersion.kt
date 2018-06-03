package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_CREDITS
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_LECTURED_TERM
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_OPTIONAL
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_PROG_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROG_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class CourseProgrammeVersion(
        @ColumnName(COURSE_PROG_PROG_ID)
        val programmeId: Int = -1,
        @ColumnName(COURSE_PROG_COURSE_ID)
        val courseId: Int = -1,
        @ColumnName(COURSE_PROG_VERSION)
        val courseProgrammeVersion: Int = 1,
        @ColumnName(COURSE_PROG_LECTURED_TERM)
        val lecturedTerm: String = "",
        @ColumnName(COURSE_PROG_OPTIONAL)
        val optional: Boolean = true,
        @ColumnName(COURSE_PROG_CREDITS)
        val credits: Int = 0,
        @ColumnName(COURSE_PROG_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(0),
        @ColumnName(COURSE_PROG_CREATED_BY)
        val createdBy: String = ""
)