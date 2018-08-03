package isel.leic.ps.eduWikiAPI.domain.outputModel.reports

import java.sql.Timestamp
import java.time.LocalDateTime

data class CourseProgrammeReportOutputModel (
        val reportId: Int = -1,
        val courseId: Int = 0,
        val programmeId: Int = 0,
        val lecturedTerm: String? = null,
        val optional: Boolean? = null,
        val credits: Int? = null,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val reportedBy: String = "",
        val votes: Int = 0,
        val deleteFlag: Boolean = false
)