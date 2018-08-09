package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_BEGINS
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_DURATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_LOCATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_VERSION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_WEEK_DAY
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Time
import java.sql.Timestamp
import java.time.*

data class LectureVersionOutputModel (
        val version: Int = 1,
        val lectureId: Int = 0,
        val createdBy: String = "",
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        val begins: LocalTime = LocalTime.now(),
        val duration: Duration = Duration.ZERO,
        val location: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)