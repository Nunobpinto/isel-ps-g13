package isel.leic.ps.eduWikiAPI.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class CourseInputModel(
        @JsonProperty("full_name")
        val fullName: String,
        @JsonProperty("short_name")
        val shortName: String,
        val semester: Int,
        val optional: Boolean,
        val credits: Int
)