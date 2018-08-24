package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_TYPE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_VOTES
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class ExamStage (
        @ColumnName(EXAM_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(COURSE_MISC_UNIT_STAGE_COURSE_ID)
        val courseId: Int = -1,
        @ColumnName(COURSE_MISC_UNIT_STAGE_TERM_ID)
        val termId: Int = -1,
        @ColumnName(EXAM_STAGE_LOG_ID)
        val logId: Int = 0,
        @ColumnName(EXAM_STAGE_SHEET_ID)
        val sheetId: UUID = UUID.randomUUID(),
        @ColumnName(EXAM_STAGE_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(EXAM_STAGE_TYPE)
        val type: ExamType,
        @ColumnName(EXAM_STAGE_PHASE)
        val phase: String = "",
        @ColumnName(EXAM_STAGE_LOCATION)
        val location: String = "",
        @ColumnName(EXAM_STAGE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(EXAM_STAGE_VOTES)
        val votes: Int = 0,
        @ColumnName(EXAM_STAGE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)