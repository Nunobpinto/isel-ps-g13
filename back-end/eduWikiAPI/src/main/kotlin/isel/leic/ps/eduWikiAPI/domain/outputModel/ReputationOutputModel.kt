package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import java.sql.Timestamp
import java.time.LocalDateTime

data class ReputationOutputModel (
        val reputationId: Int = 0,
        val points: Int = 0,
        val rank: Int = 0,
        val version: Int = 0,
        val studentId: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)