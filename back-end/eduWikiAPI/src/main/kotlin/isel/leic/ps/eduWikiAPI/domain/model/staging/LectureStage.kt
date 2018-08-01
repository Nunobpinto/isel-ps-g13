package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_BEGINS
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_DURATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_LOCATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_VOTES
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_WEEK_DAY
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

data class LectureStage(
        @ColumnName(LECTURE_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(LECTURE_WEEK_DAY)
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        @ColumnName(LECTURE_BEGINS)
        val begins: LocalDate = LocalDate.now(),
        @ColumnName(LECTURE_DURATION)
        val duration: Duration = Duration.ZERO,
        @ColumnName(LECTURE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(LECTURE_VOTES)
        val votes: Int = 0,
        @ColumnName(LECTURE_LOCATION)
        val location: String = "",
        @ColumnName(LECTURE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)