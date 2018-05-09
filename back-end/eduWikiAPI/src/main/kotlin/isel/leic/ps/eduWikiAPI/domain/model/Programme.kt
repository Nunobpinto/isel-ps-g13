package isel.leic.ps.eduWikiAPI.domain.model

import java.time.Duration

data class Programme(
        val id: Int = 0,
        val version: Int = 0,
        val createdby: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val academicDegree: String = "",
        val totalCredits: Int = 0,
        val duration: Duration = Duration.ZERO
)