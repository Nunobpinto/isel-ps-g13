package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseClass (
        @ColumnName(COURSE_CLASS_ID)
        val courseClassId: Int = -1,
        @ColumnName(COURSE_CLASS_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_CLASS_CLASS_ID)
        val classId: Int = 0,
        @ColumnName(COURSE_CLASS_TERM_ID)
        val termId: Int = 0,
        @ColumnName(COURSE_CLASS_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(COURSE_CLASS_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_CLASS_LOG_ID)
        val logId: Int = 0,
        @ColumnName(COURSE_CLASS_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)