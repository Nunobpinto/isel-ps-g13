package isel.leic.ps.eduWikiAPI.domain.model.report

import java.time.Year

data class TermReport (
        val reportId: Int = 0,
        val termId: Int = 0,
        val termShortName: String = "",
        val termYear: Year = Year.now(),
        val termType: String = "",
        val createdBy: String = "",
        val votes: Int = 0
)