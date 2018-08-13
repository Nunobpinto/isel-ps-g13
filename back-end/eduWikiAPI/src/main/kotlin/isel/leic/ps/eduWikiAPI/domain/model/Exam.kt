package isel.leic.ps.eduWikiAPI.domain.model;

import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_TYPE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VERSION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VOTES
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate;
import java.time.LocalDateTime
import java.util.*

data class Exam (
        @ColumnName(EXAM_ID)
        val examId: Int = 0,
        @ColumnName(EXAM_VERSION)
        val version: Int = 1,
        @ColumnName(EXAM_LOG_ID)
        val logId: Int = 0,
        @ColumnName(EXAM_VOTES)
        val votes: Int = 0,
        @ColumnName(EXAM_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(EXAM_SHEET_ID)
        val sheetId: UUID = UUID.randomUUID(),
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
