package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class OrganizationInputModel(
        @JsonProperty("full_name")
        val fullName: String,
        @JsonProperty("short_name")
        val shortName: String,
        val address: String,
        val contact: String,
        @JsonProperty("created_by")
        val createdBy: String
)