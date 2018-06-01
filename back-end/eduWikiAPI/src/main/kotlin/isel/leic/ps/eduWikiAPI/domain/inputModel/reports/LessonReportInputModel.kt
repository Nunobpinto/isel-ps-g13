package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class LessonReportInputModel (
        val weekday: String? = null,
        val begins: String? =null,
        val duration: String? =null,
        val location: String? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)