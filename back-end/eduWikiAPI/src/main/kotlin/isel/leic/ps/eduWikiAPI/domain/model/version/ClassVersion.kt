package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_NAME
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_VERSION
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class ClassVersion(
        @ColumnName(CLASS_ID)
        val classId: Int = 0,
        @ColumnName(CLASS_NAME)
        val className: String = "",
        @ColumnName(TERM_ID)
        val termId: Int = 0,
        @ColumnName(CLASS_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(CLASS_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(0),
        @ColumnName(CLASS_VERSION)
        val version: Int = 0
)