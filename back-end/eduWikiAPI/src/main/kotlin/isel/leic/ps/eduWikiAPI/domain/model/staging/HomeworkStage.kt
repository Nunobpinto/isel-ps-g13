package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class HomeworkStage (
        @ColumnName(HOMEWORK_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(HOMEWORK_STAGE_LOG_ID)
        val logId: Int = -1,
        @ColumnName(HOMEWORK_STAGE_SHEET_ID)
        val sheetId: UUID = UUID.randomUUID(),
        @ColumnName(HOMEWORK_STAGE_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(HOMEWORK_STAGE_LATE_DELIVERY)
        val lateDelivery: Boolean = false,
        @ColumnName(HOMEWORK_STAGE_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean = false,
        @ColumnName(HOMEWORK_STAGE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(HOMEWORK_STAGE_VOTES)
        val votes: Int = 0,
        @ColumnName(HOMEWORK_STAGE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)