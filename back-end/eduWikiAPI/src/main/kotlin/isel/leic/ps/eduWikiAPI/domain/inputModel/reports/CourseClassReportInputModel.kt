package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class CourseClassReportInputModel (
        @JsonProperty("course_id")
        val courseId: Int? = null,
        @JsonProperty("class_id")
        val classId: Int? = null,
        @JsonProperty("term_id")
        val termId: Int = 0,
        @JsonProperty("delete_permanently")
        val deletePermanently: Boolean = false
)
