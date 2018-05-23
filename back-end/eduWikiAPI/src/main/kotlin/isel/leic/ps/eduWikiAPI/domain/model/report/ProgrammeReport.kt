package isel.leic.ps.eduWikiAPI.domain.model.report

import java.sql.Timestamp

data class ProgrammeReport(
        val reportId: Int = 0,
        val programmeId: Int? = 0,
        val programmeFullName: String? = "",
        val programmeShortName: String? = "",
        val programmeAcademicDegree: String? = "",
        val programmeTotalCredits: Int? = 0,
        val programmeDuration: Int? = 0,
        val reportedBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp
)