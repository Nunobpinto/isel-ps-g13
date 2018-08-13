package isel.leic.ps.eduWikiAPI.domain.model;

import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_BEGINS
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_DURATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_LOCATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_VERSION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_VOTES
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_WEEK_DAY
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Time
import java.sql.Timestamp
import java.time.*

data class Lecture(
        @ColumnName(LECTURE_ID)
        val lectureId: Int = 0,
        @ColumnName(LECTURE_VERSION)
        val version: Int = 1,
        @ColumnName(LECTURE_VOTES)
        val votes: Int = 0,
        @ColumnName(LECTURE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(LECTURE_WEEK_DAY)
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        @ColumnName(LECTURE_BEGINS)
        val begins: LocalTime = LocalTime.now(),
        @ColumnName(LECTURE_DURATION)
        val duration: Duration = Duration.ZERO,
        @ColumnName(LECTURE_LOCATION)
        val location: String = "",
        @ColumnName(LECTURE_LOG_ID)
        val logId: Int = 0,
        @ColumnName(LECTURE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)
