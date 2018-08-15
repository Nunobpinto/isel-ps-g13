package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_CREDITS
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_LECTURED_TERM
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_OPTIONAL
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseProgrammeStage(
        @ColumnName(COURSE_PROGRAMME_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(COURSE_PROGRAMME_STAGE_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_STAGE_PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(COURSE_PROGRAMME_STAGE_LECTURED_TERM)
        val lecturedTerm: String = "",
        @ColumnName(COURSE_PROGRAMME_STAGE_OPTIONAL)
        val optional: Boolean = false,
        @ColumnName(COURSE_PROGRAMME_STAGE_CREDITS)
        val credits: Int = 0,
        @ColumnName(COURSE_PROGRAMME_STAGE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(COURSE_PROGRAMME_STAGE_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_PROGRAMME_STAGE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        @ColumnName(COURSE_PROGRAMME_STAGE_LOG_ID)
        val logId: Int = 0
)