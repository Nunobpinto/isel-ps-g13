package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.model.TenantDetails
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TenantDetailsOutputModel

fun toTenantDetailsOutputModel(tenant: TenantDetails) = TenantDetailsOutputModel(
        tenantId = tenant.uuid,
        schemaName = tenant.schemaName,
        emailPattern = tenant.emailPattern,
        createdAt = tenant.createdAt
)