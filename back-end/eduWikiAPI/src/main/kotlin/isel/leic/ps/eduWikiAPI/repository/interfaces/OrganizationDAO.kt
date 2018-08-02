package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import java.util.*

interface OrganizationDAO {

    /**
     * Main entities queries
     */

    fun getSpecificOrganization(organizationId: Int) : Optional<Organization>

    fun getAllOrganizations() : List<Organization>

    fun deleteOrganization(organizationId: Int): Int

    fun deleteAllOrganizations() : Int

    fun updateOrganization(organization: Organization) : Organization

    fun createOrganization(organization: Organization) : Organization

    fun getVotesOnOrganization(organizationId: Int): Int

    fun updateVotesOnOrganization(organizationId: Int, votes: Int): Int


    /**
     * Version entity queries
     */

    fun getAllVersionsOfOrganization(organizationId: Int): List<OrganizationVersion>

    fun getSpecificVersionOfOrganization(organizationId: Int, version: Int): Optional<OrganizationVersion>

    fun createOrganizationVersion(version: OrganizationVersion): OrganizationVersion

    fun deleteAllOrganizationVersions(organizationId: Int) : Int

    fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int): Int

    /**
     * Report entity queries
     */

    fun reportOrganization(organizationReport: OrganizationReport) : OrganizationReport

    fun deleteSpecificReportOnOrganization(reportId: Int) : Int

    fun deleteAllReportsOnOrganization(organizationId : Int) : Int

    fun getAllReportsOnOrganization(organizationId: Int) : List<OrganizationReport>

    fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int) : Optional<OrganizationReport>

    fun updateVotesOnOrganizationReport(organizationId: Int, reportId: Int, votes: Int): Int

    fun getVotesOnOrganizationReport(organizationId: Int, reportId: Int): Int

    fun deleteReportOnOrganization(organizationId: Int, reportId: Int): Int

}