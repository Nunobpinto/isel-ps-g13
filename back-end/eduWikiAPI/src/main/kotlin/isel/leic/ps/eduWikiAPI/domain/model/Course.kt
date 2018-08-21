package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VERSION
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class Course(
        @ColumnName(COURSE_ID)
        val courseId: Int = -1,
        @ColumnName(COURSE_VERSION)
        val version: Int = 1,
        @ColumnName(COURSE_LOG_ID)
        val logId: Int = 0,
        @ColumnName(COURSE_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(COURSE_FULL_NAME)
        val fullName: String = "",
        @ColumnName(COURSE_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(COURSE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)