package isel.leic.ps.eduWikiAPI.eventListeners.events

import org.springframework.context.ApplicationEvent
import java.sql.Timestamp
import java.time.LocalDateTime

data class ResourceCreatedEvent (
        val creator: String,
        val entity: String,
        val logId: Int,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)