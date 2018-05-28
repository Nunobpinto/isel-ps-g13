package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_TYPE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

class CourseMiscUnit(
        @ColumnName(COURSE_MISC_UNIT_ID)
        val id: Int? = 0,
        @ColumnName(COURSE_MISC_TYPE)
        val miscType: String? = null,
        @ColumnName(COURSE_ID)
        val courseId: Int? = 0,
        @ColumnName(TERM_ID)
        val termId: Int? = 0,
        @ColumnName(COURSE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(1)
)