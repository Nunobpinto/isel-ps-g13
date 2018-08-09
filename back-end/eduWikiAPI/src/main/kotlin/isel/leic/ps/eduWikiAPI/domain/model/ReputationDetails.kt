package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.ReputationDAOJdbi.Companion.REPUTATION_ID
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOJdbi.Companion.REPUTATION_POINTS
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOJdbi.Companion.REPUTATION_ROLE
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOJdbi.Companion.REPUTATION_USER
import org.jdbi.v3.core.mapper.reflect.ColumnName

class ReputationDetails(
        @ColumnName(REPUTATION_ID)
        val repId: Int,
        @ColumnName(REPUTATION_USER)
        val user: String,
        @ColumnName(REPUTATION_POINTS)
        var points: Int,
        @ColumnName(REPUTATION_ROLE)
        var role: String
)
