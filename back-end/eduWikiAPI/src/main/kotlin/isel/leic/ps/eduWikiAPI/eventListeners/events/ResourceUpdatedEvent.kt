package isel.leic.ps.eduWikiAPI.eventListeners.events

import java.sql.Timestamp
import java.time.LocalDateTime

data class ResourceUpdatedEvent(
        val user: String,
        val entity: String,
        val logId: Int,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)

