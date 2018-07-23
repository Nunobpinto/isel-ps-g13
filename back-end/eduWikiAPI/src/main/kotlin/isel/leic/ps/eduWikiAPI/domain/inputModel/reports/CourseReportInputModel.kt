package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class CourseReportInputModel (
        @JsonProperty("course_id")
        val courseId: Int = 0,
        @JsonProperty("course_full_name")
        val fullName: String? = null,
        @JsonProperty("course_short_name")
        val shortName: String? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)