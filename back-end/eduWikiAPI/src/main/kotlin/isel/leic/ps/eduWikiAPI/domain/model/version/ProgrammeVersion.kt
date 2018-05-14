package isel.leic.ps.eduWikiAPI.domain.model.version

import java.sql.Timestamp

data class ProgrammeVersion (
        val programmeId: Int = 0,
        val fullName: String = "",
        val shortName: String = "",
        val academicDegree: String = "",
        val totalCredits: Int = 0,
        val duration: Int = 0,
        val createdBy: String = "",
        val timestamp: Timestamp = Timestamp(0),
        val version: Int = 0
)