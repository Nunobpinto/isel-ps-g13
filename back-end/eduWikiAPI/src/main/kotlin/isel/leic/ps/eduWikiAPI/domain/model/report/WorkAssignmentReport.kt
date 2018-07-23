package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_INDIVIDUAL
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REQUIRES_REPORT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_SHEET
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_SUPPLEMENT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkAssignmentReport (
        @ColumnName(WORK_ASSIGNMENT_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(WORK_ASSIGNMENT_ID)
        val workAssignmentId: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_SHEET)
        val sheet: String? = null, //TODO ficheiros
        @ColumnName(WORK_ASSIGNMENT_SUPPLEMENT)
        val supplement: String? = null, //TODO ficheiros
        @ColumnName(WORK_ASSIGNMENT_DUE_DATE)
        val dueDate: LocalDate? = null,
        @ColumnName(WORK_ASSIGNMENT_INDIVIDUAL)
        val individual: Boolean? = null,
        @ColumnName(WORK_ASSIGNMENT_LATE_DELIVERY)
        val lateDelivery: Boolean? = null,
        @ColumnName(WORK_ASSIGNMENT_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean? = null,
        @ColumnName(WORK_ASSIGNMENT_REQUIRES_REPORT)
        val requiresReport: Boolean? = null,
        @ColumnName(WORK_ASSIGNMENT_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(WORK_ASSIGNMENT_VOTES)
        val votes: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)