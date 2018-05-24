package isel.leic.ps.eduWikiAPI.domain.model.report

import java.sql.Timestamp
import java.time.Year

data class TermReport (
        val reportId: Int = 0,
        val termId: Int = 0,
        val termShortName: String = "",
        val termYear: Year = Year.now(),
        val termType: String = "",
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp
)