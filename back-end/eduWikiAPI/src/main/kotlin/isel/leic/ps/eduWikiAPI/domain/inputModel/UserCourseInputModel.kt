package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class UserCourseInputModel (
        @JsonProperty("course_id")
        val courseId: String = ""
)