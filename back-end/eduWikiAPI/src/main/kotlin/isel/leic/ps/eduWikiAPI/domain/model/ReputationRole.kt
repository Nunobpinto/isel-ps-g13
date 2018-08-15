package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_ROLE_HIERARCHY_LEVEL
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_ROLE_ID
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_ROLE_MAX_POINTS
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.REPUTATION_ROLE_MIN_POINTS
import org.jdbi.v3.core.mapper.reflect.ColumnName
import org.springframework.security.core.GrantedAuthority

class ReputationRole(
        @ColumnName(REPUTATION_ROLE_ID)
        val reputationRoleId: String = "",
        @ColumnName(REPUTATION_ROLE_MAX_POINTS)
        val maxPoints: Int = 0,
        @ColumnName(REPUTATION_ROLE_MIN_POINTS)
        val minPoints: Int = 0,
        @ColumnName(REPUTATION_ROLE_HIERARCHY_LEVEL)
        val hierarchyLevel: Int = 0
) : GrantedAuthority {
    override fun getAuthority(): String = reputationRoleId
}