package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import org.jdbi.v3.core.Handle

import org.jdbi.v3.core.Jdbi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class OrganizationDAOImpl : OrganizationDAO {

    companion object {
        //TABLE NAMES
        const val ORG_TABLE = "organization"
        const val ORG_REPORT_TABLE = "organization_report"
        const val ORG_VERSION_TABLE = "organization_version"
        // FIELDS
        const val ORG_ID = "organization_id"
        const val ORG_VERSION = "organization_version"
        const val ORG_FULL_NAME = "organization_full_name"
        const val ORG_SHORT_NAME = "organization_short_name"
        const val ORG_ADDRESS = "organization_address"
        const val ORG_CONTACT = "organization_contact"
        const val ORG_VOTE = "votes"
        const val ORG_TIMESTAMP = "time_stamp"
        const val ORG_REPORT_ID = "report_id"
        const val ORG_CREATED_BY = "created_by"
        const val ORG_REPORTED_BY = "reported_by"
    }

    @Autowired
    lateinit var handle: Handle

    override fun getSpecificOrganization(organizationId: Int) =
            handle.createQuery(
                    "select * from $ORG_TABLE where organization_id = :organizationId"
            ).bind("organizationId", organizationId).mapTo(Organization::class.java).findFirst()

    override fun getAllOrganizations() =
            handle.createQuery(
                    "select * from $ORG_TABLE"
            ).mapTo(Organization::class.java).toList()

    override fun deleteOrganization(organizationId: Int) =
            handle.createUpdate("delete from $ORG_TABLE where organization_id = :organizationId")
                    .bind("organizationId", organizationId)
                    .execute()

    override fun deleteAllOrganizations() =
            handle.createUpdate(
                    "delete from $ORG_TABLE"
            ).execute()

    override fun updateOrganization(organization: Organization) =
            handle.createUpdate(
                    "update $ORG_TABLE SET " +
                            "$ORG_VERSION = :version, $ORG_CREATED_BY = :createdBy, " +
                            "$ORG_FULL_NAME = :fullName, $ORG_SHORT_NAME = :shortName, " +
                            "$ORG_CONTACT = :contact, $ORG_ADDRESS = :address, " +
                            "$ORG_TIMESTAMP = :timestamp " +
                            "where $ORG_ID = :organization_id"
            )
                    .bind("version", organization.version)
                    .bind("createdBy", organization.createdBy)
                    .bind("fullName", organization.fullName)
                    .bind("shortName", organization.shortName)
                    .bind("contact", organization.contact)
                    .bind("address", organization.address)
                    .bind("timestamp", organization.timestamp)
                    .bind("organization_id", organization.id)
                    .execute()

    override fun createOrganization(organization: Organization) =
            handle.createUpdate(
                    "insert into $ORG_TABLE " +
                            "($ORG_FULL_NAME, " +
                            "$ORG_SHORT_NAME, $ORG_CREATED_BY, $ORG_ADDRESS, " +
                            "$ORG_CONTACT, $ORG_TIMESTAMP) " +
                            "values(:fullName, :shortName, :created_by, :address, :contact, :timestamp)"
            )
                    .bind("fullName", organization.fullName)
                    .bind("shortName", organization.shortName)
                    .bind("address", organization.address)
                    .bind("contact", organization.contact)
                    .bind("created_by", organization.createdBy)
                    .bind("timestamp", organization.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Organization::class.java)
                    .findFirst()

    override fun voteOnOrganization(organizationId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select votes from $ORG_TABLE where organization_id = :organizationId"
        )
                .bind("organizationId", organizationId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $ORG_TABLE set votes = :votes where organization_id = :organizationId"
        )
                .bind("votes", votes)
                .bind("organizationId", organizationId)
                .execute()
    }

    override fun reportOrganization(organizationReport: OrganizationReport) =
            handle.createUpdate(
                    "insert into $ORG_REPORT_TABLE" +
                            "($ORG_FULL_NAME, $ORG_ID," +
                            "$ORG_SHORT_NAME, $ORG_REPORTED_BY, $ORG_ADDRESS, " +
                            "$ORG_CONTACT, $ORG_TIMESTAMP) " +
                            "values(:fullName, :organization_id, :shortName, :reported_by, :address, :contact, :timestamp)"
            )
                    .bind("fullName", organizationReport.fullName)
                    .bind("shortName", organizationReport.shortName)
                    .bind("address", organizationReport.address)
                    .bind("contact", organizationReport.contact)
                    .bind("reported_by", organizationReport.reportedBy)
                    .bind("organization_id", organizationReport.organization_id)
                    .bind("timestamp", organizationReport.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(OrganizationReport::class.java)
                    .findFirst()

    override fun deleteAllReportsOnOrganization(organizationId: Int) =
            handle.createUpdate(
                    "delete from $ORG_REPORT_TABLE where organization_id = :organizationId"
            )
                    .bind("organizationId", organizationId)
                    .execute()

    override fun deleteReportOnOrganization(reportId: Int) =
            handle.createUpdate(
                    "delete from $ORG_REPORT_TABLE where report_id = :reportId"
            )
                    .bind("reportId", reportId)
                    .execute()

    override fun getAllReportsOnOrganization(organizationId: Int): List<OrganizationReport> =
            handle.createQuery(
                    "select * from $ORG_REPORT_TABLE where organization_id = :organization_id "
            ).bind("organization_id", organizationId).mapTo(OrganizationReport::class.java).toList()

    override fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int) =
            handle.createQuery(
                    "select * from $ORG_REPORT_TABLE where report_id = :organization_id "
            ).bind("organization_id", reportId).mapTo(OrganizationReport::class.java).findFirst()

    override fun voteOnReport(organizationId: Int, reportId: Int, vote: Vote): Int {
        val voteQuery = "select votes from $ORG_REPORT_TABLE where report_id = :reportId"
        var votes = handle.createQuery(voteQuery)
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $ORG_REPORT_TABLE set votes = :votes where report_id = :reportId"
        return handle.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getAllVersionsOfOrganization(organizationId: Int) =
            handle.createQuery(
                    "select * from $ORG_VERSION_TABLE where organization_id = :organization_id "
            ).bind("organization_id", organizationId).mapTo(OrganizationVersion::class.java).toList()

    override fun getSpecificVersionOfOrganization(organizationId: Int, version: Int) =
            handle.createQuery(
                    "select * from $ORG_VERSION_TABLE where organization_id = :organization_id and  organization_version = :version"
            )
                    .bind("organization_id", organizationId)
                    .bind("version", version)
                    .mapTo(OrganizationVersion::class.java).findFirst()

    override fun createVersion(version: OrganizationVersion) =
            handle.createUpdate(
                    "insert into $ORG_VERSION_TABLE " +
                            "($ORG_ID, $ORG_VERSION, $ORG_FULL_NAME, $ORG_SHORT_NAME, " +
                            "$ORG_CONTACT, $ORG_ADDRESS, $ORG_TIMESTAMP, $ORG_CREATED_BY) " +
                            "values (:organization_id, :version, :fullName, :shortName, " +
                            ":contact, :address, :timestamp, :createdBy)"
            )
                    .bind("organization_id", version.organizationId)
                    .bind("version", version.version)
                    .bind("fullName", version.fullName)
                    .bind("shortName", version.shortName)
                    .bind("contact", version.contact)
                    .bind("address", version.address)
                    .bind("timestamp", version.timestamp)
                    .bind("createdBy", version.createdBy)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(OrganizationVersion::class.java)
                    .findFirst()

    override fun deleteAllVersionsOfOrganization(organizationId: Int) =
            handle.createUpdate(
                    "delete from $ORG_REPORT_TABLE where organization_id = :organizationId"
            )
                    .bind("organizationId", organizationId)
                    .execute()

    override fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int) =
            handle.createUpdate(
                    "delete from $ORG_VERSION_TABLE where $ORG_ID = :organizationId and $ORG_VERSION = :version"
            )
                    .bind("organizationId", organizationId)
                    .bind("version", version)
                    .execute()

    override fun addToOrganizationVersion(organization: Organization) =
            handle.createUpdate(
                    "insert into $ORG_VERSION_TABLE " +
                            "($ORG_ID, $ORG_VERSION, $ORG_FULL_NAME, $ORG_SHORT_NAME, " +
                            "$ORG_CONTACT, $ORG_ADDRESS, $ORG_TIMESTAMP, $ORG_CREATED_BY) " +
                            "values (:organization_id, :version, :fullName, :shortName, " +
                            ":contact, :address, :timestamp, :createdBy)"
            )
                    .bind("organization_id", organization.id)
                    .bind("version", organization.version)
                    .bind("fullName", organization.fullName)
                    .bind("shortName", organization.shortName)
                    .bind("contact", organization.contact)
                    .bind("address", organization.address)
                    .bind("timestamp", organization.timestamp)
                    .bind("createdBy", organization.createdBy)
                    .execute()
}
