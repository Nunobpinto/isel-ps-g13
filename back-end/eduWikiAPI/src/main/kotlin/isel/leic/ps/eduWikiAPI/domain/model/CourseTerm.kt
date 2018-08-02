package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_TERM_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_TERM_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_TERM_TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseTerm(
        @ColumnName(COURSE_TERM_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_TERM_TERM_ID)
        val termId: Int = 0,
        @ColumnName(COURSE_TERM_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)