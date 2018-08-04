package isel.leic.ps.eduWikiAPI.domain.outputModel

import java.sql.Timestamp
import java.time.LocalDateTime

data class TermOutputModel (
        val termId: Int = 0,
        val shortName: String = "",
        val year: Int = 0,
        val type: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)