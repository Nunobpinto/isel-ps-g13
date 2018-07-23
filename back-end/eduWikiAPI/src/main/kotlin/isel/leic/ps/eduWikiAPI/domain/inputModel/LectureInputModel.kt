package isel.leic.ps.eduWikiAPI.domain.inputModel

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate

class LectureInputModel(
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        val begins: LocalDate = LocalDate.now(),
        val duration: Duration = Duration.ZERO,
        val location: String = "",
        @JsonProperty("created_by")
        val createdBy: String = ""
)