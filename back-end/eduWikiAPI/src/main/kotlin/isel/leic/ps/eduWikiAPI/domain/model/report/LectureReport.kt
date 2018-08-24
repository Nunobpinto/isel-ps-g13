package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_BEGINS
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_DURATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_LOCATION
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_REPORT_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_REPORT_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_VOTES
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_WEEK_DAY
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.*

data class LectureReport (
        @ColumnName(LECTURE_REPORT_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(LECTURE_ID)
        val lectureId: Int = 0,
        @ColumnName(COURSE_CLASS_CLASS_ID)
        val classId: Int = 0,
        @ColumnName(COURSE_CLASS_COURSE_ID)
        val courseId: Int = 0,
        @ColumnName(LECTURE_REPORT_LOG_ID)
        val logId: Int = 0,
        @ColumnName(LECTURE_WEEK_DAY)
        val weekDay: DayOfWeek? = null,
        @ColumnName(LECTURE_BEGINS)
        val begins: LocalTime? = null,
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