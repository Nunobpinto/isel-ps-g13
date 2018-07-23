package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_SHEET
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_TYPE
import java.time.LocalDate

class ExamReportInputModel(
        @JsonProperty("exam_id")
        val examId: Int = 0,
        val sheet: String? = null,
        @JsonProperty("due_date")
        val dueDate: LocalDate? = null,
        @JsonProperty("exam_type")
        val type: String? = null,
        val phase: String? = null,
        val location: String? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)