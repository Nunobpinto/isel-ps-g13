package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import java.util.*

interface OrganizationDAOJdbi : OrganizationDAO {

    companion object {
        //TABLE NAMES
        const val ORGANIZATION_TABLE = "organization"
        const val ORGANIZATION_REPORT_TABLE = "organization_report"
        const val ORGANIZATION_VERSION_TABLE = "organization_version"
        // FIELDS
        const val ORGANIZATION_ID = "organization_id"
        const val ORGANIZATION_VERSION = "organization_version"
        const val ORGANIZATION_FULL_NAME = "organization_full_name"
        const val ORGANIZATION_SHORT_NAME = "organization_short_name"
        const val ORGANIZATION_ADDRESS = "organization_address"
        const val ORGANIZATION_CONTACT = "organization_contact"
        const val ORGANIZATION_VOTES = "votes"
        const val ORGANIZATION_TIMESTAMP = "time_stamp"
        const val ORGANIZATION_REPORT_ID = "report_id"
        const val ORGANIZATION_CREATED_BY = "created_by"
        const val ORGANIZATION_REPORTED_BY = "reported_by"
    }

    @SqlQuery("SELECT * FROM $ORGANIZATION_TABLE")
    override fun getAllOrganizations(): List<Organization>

    @SqlQuery("SELECT * FROM $ORGANIZATION_TABLE WHERE $ORGANIZATION_ID = :organizationId")
    override fun getSpecificOrganization(organizationId: Int): Optional<Organization>

    @SqlUpdate("DELETE FROM $ORGANIZATION_TABLE WHERE $ORGANIZATION_ID = :organizationId")
    override fun deleteOrganization(organizationId: Int): Int

    @SqlUpdate("DELETE FROM $ORGANIZATION_TABLE")
    override fun deleteAllOrganizations(): Int

    @SqlUpdate(
            "UPDATE $ORGANIZATION_TABLE SET " +
                    "$ORGANIZATION_VERSION = :organization.version, " +
                    "$ORGANIZATION_CREATED_BY = :organization.createdBy, " +
                    "$ORGANIZATION_FULL_NAME = :organization.fullName, " +
                    "$ORGANIZATION_SHORT_NAME = :organization.shortName, " +
                    "$ORGANIZATION_ADDRESS = :organization.address, " +
                    "$ORGANIZATION_CONTACT = :organization.contact, " +
                    "$ORGANIZATION_VOTES = :organization.votes, " +
                    "$ORGANIZATION_TIMESTAMP = :organization.timestamp " +
                    "WHERE $ORGANIZATION_ID = :organization.organizationId"
    )
    @GetGeneratedKeys
    override fun updateOrganization(organization: Organization): Organization

    @SqlUpdate(
            "INSERT INTO $ORGANIZATION_TABLE (" +
                    "$ORGANIZATION_VERSION, " +
                    "$ORGANIZATION_CREATED_BY, " +
                    "$ORGANIZATION_FULL_NAME, " +
                    "$ORGANIZATION_SHORT_NAME, " +
                    "$ORGANIZATION_ADDRESS, " +
                    "$ORGANIZATION_CONTACT, " +
                    "$ORGANIZATION_VOTES, " +
                    "$ORGANIZATION_TIMESTAMP) " +
                    "VALUES(:organization.version, :organization.createdBy, :organization.fullName, :organization.shortName, " +
                    ":organization.address, :organization.contact, :organization.votes, :organization.timestamp)"
    )
    @GetGeneratedKeys
    override fun createOrganization(organization: Organization): Organization

    @SqlQuery("SELECT $ORGANIZATION_VOTES FROM $ORGANIZATION_TABLE WHERE $ORGANIZATION_ID = :organizationId")
    override fun getVotesOnOrganization(organizationId: Int): Int

    @SqlUpdate("UPDATE  $ORGANIZATION_TABLE SET $ORGANIZATION_VOTES = :votes WHERE $ORGANIZATION_ID = :organizationId")
    override fun updateVotesOnOrganization(organizationId: Int, votes: Int): Int

    @SqlUpdate(
            "INSERT INTO $ORGANIZATION_REPORT_TABLE( " +
                    "$ORGANIZATION_ID, " +
                    "$ORGANIZATION_FULL_NAME, " +
                    "$ORGANIZATION_SHORT_NAME, " +
                    "$ORGANIZATION_ADDRESS, " +
                    "$ORGANIZATION_CONTACT, " +
                    "$ORGANIZATION_REPORTED_BY, " +
                    "$ORGANIZATION_VOTES, " +
                    "$ORGANIZATION_TIMESTAMP) " +
                    "VALUES(:organizationReport.organizationId, :organizationReport.fullName, :organizationReport.shortName, " +
                    ":organizationReport.address, :organizationReport.contact, :organizationReport.reportedBy, " +
                    ":organizationReport.votes, :organizationReport.timestamp)"
    )
    @GetGeneratedKeys
    override fun reportOrganization(organizationReport: OrganizationReport): OrganizationReport

    @SqlUpdate("DELETE FROM $ORGANIZATION_REPORT_TABLE WHERE $ORGANIZATION_ID = :organizationId")
    override fun deleteAllReportsOnOrganization(organizationId: Int): Int

    @SqlUpdate("DELETE FROM $ORGANIZATION_REPORT_TABLE WHERE $ORGANIZATION_REPORT_ID = :reportId")
    override fun deleteSpecificReportOnOrganization(reportId: Int): Int

    @SqlQuery("SELECT * FROM $ORGANIZATION_REPORT_TABLE WHERE $ORGANIZATION_ID = :organizationId")
    override fun getAllReportsOnOrganization(organizationId: Int): List<OrganizationReport>

    @SqlQuery("SELECT * FROM $ORGANIZATION_REPORT_TABLE WHERE $ORGANIZATION_ID = :organizationId AND $ORGANIZATION_REPORT_ID = :reportId")
    override fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int): Optional<OrganizationReport>

    @SqlQuery("SELECT $ORGANIZATION_VOTES FROM $ORGANIZATION_REPORT_TABLE WHERE $ORGANIZATION_REPORT_ID = :reportId AND $ORGANIZATION_ID = :organizationId")
    override fun getVotesOnOrganizationReport(organizationId: Int, reportId: Int): Int

    @SqlUpdate("UPDATE $ORGANIZATION_REPORT_TABLE SET $ORGANIZATION_VOTES = :votes WHERE $ORGANIZATION_REPORT_ID = :reportId AND $ORGANIZATION_ID = :organizationId")
    override fun updateVotesOnOrganizationReport(organizationId: Int, reportId: Int, votes: Int): Int

    @SqlQuery("SELECT * FROM $ORGANIZATION_VERSION_TABLE WHERE $ORGANIZATION_ID = :organizationId")
    override fun getAllVersionsOfOrganization(organizationId: Int): List<OrganizationVersion>

    @SqlQuery("SELECT * FROM $ORGANIZATION_VERSION_TABLE WHERE $ORGANIZATION_ID = :organizationId AND $ORGANIZATION_VERSION = :version"
    )
    override fun getSpecificVersionOfOrganization(organizationId: Int, version: Int): Optional<OrganizationVersion>

    @SqlUpdate(
            "INSERT INTO $ORGANIZATION_VERSION_TABLE (" +
                    "$ORGANIZATION_ID, " +
                    "$ORGANIZATION_VERSION, " +
                    "$ORGANIZATION_CREATED_BY, " +
                    "$ORGANIZATION_FULL_NAME," +
                    "$ORGANIZATION_SHORT_NAME, " +
                    "$ORGANIZATION_ADDRESS, " +
                    "$ORGANIZATION_CONTACT, " +
                    "$ORGANIZATION_TIMESTAMP) " +
                    "VALUES (:version.organizationId, :version.version, :version.createdBy, :version.fullName, :version.shortName, " +
                    ":version.address, :version.contact, :version.timestamp)"
    )
    @GetGeneratedKeys
    override fun createOrganizationVersion(version: OrganizationVersion): OrganizationVersion

    @SqlUpdate(
            "DELETE FROM $ORGANIZATION_VERSION_TABLE WHERE $ORGANIZATION_ID = :organizationId"
    )
    override fun deleteAllOrganizationVersions(organizationId: Int): Int

    @SqlUpdate(
            "DELETE FROM $ORGANIZATION_VERSION_TABLE WHERE $ORGANIZATION_ID = :organizationId AND $ORGANIZATION_VERSION = :version"
    )
    override fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int): Int

    @SqlUpdate(
            "DELETE FROM $ORGANIZATION_REPORT_TABLE WHERE $ORGANIZATION_ID = :organizationId AND $ORGANIZATION_REPORT_ID = :reportId"
    )
    override fun deleteReportOnOrganization(organizationId: Int, reportId: Int): Int
}
