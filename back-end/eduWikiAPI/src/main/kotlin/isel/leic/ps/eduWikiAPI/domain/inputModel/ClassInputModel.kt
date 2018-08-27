package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class ClassInputModel (
        @JsonProperty("class_name")
        val className: String = "",
        @JsonProperty("term_id")
        val termId: Int = 0,
        @JsonProperty("programme_id")
        val programmeId: Int = 0
)