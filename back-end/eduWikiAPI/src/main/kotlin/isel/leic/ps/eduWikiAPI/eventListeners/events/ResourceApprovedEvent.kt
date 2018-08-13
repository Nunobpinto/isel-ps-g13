package isel.leic.ps.eduWikiAPI.eventListeners.events

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import java.sql.Timestamp
import java.time.LocalDateTime

data class ResourceApprovedEvent(
        val administrator: String,
        val adminAction: ActionType,
        val approvedEntity: String,
        val approvedLogId: Int,
        val creator: String,
        val resultingAction: ActionType,
        val newEntity: String,
        val newLogId: Int,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)