package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.versions.OrganizationVersionInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion

interface OrganizationService {

    /**
     * Main entities queries
     */

    fun getSpecificOrganization(organizationId: Int): Organization

    fun getAllOrganizations(): List<Organization>

    fun createOrganization(organizationInputModel: OrganizationInputModel)

    fun deleteOrganization(organizationId: Int): Int

    fun deleteAllOrganizations(): Int

    fun updateOrganization()

    fun getAllOrganizationReports(organizationId: Int): List<OrganizationReport>

    fun getSpecificReport(organizationId: Int, reportId: Int): OrganizationReport

    fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel)

    fun deleteAllReports(organizationId: Int): Int

    fun deleteSpecificReport(organizationId: Int, reportId: Int): Int

    fun voteOrganization(organizationId: Int, input: VoteInputModel)

    fun voteOnReport(organizationId: Int, reportId: Int, input: VoteInputModel)

    fun getAllVersions(organizationId: Int): List<OrganizationVersion>

    fun getVersion(organizationId: Int, version: Int): OrganizationVersion

    fun createVersion(organizationId: Int, input: OrganizationVersionInputModel)

    fun deleteAllVersions(organizationId: Int): Int?

    fun deleteSpecificVersion(organizationId: Int, version: Int): Int?
}