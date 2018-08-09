package isel.leic.ps.eduWikiAPI.eventListeners.events

import java.sql.Timestamp

data class ResourceDeletedEvent(
        val user: String,
        val entity: String,
        val logId: Int,
        val timestamp: Timestamp
)