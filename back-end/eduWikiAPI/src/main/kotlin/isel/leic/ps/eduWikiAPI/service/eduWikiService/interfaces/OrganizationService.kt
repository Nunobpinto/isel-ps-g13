package isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.OrganizationCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.OrganizationOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.OrganizationReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.OrganizationVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.OrganizationReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.OrganizationVersionOutputModel
import java.security.Principal

interface OrganizationService {

    fun getOrganization(): OrganizationOutputModel

    fun createOrganization(organizationInputModel: OrganizationInputModel, principal: Principal) : OrganizationOutputModel

    fun updateOrganization(organizationInputModel: OrganizationInputModel, principal: Principal): OrganizationOutputModel

    fun getAllReportsOnOrganization(): OrganizationReportCollectionOutputModel

    fun getSpecificReportOnOrganization(reportId: Int): OrganizationReportOutputModel

    fun reportOrganization(input: OrganizationReportInputModel, principal: Principal): OrganizationReportOutputModel

    fun deleteSpecificReportOnOrganization(reportId: Int, principal: Principal): Int

    fun voteOnOrganizationReport(reportId: Int, vote: VoteInputModel, principal: Principal): Int

    fun getAllVersionsOfOrganization(): OrganizationVersionCollectionOutputModel

    fun getSpecificVersionOfOrganization(version: Int): OrganizationVersionOutputModel

    fun updateReportedOrganization(reportId: Int, principal: Principal): OrganizationOutputModel
}