package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_VERSION_ACADEMIC_DEGREE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_VERSION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_VERSION_DURATION
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_VERSION_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_VERSION_PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_VERSION_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_VERSION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_VERSION_TOTAL_CREDITS
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOJdbi.Companion.PROGRAMME_VERSION_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class ProgrammeVersion (
        @ColumnName(PROGRAMME_VERSION_ID)
        val version: Int = 1,
        @ColumnName(PROGRAMME_VERSION_PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(PROGRAMME_VERSION_FULL_NAME)
        val fullName: String = "",
        @ColumnName(PROGRAMME_VERSION_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(PROGRAMME_VERSION_ACADEMIC_DEGREE)
        val academicDegree: String = "",
        @ColumnName(PROGRAMME_VERSION_TOTAL_CREDITS)
        val totalCredits: Int = 0,
        @ColumnName(PROGRAMME_VERSION_DURATION)
        val duration: Int = 0,
        @ColumnName(PROGRAMME_VERSION_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(PROGRAMME_VERSION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)