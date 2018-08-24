package isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.TenantRequestDetails
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.PendingTenantDetailsCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.TenantDetailsCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.PendingTenantDetailsOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TenantDetailsOutputModel
import java.security.Principal

interface TenantService {

    fun findTenantById(tenantId: String): TenantDetailsOutputModel

    fun getAllActiveTenants(): TenantDetailsCollectionOutputModel

    fun getAllPendingTenants(): PendingTenantDetailsCollectionOutputModel

    fun findPendingTenantById(tenantUuid: String): PendingTenantDetailsOutputModel

    fun createPendingTenant(requestDetails: TenantRequestDetails): PendingTenantDetailsOutputModel

    fun realizePendingTenant(tenantUuid: String, principal: Principal)

    fun rejectPendingTenant(tenantUuid: String, principal: Principal)

}