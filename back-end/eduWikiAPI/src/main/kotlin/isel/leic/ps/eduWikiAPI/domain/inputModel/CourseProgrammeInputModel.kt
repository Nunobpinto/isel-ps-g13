package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class CourseProgrammeInputModel (
        @JsonProperty("course_id")
        val courseId: Int = 0,
        @JsonProperty("programme_id")
        val programmeId: Int = 0,
        @JsonProperty("course_lectured_term")
        val lecturedTerm: String = "",
        @JsonProperty("created_by")
        val createdBy: String = "",
        @JsonProperty("course_optional")
        val optional: Boolean = false,
        @JsonProperty("course_credits")
        val credits: Int = 0
)