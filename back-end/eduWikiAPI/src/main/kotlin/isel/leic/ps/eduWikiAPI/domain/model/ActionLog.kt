package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.ACTION_LOG_ACTION
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.ACTION_LOG_ENTITY
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.ACTION_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.ACTION_LOG_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.ACTION_LOG_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOImpl.Companion.ACTION_LOG_USER
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class ActionLog(
        @ColumnName(ACTION_LOG_ID)
        val actionId: Int,
        @ColumnName(ACTION_LOG_ACTION)
        val actionType: ActionType,
        @ColumnName(ACTION_LOG_USER)
        val user: String,
        @ColumnName(ACTION_LOG_ENTITY)
        val entity: String,
        @ColumnName(ACTION_LOG_LOG_ID)
        val logId: Int,
        @ColumnName(ACTION_LOG_TIMESTAMP)
        val timestamp: Timestamp
)