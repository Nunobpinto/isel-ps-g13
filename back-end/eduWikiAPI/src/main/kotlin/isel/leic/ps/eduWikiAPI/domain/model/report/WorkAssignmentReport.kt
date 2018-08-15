package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_INDIVIDUAL
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_WORK_ASSIGN_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_REQUIRES_REPORT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_SUPPLEMENT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class WorkAssignmentReport (
        @ColumnName(WORK_ASSIGNMENT_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(WORK_ASSIGNMENT_REPORT_LOG_ID)
        val logId: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_REPORT_WORK_ASSIGN_ID)
        val workAssignmentId: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_REPORT_SHEET_ID)
        val sheetId: UUID = UUID.randomUUID(),
        @ColumnName(WORK_ASSIGNMENT_REPORT_SUPPLEMENT)
        val supplementId: UUID = UUID.randomUUID(),
        @ColumnName(WORK_ASSIGNMENT_REPORT_DUE_DATE)
        val dueDate: LocalDate? = null,
        @ColumnName(WORK_ASSIGNMENT_REPORT_INDIVIDUAL)
        val individual: Boolean? = null,
        @ColumnName(WORK_ASSIGNMENT_REPORT_LATE_DELIVERY)
        val lateDelivery: Boolean? = null,
        @ColumnName(WORK_ASSIGNMENT_REPORT_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean? = null,
        @ColumnName(WORK_ASSIGNMENT_REPORT_REQUIRES_REPORT)
        val requiresReport: Boolean? = null,
        @ColumnName(WORK_ASSIGNMENT_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(WORK_ASSIGNMENT_REPORT_VOTES)
        val votes: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_REPORT_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)