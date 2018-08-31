package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_INDIVIDUAL
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_REQUIRES_REPORT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_SHEET_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_SUPPLEMENT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_VOTES
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_PHASE
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class WorkAssignmentStage (
        @ColumnName(WORK_ASSIGNMENT_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(COURSE_MISC_UNIT_STAGE_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(COURSE_MISC_UNIT_STAGE_TERM_ID)
        val termId: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_STAGE_LOG_ID)
        val logId: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_STAGE_PHASE)
        val phase: String = "",
        @ColumnName(WORK_ASSIGNMENT_STAGE_SHEET_ID)
        val sheetId: UUID? = UUID.randomUUID(),
        @ColumnName(WORK_ASSIGNMENT_STAGE_SUPPLEMENT)
        val supplementId: UUID? = UUID.randomUUID(),
        @ColumnName(WORK_ASSIGNMENT_STAGE_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(WORK_ASSIGNMENT_STAGE_INDIVIDUAL)
        val individual: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_STAGE_LATE_DELIVERY)
        val lateDelivery: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_STAGE_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_STAGE_REQUIRES_REPORT)
        val requiresReport: Boolean = false,
        @ColumnName(WORK_ASSIGNMENT_STAGE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(WORK_ASSIGNMENT_STAGE_VOTES)
        val votes: Int = 0,
        @ColumnName(WORK_ASSIGNMENT_STAGE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)