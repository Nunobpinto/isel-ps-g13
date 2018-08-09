package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.ReputationDAOJdbi.Companion.REPUTATION_MATCHER_ID
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOJdbi.Companion.REPUTATION_MATCHER_URI
import org.jdbi.v3.core.mapper.reflect.ColumnName

class ReputationMatcher(
    @ColumnName(REPUTATION_MATCHER_URI)
    val UriMatch: String = "",
    @ColumnName(REPUTATION_MATCHER_ID)
    val reputationId: String = ""
)