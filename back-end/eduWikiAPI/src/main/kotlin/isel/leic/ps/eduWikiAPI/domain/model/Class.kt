package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_NAME
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_VERSION
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_VOTES
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_PROGRAMME_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class Class(
        @ColumnName(CLASS_ID)
        val classId: Int = -1,
        @ColumnName(CLASS_VERSION)
        val version: Int = 1,
        @ColumnName(CLASS_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(CLASS_NAME)
        val className: String = "",
        @ColumnName(CLASS_TERM_ID)
        val termId: Int = 0,
        @ColumnName(CLASS_PROGRAMME_ID)
        val programmeId: Int = 0,
        @ColumnName(CLASS_VOTES)
        val votes: Int = 0,
        @ColumnName(CLASS_LOG_ID)
        val logId: Int = 0,
        @ColumnName(CLASS_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)