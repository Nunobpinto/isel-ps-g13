package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class ClassReportInputModel (
        @JsonProperty("class_name")
        val className: String? = null,
        @JsonProperty("programme_id")
        val programmeId: Int? = null
)