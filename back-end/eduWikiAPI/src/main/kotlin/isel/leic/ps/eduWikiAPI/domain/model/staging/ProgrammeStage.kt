package isel.leic.ps.eduWikiAPI.domain.model.staging

import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_ACADEMIC_DEGREE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_DURATION
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_TOTAL_CREDITS
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class ProgrammeStage(
        @ColumnName(PROGRAMME_STAGE_ID)
        val stageId: Int = -1,
        @ColumnName(PROGRAMME_STAGE_LOG_ID)
        val logId: Int = 0,
        @ColumnName(PROGRAMME_STAGE_FULL_NAME)
        val fullName: String = "",
        @ColumnName(PROGRAMME_STAGE_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(PROGRAMME_STAGE_ACADEMIC_DEGREE)
        val academicDegree: String = "",
        @ColumnName(PROGRAMME_STAGE_TOTAL_CREDITS)
        val totalCredits: Int = 0,
        @ColumnName(PROGRAMME_STAGE_DURATION)
        val duration: Int = 0,
        @ColumnName(PROGRAMME_STAGE_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(PROGRAMME_STAGE_VOTES)
        val votes: Int = 0,
        @ColumnName(PROGRAMME_STAGE_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)