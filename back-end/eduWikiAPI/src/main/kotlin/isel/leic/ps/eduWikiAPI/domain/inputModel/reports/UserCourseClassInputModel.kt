package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class UserCourseClassInputModel (
        @JsonProperty("course_id")
        val courseId: Int = 0,
        @JsonProperty("class_id")
        val classId: Int? = null,
        @JsonProperty("term_id")
        val termId: Int? = null
)