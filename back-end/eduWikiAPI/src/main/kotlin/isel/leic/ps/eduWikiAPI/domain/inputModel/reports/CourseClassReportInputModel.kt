package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class CourseClassReportInputModel (
        @JsonProperty("reported_by")
        val reportedBy: String = "",
        @JsonProperty("course_id")
        val courseId: Int = -1,
        @JsonProperty("class_id")
        val classId: Int = -1,
        @JsonProperty("term_id")
        val termId: Int = -1
)