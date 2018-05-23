package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion

interface OrganizationDAO {

    /**
     * Main entities queries
     */

    fun getSpecificOrganization(organizationId: Int) : Organization

    fun getAllOrganizations() : List<Organization>

    fun deleteOrganization(organizationId: Int): Int

    fun deleteAllOrganizations() : Int

    fun updateOrganization(organization: Organization)

    fun createOrganization(organization: Organization)

    fun voteOnOrganization(organizationId: Int, vote: Vote)

    /**
     * Version entity queries
     */

    fun getAllVersions(organizationId: Int): List<OrganizationVersion>

    fun getVersion(organizationId: Int, version: Int): OrganizationVersion

    fun createVersion(version: OrganizationVersion)

    fun deleteAllVersions(organizationId: Int): Int?

    fun deleteVersion(organizationId: Int, version: Int): Int?

    fun addToOrganizationVersion(organization: Organization)

    /**
     * Report entity queries
     */

    fun reportOrganization(organizationReport: OrganizationReport)

    fun deleteReportOnOrganization(reportId: Int) : Int

    fun deleteAllReportsOnOrganization(organizationId : Int) : Int

    fun getAllOrganizationReports(organizationId: Int) : List<OrganizationReport>

    fun getSpecificReportOfOrganization(organizationId: Int, reportId: Int) : OrganizationReport

    fun voteOnReport(organizationId: Int, reportId: Int, vote: Vote)

}