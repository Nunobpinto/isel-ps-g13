package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class LectureReportInputModel (
        @JsonProperty("lecture_id")
        val lectureId: Int = 0,
        val weekday: DayOfWeek? = null,
        val begins: LocalTime? = null,
        val duration: Long? = null,
        val location: String? = null,
        @JsonProperty("reported_by")
        val reportedBy: String = ""
)