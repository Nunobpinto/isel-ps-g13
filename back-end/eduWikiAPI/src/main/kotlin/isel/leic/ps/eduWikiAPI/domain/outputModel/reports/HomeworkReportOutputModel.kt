package isel.leic.ps.eduWikiAPI.domain.outputModel.reports

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

data class HomeworkReportOutputModel (
        val reportId: Int = -1,
        val homeworkId: Int = 0,
        val sheet: String? = null,
        val dueDate: LocalDate? = null,
        val lateDelivery: Boolean? = null,
        val multipleDeliveries: Boolean? = null,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)