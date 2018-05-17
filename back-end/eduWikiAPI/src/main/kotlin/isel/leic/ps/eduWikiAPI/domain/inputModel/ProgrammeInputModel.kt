package isel.leic.ps.eduWikiAPI.domain.inputModel

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
        @JsonProperty("created_by")
        val createdBy: String,
        val duration: Int,
        @JsonProperty("organization_id")
        val organizationId: Int,
        val version: Int
)