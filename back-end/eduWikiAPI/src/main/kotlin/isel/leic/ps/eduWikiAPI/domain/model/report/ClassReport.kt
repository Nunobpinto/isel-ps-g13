package isel.leic.ps.eduWikiAPI.domain.model.report;

import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_NAME
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_PROGRAMME_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_REPORT_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_REPORT_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_VOTES
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class ClassReport(
        @ColumnName(CLASS_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(CLASS_ID)
        val classId: Int = 0,
        @ColumnName(CLASS_NAME)
        val className: String? = null,
        @ColumnName(CLASS_PROGRAMME_ID)
        val programmeId: Int? = null,
        @ColumnName(CLASS_TERM_ID)
        val termId: Int = 0,
        @ColumnName(CLASS_REPORTED_BY)
        val reportedBy: String = "",
        @ColumnName(CLASS_VOTES)
        val votes: Int = 0,
        @ColumnName(CLASS_REPORT_LOG_ID)
        val logId: Int = 0,
        @ColumnName(CLASS_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)
