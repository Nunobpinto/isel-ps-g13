package isel.leic.ps.eduWikiAPI.domain.model

import java.sql.Timestamp

data class Course(
        val id: Int = 0,
        val organizationId: Int = 0,
        val version: Int = 0,
        val votes: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val lecturedTerm: Int = 0,
        val programmeId: Int = 0,
        val optional: Boolean = false,
        val credits: Int = 0,
        val timestamp: Timestamp = Timestamp(1)
)