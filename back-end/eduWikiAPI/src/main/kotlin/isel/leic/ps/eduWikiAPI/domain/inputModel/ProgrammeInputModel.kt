package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class ProgrammeInputModel(
        @JsonProperty("programme_full_name")
        val fullName: String = "",
        @JsonProperty("programme_short_name")
        val shortName: String = "",
        @JsonProperty("programme_academic_degree")
        val academicDegree: String = "",
        @JsonProperty("programme_total_credits")
        val totalCredits: Int = 0,
        @JsonProperty("created_by")
        val createdBy: String = "",
        @JsonProperty("programme_duration")
        val duration: Int = 0
)