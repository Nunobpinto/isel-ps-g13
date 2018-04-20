package isel.leic.ps.eduWikiAPI.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

class LessonInputModel (
    @JsonProperty("begin_hour")
    val beginHour: String,
    val duration: String,
    val location: String
)