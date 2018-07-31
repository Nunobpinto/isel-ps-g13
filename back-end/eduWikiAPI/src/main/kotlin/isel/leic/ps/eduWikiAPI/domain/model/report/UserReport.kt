package isel.leic.ps.eduWikiAPI.domain.model.report;

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class UserReport(
        @ColumnName("report_id")
        val reportId: Int = 0,
        @ColumnName("user_username")
        val username: String = "",
        val reason: String = "",
        @ColumnName("reported_by")
        val reportedBy: String = "",
        @ColumnName("time_stamp")
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)

