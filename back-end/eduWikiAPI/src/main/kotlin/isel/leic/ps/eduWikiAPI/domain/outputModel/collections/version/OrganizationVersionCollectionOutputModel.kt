package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.OrganizationVersionOutputModel

data class OrganizationVersionCollectionOutputModel (
        val organizationVersionList: List<OrganizationVersionOutputModel>
)