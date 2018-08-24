package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.PendingTenantDetailsOutputModel

data class PendingTenantDetailsCollectionOutputModel(
        val pendingTenantList: List<PendingTenantDetailsOutputModel>
)
