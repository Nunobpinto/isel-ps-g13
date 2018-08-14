package isel.leic.ps.eduWikiAPI.service.interfaces

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
import java.util.*

interface OrganizationService {

    fun getSpecificOrganization(organizationId: Int): OrganizationOutputModel

    fun getAllOrganizations(): OrganizationCollectionOutputModel

    fun createOrganization(organizationInputModel: OrganizationInputModel, principal: Principal) : OrganizationOutputModel

    fun deleteOrganization(organizationId: Int): Int

    fun updateOrganization(organizationId: Int, organizationInputModel: OrganizationInputModel, principal: Principal): OrganizationOutputModel

    fun getAllReportsOnOrganization(organizationId: Int): OrganizationReportCollectionOutputModel

    fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int): OrganizationReportOutputModel

    fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel, principal: Principal): OrganizationReportOutputModel

    fun deleteSpecificReportOnOrganization(organizationId: Int, reportId: Int, principal: Principal): Int

    fun voteOnOrganization(organizationId: Int, vote: VoteInputModel, principal: Principal): Int

    fun voteOnOrganizationReport(organizationId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int

    fun getAllVersionsOfOrganization(organizationId: Int): OrganizationVersionCollectionOutputModel

    fun getSpecificVersionOfOrganization(organizationId: Int, version: Int): OrganizationVersionOutputModel

    fun updateReportedOrganization(organizationId: Int, reportId: Int, principal: Principal): OrganizationOutputModel
}