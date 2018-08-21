package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class CourseInputModel(
        @JsonProperty("course_full_name")
        val fullName: String = "",
        @JsonProperty("course_short_name")
        val shortName: String = ""
)