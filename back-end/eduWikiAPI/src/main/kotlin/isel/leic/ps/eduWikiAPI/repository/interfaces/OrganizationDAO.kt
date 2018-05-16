package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion

interface OrganizationDAO {

    /**
     * Main entities queries
     */

    fun getOrganization(organizationId: Int) : Organization

    fun getAllOrganizations() : List<Organization>

    fun deleteOrganization(organizationId: Int) : Int

    fun deleteAllOrganizations() : Int

    fun updateOrganization(organization: Organization) : Int

    fun createOrganization(organization: Organization)

    fun voteOnOrganization(organizationId: Int, voteType: Int)

    /**
     * Version entities queries
     */

    fun getVersionOrganization(versionOrganizationId: Int, version: Int) : OrganizationVersion

    fun getAllVersionOrganizations() : List<OrganizationVersion>

    fun deleteVersionOrganization(versionOrganizationId: Int, version: Int) : Int

    fun deleteAllVersionOrganizations() : Int

    fun createVersionOrganization(organizationVersion: OrganizationVersion)

    /**
     * Report entity queries
     */

    fun reportOrganization(organizationReport: OrganizationReport)

    fun deleteReportOnOrganization(reportId: Int) : Int

    fun deleteAllReportsOnOrganization(organizationId : Int) : Int

    fun deleteAllReports() : Int

}