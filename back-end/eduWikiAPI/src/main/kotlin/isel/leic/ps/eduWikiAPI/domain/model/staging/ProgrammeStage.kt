package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_ACADEMIC_DEGREE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_DURATION
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_TOTAL_CREDITS
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROG_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class ProgrammeStage(
        @ColumnName(PROG_ID)
        val programmeId: Int = 0,
        @ColumnName(PROG_VOTES)
        val votes: Int = 1,
        @ColumnName(PROG_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(PROG_FULL_NAME)
        val fullName: String = "",
        @ColumnName(PROG_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(PROG_ACADEMIC_DEGREE)
        val academicDegree: String = "",
        @ColumnName(PROG_TOTAL_CREDITS)
        val totalCredits: Int = 0,
        @ColumnName(PROG_DURATION)
        val duration: Int = 0,
        @ColumnName(PROG_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(0)
)