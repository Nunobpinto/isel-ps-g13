package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VERSION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VERSION_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VERSION_EXAM_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VERSION_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VERSION_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VERSION_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VERSION_TYPE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VERSION_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VERSION_TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class ExamVersion (
        @ColumnName(EXAM_VERSION_ID)
        val version: Int = 1,
        @ColumnName(EXAM_VERSION_EXAM_ID)
        val examId: Int = 0,
        @ColumnName(EXAM_VERSION_SHEET_ID)
        val sheetId: UUID = UUID.randomUUID(),
        @ColumnName(EXAM_VERSION_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(EXAM_VERSION_TYPE)
        val type: ExamType,
        @ColumnName(EXAM_VERSION_PHASE)
        val phase: String = "",
        @ColumnName(EXAM_VERSION_LOCATION)
        val location: String = "",
        @ColumnName(EXAM_VERSION_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(EXAM_VERSION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)