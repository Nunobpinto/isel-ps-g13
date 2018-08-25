package isel.leic.ps.eduWikiAPI.domain.model;

import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_TYPE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_VERSION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_VOTES
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate;
import java.time.LocalDateTime
import java.util.*

data class Exam (
        @ColumnName(EXAM_ID)
        val examId: Int = 0,
        @ColumnName(COURSE_MISC_UNIT_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_MISC_UNIT_TERM_ID)
        val termId: Int = 0,
        @ColumnName(EXAM_VERSION)
        val version: Int = 1,
        @ColumnName(EXAM_LOG_ID)
        val logId: Int = 0,
        @ColumnName(EXAM_VOTES)
        val votes: Int = 0,
        @ColumnName(EXAM_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(EXAM_SHEET_ID)
        val sheetId: UUID? = UUID.randomUUID(),
        @ColumnName(EXAM_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(EXAM_TYPE)
        val type: ExamType,
        @ColumnName(EXAM_PHASE)
        val phase: String = "",
        @ColumnName(EXAM_LOCATION)
        val location: String = "",
        @ColumnName(EXAM_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)
