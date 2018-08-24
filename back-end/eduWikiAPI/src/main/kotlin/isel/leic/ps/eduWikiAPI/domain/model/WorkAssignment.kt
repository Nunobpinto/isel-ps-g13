package isel.leic.ps.eduWikiAPI.domain.model;

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_INDIVIDUAL
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_PHASE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REQUIRES_REPORT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_SUPPLEMENT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VERSION
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class WorkAssignment (
        @ColumnName(WORK_ASSIGNMENT_ID)
        val workAssignmentId: Int = -1,
        @ColumnName(COURSE_MISC_UNIT_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_MISC_UNIT_TERM_ID)
        val termId: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_VERSION)
        val version: Int = 1,
        @ColumnName(WORK_ASSIGNMENT_LOG_ID)
        val logId: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_VOTES)
        val votes: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(WORK_ASSIGNMENT_PHASE)
        val phase: String = "",
        @ColumnName(WORK_ASSIGNMENT_SHEET_ID)
        val sheetId:  UUID = UUID.randomUUID(),
        @ColumnName(WORK_ASSIGNMENT_SUPPLEMENT)
        val supplementId:  UUID = UUID.randomUUID(),
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
        @ColumnName(WORK_ASSIGNMENT_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)
