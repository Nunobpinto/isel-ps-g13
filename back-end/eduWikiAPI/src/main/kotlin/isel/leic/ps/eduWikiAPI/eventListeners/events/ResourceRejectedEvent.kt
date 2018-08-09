package isel.leic.ps.eduWikiAPI.eventListeners.events

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import java.sql.Timestamp
import java.time.LocalDateTime

class ResourceRejectedEvent(
        val administrator: String,
        val creator: String,
        val action: ActionType,
        val rejectedEntity: String,
        val rejectedLogId: Int,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)