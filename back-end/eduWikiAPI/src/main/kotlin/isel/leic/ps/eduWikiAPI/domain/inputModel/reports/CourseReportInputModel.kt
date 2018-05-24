package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class CourseReportInputModel (
        @JsonProperty("full_name")
        val fullName: String? = null ,
        @JsonProperty("short_name")
        val shortName: String? = null,
        @JsonProperty("reported_by")
        val reportedBy: String
)