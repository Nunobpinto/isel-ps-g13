package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class UserCourseClassInputModel (
        @JsonProperty("course_id")
        val courseId: Int = 0,
        @JsonProperty("course_class_id")
        val courseClassId: Int? = null
)