package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime

class LectureInputModel(
        @JsonProperty("weekDay")
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        val begins: String = "",
        val duration: Long = 0,
        val location: String = ""
)