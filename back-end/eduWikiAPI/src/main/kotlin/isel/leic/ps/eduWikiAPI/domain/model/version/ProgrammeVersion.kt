package isel.leic.ps.eduWikiAPI.domain.model.version

import ch.qos.logback.core.joran.action.TimestampAction
import java.sql.Timestamp
import java.time.Duration

data class ProgrammeVersion (
        val programmeId: Int = 0,
        val fullName: String = "",
        val shortName: String = "",
        val academicDegree: String = "",
        val totalCredits: Int = 0,
        val duration: Duration = Duration.ZERO,
        val createdBy: String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(0),
        val version: Int = 0
)