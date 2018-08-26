package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import java.sql.Timestamp
import java.time.LocalDateTime

data class UserActionOutputModel(
        val action_type: String = "",
        val action_user: String = "",
        val entity_type: String = "",
        val entity_link: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)