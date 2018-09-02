package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_DELETE_FLAG
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseClassReport(
        @ColumnName(COURSE_CLASS_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(COURSE_CLASS_REPORT_COURSE_CLASS_ID)
        val courseClassId: Int = 0,
        @ColumnName(COURSE_CLASS_REPORT_CLASS_ID)
        val classId: Int? = null,
        @ColumnName(COURSE_CLASS_REPORT_COURSE_ID)
        val courseId: Int? = null,
        @ColumnName(COURSE_CLASS_REPORT_TERM_ID)
        val termId: Int = 0,
        @ColumnName(COURSE_CLASS_REPORT_DELETE_FLAG)
        val deletePermanently: Boolean = false,
        @ColumnName(COURSE_CLASS_REPORT_LOG_ID)
        val logId: Int = 0,
        @ColumnName(COURSE_CLASS_REPORT_VOTES)
        val votes: Int = 0,
        @ColumnName(COURSE_CLASS_REPORT_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(COURSE_CLASS_REPORT_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)