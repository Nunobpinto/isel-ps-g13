package isel.leic.ps.eduWikiAPI.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class ProgrammeInputModel (
        @JsonProperty("full_name")
        val fullName: String,
        @JsonProperty("short_name")
        val shortName: String,
        @JsonProperty("academic_degree")
        val academicDegree: String,
        @JsonProperty("total_credits")
        val totalCredits: Int,
        val duration: Int
)