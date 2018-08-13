package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_STAGE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_STAGE_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_STAGE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_STAGE_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_STAGE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_STAGE_VOTES
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_STAGE_LOG_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseClassStage (
        @ColumnName(COURSE_CLASS_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(COURSE_CLASS_STAGE_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_CLASS_STAGE_CLASS_ID)
        val classId: Int = 0,
        @ColumnName(COURSE_CLASS_STAGE_TERM_ID)
        val termId: Int = 0,
        @ColumnName(COURSE_CLASS_STAGE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(COURSE_CLASS_STAGE_LOG_ID)
        val logId: Int = 0,
        @ColumnName(COURSE_CLASS_STAGE_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_CLASS_STAGE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)