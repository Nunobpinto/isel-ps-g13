package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganizationServiceImpl : OrganizationService {

    @Autowired
    lateinit var jdbi: Jdbi

    /**
     * Organization Methods
     */

    override fun getSpecificOrganization(organizationId: Int): Optional<Organization> =
            jdbi.withExtension<Optional<Organization>, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.getSpecificOrganization(organizationId)
            }

    override fun getAllOrganizations(): List<Organization> =
            jdbi.withExtension<List<Organization>, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.getAllOrganizations()
            }

    override fun createOrganization(organizationInputModel: OrganizationInputModel): Organization =
            jdbi.inTransaction<Organization, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)
                val organization = organizationDAO.createOrganization(toOrganization(organizationInputModel))
                organizationDAO.createOrganizationVersion(toOrganizationVersion(organization))
                organization
            }

    override fun deleteOrganization(organizationId: Int): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteOrganization(organizationId)
            }

    override fun deleteAllOrganizations(): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteAllOrganizations()
            }

    override fun updateOrganization(organizationId: Int, organizationInputModel: OrganizationInputModel): Organization =
            jdbi.inTransaction<Organization, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)
                val prevOrganization = organizationDAO.getSpecificOrganization(organizationId).get()
                val organization = Organization(
                        organizationId = prevOrganization.organizationId,
                        version = prevOrganization.version.inc(),
                        createdBy = organizationInputModel.createdBy,
                        fullName = if (organizationInputModel.fullName.isEmpty()) prevOrganization.fullName else organizationInputModel.fullName,
                        shortName = if (organizationInputModel.shortName.isEmpty()) prevOrganization.shortName else organizationInputModel.shortName,
                        address = if (organizationInputModel.address.isEmpty()) prevOrganization.address else organizationInputModel.address,
                        contact = if (organizationInputModel.contact.isEmpty()) prevOrganization.contact else organizationInputModel.contact
                )
                val updatedOrganization = organizationDAO.updateOrganization(organization)
                organizationDAO.createOrganizationVersion(toOrganizationVersion(updatedOrganization))
                updatedOrganization
            }

    override fun voteOnOrganization(organizationId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)
                var votes = organizationDAO.getVotesOnOrganization(organizationId)
                votes = if(Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                organizationDAO.updateVotesOnOrganization(organizationId, votes)
            }

    override fun getAllReportsOnOrganization(organizationId: Int): List<OrganizationReport> =
            jdbi.withExtension<List<OrganizationReport>, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.getAllReportsOnOrganization(organizationId)
            }

    override fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int): Optional<OrganizationReport> =
            jdbi.withExtension<Optional<OrganizationReport>, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.getSpecificReportOnOrganization(organizationId, reportId)
            }

    override fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel): OrganizationReport =
            jdbi.withExtension<OrganizationReport, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.reportOrganization(toOrganizationReport(organizationId, input))
            }

    override fun deleteAllReportsOnOrganization(organizationId: Int): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteAllReportsOnOrganization(organizationId)
            }

    override fun deleteSpecificReportOnOrganization(organizationId: Int, reportId: Int): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteSpecificReportOnOrganization(reportId)
            }

    override fun voteOnOrganizationReport(organizationId: Int, reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)
                var votes = organizationDAO.getVotesOnOrganizationReport(organizationId, reportId)
                votes = if(Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                organizationDAO.updateVotesOnOrganizationReport(organizationId, reportId, votes)
            }

    override fun getAllVersionsOfOrganization(organizationId: Int): List<OrganizationVersion> =
            jdbi.withExtension<List<OrganizationVersion>, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.getAllVersionsOfOrganization(organizationId)
            }

    override fun getSpecificVersionOfOrganization(organizationId: Int, version: Int): Optional<OrganizationVersion> =
            jdbi.withExtension<Optional<OrganizationVersion>, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.getSpecificVersionOfOrganization(organizationId, version)
            }

    override fun deleteAllVersionsOfOrganization(organizationId: Int): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteAllOrganizationVersions(organizationId)
            }

    override fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteSpecificVersionOfOrganization(organizationId, version)
            }

    override fun updateReportedOrganization(organizationId: Int, reportId: Int): Organization =
            jdbi.inTransaction<Organization, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)
                val prevorganization = organizationDAO.getSpecificOrganization(organizationId).get()
                val report = organizationDAO.getSpecificReportOnOrganization(organizationId, reportId).get()
                val organization = Organization(
                        organizationId = prevorganization.organizationId,
                        version = prevorganization.version.inc(),
                        votes = prevorganization.votes,
                        createdBy = report.reportedBy,
                        fullName = report.fullName ?: prevorganization.fullName,
                        shortName = report.shortName ?: prevorganization.shortName,
                        contact = report.contact ?: prevorganization.contact,
                        address = report.address ?: prevorganization.address
                )
                val updatedOrganization = organizationDAO.updateOrganization(prevorganization)
                organizationDAO.createOrganizationVersion(toOrganizationVersion(updatedOrganization))
                organizationDAO.deleteReportOnOrganization(organizationId, reportId)
                updatedOrganization
            }
}