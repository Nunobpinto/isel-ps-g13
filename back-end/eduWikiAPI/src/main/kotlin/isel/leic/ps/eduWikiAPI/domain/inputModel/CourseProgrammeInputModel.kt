package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class CourseProgrammeInputModel (
        @JsonProperty("course_id")
        val courseId: Int,
        @JsonProperty("programme_id")
        val programmeId: Int,
        @JsonProperty("lectured_term")
        val lecturedTerm: String = "",
        @JsonProperty("created_by")
        val createdBy: String = "",
        @JsonProperty("optional")
        val optional: Boolean = false,
        @JsonProperty("credits")
        val credits: Int = 0

)