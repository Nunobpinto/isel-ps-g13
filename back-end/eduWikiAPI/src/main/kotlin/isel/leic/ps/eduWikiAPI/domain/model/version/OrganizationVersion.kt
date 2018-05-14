package isel.leic.ps.eduWikiAPI.domain.model.version

import java.sql.Timestamp

data class OrganizationVersion (
        val organizationId: Int = 0,
        val version: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val address: String = "",
        val contact: Int = 0,
        val votes: Int = 0,
        val timestamp: Timestamp = Timestamp(0)
)