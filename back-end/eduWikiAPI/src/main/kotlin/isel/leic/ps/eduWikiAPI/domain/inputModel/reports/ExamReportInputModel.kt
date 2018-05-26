package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.Year

class ExamReportInputModel(
        val sheet: String? = null,
        @JsonProperty("due_date")
        val dueDate: LocalDate? = null,
        val type: String? = null,
        val phase: String? = null,
        val location: String? = null,
        @JsonProperty("reported_by")
        val reportedBy: String
)