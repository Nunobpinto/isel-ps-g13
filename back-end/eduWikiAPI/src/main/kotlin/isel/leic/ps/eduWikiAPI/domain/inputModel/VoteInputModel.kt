package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class VoteInputModel (
        val vote: String,
        @JsonProperty("created_by")
        val createdBy: String = ""
)
