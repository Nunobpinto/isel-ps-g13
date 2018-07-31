package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

data class UserProgrammeInputModel (
        @JsonProperty("programme_id")
        val programmeId: Int
)