package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class OrganizationReportInputModel (
        @JsonProperty("organization_id")
        val organizationId: Int = 0,
        @JsonProperty("organization_full_name")
        val fullName: String? = null,
        @JsonProperty("organization_short_name")
        val shortName: String? = null,
        @JsonProperty("organization_address")
        val address: String? = null,
        @JsonProperty("organization_contact")
        val contact: String? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)