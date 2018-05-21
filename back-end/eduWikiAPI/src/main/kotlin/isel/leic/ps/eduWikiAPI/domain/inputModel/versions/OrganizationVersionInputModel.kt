package isel.leic.ps.eduWikiAPI.domain.inputModel.versions

import com.fasterxml.jackson.annotation.JsonProperty

class OrganizationVersionInputModel (
        val version: Int,
        @JsonProperty("created_by")
        val createdBy: String,
        @JsonProperty("full_name")
        val fullName: String,
        @JsonProperty("short_name")
        val shortName: String,
        val address: String,
        val contact: String,
        val timestamp: String
)