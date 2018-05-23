package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class ProgrammeReportInputModel (
        @JsonProperty("full_name")
        val fullName: String? = null ,
        @JsonProperty("short_name")
        val shortName: String? = null,
        @JsonProperty("academic_degree")
        val academicDegree: String? = null,
        @JsonProperty("total_credits")
        val totalCredits: Int? = null,
        @JsonProperty("reported_by")
        val reportedBy: String ,
        val duration: Int? = null
)