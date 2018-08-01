package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_ORGANIZATION_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_VERSION
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_VOTES
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_CREDITS
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_LECTURED_TERM
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_OPTIONAL
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_PROGRAMME_PROGRAMME_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class Course(
        @ColumnName(COURSE_ID)
        val courseId: Int = -1,
        @ColumnName(COURSE_ORGANIZATION_ID)
        val organizationId: Int = 0,
        @ColumnName(COURSE_VERSION)
        val version: Int = 1,
        @ColumnName(COURSE_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(COURSE_FULL_NAME)
        val fullName: String = "",
        @ColumnName(COURSE_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(COURSE_PROGRAMME_LECTURED_TERM)
        val lecturedTerm: String = "",
        @ColumnName(COURSE_PROGRAMME_PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_OPTIONAL)
        val optional: Boolean = false,
        @ColumnName(COURSE_PROGRAMME_CREDITS)
        val credits: Int = 0,
        @ColumnName(COURSE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)