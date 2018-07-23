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

    fun updateOrganization(organization: Organization) : Int

    fun createOrganization(organization: Organization) : Optional<Organization>

    fun voteOnOrganization(organizationId: Int, vote: Vote): Int

    /**
     * Version entity queries
     */

    fun getAllVersionsOfOrganization(organizationId: Int): List<OrganizationVersion>

    fun getSpecificVersionOfOrganization(organizationId: Int, version: Int): Optional<OrganizationVersion>

    fun createVersion(version: OrganizationVersion): Optional<OrganizationVersion>

    fun deleteAllVersionsOfOrganization(organizationId: Int): Int

    fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int): Int

    fun addToOrganizationVersion(organization: Organization) : Int

    /**
     * Report entity queries
     */

    fun reportOrganization(organizationReport: OrganizationReport) : Optional<OrganizationReport>

    fun deleteReportOnOrganization(reportId: Int) : Int

    fun deleteAllReportsOnOrganization(organizationId : Int) : Int

    fun getAllReportsOnOrganization(organizationId: Int) : List<OrganizationReport>

    fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int) : Optional<OrganizationReport>

    fun voteOnOrganizationReport(organizationId: Int, reportId: Int, vote: Vote): Int

}