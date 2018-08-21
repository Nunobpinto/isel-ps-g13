package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import java.sql.Timestamp
import java.time.LocalDateTime

data class TenantDetailsOutputModel(
        val tenantId: String = "",
        val schemaName: String = "",
        val emailPattern: String = "",
        val createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)
