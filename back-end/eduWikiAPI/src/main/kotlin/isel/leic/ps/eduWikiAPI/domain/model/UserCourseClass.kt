package isel.leic.ps.eduWikiAPI.domain.model

import org.jdbi.v3.core.mapper.reflect.ColumnName
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.USER_USERNAME
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi.Companion.TERM_ID

data class UserCourseClass(
        @ColumnName(USER_USERNAME)
        val username: String = "",
        @ColumnName(COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(CLASS_ID)
        val classId: Int? = null,
        @ColumnName(TERM_ID)
        val termId: Int? = null
)