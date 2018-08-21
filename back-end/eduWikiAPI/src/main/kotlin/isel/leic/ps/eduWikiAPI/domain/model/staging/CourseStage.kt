package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseStage (
        @ColumnName(COURSE_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(COURSE_STAGE_FULL_NAME)
        val fullName: String = "",
        @ColumnName(COURSE_STAGE_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(COURSE_STAGE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(COURSE_STAGE_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_STAGE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        @ColumnName(COURSE_STAGE_LOG_ID)
        val logId: Int = 0
)