package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_SHEET
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_TYPE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_VOTES
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate

data class ExamReport (
        @ColumnName(EXM_REPORT_ID)
        val reportId: Int = 0,
        @ColumnName(COURSE_MISC_UNIT_ID)
        val courseMiscUnitId: Int = 0,
        @ColumnName(EXM_SHEET)
        val sheet: String? = null,
        @ColumnName(EXM_DUE_DATE)
        val dueDate: LocalDate? = LocalDate.now(),
        @ColumnName(EXM_TYPE)
        val type: String? = "",
        @ColumnName(EXM_PHASE)
        val phase: String? = null,
        @ColumnName(EXM_LOCATION)
        val location: String? = null,
        @ColumnName(COURSE_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(EXM_VOTES)
        val votes: Int = 0,
        @ColumnName(TIMESTAMP)
        val timestamp: Timestamp = Timestamp(1)
)