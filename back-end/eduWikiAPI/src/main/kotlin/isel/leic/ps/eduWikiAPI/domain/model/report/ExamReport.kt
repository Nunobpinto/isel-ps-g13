package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_EXAM_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_TYPE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_VOTES
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class ExamReport (
        @ColumnName(EXAM_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(EXAM_REPORT_EXAM_ID)
        val examId: Int = 0,
        @ColumnName(COURSE_MISC_UNIT_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_MISC_UNIT_TERM_ID)
        val termId: Int = 0,
        @ColumnName(EXAM_REPORT_LOG_ID)
        val logId: Int = 0,
        @ColumnName(EXAM_REPORT_SHEET_ID)
        val sheetId: UUID? = null,
        @ColumnName(EXAM_REPORT_DUE_DATE)
        val dueDate: LocalDate? = null,
        @ColumnName(EXAM_REPORT_TYPE)
        val type: ExamType? = null,
        @ColumnName(EXAM_REPORT_PHASE)
        val phase: String? = null,
        @ColumnName(EXAM_REPORT_LOCATION)
        val location: String? = null,
        @ColumnName(EXAM_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(EXAM_REPORT_VOTES)
        val votes: Int = 0,
        @ColumnName(EXAM_REPORT_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)