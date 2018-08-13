package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_BEGINS
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_DURATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_LOCATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_VOTES
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_WEEK_DAY
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_LOG_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Time
import java.sql.Timestamp
import java.time.*

data class LectureStage(
        @ColumnName(LECTURE_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(LECTURE_STAGE_LOG_ID)
        val logId: Int = 0,
        @ColumnName(LECTURE_STAGE_WEEK_DAY)
        val weekDay: DayOfWeek = DayOfWeek.MONDAY,
        @ColumnName(LECTURE_STAGE_BEGINS)
        val begins: LocalTime = LocalTime.now(),
        @ColumnName(LECTURE_STAGE_DURATION)
        val duration: Duration = Duration.ZERO,
        @ColumnName(LECTURE_STAGE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(LECTURE_STAGE_VOTES)
        val votes: Int = 0,
        @ColumnName(LECTURE_STAGE_LOCATION)
        val location: String = "",
        @ColumnName(LECTURE_STAGE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)