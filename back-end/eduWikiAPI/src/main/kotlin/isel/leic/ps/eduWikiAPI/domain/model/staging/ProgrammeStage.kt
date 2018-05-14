package isel.leic.ps.eduWikiAPI.domain.model.staging

import java.sql.Timestamp
import java.time.Duration

data class ProgrammeStage (
    val programmeId: Int = 0,
    val version: Int = 1,
    val votes: Int = 1,
    val createdBy: String = "",
    val fullName: String = "",
    val shortName: String = "",
    val academicDegree: String = "",
    val totalCredits: Int = 0,
    val duration: Int = 0,
    val timestamp: Timestamp = Timestamp(0)
)