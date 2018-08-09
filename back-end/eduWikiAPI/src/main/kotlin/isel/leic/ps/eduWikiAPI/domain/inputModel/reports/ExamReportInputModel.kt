package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import java.time.LocalDate
import java.util.*

class ExamReportInputModel(
        @JsonProperty("exam_id")
        val examId: Int = 0,
        val sheetId: UUID? = null,
        @JsonProperty("due_date")
        val dueDate: LocalDate? = null,
        @JsonProperty("exam_type")
        val type: ExamType? = null,
        val phase: String? = null,
        val location: String? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)