package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_VERSION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_VERSION_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_VERSION_HOMEWORK_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_VERSION_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_VERSION_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_VERSION_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_VERSION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_VERSION_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_VERSION_NAME
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class HomeworkVersion(
        @ColumnName(HOMEWORK_VERSION_ID)
        val version: Int = 1,
        @ColumnName(HOMEWORK_VERSION_HOMEWORK_ID)
        val homeworkId: Int = 0,
        @ColumnName(HOMEWORK_VERSION_SHEET_ID)
        val sheetId: UUID? = UUID.randomUUID(),
        @ColumnName(HOMEWORK_VERSION_NAME)
        val homeworkName: String = "",
        @ColumnName(HOMEWORK_VERSION_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(HOMEWORK_VERSION_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(HOMEWORK_VERSION_LATE_DELIVERY)
        val lateDelivery: Boolean = false,
        @ColumnName(HOMEWORK_VERSION_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean = false,
        @ColumnName(HOMEWORK_VERSION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)