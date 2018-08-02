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

data class CourseProgrammeVersionOutputModel(
        val version: Int = 1,
        val courseId: Int = 0,
        val programmeId: Int = 0,
        val lecturedTerm: String = "",
        val optional: Boolean = false,
        val credits: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val createdBy: String = ""
)