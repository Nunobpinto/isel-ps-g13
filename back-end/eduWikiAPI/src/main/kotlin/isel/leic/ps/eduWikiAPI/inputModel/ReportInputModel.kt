package isel.leic.ps.eduWikiAPI.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class ReportInputModel (
        @JsonProperty("reported_field")
        val reportedField: String,
        @JsonProperty("suggested_value")
        val suggestedValue: String,
        val reason: String,
        val username: String
)