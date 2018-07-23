package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import java.util.*

interface OrganizationService {

    fun getSpecificOrganization(organizationId: Int): Optional<Organization>

    fun getAllOrganizations(): List<Organization>

    fun createOrganization(organizationInputModel: OrganizationInputModel) : Optional<Organization>

    fun deleteOrganization(organizationId: Int): Int

    fun deleteAllOrganizations(): Int

    fun updateOrganization(organizationId: Int, organizationInputModel: OrganizationInputModel): Int

    fun getAllReportsOnOrganization(organizationId: Int): List<OrganizationReport>

    fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int): Optional<OrganizationReport>

    fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel): Optional<OrganizationReport>

    fun deleteAllReportsOnOrganization(organizationId: Int): Int

    fun deleteSpecificReportOnOrganization(organizationId: Int, reportId: Int): Int

    fun voteOnOrganization(organizationId: Int, input: VoteInputModel): Int

    fun voteOnOrganizationReport(organizationId: Int, reportId: Int, input: VoteInputModel): Int

    fun getAllVersionsOfOrganization(organizationId: Int): List<OrganizationVersion>

    fun getSpecificVersionOfOrganization(organizationId: Int, version: Int): Optional<OrganizationVersion>

    fun deleteAllVersionsOfOrganization(organizationId: Int): Int

    fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int): Int

    fun updateReportedOrganization(organizationId: Int, reportId: Int)
}