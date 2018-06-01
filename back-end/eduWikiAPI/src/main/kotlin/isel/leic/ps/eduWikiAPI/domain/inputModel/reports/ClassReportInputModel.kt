package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class ClassReportInputModel (
        @JsonProperty("reported_by")
        val reportedBy: String = "",
        @JsonProperty("class_name")
        val className: String = ""
)