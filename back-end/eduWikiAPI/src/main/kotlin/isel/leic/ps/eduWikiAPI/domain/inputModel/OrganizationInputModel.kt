package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class OrganizationInputModel(
        @JsonProperty("organization_full_name")
        val fullName: String = "",
        @JsonProperty("organization_short_name")
        val shortName: String = "",
        @JsonProperty("organization_address")
        val address: String = "",
        @JsonProperty("organization_contact")
        val contact: String = ""
)