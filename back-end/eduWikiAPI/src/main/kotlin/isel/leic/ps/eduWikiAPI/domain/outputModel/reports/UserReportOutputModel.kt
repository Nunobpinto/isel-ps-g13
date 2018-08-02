package isel.leic.ps.eduWikiAPI.domain.outputModel.reports;

import java.sql.Timestamp
import java.time.LocalDateTime

data class UserReportOutputModel(
        val reportId: Int = 0,
        val username: String = "",
        val reason: String = "",
        val reportedBy: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)

