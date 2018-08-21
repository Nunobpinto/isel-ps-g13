package isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TenantDetailsOutputModel

interface TenantService {

    fun findTenantById(tenantId: String): TenantDetailsOutputModel

}