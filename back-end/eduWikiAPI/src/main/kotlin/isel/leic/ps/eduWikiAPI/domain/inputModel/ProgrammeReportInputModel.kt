package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class ProgrammeReportInputModel (
        @JsonProperty("programme_id")
        val programmeId: Int,
        @JsonProperty("full_name")
        val fullName: String,
        @JsonProperty("short_name")
        val shortName: String,
        @JsonProperty("academic_degree")
        val academicDegree: String,
        @JsonProperty("total_credits")
        val totalCredits: Int,
        @JsonProperty("reported_by")
        val reportedBy: String,
        val duration: Int,
        val timestamp: String = ""
)