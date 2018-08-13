package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_ACADEMIC_DEGREE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_DURATION
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_TOTAL_CREDITS
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_REPORT_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class ProgrammeReport(
        @ColumnName(PROGRAMME_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(PROGRAMME_REPORT_PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(PROGRAMME_REPORT_LOG_ID)
        val logId: Int = 0,
        @ColumnName(PROGRAMME_REPORT_FULL_NAME)
        val fullName: String? = null,
        @ColumnName(PROGRAMME_REPORT_SHORT_NAME)
        val shortName: String? = null,
        @ColumnName(PROGRAMME_REPORT_ACADEMIC_DEGREE)
        val academicDegree: String? = null,
        @ColumnName(PROGRAMME_REPORT_TOTAL_CREDITS)
        val totalCredits: Int? = null,
        @ColumnName(PROGRAMME_REPORT_DURATION)
        val duration: Int? = null,
        @ColumnName(PROGRAMME_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(PROGRAMME_REPORT_VOTES)
        val votes: Int = 0,
        @ColumnName(PROGRAMME_REPORT_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)