package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_SHEET
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_TYPE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_VERSION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class ExamVersion (
        @ColumnName(EXAM_VERSION)
        val version: Int = 1,
        @ColumnName(EXAM_ID)
        val examId: Int = 0,
        @ColumnName(EXAM_SHEET)
        val sheet: String = "", //TODO
        @ColumnName(EXAM_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(EXAM_TYPE)
        val type: String = "",
        @ColumnName(EXAM_PHASE)
        val phase: String = "",
        @ColumnName(EXAM_LOCATION)
        val location: String = "",
        @ColumnName(EXAM_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(EXAM_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)