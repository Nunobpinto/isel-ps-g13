package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_ID
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
        @ColumnName(COURSE_CLASS_ID)
        val courseClassId: Int? = null
)