package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_ID
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_INDIVIDUAL
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_LATE_DELIVERY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_MULTIPLE_DELIVERIES
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REQUIRES_REPORT
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_SHEET
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_SUPPLEMENT
import java.time.LocalDate

class WorkAssignmentReportInputModel (
        @JsonProperty("work_assignment_id")
        val workAssignmentId: Int = 0,
        val sheet: String? = null,
        val supplement: String? = null,
        @JsonProperty("due_date")
        val dueDate: LocalDate? = null,
        val individual: Boolean? = null,
        @JsonProperty("late_delivery")
        val lateDelivery: Boolean? = null,
        @JsonProperty("multipleDeliveries")
        val multipleDeliveries: Boolean? = null,
        @JsonProperty("requires_report")
        val requiresReport: Boolean? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)