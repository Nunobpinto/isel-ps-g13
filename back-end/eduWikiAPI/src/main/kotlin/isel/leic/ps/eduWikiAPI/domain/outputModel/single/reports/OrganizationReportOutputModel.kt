package isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports

import java.sql.Timestamp
import java.time.LocalDateTime

data class OrganizationReportOutputModel (
        val reportId: Int = -1,
        val fullName: String? = null,
        val shortName: String? = null,
        val address: String? = null,
        val contact: String? = null,
        val website: String? = null,
        val reportedBy:String = "",
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)