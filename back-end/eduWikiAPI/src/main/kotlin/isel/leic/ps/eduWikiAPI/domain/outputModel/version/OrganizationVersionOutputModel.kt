package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import java.sql.Timestamp
import java.time.LocalDateTime

data class OrganizationVersionOutputModel (
        val version: Int = 1,
        val organizationId: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val address: String = "",
        val contact: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)