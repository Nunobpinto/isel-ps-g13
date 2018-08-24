package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

data class TenantRequestDetails(
        @JsonProperty("full_name")
        val fullName: String = "",
        @JsonProperty("short_name")
        val shortName: String = "",
        @JsonProperty("address")
        val address: String = "",
        @JsonProperty("contact")
        val contact: String = "",
        @JsonProperty("website")
        val website: String = "",
        @JsonProperty("email_pattern")
        val emailPattern: String = "",
        @JsonProperty("organization_summary")
        val organizationSummary: String = "",
        @JsonProperty("requesters")
        val requesters: ArrayList<TenantRequester>
)
