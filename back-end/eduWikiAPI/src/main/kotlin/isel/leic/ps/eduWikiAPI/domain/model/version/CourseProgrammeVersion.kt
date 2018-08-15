package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_VERSION_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_VERSION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_VERSION_CREDITS
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_VERSION_LECTURED_TERM
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_VERSION_OPTIONAL
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_VERSION_PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_VERSION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_VERSION_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseProgrammeVersion(
        @ColumnName(COURSE_PROGRAMME_VERSION_ID)
        val version: Int = 1,
        @ColumnName(COURSE_PROGRAMME_VERSION_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_VERSION_PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_VERSION_LECTURED_TERM)
        val lecturedTerm: String = "",
        @ColumnName(COURSE_PROGRAMME_VERSION_OPTIONAL)
        val optional: Boolean = false,
        @ColumnName(COURSE_PROGRAMME_VERSION_CREDITS)
        val credits: Int = 0,
        @ColumnName(COURSE_PROGRAMME_VERSION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        @ColumnName(COURSE_PROGRAMME_VERSION_CREATED_BY)
        val createdBy: String = ""
)