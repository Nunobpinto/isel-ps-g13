package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
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