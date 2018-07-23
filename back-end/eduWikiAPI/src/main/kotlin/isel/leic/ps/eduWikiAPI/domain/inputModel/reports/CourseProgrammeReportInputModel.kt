package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class CourseProgrammeReportInputModel(  //TODO report course in programme
        @JsonProperty("course_id")
        val courseId: Int = 0,
        @JsonProperty("programme_id")
        val programmeId: Int = 0,
        @JsonProperty("course_lectured_term")
        val lecturedTerm: String? = null,
        val optional: Boolean? = null,
        val credits: Int? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)