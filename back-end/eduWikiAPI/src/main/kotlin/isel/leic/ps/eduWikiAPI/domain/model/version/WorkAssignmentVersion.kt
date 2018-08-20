package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_INDIVIDUAL
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_PHASE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_REQUIRES_REPORT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_SUPPLEMENT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION_WORK_ASSIGN_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class WorkAssignmentVersion (
        @ColumnName(WORK_ASSIGNMENT_VERSION_ID)
        val version: Int = 1,
        @ColumnName(WORK_ASSIGNMENT_VERSION_WORK_ASSIGN_ID)
        val workAssignmentId: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_VERSION_PHASE)
        val phase: String = "",
        @ColumnName(WORK_ASSIGNMENT_VERSION_SHEET_ID)
        val sheetId: UUID = UUID.randomUUID(),
        @ColumnName(WORK_ASSIGNMENT_VERSION_SUPPLEMENT)
        val supplementId: UUID = UUID.randomUUID(),
        @ColumnName(WORK_ASSIGNMENT_VERSION_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(WORK_ASSIGNMENT_VERSION_INDIVIDUAL)
        val individual: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_VERSION_LATE_DELIVERY)
        val lateDelivery: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_VERSION_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_VERSION_REQUIRES_REPORT)
        val requiresReport: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_VERSION_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(WORK_ASSIGNMENT_VERSION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)