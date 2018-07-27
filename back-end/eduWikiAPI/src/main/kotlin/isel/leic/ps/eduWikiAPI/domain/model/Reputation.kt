package isel.leic.ps.eduWikiAPI.domain.model;

import java.sql.Timestamp

data class Reputation (
        val reputationId: Int = 0,
        val reputationPoints: Int = 0,
        val reputationRole: String = "",
        val userId: Int = 0
)