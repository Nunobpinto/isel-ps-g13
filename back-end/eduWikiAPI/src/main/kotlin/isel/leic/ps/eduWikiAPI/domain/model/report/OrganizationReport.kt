package isel.leic.ps.eduWikiAPI.domain.model.report

import java.sql.Timestamp

data class OrganizationReport (
        val reportId: Int = 0,
        val id: Int = 0,
        val fullName: String? = "",
        val shortName: String? = "",
        val address: String? = "",
        val contact: String? = "",
        val reportedBy:String = "",
        val votes: Int = 0,
        val reporter: String = "",
        val timestamp: Timestamp
)