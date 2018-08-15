package isel.leic.ps.eduWikiAPI.domain.model

import org.jdbi.v3.core.mapper.reflect.ColumnName
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_ID
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_POINTS
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_ROLE
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_USER

data class Reputation (
        @ColumnName(REPUTATION_ID)
        val reputationId: Int = 0,
        @ColumnName(REPUTATION_POINTS)
        val reputationPoints: Int = 0,
        @ColumnName(REPUTATION_ROLE)
        val reputationRole: String = "",
        @ColumnName(REPUTATION_USER)
        val username: String = ""
)