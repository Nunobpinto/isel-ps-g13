package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.OrganizationOutputModel

data class OrganizationCollectionOutputModel (
        val organizationList: List<OrganizationOutputModel>
)