package isel.leic.ps.eduWikiAPI.eventListeners.events

import isel.leic.ps.eduWikiAPI.domain.model.Vote
import java.sql.Timestamp
import java.time.LocalDateTime

data class VoteOnResourceEvent(
        val voter: String,
        val owner: String,
        val entity: String,
        val logId: Int,
        val vote: Vote,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)