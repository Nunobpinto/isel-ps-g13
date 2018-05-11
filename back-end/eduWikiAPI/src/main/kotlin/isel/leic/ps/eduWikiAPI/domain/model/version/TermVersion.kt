package isel.leic.ps.eduWikiAPI.domain.model.version

import java.sql.Timestamp
import java.time.Year

data class TermVersion (
        val termId: Int = 0,
        val shortName: String = "",
        val year: Year = Year.now(),
        val type: String = "",
        val createdBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(0),
        val version: Int = 0
)