package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.OrganizationCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.OrganizationOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.OrganizationReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.OrganizationVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.OrganizationReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.OrganizationVersionOutputModel

interface OrganizationService {

    fun getSpecificOrganization(organizationId: Int): OrganizationOutputModel

    fun getAllOrganizations(): OrganizationCollectionOutputModel

    fun createOrganization(organizationInputModel: OrganizationInputModel) : OrganizationOutputModel

    fun deleteOrganization(organizationId: Int): Int

    fun deleteAllOrganizations(): Int

    fun updateOrganization(organizationId: Int, organizationInputModel: OrganizationInputModel): OrganizationOutputModel

    fun getAllReportsOnOrganization(organizationId: Int): OrganizationReportCollectionOutputModel

    fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int): OrganizationReportOutputModel

    fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel): OrganizationReportOutputModel

    fun deleteAllReportsOnOrganization(organizationId: Int): Int

    fun deleteSpecificReportOnOrganization(organizationId: Int, reportId: Int): Int

    fun voteOnOrganization(organizationId: Int, vote: VoteInputModel): Int

    fun voteOnOrganizationReport(organizationId: Int, reportId: Int, vote: VoteInputModel): Int

    fun getAllVersionsOfOrganization(organizationId: Int): OrganizationVersionCollectionOutputModel

    fun getSpecificVersionOfOrganization(organizationId: Int, version: Int): OrganizationVersionOutputModel

    fun deleteAllVersionsOfOrganization(organizationId: Int): Int

    fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int): Int

    fun updateReportedOrganization(organizationId: Int, reportId: Int): OrganizationOutputModel
}