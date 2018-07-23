package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_BEGINS
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_DURATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_LOCATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_VOTES
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_WEEK_DAY
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

data class LectureReport (
        @ColumnName(LECTURE_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(LECTURE_ID)
        val lectureId: Int = 0,
        @ColumnName(LECTURE_WEEK_DAY)
        val weekDay: DayOfWeek? = null,
        @ColumnName(LECTURE_BEGINS)
        val begins: LocalDate? = null,
        @ColumnName(LECTURE_DURATION)
        val duration: Duration? = null,
        @ColumnName(LECTURE_LOCATION)
        val location: String? = null,
        @ColumnName(LECTURE_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(LECTURE_VOTES)
        val votes: Int = 0,
        @ColumnName(LECTURE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)