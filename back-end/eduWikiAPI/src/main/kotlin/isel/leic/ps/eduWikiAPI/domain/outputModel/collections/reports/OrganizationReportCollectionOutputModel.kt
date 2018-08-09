package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.OrganizationReportOutputModel

data class OrganizationReportCollectionOutputModel (
        val organizationReportList: List<OrganizationReportOutputModel>
)