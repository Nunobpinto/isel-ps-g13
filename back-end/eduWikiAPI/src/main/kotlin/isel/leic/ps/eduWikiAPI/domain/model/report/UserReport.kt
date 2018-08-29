package isel.leic.ps.eduWikiAPI.domain.model.report;

import isel.leic.ps.eduWikiAPI.repository.UserDAOImpl.Companion.USER_REPORT_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.UserDAOImpl.Companion.USER_REPORT_REASON
import isel.leic.ps.eduWikiAPI.repository.UserDAOImpl.Companion.USER_REPORT_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.UserDAOImpl.Companion.USER_REPORT_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.UserDAOImpl.Companion.USER_REPORT_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.UserDAOImpl.Companion.USER_REPORT_USER
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class UserReport(
        @ColumnName(USER_REPORT_REPORT_ID)
        val reportId: Int = 0,
        @ColumnName(USER_REPORT_USER)
        val username: String = "",
        @ColumnName(USER_REPORT_REASON)
        val reason: String = "",
        @ColumnName(USER_REPORT_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(USER_REPORT_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        @ColumnName(USER_REPORT_LOG_ID)
        val logId: Int = 0
)

