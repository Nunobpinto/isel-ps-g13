package isel.leic.ps.eduWikiAPI.domain.model

import java.sql.Timestamp

data class Class(
        val id: Int = 0,
        val version: Int = 0,
        val votes: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val termId: String = "",
        val timestamp: Timestamp = Timestamp(1)
)