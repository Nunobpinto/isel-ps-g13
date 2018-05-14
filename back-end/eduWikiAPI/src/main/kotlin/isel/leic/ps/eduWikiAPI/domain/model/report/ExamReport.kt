package isel.leic.ps.eduWikiAPI.domain.model.report

import java.time.Year

data class ExamReport (
        val reportId: Int = 0,
        val courseMiscUnitId: Int = 0,
        val sheet: String = "",
        val dueDate: Year = Year.now(),
        val type: String = "",
        val phase: String = "",
        val location: String = "",
        val madeBy: String = "",
        val votes: Int = 0
)