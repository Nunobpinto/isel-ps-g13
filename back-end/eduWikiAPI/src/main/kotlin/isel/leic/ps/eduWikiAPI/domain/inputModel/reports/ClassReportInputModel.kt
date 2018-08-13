package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty

class ClassReportInputModel (
        @JsonProperty("class_id")
        val classId: Int = 0, //TODO no usage
        @JsonProperty("term_id")
        val termId: Int = 0,
        @JsonProperty("class_name")
        val className: String? = null
)