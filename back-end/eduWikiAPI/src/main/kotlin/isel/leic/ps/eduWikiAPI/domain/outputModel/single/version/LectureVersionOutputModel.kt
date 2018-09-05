package isel.leic.ps.eduWikiAPI.domain.outputModel.single.version

import java.sql.Timestamp
import java.time.*

data class LectureVersionOutputModel (
        val version: Int = 1,
        val className: String = "",
        val lecturedTerm: String = "",
        val courseShortName: String = "",
        val lectureId: Int = 0,
        val createdBy: String = "",
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        val begins: LocalTime = LocalTime.now(),
        val duration: Duration = Duration.ZERO,
        val location: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)