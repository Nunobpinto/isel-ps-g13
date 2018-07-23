package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class ProgrammeReportInputModel(
        @JsonProperty("programme_id")
        val programmeId: Int = 0,
        @JsonProperty("programme_full_name")
        val fullName: String? = null,
        @JsonProperty("programme_short_name")
        val shortName: String? = null,
        @JsonProperty("programme_academic_degree")
        val academicDegree: String? = null,
        @JsonProperty("programme_total_credits")
        val totalCredits: Int? = null,
        @JsonProperty("programme_duration")
        val duration: Int? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)