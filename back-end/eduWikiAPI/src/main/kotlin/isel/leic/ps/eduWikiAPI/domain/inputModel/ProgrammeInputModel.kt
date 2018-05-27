package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class ProgrammeInputModel(
        @JsonProperty("full_name")
        val fullName: String? = null,
        @JsonProperty("short_name")
        val shortName: String? = null,
        @JsonProperty("academic_degree")
        val academicDegree: String? = null,
        @JsonProperty("total_credits")
        val totalCredits: Int? = null,
        @JsonProperty("created_by")
        val createdBy: String? = null,
        val duration: Int? = null,
        @JsonProperty("organization_id")
        val organizationId: Int? = null,
        val version: Int = 1
)