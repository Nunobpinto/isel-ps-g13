package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class ClassInputModel (
        @JsonProperty("full_name")
        val fullName: String = "",
        val courseId: String,
        val term: Int,
        @JsonProperty("created_by")
        val createdBy: String

)