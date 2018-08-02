package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_ORGANIZATION_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseVersionOutputModel(
        val version: Int = 1,
        val courseId: Int = 0,
        val organizationId: Int = 0,
        val fullName: String = "",
        val shortName: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val createdBy: String = ""
)