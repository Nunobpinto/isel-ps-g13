package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import java.sql.Timestamp
import java.time.LocalDateTime

data class ReputationOutputModel (
        val points: Int = 0,
        val role: String = ""
)