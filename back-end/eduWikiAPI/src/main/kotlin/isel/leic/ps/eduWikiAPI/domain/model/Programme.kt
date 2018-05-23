package isel.leic.ps.eduWikiAPI.domain.model

import java.sql.Timestamp

data class Programme(
        val id: Int = 0,
        val version: Int = 1,
        val votes: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val academicDegree: String = "",
        val totalCredits: Int = 0,
        val duration: Int = 0,
        val timestamp: Timestamp = Timestamp(1)
)