package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_CREDITS
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_LECTURED_TERM
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_OPTIONAL
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseProgrammeVersion(
        @ColumnName(COURSE_PROGRAMME_VERSION)
        val version: Int = 1,
        @ColumnName(COURSE_PROGRAMME_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_LECTURED_TERM)
        val lecturedTerm: String = "",
        @ColumnName(COURSE_PROGRAMME_OPTIONAL)
        val optional: Boolean = false,
        @ColumnName(COURSE_PROGRAMME_CREDITS)
        val credits: Int = 0,
        @ColumnName(COURSE_PROGRAMME_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        @ColumnName(COURSE_PROGRAMME_CREATED_BY)
        val createdBy: String = ""
)