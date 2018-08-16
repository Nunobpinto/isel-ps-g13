package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_VERSION_BEGINS
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_VERSION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_VERSION_DURATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_VERSION_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_VERSION_LOCATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_VERSION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_VERSION_LECTURE_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_VERSION_WEEK_DAY
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.*

data class LectureVersion (
        @ColumnName(LECTURE_VERSION_ID)
        val version: Int = 1,
        @ColumnName(LECTURE_VERSION_LECTURE_ID)
        val lectureId: Int = 0,
        @ColumnName(LECTURE_VERSION_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(LECTURE_VERSION_WEEK_DAY)
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        @ColumnName(LECTURE_VERSION_BEGINS)
        val begins: LocalTime = LocalTime.now(),
        @ColumnName(LECTURE_VERSION_DURATION)
        val duration: Duration = Duration.ZERO,
        @ColumnName(LECTURE_VERSION_LOCATION)
        val location: String = "",
        @ColumnName(LECTURE_VERSION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)