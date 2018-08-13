package isel.leic.ps.eduWikiAPI.domain.inputModel.reports

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class LectureReportInputModel (
        val weekday: DayOfWeek? = null,
        val begins: LocalTime? = null,
        val duration: Long? = null,
        val location: String? = null
)