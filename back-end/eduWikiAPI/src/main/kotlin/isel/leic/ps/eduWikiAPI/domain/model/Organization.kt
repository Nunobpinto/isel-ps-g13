package isel.leic.ps.eduWikiAPI.domain.model

import java.sql.Timestamp

data class Organization(
        val id: Int = 0,
        val version: Int = 1,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val address: String = "",
        val contact: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(1)
)