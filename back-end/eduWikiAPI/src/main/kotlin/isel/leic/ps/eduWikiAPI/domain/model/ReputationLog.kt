package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_LOG_ACTION
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_LOG_GIVEN_BY
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_LOG_POINTS
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_LOG_REP_ID
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_LOG_USER
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class ReputationLog(
        @ColumnName(REPUTATION_LOG_ID)
        val repLogId: Int,
        @ColumnName(REPUTATION_LOG_ACTION)
        val repActionId: Int,
        @ColumnName(REPUTATION_LOG_GIVEN_BY)
        val givenBy: String,
        @ColumnName(REPUTATION_LOG_POINTS)
        val points: Int,
        @ColumnName(REPUTATION_LOG_REP_ID)
        val userRepId: Int,
        @ColumnName(REPUTATION_LOG_USER)
        val user: String
)
