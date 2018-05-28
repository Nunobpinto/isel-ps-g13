package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WRK_ASS_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WRK_ASS_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WRK_ASS_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WRK_ASS_INDIVIDUAL
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WRK_ASS_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WRK_ASS_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WRK_ASS_REQUIRES_REPORT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WRK_ASS_SHEET
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WRK_ASS_SUPPLEMENT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WRK_ASS_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate

data class WorkAssignmentStage (
        @ColumnName(CourseDAOImpl.COURSE_MISC_UNIT_ID)
        val workAssignmentId: Int = 0,
        @ColumnName(WRK_ASS_VOTES)
        val votes: Int = 0,
        @ColumnName(WRK_ASS_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(WRK_ASS_SHEET)
        val sheet: String? = "", //TODO
        @ColumnName(WRK_ASS_SUPPLEMENT)
        val supplement: String? = "", //TODO
        @ColumnName(WRK_ASS_DUE_DATE)
        val dueDate: LocalDate? = LocalDate.now(),
        @ColumnName(WRK_ASS_INDIVIDUAL)
        val individual: Boolean? = false,
        @ColumnName(WRK_ASS_LATE_DELIVERY)
        val lateDelivery: Boolean? = false,
        @ColumnName(WRK_ASS_MULTIPLE_DELIVERIES)
        val multipleDeliveries: Boolean? = false,
        @ColumnName(WRK_ASS_REQUIRES_REPORT)
        val requiresReport: Boolean? = false,
        @ColumnName(WRK_ASS_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(0)
)