package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.OrganizationOutputModel

data class OrganizationCollectionOutputModel (
        val organizationList: List<OrganizationOutputModel>
)