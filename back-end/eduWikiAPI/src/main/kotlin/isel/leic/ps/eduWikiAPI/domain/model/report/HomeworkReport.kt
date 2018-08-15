package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_HOMEWORK_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class HomeworkReport (
        @ColumnName(HOMEWORK_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(HOMEWORK_REPORT_HOMEWORK_ID)
        val homeworkId: Int = 0,
        @ColumnName(HOMEWORK_REPORT_LOG_ID)
        val logId: Int = 0,
        @ColumnName(HOMEWORK_REPORT_SHEET_ID)
        val sheetId: UUID? = null,
        @ColumnName(HOMEWORK_REPORT_DUE_DATE)
        val dueDate: LocalDate? = null,
        @ColumnName(HOMEWORK_REPORT_LATE_DELIVERY)
        val lateDelivery: Boolean? = null,
        @ColumnName(HOMEWORK_REPORT_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean? = null,
        @ColumnName(HOMEWORK_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(HOMEWORK_REPORT_VOTES)
        val votes: Int = 0,
        @ColumnName(HOMEWORK_REPORT_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)