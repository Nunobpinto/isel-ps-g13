package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.TenantDetails
import java.util.*

interface TenantRepository {

    fun findById(tenantUuid: String): Optional<TenantDetails>
}