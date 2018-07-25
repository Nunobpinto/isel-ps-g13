package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import org.jdbi.v3.core.Handle

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

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
        const val ORGANIZATION_TIMESTAMP = "time_stamp"
        const val ORGANIZATION_REPORT_ID = "report_id"
        const val ORGANIZATION_CREATED_BY = "created_by"
        const val ORGANIZATION_REPORTED_BY = "reported_by"
    }

    @Autowired
    lateinit var handle: Handle

    override fun getAllOrganizations() =
            handle.createQuery("select * from $ORGANIZATION_TABLE")
                    .mapTo(Organization::class.java)
                    .list()

    override fun getSpecificOrganization(organizationId: Int) =
            handle.createQuery(
                    "select * from $ORGANIZATION_TABLE " +
                            "where $ORGANIZATION_ID = :organizationId"
            )
                    .bind("organizationId", organizationId)
                    .mapTo(Organization::class.java)
                    .findFirst()

    override fun deleteOrganization(organizationId: Int) =
            handle.createUpdate(
                    "delete from $ORGANIZATION_TABLE " +
                            "where $ORGANIZATION_ID = :organizationId"
            )
                    .bind("organizationId", organizationId)
                    .execute()

    override fun deleteAllOrganizations() =
            handle.createUpdate("delete from $ORGANIZATION_TABLE")
                    .execute()

    override fun updateOrganization(organization: Organization) =
            handle.createUpdate(
                    "update $ORGANIZATION_TABLE SET " +
                            "$ORGANIZATION_VERSION = :version, " +
                            "$ORGANIZATION_CREATED_BY = :createdBy, " +
                            "$ORGANIZATION_FULL_NAME = :fullName, " +
                            "$ORGANIZATION_SHORT_NAME = :shortName, " +
                            "$ORGANIZATION_ADDRESS = :address, " +
                            "$ORGANIZATION_CONTACT = :contact, " +
                            "$ORGANIZATION_VOTES = :votes " +
                            "$ORGANIZATION_TIMESTAMP = :timestamp " +
                            "where $ORGANIZATION_ID = :organizationId"
            )
                    .bind("organizationId", organization.organizationId)
                    .bind("version", organization.version)
                    .bind("createdBy", organization.createdBy)
                    .bind("fullName", organization.fullName)
                    .bind("shortName", organization.shortName)
                    .bind("address", organization.address)
                    .bind("contact", organization.contact)
                    .bind("votes", organization.votes)
                    .bind("timestamp", organization.timestamp)
                    .execute()

    override fun createOrganization(organization: Organization) =
            handle.createUpdate("insert into $ORGANIZATION_TABLE (" +
                    "$ORGANIZATION_VERSION, " +
                    "$ORGANIZATION_CREATED_BY, " +
                    "$ORGANIZATION_FULL_NAME, " +
                    "$ORGANIZATION_SHORT_NAME, " +
                    "$ORGANIZATION_ADDRESS, " +
                    "$ORGANIZATION_CONTACT, " +
                    "$ORGANIZATION_VOTES, " +
                    "$ORGANIZATION_TIMESTAMP " +
                    ") " +
                    "values(:version, :createdby, :fullName, :shortName, " +
                    ":address, :contact, :votes, :timestamp)"
            )
                    .bind("version", organization.version)
                    .bind("createdBy", organization.createdBy)
                    .bind("fullName", organization.fullName)
                    .bind("shortName", organization.shortName)
                    .bind("address", organization.address)
                    .bind("contact", organization.contact)
                    .bind("votes", organization.votes)
                    .bind("timestamp", organization.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Organization::class.java)
                    .findFirst()

    override fun voteOnOrganization(organizationId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $ORGANIZATION_VOTES from $ORGANIZATION_TABLE " +
                        "where $ORGANIZATION_ID = :organizationId"
        )
                .bind("organizationId", organizationId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $ORGANIZATION_TABLE set $ORGANIZATION_VOTES = :votes " +
                        "where $ORGANIZATION_ID = :organizationId"
        )
                .bind("votes", votes)
                .bind("organizationId", organizationId)
                .execute()
    }

    override fun reportOrganization(organizationReport: OrganizationReport) =
            handle.createUpdate(
                    "insert into $ORGANIZATION_REPORT_TABLE(" +
                            "$ORGANIZATION_ID, " +
                            "$ORGANIZATION_FULL_NAME, " +
                            "$ORGANIZATION_SHORT_NAME, " +
                            "$ORGANIZATION_ADDRESS, " +
                            "$ORGANIZATION_CONTACT, " +
                            "$ORGANIZATION_REPORTED_BY, " +
                            "$ORGANIZATION_VOTES, " +
                            "$ORGANIZATION_TIMESTAMP " +
                            ") " +
                            "values(:orgId, :fullName, :shortName," +
                            ":address, :contact, :reportedBy, :votes, :timestamp)"
            )
                    .bind("orgId", organizationReport.organizationId)
                    .bind("fullName", organizationReport.fullName)
                    .bind("shortName", organizationReport.shortName)
                    .bind("address", organizationReport.address)
                    .bind("contact", organizationReport.contact)
                    .bind("reportedBy", organizationReport.reportedBy)
                    .bind("votes", organizationReport.votes)
                    .bind("timestamp", organizationReport.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(OrganizationReport::class.java)
                    .findFirst()

    override fun deleteAllReportsOnOrganization(organizationId: Int) =
            handle.createUpdate(
                    "delete from $ORGANIZATION_REPORT_TABLE " +
                            "where $ORGANIZATION_ID = :organizationId"
            )
                    .bind("organizationId", organizationId)
                    .execute()

    override fun deleteReportOnOrganization(reportId: Int) =
            handle.createUpdate(
                    "delete from $ORGANIZATION_REPORT_TABLE" +
                            " where $ORGANIZATION_REPORT_ID = :reportId"
            )
                    .bind("reportId", reportId)
                    .execute()

    override fun getAllReportsOnOrganization(organizationId: Int): List<OrganizationReport> =
            handle.createQuery(
                    "select * from $ORGANIZATION_REPORT_TABLE " +
                            "where $ORGANIZATION_ID = :organizationId "
            )
                    .bind("organizationId", organizationId)
                    .mapTo(OrganizationReport::class.java)
                    .list()

    override fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int) =
            handle.createQuery(
                    "select * from $ORGANIZATION_REPORT_TABLE " +
                            "where $ORGANIZATION_ID = :organizationId " +
                            "and $ORGANIZATION_REPORT_ID = :reportId"
            )
                    .bind("organizationId", organizationId)
                    .bind("reportId", reportId)
                    .mapTo(OrganizationReport::class.java)
                    .findFirst()

    override fun voteOnOrganizationReport(organizationId: Int, reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $ORGANIZATION_VOTES from $ORGANIZATION_REPORT_TABLE " +
                        "where $ORGANIZATION_REPORT_ID = :reportId " +
                        "and $ORGANIZATION_ID = :orgId"
        )
                .bind("reportId", reportId)
                .bind("orgId", organizationId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        val updateQuery = "update $ORGANIZATION_REPORT_TABLE set $ORGANIZATION_VOTES = :votes" +
                "where $ORGANIZATION_REPORT_ID = :reportId" +
                "and $ORGANIZATION_ID = :orgId"
        return handle.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("reportId", reportId)
                .bind("orgId", organizationId)
                .execute()
    }

    override fun getAllVersionsOfOrganization(organizationId: Int) =
            handle.createQuery(
                    "select * from $ORGANIZATION_VERSION_TABLE " +
                            "where $ORGANIZATION_ID = :organizationId "
            )
                    .bind("organizationId", organizationId)
                    .mapTo(OrganizationVersion::class.java)
                    .list()

    override fun getSpecificVersionOfOrganization(organizationId: Int, version: Int) =
            handle.createQuery(
                    "select * from $ORGANIZATION_VERSION_TABLE " +
                            "where $ORGANIZATION_ID = :orgId " +
                            "and $ORGANIZATION_VERSION = :version"
            )
                    .bind("orgId", organizationId)
                    .bind("version", version)
                    .mapTo(OrganizationVersion::class.java)
                    .findFirst()

    override fun createVersion(version: OrganizationVersion) =
            handle.createUpdate(
                    "insert into $ORGANIZATION_VERSION_TABLE (" +
                            "$ORGANIZATION_ID, " +
                            "$ORGANIZATION_VERSION, " +
                            "$ORGANIZATION_CREATED_BY, " +
                            "$ORGANIZATION_FULL_NAME," +
                            "$ORGANIZATION_SHORT_NAME, " +
                            "$ORGANIZATION_ADDRESS, " +
                            "$ORGANIZATION_CONTACT, " +
                            "$ORGANIZATION_TIMESTAMP, " +
                            ")" +
                            "values (:orgId, :version, :createdBy, :fullName, :shortName, " +
                            ":address, :contact, :timestamp)"
            )
                    .bind("orgId", version.organizationId)
                    .bind("version", version.version)
                    .bind("createdBy", version.createdBy)
                    .bind("fullName", version.fullName)
                    .bind("shortName", version.shortName)
                    .bind("address", version.address)
                    .bind("contact", version.contact)
                    .bind("timestamp", version.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(OrganizationVersion::class.java)
                    .findFirst()

    override fun deleteAllVersionsOfOrganization(organizationId: Int) =
            handle.createUpdate(
                    "delete from $ORGANIZATION_REPORT_TABLE " +
                            "where $ORGANIZATION_ID = :orgId"
            )
                    .bind("orgId", organizationId)
                    .execute()

    override fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int) =
            handle.createUpdate(
                    "delete from $ORGANIZATION_VERSION_TABLE " +
                            "where $ORGANIZATION_ID = :orgId " +
                            "and $ORGANIZATION_VERSION = :version"
            )
                    .bind("orgId", organizationId)
                    .bind("version", version)
                    .execute()

    override fun addToOrganizationVersion(organization: Organization) =
            handle.createUpdate(
                    "insert into $ORGANIZATION_VERSION_TABLE (" +
                            "$ORGANIZATION_ID, " +
                            "$ORGANIZATION_VERSION, " +
                            "$ORGANIZATION_FULL_NAME, " +
                            "$ORGANIZATION_SHORT_NAME, " +
                            "$ORGANIZATION_CONTACT, " +
                            "$ORGANIZATION_ADDRESS, " +
                            "$ORGANIZATION_TIMESTAMP, " +
                            "$ORGANIZATION_CREATED_BY " +
                            ") " +
                            "values (:orgId, :version, :fullName, :shortName, " +
                            ":contact, :address, :timestamp, :createdBy)"
            )
                    .bind("orgId", organization.organizationId)
                    .bind("version", organization.version)
                    .bind("fullName", organization.fullName)
                    .bind("shortName", organization.shortName)
                    .bind("contact", organization.contact)
                    .bind("address", organization.address)
                    .bind("timestamp", organization.timestamp)
                    .bind("createdBy", organization.createdBy)
                    .execute()
}
