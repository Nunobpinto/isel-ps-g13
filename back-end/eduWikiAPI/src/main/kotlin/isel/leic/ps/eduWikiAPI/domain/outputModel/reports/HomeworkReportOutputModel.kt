package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_SHEET
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class HomeworkReport (
        @ColumnName(HOMEWORK_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(HOMEWORK_ID)
        val homeworkId: Int = 0,
        @ColumnName(HOMEWORK_SHEET)
        val sheet: String? = null,
        @ColumnName(HOMEWORK_DUE_DATE)
        val dueDate: LocalDate? = null,
        @ColumnName(HOMEWORK_LATE_DELIVERY)
        val lateDelivery: Boolean? = null,
        @ColumnName(HOMEWORK_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean? = null,
        @ColumnName(HOMEWORK_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(HOMEWORK_VOTES)
        val votes: Int = 0,
        @ColumnName(HOMEWORK_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)