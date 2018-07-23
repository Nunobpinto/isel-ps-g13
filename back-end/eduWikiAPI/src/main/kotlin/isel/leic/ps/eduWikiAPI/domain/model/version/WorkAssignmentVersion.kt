package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_INDIVIDUAL
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REQUIRES_REPORT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_SHEET
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_SUPPLEMENT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkAssignmentVersion (
        @ColumnName(WORK_ASSIGNMENT_VERSION)
        val version: Int = 1,
        @ColumnName(WORK_ASSIGNMENT_ID)
        val workAssignmentId: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_SHEET)
        val sheet: String = "", //TODO
        @ColumnName(WORK_ASSIGNMENT_SUPPLEMENT)
        val supplement: String? = null, //TODO
        @ColumnName(WORK_ASSIGNMENT_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(WORK_ASSIGNMENT_INDIVIDUAL)
        val individual: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_LATE_DELIVERY)
        val lateDelivery: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_REQUIRES_REPORT)
        val requiresReport: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(WORK_ASSIGNMENT_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)