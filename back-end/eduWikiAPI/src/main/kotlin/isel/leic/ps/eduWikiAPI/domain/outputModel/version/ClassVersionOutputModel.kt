package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_NAME
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class ClassVersionOutputModel(
        val version: Int = 1,
        val classId: Int = 0,
        val termId: Int = 0,
        val className: String = "",
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)