package isel.leic.ps.eduWikiAPI.eventListeners.events

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import java.sql.Timestamp
import java.time.LocalDateTime

class ResourceApprovedEvent(
        val administrator: String,
        val creator: String,
        val approvedEntity: String,
        val approvedLogId: Int,
        val newEntity: String,
        val newLogId: Int,
        val action: ActionType,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)