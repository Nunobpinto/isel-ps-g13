package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class OrganizationReportInputModel (
        @JsonProperty("organization_full_name")
        val fullName: String? = null,
        @JsonProperty("organization_short_name")
        val shortName: String? = null,
        @JsonProperty("organization_address")
        val address: String? = null,
        @JsonProperty("organization_contact")
        val contact: String? = null,
        @JsonProperty("organization_website")
        val website: String? = null
)