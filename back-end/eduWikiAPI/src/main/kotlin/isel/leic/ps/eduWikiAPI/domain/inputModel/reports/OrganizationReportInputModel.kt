package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class OrganizationReportInputModel (
        @JsonProperty("full_name")
        val fullName: String? = null,
        @JsonProperty("short_name")
        val shortName: String? =null,
        val address: String? =null,
        val contact: String? = null,
        @JsonProperty("created_by")
        val createdBy: String = ""
)