package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_ORG_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class CourseVersion(
        @ColumnName(COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_ORG_ID)
        val organizationId: Int = 0,
        @ColumnName(COURSE_VERSION)
        val version: Int = 0,
        @ColumnName(COURSE_FULL_NAME)
        val fullName: String = "",
        @ColumnName(COURSE_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(COURSE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(0),
        @ColumnName(COURSE_CREATED_BY)
        val createdBy: String = ""
)