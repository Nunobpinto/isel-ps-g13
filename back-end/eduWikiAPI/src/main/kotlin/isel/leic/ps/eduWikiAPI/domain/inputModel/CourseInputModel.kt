package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class CourseInputModel(
        @JsonProperty("full_name")
        val fullName: String?,
        @JsonProperty("short_name")
        val shortName: String?,
        @JsonProperty("organization_id")
        val organizationId: Int?,
        @JsonProperty("created_by")
        val createdBy: String = ""
)