package isel.leic.ps.eduWikiAPI.domain.model.report;

import java.sql.Timestamp

data class ClassReport(
        val reportId: Int = 0,
        val classId: Int = 0,
        val className: String = "",
        val termId: Int = 0,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp
)
