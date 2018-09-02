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

    fun getOrganization() : Optional<Organization>

    fun updateOrganization(organization: Organization) : Organization

    /**
     * Version entity queries
     */

    fun getAllVersionsOfOrganization(): List<OrganizationVersion>

    fun getSpecificVersionOfOrganization(version: Int): Optional<OrganizationVersion>

    fun createOrganizationVersion(version: OrganizationVersion): OrganizationVersion

    /**
     * Report entity queries
     */

    fun reportOrganization(organizationReport: OrganizationReport) : OrganizationReport

    fun deleteSpecificReportOnOrganization(reportId: Int) : Int

    fun deleteAllReportsOnOrganization() : Int

    fun getAllReportsOnOrganization() : List<OrganizationReport>

    fun getSpecificReportOnOrganization(reportId: Int) : Optional<OrganizationReport>

    fun updateVotesOnOrganizationReport(reportId: Int, votes: Int): Int

    fun getOrganizationReportByLogId(logId: Int): Optional<OrganizationReport>

}