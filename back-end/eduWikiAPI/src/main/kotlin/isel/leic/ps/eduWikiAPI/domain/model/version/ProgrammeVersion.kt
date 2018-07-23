package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_ACADEMIC_DEGREE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_DURATION
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_TOTAL_CREDITS
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class ProgrammeVersion (
        @ColumnName(PROGRAMME_VERSION)
        val version: Int = 1,
        @ColumnName(PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(PROGRAMME_FULL_NAME)
        val fullName: String = "",
        @ColumnName(PROGRAMME_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(PROGRAMME_ACADEMIC_DEGREE)
        val academicDegree: String = "",
        @ColumnName(PROGRAMME_TOTAL_CREDITS)
        val totalCredits: Int = 0,
        @ColumnName(PROGRAMME_DURATION)
        val duration: Int = 0,
        @ColumnName(PROGRAMME_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(PROGRAMME_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)