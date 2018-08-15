package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class OrganizationDAOImpl : OrganizationDAO {

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
        const val ORGANIZATION_LOG_ID = "log_id"
        const val ORGANIZATION_REPORT_LOG_ID = "log_id"
        const val ORGANIZATION_TIMESTAMP = "time_stamp"
        const val ORGANIZATION_REPORT_ID = "organization_report_id"
        const val ORGANIZATION_CREATED_BY = "created_by"
        const val ORGANIZATION_REPORTED_BY = "reported_by"
    }

    @Qualifier("MainJdbi")
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getSpecificOrganization(organizationId: Int): Optional<Organization> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getSpecificOrganization(organizationId)

    override fun getAllOrganizations(): List<Organization> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getAllOrganizations()

    override fun deleteOrganization(organizationId: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).deleteOrganization(organizationId)

    override fun deleteAllOrganizations(): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).deleteAllOrganizations()

    override fun updateOrganization(organization: Organization): Organization =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).updateOrganization(organization)

    override fun createOrganization(organization: Organization): Organization =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).createOrganization(organization)

    override fun getVotesOnOrganization(organizationId: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getVotesOnOrganization(organizationId)

    override fun updateVotesOnOrganization(organizationId: Int, votes: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).updateVotesOnOrganization(organizationId, votes)

    override fun getAllVersionsOfOrganization(organizationId: Int): List<OrganizationVersion> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getAllVersionsOfOrganization(organizationId)

    override fun getSpecificVersionOfOrganization(organizationId: Int, version: Int): Optional<OrganizationVersion> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getSpecificVersionOfOrganization(organizationId, version)

    override fun createOrganizationVersion(version: OrganizationVersion): OrganizationVersion =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).createOrganizationVersion(version)

    override fun deleteAllOrganizationVersions(organizationId: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).deleteAllOrganizationVersions(organizationId)

    override fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).deleteSpecificVersionOfOrganization(organizationId, version)

    override fun reportOrganization(organizationReport: OrganizationReport): OrganizationReport =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).reportOrganization(organizationReport)

    override fun deleteSpecificReportOnOrganization(reportId: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).deleteSpecificReportOnOrganization(reportId)

    override fun deleteAllReportsOnOrganization(organizationId: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).deleteAllReportsOnOrganization(organizationId)

    override fun getAllReportsOnOrganization(organizationId: Int): List<OrganizationReport> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getAllReportsOnOrganization(organizationId)

    override fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int): Optional<OrganizationReport> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getSpecificReportOnOrganization(organizationId, reportId)

    override fun updateVotesOnOrganizationReport(organizationId: Int, reportId: Int, votes: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).updateVotesOnOrganizationReport(organizationId, reportId, votes)

    override fun getVotesOnOrganizationReport(organizationId: Int, reportId: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getVotesOnOrganizationReport(organizationId, reportId)

    override fun deleteReportOnOrganization(organizationId: Int, reportId: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).deleteReportOnOrganization(organizationId, reportId)

    internal interface OrganizationDAOJdbi : OrganizationDAO {
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
}
