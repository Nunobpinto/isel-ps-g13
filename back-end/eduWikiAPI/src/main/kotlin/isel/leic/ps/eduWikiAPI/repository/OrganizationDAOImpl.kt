package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
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
    }

    @Autowired
    lateinit var dbi: Jdbi

    override fun getOrganization(organizationId: Int): Organization = dbi.withHandle<Organization, Exception> {
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

    override fun updateOrganization(organization: Organization) = TODO("dynamically update org by filled values in Organization parameter")

    override fun createOrganization(organization: Organization) = dbi.useHandle<Exception> {
        val insert = "insert into $ORG_TABLE " +
                "($ORG_FULL_NAME, " +
                "$ORG_SHORT_NAME, $ORG_CREATED_BY, $ORG_ADDRESS, " +
                "$ORG_CONTACT) " +
                "values(:fullName, :shortName, :created_by, :address, :contact)"
        it.createUpdate(insert)
                .bind("fullName", organization.fullName)
                .bind("shortName", organization.shortName)
                .bind("address", organization.address)
                .bind("contact", organization.contact)
                .bind("created_by", organization.createdBy)
                .execute()
    }

    override fun voteOnOrganization(organizationId: Int, vote: Vote) = dbi.useTransaction<Exception> {
        val voteQuery = "select votes from $ORG_TABLE where organization_id = :organizationId"
        var votes = it.createQuery(voteQuery)
                .bind("organizationId", organizationId)
                .mapTo(Int::class.java).findOnly()
        votes = if(vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $ORG_TABLE set votes = :votes where organization_id = :organizationId"
        it.createUpdate(updateQuery)
                .bind("votes",votes)
                .bind("organizationId",organizationId)
                .execute()

    }

    override fun reportOrganization(organizationReport: OrganizationReport) = dbi.useHandle<Exception> {
        val insert = "insert into $ORG_REPORT_TABLE" +
                "($ORG_FULL_NAME, $ORG_ID," +
                "$ORG_SHORT_NAME, $ORG_CREATED_BY, $ORG_ADDRESS, " +
                "$ORG_CONTACT) " +
                "values(:fullName,:id, :shortName, :created_by, :address, :contact)"
        it.createUpdate(insert)
                .bind("fullName", organizationReport.fullName)
                .bind("shortName", organizationReport.shortName)
                .bind("address", organizationReport.address)
                .bind("contact", organizationReport.contact)
                .bind("created_by", organizationReport.createdBy)
                .bind("id", organizationReport.id)
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

    override fun getSpecificReport(organizationId: Int, reportId: Int): OrganizationReport = dbi.withHandle<OrganizationReport, Exception> {
        val select = "select * from $ORG_REPORT_TABLE where report_id = :id "
        it.createQuery(select).bind("id", reportId).mapTo(OrganizationReport::class.java).findOnly()
    }

    override fun voteOnReport(organizationId: Int, reportId: Int, vote: Vote)  = dbi.useTransaction<Exception> {
        val voteQuery = "select votes from $ORG_REPORT_TABLE where report_id = :reportId"
        var votes = it.createQuery(voteQuery)
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if(vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $ORG_REPORT_TABLE set votes = :votes where report_id = :reportId"
        it.createUpdate(updateQuery)
                .bind("votes",votes)
                .bind("reportId",reportId)
                .execute()
    }

}
