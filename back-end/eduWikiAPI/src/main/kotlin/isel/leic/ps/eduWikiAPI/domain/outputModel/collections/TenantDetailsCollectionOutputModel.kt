package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TenantDetailsOutputModel

data class TenantDetailsCollectionOutputModel(
        val tenantList: List<TenantDetailsOutputModel>
)
