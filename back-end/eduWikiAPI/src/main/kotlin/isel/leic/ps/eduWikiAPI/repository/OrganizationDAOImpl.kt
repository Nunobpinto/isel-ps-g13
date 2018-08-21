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
        // ORGANIZATION DEFAULT KEY
        const val ORGANIZATION_KEY = 'X'
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
        const val ORGANIZATION_WEBSITE = "organization_website"
        const val ORGANIZATION_LOG_ID = "log_id"
        const val ORGANIZATION_REPORT_LOG_ID = "log_id"
        const val ORGANIZATION_TIMESTAMP = "time_stamp"
        const val ORGANIZATION_REPORT_ID = "organization_report_id"
        const val ORGANIZATION_CREATED_BY = "created_by"
        const val ORGANIZATION_REPORTED_BY = "reported_by"
        const val ORGANIZATION_REPORT_VOTES = "votes"
    }

    @Qualifier("MainJdbi")
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getOrganization(): Optional<Organization> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getOrganization()

    override fun updateOrganization(organization: Organization): Organization =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).updateOrganization(organization)

    override fun createOrganization(organization: Organization): Organization =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).createOrganization(organization)

    override fun getAllVersionsOfOrganization(): List<OrganizationVersion> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getAllVersionsOfOrganization()

    override fun getSpecificVersionOfOrganization(version: Int): Optional<OrganizationVersion> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getSpecificVersionOfOrganization(version)

    override fun createOrganizationVersion(version: OrganizationVersion): OrganizationVersion =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).createOrganizationVersion(version)

    override fun reportOrganization(organizationReport: OrganizationReport): OrganizationReport =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).reportOrganization(organizationReport)

    override fun deleteSpecificReportOnOrganization(reportId: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).deleteSpecificReportOnOrganization(reportId)

    override fun deleteAllReportsOnOrganization(): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).deleteAllReportsOnOrganization()

    override fun getAllReportsOnOrganization(): List<OrganizationReport> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getAllReportsOnOrganization()

    override fun getSpecificReportOnOrganization(reportId: Int): Optional<OrganizationReport> =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getSpecificReportOnOrganization(reportId)

    override fun updateVotesOnOrganizationReport(reportId: Int, votes: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).updateVotesOnOrganizationReport(reportId, votes)

    override fun getVotesOnOrganizationReport(reportId: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).getVotesOnOrganizationReport(reportId)

    override fun deleteReportOnOrganization(reportId: Int): Int =
            jdbi.open().attach(OrganizationDAOJdbi::class.java).deleteReportOnOrganization(reportId)

    internal interface OrganizationDAOJdbi : OrganizationDAO {

        @SqlQuery("SELECT * FROM :schema.$ORGANIZATION_TABLE WHERE $ORGANIZATION_ID = '$ORGANIZATION_KEY'")
        override fun getOrganization(): Optional<Organization>

        @SqlUpdate(
                "UPDATE :schema.$ORGANIZATION_TABLE SET " +
                        "$ORGANIZATION_VERSION = :organization.version, " +
                        "$ORGANIZATION_CREATED_BY = :organization.createdBy, " +
                        "$ORGANIZATION_FULL_NAME = :organization.fullName, " +
                        "$ORGANIZATION_SHORT_NAME = :organization.shortName, " +
                        "$ORGANIZATION_ADDRESS = :organization.address, " +
                        "$ORGANIZATION_CONTACT = :organization.contact, " +
                        "$ORGANIZATION_WEBSITE = :organization.website, " +
                        "$ORGANIZATION_TIMESTAMP = :organization.timestamp " +
                        "WHERE $ORGANIZATION_ID = '$ORGANIZATION_KEY'"
        )
        @GetGeneratedKeys
        override fun updateOrganization(organization: Organization): Organization

        @SqlUpdate(
                "INSERT INTO :schema.$ORGANIZATION_TABLE (" +
                        "$ORGANIZATION_VERSION, " +
                        "$ORGANIZATION_CREATED_BY, " +
                        "$ORGANIZATION_FULL_NAME, " +
                        "$ORGANIZATION_SHORT_NAME, " +
                        "$ORGANIZATION_ADDRESS, " +
                        "$ORGANIZATION_CONTACT, " +
                        "$ORGANIZATION_WEBSITE, " +
                        "$ORGANIZATION_TIMESTAMP) " +
                        "VALUES(:organization.version, :organization.createdBy, :organization.fullName, :organization.shortName, " +
                        ":organization.address, :organization.contact, :organization.website, :organization.timestamp)"
        )
        @GetGeneratedKeys
        override fun createOrganization(organization: Organization): Organization

        @SqlUpdate(
                "INSERT INTO :schema.$ORGANIZATION_REPORT_TABLE( " +
                        "$ORGANIZATION_FULL_NAME, " +
                        "$ORGANIZATION_SHORT_NAME, " +
                        "$ORGANIZATION_ADDRESS, " +
                        "$ORGANIZATION_CONTACT, " +
                        "$ORGANIZATION_WEBSITE, " +
                        "$ORGANIZATION_REPORTED_BY, " +
                        "$ORGANIZATION_REPORT_VOTES, " +
                        "$ORGANIZATION_TIMESTAMP) " +
                        "VALUES(:organizationReport.fullName, :organizationReport.shortName, " +
                        ":organizationReport.address, :organizationReport.contact, :organizationReport.website, :organizationReport.reportedBy, " +
                        ":organizationReport.votes, :organizationReport.timestamp)"
        )
        @GetGeneratedKeys
        override fun reportOrganization(organizationReport: OrganizationReport): OrganizationReport

        @SqlUpdate("DELETE FROM :schema.$ORGANIZATION_REPORT_TABLE")
        override fun deleteAllReportsOnOrganization(): Int

        @SqlUpdate("DELETE FROM :schema.$ORGANIZATION_REPORT_TABLE WHERE $ORGANIZATION_REPORT_ID = :reportId")
        override fun deleteSpecificReportOnOrganization(reportId: Int): Int

        @SqlQuery("SELECT * FROM :schema.$ORGANIZATION_REPORT_TABLE")
        override fun getAllReportsOnOrganization(): List<OrganizationReport>

        @SqlQuery("SELECT * FROM :schema.$ORGANIZATION_REPORT_TABLE WHERE $ORGANIZATION_REPORT_ID = :reportId")
        override fun getSpecificReportOnOrganization(reportId: Int): Optional<OrganizationReport>

        @SqlQuery("SELECT $ORGANIZATION_REPORT_VOTES FROM :schema.$ORGANIZATION_REPORT_TABLE WHERE $ORGANIZATION_REPORT_ID = :reportId")
        override fun getVotesOnOrganizationReport(reportId: Int): Int

        @SqlUpdate("UPDATE :schema.$ORGANIZATION_REPORT_TABLE SET $ORGANIZATION_REPORT_VOTES = :votes WHERE $ORGANIZATION_REPORT_ID = :reportId")
        override fun updateVotesOnOrganizationReport(reportId: Int, votes: Int): Int

        @SqlQuery("SELECT * FROM :schema.$ORGANIZATION_VERSION_TABLE")
        override fun getAllVersionsOfOrganization(): List<OrganizationVersion>

        @SqlQuery("SELECT * FROM :schema.$ORGANIZATION_VERSION_TABLE WHERE $ORGANIZATION_VERSION = :version"
        )
        override fun getSpecificVersionOfOrganization(version: Int): Optional<OrganizationVersion>

        @SqlUpdate(
                "INSERT INTO :schema.$ORGANIZATION_VERSION_TABLE (" +
                        "$ORGANIZATION_VERSION, " +
                        "$ORGANIZATION_CREATED_BY, " +
                        "$ORGANIZATION_FULL_NAME," +
                        "$ORGANIZATION_SHORT_NAME, " +
                        "$ORGANIZATION_ADDRESS, " +
                        "$ORGANIZATION_CONTACT, " +
                        "$ORGANIZATION_WEBSITE, " +
                        "$ORGANIZATION_TIMESTAMP) " +
                        "VALUES (:version.version, :version.createdBy, :version.fullName, :version.shortName, " +
                        ":version.address, :version.contact, :version.website, :version.timestamp)"
        )
        @GetGeneratedKeys
        override fun createOrganizationVersion(version: OrganizationVersion): OrganizationVersion

        @SqlUpdate(
                "DELETE FROM :schema.$ORGANIZATION_REPORT_TABLE WHERE $ORGANIZATION_REPORT_ID = :reportId"
        )
        override fun deleteReportOnOrganization(reportId: Int): Int
    }
}
