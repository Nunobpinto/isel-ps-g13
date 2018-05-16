package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO

import org.jdbi.v3.core.Jdbi

import org.jooq.DSLContext
import org.jooq.impl.DSL.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class OrganizationDAOImpl : OrganizationDAO {

    companion object {
        //TABLE NAMES
        const val ORG_TABLE = "organization"
        const val ORG_VERSION_TABLE = "organization_version"
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
    lateinit var dsl: DSLContext
    @Autowired
    lateinit var dbi: Jdbi

    override fun getOrganization(organizationId: Int) : Organization = dbi.withHandle<Organization, Exception>{
        it.createQuery(dsl
                .select(
                        field(ORG_ID),
                        field(ORG_VERSION),
                        field(ORG_FULL_NAME),
                        field(ORG_SHORT_NAME),
                        field(ORG_ADDRESS),
                        field(ORG_CONTACT),
                        field(ORG_CREATED_BY)
                )
                .from(table(ORG_TABLE))
                .where(field(ORG_ID).eq(organizationId))
                .sql
        ).mapTo(Organization::class.java).findOnly()
    }

    override fun getAllOrganizations(): List<Organization> = dbi.withHandle<List<Organization>, Exception> {
        it.createQuery(dsl
                .select(
                        field(ORG_ID),
                        field(ORG_VERSION),
                        field(ORG_FULL_NAME),
                        field(ORG_SHORT_NAME),
                        field(ORG_ADDRESS),
                        field(ORG_CONTACT),
                        field(ORG_CREATED_BY)
                )
                .from(table(ORG_TABLE))
                .sql
        ).mapTo(Organization::class.java).list()
    }

    override fun deleteOrganization(organizationId: Int) = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(ORG_TABLE))
                .where(field(ORG_ID).eq(organizationId))
                .sql
        )
    }

    override fun deleteAllOrganizations() = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(ORG_TABLE))
                .sql
        )
    }

    override fun updateOrganization(organization: Organization) = TODO("dynamically update org by filled values in Organization parameter")

    override fun createOrganization(organization: Organization) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(ORG_TABLE),
                        field(ORG_ID),
                        field(ORG_VERSION),
                        field(ORG_FULL_NAME),
                        field(ORG_SHORT_NAME),
                        field(ORG_ADDRESS),
                        field(ORG_CONTACT),
                        field(ORG_CREATED_BY)
                        )
                .values(
                        organization.id,
                        organization.version,
                        organization.fullName,
                        organization.shortName,
                        organization.address,
                        organization.contact,
                        organization.createdBy
                ).sql
        )
    }

    override fun voteOnOrganization(organizationId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field(ORG_VOTE))
                .from(table(ORG_TABLE))
                .where(field(ORG_ID).eq(organizationId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(ORG_TABLE))
                .set(field(ORG_VOTE), if(voteType == -1) votes.dec() else votes.inc())
                .where(field(ORG_ID).eq(organizationId))
                .sql
        )
    }

    override fun getVersionOrganization(versionOrganizationId: Int, version: Int): OrganizationVersion  = dbi.withHandle<OrganizationVersion, Exception> {
        it.createQuery(dsl
                .select(
                        field(ORG_ID),
                        field(ORG_VERSION),
                        field(ORG_FULL_NAME),
                        field(ORG_SHORT_NAME),
                        field(ORG_ADDRESS),
                        field(ORG_CONTACT),
                        field(ORG_CREATED_BY),
                        field(ORG_VOTE),
                        field(ORG_TIMESTAMP)
                )
                .from(table(ORG_VERSION_TABLE))
                .where(field(ORG_ID).eq(versionOrganizationId).and(field(ORG_VERSION).eq(version)))
                .sql
        ).mapTo(OrganizationVersion::class.java).first()
    }

    override fun getAllVersionOrganizations(): List<OrganizationVersion> = dbi.withHandle<List<OrganizationVersion>, Exception> {
        it.createQuery(dsl
                .select(
                        field(ORG_ID),
                        field(ORG_VERSION),
                        field(ORG_FULL_NAME),
                        field(ORG_SHORT_NAME),
                        field(ORG_ADDRESS),
                        field(ORG_CONTACT),
                        field(ORG_CREATED_BY),
                        field(ORG_VOTE),
                        field(ORG_TIMESTAMP)
                )
                .from(table(ORG_VERSION_TABLE))
                .sql
        ).mapTo(OrganizationVersion::class.java).list()
    }

    override fun deleteVersionOrganization(versionOrganizationId: Int, version: Int) = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(ORG_VERSION_TABLE))
                .where(field(ORG_ID).eq(versionOrganizationId).and(field(ORG_VERSION).eq(version)))
                .sql
        )
    }

    override fun deleteAllVersionOrganizations() = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(ORG_VERSION_TABLE))
                .sql
        )
    }

    override fun createVersionOrganization(organizationVersion: OrganizationVersion) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(ORG_VERSION_TABLE),
                        field(ORG_ID),
                        field(ORG_VERSION),
                        field(ORG_FULL_NAME),
                        field(ORG_SHORT_NAME),
                        field(ORG_ADDRESS),
                        field(ORG_CONTACT),
                        field(ORG_CREATED_BY))
                .values(
                        organizationVersion.organizationId,
                        organizationVersion.version,
                        organizationVersion.fullName,
                        organizationVersion.shortName,
                        organizationVersion.address,
                        organizationVersion.contact,
                        organizationVersion.createdBy
                ).sql
        )
    }

    override fun reportOrganization(organizationReport: OrganizationReport) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(ORG_REPORT_TABLE),
                        field(ORG_REPORT_ID),
                        field(ORG_ID),
                        field(ORG_FULL_NAME),
                        field(ORG_SHORT_NAME),
                        field(ORG_ADDRESS),
                        field(ORG_CONTACT),
                        field(ORG_CREATED_BY))
                .values(
                        organizationReport.reportId,
                        organizationReport.organizationId,
                        organizationReport.organizationFullName,
                        organizationReport.organizationShortName,
                        organizationReport.organizationAddress,
                        organizationReport.organizationContact,
                        organizationReport.createdBy
                ).sql
        )
    }

    override fun deleteAllReportsOnOrganization(organizationId: Int) = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(ORG_REPORT_TABLE))
                .where(field(ORG_ID).eq(organizationId))
                .sql
        )
    }

    override fun deleteReportOnOrganization(reportId: Int) = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(ORG_REPORT_TABLE))
                .where(field(ORG_REPORT_ID).eq(reportId))
                .sql
        )
    }

    override fun deleteAllReports(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl.
                delete(table(ORG_REPORT_TABLE))
                .sql
        )
    }

}
