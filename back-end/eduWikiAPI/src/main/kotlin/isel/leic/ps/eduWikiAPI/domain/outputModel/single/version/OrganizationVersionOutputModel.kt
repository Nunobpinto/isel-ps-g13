package isel.leic.ps.eduWikiAPI.domain.outputModel.single.version

import java.sql.Timestamp
import java.time.LocalDateTime

data class OrganizationVersionOutputModel (
        val version: Int = 1,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val address: String = "",
        val contact: String = "",
        val website: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)