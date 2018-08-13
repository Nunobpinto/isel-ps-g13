package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class CourseProgrammeReportInputModel(
        @JsonProperty("course_id")
        val courseId: Int = 0,
        @JsonProperty("programme_id")
        val programmeId: Int = 0,
        @JsonProperty("course_lectured_term")
        val lecturedTerm: String? = null,
        @JsonProperty("to_delete")
        val deleteFlag: Boolean = false,
        val optional: Boolean? = null,
        val credits: Int? = null
)