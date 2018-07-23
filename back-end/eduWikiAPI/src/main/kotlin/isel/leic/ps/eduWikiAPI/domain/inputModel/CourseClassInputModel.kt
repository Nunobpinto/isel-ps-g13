package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

data class CourseClassInputModel(
        @JsonProperty("course_id")
        val courseId: Int = 0,
        @JsonProperty("class_id")
        val classId: Int = 0,
        @JsonProperty("term_id")
        val termId: Int = 0,
        @JsonProperty("created_by")
        val createdBy: String = ""
)