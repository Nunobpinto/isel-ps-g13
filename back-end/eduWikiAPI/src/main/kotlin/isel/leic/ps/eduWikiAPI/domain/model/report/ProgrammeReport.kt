package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_ACADEMIC_DEGREE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_DURATION
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_TOTAL_CREDITS
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class ProgrammeReport(
        @ColumnName(PROG_REPORT_ID)
        val reportId: Int = 0,
        @ColumnName(PROG_ID)
        val programmeId: Int? = 0,
        @ColumnName(PROG_FULL_NAME)
        val programmeFullName: String? = "",
        @ColumnName(PROG_SHORT_NAME)
        val programmeShortName: String? = "",
        @ColumnName(PROG_ACADEMIC_DEGREE)
        val programmeAcademicDegree: String? = "",
        @ColumnName(PROG_TOTAL_CREDITS)
        val programmeTotalCredits: Int? = 0,
        @ColumnName(PROG_DURATION)
        val programmeDuration: Int? = 0,
        @ColumnName(PROG_REPORT_ID)
        val reportedBy: String = "",
        @ColumnName(PROG_VOTES)
        val votes: Int = 0,
        @ColumnName(PROG_TIMESTAMP)
        val timestamp: Timestamp
)