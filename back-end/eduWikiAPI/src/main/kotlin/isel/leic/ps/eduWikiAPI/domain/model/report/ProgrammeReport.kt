package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_ACADEMIC_DEGREE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_DURATION
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_TOTAL_CREDITS
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class ProgrammeReport(
        @ColumnName(PROGRAMME_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(PROGRAMME_FULL_NAME)
        val programmeFullName: String? = null,
        @ColumnName(PROGRAMME_SHORT_NAME)
        val programmeShortName: String? = null,
        @ColumnName(PROGRAMME_ACADEMIC_DEGREE)
        val programmeAcademicDegree: String? = null,
        @ColumnName(PROGRAMME_TOTAL_CREDITS)
        val programmeTotalCredits: Int? = null,
        @ColumnName(PROGRAMME_DURATION)
        val programmeDuration: Int? = null,
        @ColumnName(PROGRAMME_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(PROGRAMME_VOTES)
        val votes: Int = 0,
        @ColumnName(PROGRAMME_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)