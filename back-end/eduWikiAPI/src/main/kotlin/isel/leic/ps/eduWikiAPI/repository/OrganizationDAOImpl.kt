package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO

import org.jdbi.v3.core.Jdbi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

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
    lateinit var dbi: Jdbi

    override fun getSpecificOrganization(organizationId: Int): Organization = dbi.withHandle<Organization, Exception> {
        val select = "select * from $ORG_TABLE where organization_id = :organizationId"
        it.createQuery(select).bind("organizationId", organizationId).mapTo(Organization::class.java).findOnly()
    }

    override fun getAllOrganizations(): List<Organization> = dbi.withHandle<List<Organization>, Exception> {
        val select = "select * from $ORG_TABLE "
        it.createQuery(select).mapTo(Organization::class.java).toList()
    }

    override fun deleteOrganization(organizationId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $ORG_TABLE where organization_id = :organizationId"
        it.createUpdate(delete)
                .bind("organizationId", organizationId)
                .execute()
    }

    override fun deleteAllOrganizations(): Int = dbi.withHandle<Int, Exception> {
        it.createUpdate("delete from $ORG_TABLE").execute()
    }

    override fun updateOrganization(organization: Organization) = dbi.useHandle<Exception> {
        val update = "update $ORG_TABLE SET " +
                "$ORG_VERSION = :version, $ORG_CREATED_BY = :createdBy, " +
                "$ORG_FULL_NAME = :fullName, $ORG_SHORT_NAME = :shortName, " +
                "$ORG_CONTACT = :contact, $ORG_ADDRESS = :address, " +
                "$ORG_VOTE = :votes, $ORG_TIMESTAMP = :timestamp " +
                "where $ORG_ID = :id"

        it.createUpdate(update)
                .bind("version", organization.version)
                .bind("createdBy", organization.createdBy)
                .bind("fullName", organization.fullName)
                .bind("shortName", organization.shortName)
                .bind("contact", organization.contact)
                .bind("address", organization.address)
                .bind("votes", organization.votes)
                .bind("id", organization.id)
                .bind("timestamp", organization.timestamp)
                .execute()
    }

    override fun createOrganization(organization: Organization) = dbi.useHandle<Exception> {
        val insert = "insert into $ORG_TABLE " +
                "($ORG_FULL_NAME, " +
                "$ORG_SHORT_NAME, $ORG_CREATED_BY, $ORG_ADDRESS, " +
                "$ORG_CONTACT, $ORG_TIMESTAMP) " +
                "values(:fullName, :shortName, :created_by, :address, :contact, :timestamp)"
        it.createUpdate(insert)
                .bind("fullName", organization.fullName)
                .bind("shortName", organization.shortName)
                .bind("address", organization.address)
                .bind("contact", organization.contact)
                .bind("created_by", organization.createdBy)
                .bind("timestamp", organization.timestamp)
                .execute()
    }

    override fun voteOnOrganization(organizationId: Int, vote: Vote) = dbi.useTransaction<Exception> {
        val voteQuery = "select votes from $ORG_TABLE where organization_id = :organizationId"
        var votes = it.createQuery(voteQuery)
                .bind("organizationId", organizationId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $ORG_TABLE set votes = :votes where organization_id = :organizationId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("organizationId", organizationId)
                .execute()

    }

    override fun reportOrganization(organizationReport: OrganizationReport) = dbi.useHandle<Exception> {
        val insert = "insert into $ORG_REPORT_TABLE" +
                "($ORG_FULL_NAME, $ORG_ID," +
                "$ORG_SHORT_NAME, $ORG_REPORTED_BY, $ORG_ADDRESS, " +
                "$ORG_CONTACT, $ORG_TIMESTAMP) " +
                "values(:fullName,:id, :shortName, :reported_by, :address, :contact, :timestamp)"
        it.createUpdate(insert)
                .bind("fullName", organizationReport.fullName)
                .bind("shortName", organizationReport.shortName)
                .bind("address", organizationReport.address)
                .bind("contact", organizationReport.contact)
                .bind("reported_by", organizationReport.reportedBy)
                .bind("id", organizationReport.id)
                .bind("timestamp", organizationReport.timestamp)
                .execute()
    }

    override fun deleteAllReportsOnOrganization(organizationId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $ORG_REPORT_TABLE where organization_id = :organizationId"
        it.createUpdate(delete)
                .bind("organizationId", organizationId)
                .execute()
    }

    override fun deleteReportOnOrganization(reportId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $ORG_REPORT_TABLE where report_id = :reportId"
        it.createUpdate(delete)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getAllOrganizationReports(organizationId: Int): List<OrganizationReport> = dbi.withHandle<List<OrganizationReport>, Exception> {
        val select = "select * from $ORG_REPORT_TABLE where organization_id = :id "
        it.createQuery(select).bind("id", organizationId).mapTo(OrganizationReport::class.java).toList()
    }

    override fun getSpecificReportOfOrganization(organizationId: Int, reportId: Int): OrganizationReport = dbi.withHandle<OrganizationReport, Exception> {
        val select = "select * from $ORG_REPORT_TABLE where report_id = :id "
        it.createQuery(select).bind("id", reportId).mapTo(OrganizationReport::class.java).findOnly()
    }

    override fun voteOnReport(organizationId: Int, reportId: Int, vote: Vote) = dbi.useTransaction<Exception> {
        val voteQuery = "select votes from $ORG_REPORT_TABLE where report_id = :reportId"
        var votes = it.createQuery(voteQuery)
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $ORG_REPORT_TABLE set votes = :votes where report_id = :reportId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getAllVersions(organizationId: Int): List<OrganizationVersion> = dbi.withHandle<List<OrganizationVersion>, Exception> {
        val select = "select * from $ORG_VERSION_TABLE where organization_id = :id "
        it.createQuery(select).bind("id", organizationId).mapTo(OrganizationVersion::class.java).toList()
    }

    override fun getVersion(organizationId: Int, version: Int): OrganizationVersion = dbi.withHandle<OrganizationVersion, Exception> {
        val select = "select * from $ORG_VERSION_TABLE where organization_id = :id and  organization_version = :version"
        it.createQuery(select)
                .bind("id", organizationId)
                .bind("version", version)
                .mapTo(OrganizationVersion::class.java).findOnly()
    }

    override fun createVersion(version: OrganizationVersion) = dbi.useHandle<Exception> {
        val insert = "insert into $ORG_VERSION_TABLE" +
                "($ORG_FULL_NAME, $ORG_VERSION," +
                "$ORG_SHORT_NAME, $ORG_CREATED_BY, $ORG_ADDRESS, " +
                "$ORG_CONTACT, $ORG_TIMESTAMP) " +
                "values(:fullName,:version, :shortName, :created_by, :address, :contact, :timestamp)"
        it.createUpdate(insert)
                .bind("fullName", version.fullName)
                .bind("shortName", version.shortName)
                .bind("address", version.address)
                .bind("contact", version.contact)
                .bind("created_by", version.createdBy)
                .bind("version", version.version)
                .bind("timestamp", version.timestamp)
                .execute()
    }

    override fun deleteAllVersions(organizationId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $ORG_REPORT_TABLE where organization_id = :organizationId"
        it.createUpdate(delete)
                .bind("organizationId", organizationId)
                .execute()
    }

    override fun deleteVersion(organizationId: Int, version: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $ORG_REPORT_TABLE where $ORG_ID = :organizationId and $ORG_VERSION = :version"
        it.createUpdate(delete)
                .bind("organizationId", organizationId)
                .bind("version", version)
                .execute()
    }

    override fun addToOrganizationVersion(organization: Organization) = dbi.useHandle<Exception> {
        val insert = "insert into $ORG_VERSION_TABLE " +
                "($ORG_ID, $ORG_VERSION, $ORG_FULL_NAME, $ORG_SHORT_NAME, " +
                "$ORG_CONTACT, $ORG_ADDRESS, $ORG_TIMESTAMP, $ORG_CREATED_BY) " +
                "values (:id, :version, :fullName, :shortName, " +
                ":contact, :address, :timestamp, :createdBy)"
        it.createUpdate(insert)
                .bind("id", organization.id)
                .bind("version", organization.version)
                .bind("fullName", organization.fullName)
                .bind("shortName", organization.shortName)
                .bind("contact", organization.contact)
                .bind("address", organization.address)
                .bind("timestamp", organization.timestamp)
                .bind("createdBy", organization.createdBy)
                .execute()
    }

}
