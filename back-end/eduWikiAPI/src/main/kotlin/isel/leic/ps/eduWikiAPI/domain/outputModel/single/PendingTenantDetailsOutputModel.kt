package isel.leic.ps.eduWikiAPI.domain.outputModel.single

import java.sql.Timestamp
import java.time.LocalDateTime

data class PendingTenantDetailsOutputModel(
        val tenantUuid: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val address: String = "",
        val contact: String = "",
        val website: String = "",
        val emailPattern: String = "",
        val orgSummary: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
        val creators: List<PendingTenantCreatorOutputModel>
)
