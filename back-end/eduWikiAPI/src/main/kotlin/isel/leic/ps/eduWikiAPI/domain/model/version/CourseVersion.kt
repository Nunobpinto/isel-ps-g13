package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION_ORG_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseVersion(
        @ColumnName(COURSE_VERSION_ID)
        val version: Int = 1,
        @ColumnName(COURSE_VERSION_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_VERSION_ORG_ID)
        val organizationId: Int = 0,
        @ColumnName(COURSE_VERSION_FULL_NAME)
        val fullName: String = "",
        @ColumnName(COURSE_VERSION_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(COURSE_VERSION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        @ColumnName(COURSE_VERSION_CREATED_BY)
        val createdBy: String = ""
)