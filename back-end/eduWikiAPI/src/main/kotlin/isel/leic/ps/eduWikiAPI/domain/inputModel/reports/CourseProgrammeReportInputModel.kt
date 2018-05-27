package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class CourseProgrammeReportInputModel(
        @JsonProperty("lectured_term")
        val lecturedTerm: String? = null ,
        val optional: Boolean? = null,
        val credits: Int? = null,
        @JsonProperty("reported_by")
        val reportedBy: String
)