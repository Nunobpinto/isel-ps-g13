package isel.leic.ps.eduWikiAPI.domain.model;

import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_DUE_DATE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_LOCATION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_PHASE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_SHEET
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_TYPE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_VERSION
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_VOTES
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXM_TIMESTAMP
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDate;

data class Exam (
        @ColumnName(COURSE_MISC_UNIT_ID)
        val id: Int = 0,
        @ColumnName(EXM_VERSION)
        val version: Int = 0,
        @ColumnName(EXM_VOTES)
        val votes: Int = 0,
        @ColumnName(EXM_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(EXM_SHEET)
        val sheet: String = "", //TODO
        @ColumnName(EXM_DUE_DATE)
        val dueDate: LocalDate = LocalDate.now(),
        @ColumnName(EXM_TYPE)
        val type: String = "",
        @ColumnName(EXM_PHASE)
        val phase: String = "",
        @ColumnName(EXM_LOCATION)
        val location: String = "",
        @ColumnName(EXM_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(1)
)
