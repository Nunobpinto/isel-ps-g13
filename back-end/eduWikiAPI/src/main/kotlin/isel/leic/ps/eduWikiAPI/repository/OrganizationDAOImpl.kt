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

    @Autowired
    lateinit var dsl: DSLContext
    @Autowired
    lateinit var dbi: Jdbi

    override fun getOrganization(organizationId: Int) : Organization = dbi.withHandle<Organization, Exception>{
        it.createQuery(dsl
                .select(
                        field("organization_id"),
                        field("organization_version"),
                        field("organization_full_name"),
                        field("organization_short_name"),
                        field("organization_address"),
                        field("organization_contact"),
                        field("created_by")
                )
                .from(table("organization"))
                .where(field("organization_id").eq(organizationId))
                .sql
        ).mapTo(Organization::class.java).findOnly()
    }

    override fun getAllOrganizations(): List<Organization> = dbi.withHandle<List<Organization>, Exception> {
        it.createQuery(dsl
                .select(
                        field("organization_id"),
                        field("organization_version"),
                        field("organization_full_name"),
                        field("organization_short_name"),
                        field("organization_address"),
                        field("organization_contact"),
                        field("created_by")
                )
                .from(table("organization"))
                .sql
        ).mapTo(Organization::class.java).list()
    }

    override fun deleteOrganization(organizationId: Int) = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table("organization"))
                .where(field("organization_id").eq(organizationId))
                .sql
        )
    }

    override fun deleteAllOrganizations() = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table("organization"))
                .sql
        )
    }

    override fun updateOrganization(organization: Organization, user: String) = TODO("dynamically update org by filled values in Organization parameter")

    override fun createOrganization(organization: Organization, user: String) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table("organization"),
                        field("organization_id"),
                        field("organization_version"),
                        field("organization_full_name"),
                        field("organization_short_name"),
                        field("organization_address"),
                        field("organization_contact"),
                        field("created_by")
                        )
                .values(
                        organization.id,
                        organization.version,
                        organization.fullName,
                        organization.shortName,
                        organization.address,
                        organization.contact,
                        user
                ).sql
        )
    }

    override fun voteOnOrganization(organizationId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field("votes"))
                .from(table("organization"))
                .where(field("organization_id").eq(organizationId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table("organization"))
                .set(field("votes"), if(voteType == -1) votes.dec() else votes.inc())
                .where(field("organization_id").eq(organizationId))
                .sql
        )
    }

    override fun getVersionOrganization(versionOrganizationId: Int, version: Int): OrganizationVersion  = dbi.withHandle<OrganizationVersion, Exception> {
        it.createQuery(dsl
                .select(
                        field("organization_id"),
                        field("organization_version"),
                        field("organization_full_name"),
                        field("organization_short_name"),
                        field("organization_address"),
                        field("organization_contact"),
                        field("created_by"),
                        field("vote_count"),
                        field("time_stamp")
                )
                .from(table("organization_version"))
                .where(field("organization_id").eq(versionOrganizationId).and(field("organization_version").eq(version)))
                .sql
        ).mapTo(OrganizationVersion::class.java).first()
    }

    override fun getAllVersionOrganizations(): List<OrganizationVersion> = dbi.withHandle<List<OrganizationVersion>, Exception> {
        it.createQuery(dsl
                .select(
                        field("organization_id"),
                        field("organization_version"),
                        field("organization_full_name"),
                        field("organization_short_name"),
                        field("organization_address"),
                        field("organization_contact"),
                        field("created_by"),
                        field("vote_count"),
                        field("time_stamp")
                )
                .from(table("organization_version"))
                .sql
        ).mapTo(OrganizationVersion::class.java).list()
    }

    override fun deleteVersionOrganization(versionOrganizationId: Int, version: Int) = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table("organization_version"))
                .where(field("organization_id").eq(versionOrganizationId).and(field("organization_version").eq(version)))
                .sql
        )
    }

    override fun deleteAllVersionOrganizations() = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table("organization_version"))
                .sql
        )
    }

    override fun createVersionOrganization(organizationVersion: OrganizationVersion) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table("organization_version"),
                        field("organization_id"),
                        field("organization_version"),
                        field("organization_full_name"),
                        field("organization_short_name"),
                        field("organization_address"),
                        field("organization_contact"),
                        field("created_by"))
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
                .insertInto(table("organization_report"),
                        field("organization_id"),
                        field("organization_full_name"),
                        field("organization_short_name"),
                        field("organization_address"),
                        field("organization_contact"),
                        field("made_by"))
                .values(
                        organizationReport.organizationId,
                        organizationReport.organizationFullName,
                        organizationReport.organizationShortName,
                        organizationReport.organizationAddress,
                        organizationReport.organizationContact,
                        organizationReport.madeBy
                ).sql
        )
    }

    override fun deleteAllReportsOnOrganization() = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table("organization_report"))
                .sql
        )
    }

    override fun deleteReportOnOrganization(reportId: Int) = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table("organization_report"))
                .where(field("report_id").eq(reportId))
                .sql
        )
    }

    override fun voteOnReportOrganization(reportId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field("votes"))
                .from(table("organization_report"))
                .where(field("report_id").eq(reportId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table("organization_report"))
                .set(field("votes"), if(voteType == -1) votes.dec() else votes.inc())
                .where(field("report_id").eq(reportId))
                .sql
        )
    }

}
