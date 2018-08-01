package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_SHEET
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_TYPE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_VOTES
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class ExamStage (
        @ColumnName(EXAM_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(EXAM_SHEET)
        val sheet: String = "",
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
        @ColumnName(EXAM_VOTES)
        val votes: Int = 0,
        @ColumnName(EXAM_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)