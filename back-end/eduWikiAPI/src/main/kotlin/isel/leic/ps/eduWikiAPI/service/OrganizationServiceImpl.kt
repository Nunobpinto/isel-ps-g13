package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.OrganizationCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.OrganizationOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.OrganizationReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.OrganizationVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.OrganizationReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.OrganizationVersionOutputModel
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrganizationServiceImpl : OrganizationService {

    @Autowired
    lateinit var jdbi: Jdbi

    /**
     * Organization Methods
     */

    override fun getSpecificOrganization(organizationId: Int): OrganizationOutputModel =
            jdbi.withExtension<OrganizationOutputModel, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                toOrganizationOutputModel(
                        it.getSpecificOrganization(organizationId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No organization found",
                                            action = "Try other ID"
                                    )
                                }
                )
            }

    override fun getAllOrganizations(): OrganizationCollectionOutputModel =
            jdbi.withExtension<OrganizationCollectionOutputModel, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                val organizations = it.getAllOrganizations().map { toOrganizationOutputModel(it) }
                toOrganizationCollectionOutputModel(organizations)
            }

    override fun createOrganization(organizationInputModel: OrganizationInputModel): OrganizationOutputModel =
            jdbi.inTransaction<OrganizationOutputModel, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)
                val organization = organizationDAO.createOrganization(toOrganization(organizationInputModel))
                organizationDAO.createOrganizationVersion(toOrganizationVersion(organization))
                toOrganizationOutputModel(organization)
            }

    override fun deleteOrganization(organizationId: Int): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteOrganization(organizationId)
            }

    override fun deleteAllOrganizations(): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteAllOrganizations()
            }

    override fun updateOrganization(organizationId: Int, organizationInputModel: OrganizationInputModel): OrganizationOutputModel =
            jdbi.inTransaction<OrganizationOutputModel, Exception> {
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
                toOrganizationOutputModel(updatedOrganization)
            }

    override fun voteOnOrganization(organizationId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)
                var votes = organizationDAO.getVotesOnOrganization(organizationId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                organizationDAO.updateVotesOnOrganization(organizationId, votes)
            }

    override fun getAllReportsOnOrganization(organizationId: Int): OrganizationReportCollectionOutputModel =
            jdbi.withExtension<OrganizationReportCollectionOutputModel, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                val reports = it.getAllReportsOnOrganization(organizationId).map { toOrganizationReportOutputModel(it) }
                toOrganizationReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int): OrganizationReportOutputModel =
            jdbi.withExtension<OrganizationReportOutputModel, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                toOrganizationReportOutputModel(
                        it.getSpecificReportOnOrganization(organizationId, reportId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No report found for this organization",
                                            action = "Try other report ID"
                                    )
                                }
                )
            }

    override fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel): OrganizationReportOutputModel =
            jdbi.withExtension<OrganizationReportOutputModel, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                toOrganizationReportOutputModel(it.reportOrganization(toOrganizationReport(organizationId, input)))
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
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                organizationDAO.updateVotesOnOrganizationReport(organizationId, reportId, votes)
            }

    override fun getAllVersionsOfOrganization(organizationId: Int): OrganizationVersionCollectionOutputModel =
            jdbi.withExtension<OrganizationVersionCollectionOutputModel, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                val organizationVersions = it.getAllVersionsOfOrganization(organizationId).map { toOrganizationVersionOutputModel(it) }
                toOrganizationVersionCollectionOutputModel(organizationVersions)
            }

    override fun getSpecificVersionOfOrganization(organizationId: Int, version: Int): OrganizationVersionOutputModel =
            jdbi.withExtension<OrganizationVersionOutputModel, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                toOrganizationVersionOutputModel(
                        it.getSpecificVersionOfOrganization(organizationId, version)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No version found for this organization",
                                            action = "Try another version number"
                                    )
                                }
                )
            }

    override fun deleteAllVersionsOfOrganization(organizationId: Int): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteAllOrganizationVersions(organizationId)
            }

    override fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteSpecificVersionOfOrganization(organizationId, version)
            }

    override fun updateReportedOrganization(organizationId: Int, reportId: Int): OrganizationOutputModel =
            jdbi.inTransaction<OrganizationOutputModel, Exception> {
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
                val updatedOrganization = organizationDAO.updateOrganization(organization)
                organizationDAO.createOrganizationVersion(toOrganizationVersion(updatedOrganization))
                organizationDAO.deleteReportOnOrganization(organizationId, reportId)
                toOrganizationOutputModel(updatedOrganization)
            }
}