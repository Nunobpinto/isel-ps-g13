package isel.leic.ps.eduWikiAPI.domain.outputModel

import isel.leic.ps.eduWikiAPI.domain.enums.TermType
import java.sql.Timestamp
import java.time.LocalDateTime

data class TermOutputModel (
        val termId: Int = 0,
        val shortName: String = "",
        val year: Int = 0,
        val type: TermType,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)