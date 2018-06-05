package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_SHEET
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_TYPE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_VOTES
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate

data class ExamReport (
        @ColumnName(EXAM_REPORT_ID)
        val reportId: Int = 0,
        @ColumnName(COURSE_MISC_UNIT_ID)
        val courseMiscUnitId: Int = 0,
        @ColumnName(EXAM_SHEET)
        val sheet: String? = null,
        @ColumnName(EXAM_DUE_DATE)
        val dueDate: LocalDate? = LocalDate.now(),
        @ColumnName(EXAM_TYPE)
        val type: String? = "",
        @ColumnName(EXAM_PHASE)
        val phase: String? = null,
        @ColumnName(EXAM_LOCATION)
        val location: String? = null,
        @ColumnName(COURSE_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(EXAM_VOTES)
        val votes: Int = 0,
        @ColumnName(EXAM_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(1)
)