package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class CourseClassReportInputModel (
        @JsonProperty("course_class_id")
        val courseClassId: Int = 0, //TODO no usage
        @JsonProperty("course_id")
        val courseId: Int? = null,
        @JsonProperty("class_id")
        val classId: Int? = null,
        @JsonProperty("term_id")
        val termId: Int? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = "",
        @JsonProperty("delete_permanently")
        val deletePermanently: Boolean = false
)
