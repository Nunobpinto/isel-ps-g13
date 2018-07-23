package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_SHEET
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class HomeworkVersion(
        @ColumnName(HOMEWORK_VERSION)
        val version: Int = 1,
        @ColumnName(HOMEWORK_ID)
        val homeworkId: Int = 0,
        @ColumnName(HOMEWORK_SHEET)
        val sheet: String = "",
        @ColumnName(HOMEWORK_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(HOMEWORK_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(HOMEWORK_LATE_DELIVERY)
        val lateDelivery: Boolean = false,
        @ColumnName(HOMEWORK_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean = false,
        @ColumnName(HOMEWORK_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)