package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.eventListeners.events.*
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_TABLE
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.OrganizationCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.OrganizationOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.OrganizationReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.OrganizationVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.OrganizationReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.OrganizationVersionOutputModel
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_VERSION_TABLE
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.context.ApplicationEventPublisher
import org.jdbi.v3.core.Jdbi
import java.security.Principal


@Service
class OrganizationServiceImpl : OrganizationService {

    @Autowired
    lateinit var jdbi: Jdbi
    @Autowired
    lateinit var publisher: ApplicationEventPublisher

    /**
     * Organization Methods
     */

    override fun getSpecificOrganization(organizationId: Int): OrganizationOutputModel =
            jdbi.withExtension<OrganizationOutputModel, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                toOrganizationOutputModel(it.getSpecificOrganization(organizationId)
                        .orElseThrow { NotFoundException("No organization found", "Try other ID") }
                )
            }

    override fun getAllOrganizations(): OrganizationCollectionOutputModel =
            jdbi.withExtension<OrganizationCollectionOutputModel, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                val organizations = it.getAllOrganizations().map { toOrganizationOutputModel(it) }
                toOrganizationCollectionOutputModel(organizations)
            }

    override fun createOrganization(organizationInputModel: OrganizationInputModel, principal: Principal): OrganizationOutputModel =
            jdbi.inTransaction<OrganizationOutputModel, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)

                val organization = organizationDAO.createOrganization(toOrganization(organizationInputModel, principal.name))
                organizationDAO.createOrganizationVersion(toOrganizationVersion(organization))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        ORGANIZATION_TABLE,
                        organization.logId
                ))
                toOrganizationOutputModel(organization)
            }

    override fun deleteOrganization(organizationId: Int): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                it.deleteOrganization(organizationId)
            }

    override fun updateOrganization(organizationId: Int, organizationInputModel: OrganizationInputModel, principal: Principal): OrganizationOutputModel =
            jdbi.inTransaction<OrganizationOutputModel, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)

                val prevOrganization = organizationDAO.getSpecificOrganization(organizationId)
                        .orElseThrow { NotFoundException("No organization found", "Try other ID") }
                val updatedOrganization = organizationDAO.updateOrganization(Organization(
                        organizationId = prevOrganization.organizationId,
                        version = prevOrganization.version.inc(),
                        createdBy = principal.name,
                        logId = prevOrganization.logId,
                        fullName = if(organizationInputModel.fullName.isEmpty()) prevOrganization.fullName else organizationInputModel.fullName,
                        shortName = if(organizationInputModel.shortName.isEmpty()) prevOrganization.shortName else organizationInputModel.shortName,
                        address = if(organizationInputModel.address.isEmpty()) prevOrganization.address else organizationInputModel.address,
                        contact = if(organizationInputModel.contact.isEmpty()) prevOrganization.contact else organizationInputModel.contact
                ))
                organizationDAO.createOrganizationVersion(toOrganizationVersion(updatedOrganization))

                publisher.publishEvent(ResourceUpdatedEvent(
                        principal.name,
                        ORGANIZATION_VERSION_TABLE,
                        updatedOrganization.logId
                ))
                toOrganizationOutputModel(updatedOrganization)
            }

    override fun voteOnOrganization(organizationId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)

                val organization = organizationDAO.getSpecificOrganization(organizationId)
                        .orElseThrow { NotFoundException("No organization found", "Try other ID") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) organization.votes - 1 else organization.votes + 1
                val success = organizationDAO.updateVotesOnOrganization(organizationId, votes)

                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        organization.createdBy,
                        ORGANIZATION_TABLE,
                        organization.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
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
                                .orElseThrow { NotFoundException("No report found for this organization", "Try other report ID") }
                )
            }

    override fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel, principal: Principal): OrganizationReportOutputModel =
            jdbi.withExtension<OrganizationReportOutputModel, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                val reportOrganization = it.reportOrganization(toOrganizationReport(organizationId, input, principal.name))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        ORGANIZATION_REPORT_TABLE,
                        reportOrganization.logId
                ))
                toOrganizationReportOutputModel(reportOrganization)
            }

    override fun deleteSpecificReportOnOrganization(organizationId: Int, reportId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, OrganizationDAOJdbi, Exception>(OrganizationDAOJdbi::class.java) {
                val report = it.getSpecificReportOnOrganization(organizationId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try other ID") }
                val success = it.deleteSpecificReportOnOrganization(reportId)

                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        report.reportedBy,
                        ActionType.REJECT_REPORT,
                        ORGANIZATION_REPORT_TABLE,
                        report.logId
                ))
                success

            }

    override fun voteOnOrganizationReport(organizationId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)

                val organizationReport = organizationDAO.getSpecificReportOnOrganization(organizationId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try other ID") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) organizationReport.votes - 1 else organizationReport.votes + 1
                val success = organizationDAO.updateVotesOnOrganizationReport(organizationId, reportId, votes)

                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        organizationReport.reportedBy,
                        ORGANIZATION_REPORT_TABLE,
                        organizationReport.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
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
                                .orElseThrow { NotFoundException("No version found for this organization", "Try another version number") }
                )
            }

    override fun updateReportedOrganization(organizationId: Int, reportId: Int, principal: Principal): OrganizationOutputModel =
            jdbi.inTransaction<OrganizationOutputModel, Exception> {
                val organizationDAO = it.attach(OrganizationDAOJdbi::class.java)

                val prevOrganization = organizationDAO.getSpecificOrganization(organizationId)
                        .orElseThrow { NotFoundException("No organization found", "Try other ID") }
                val report = organizationDAO.getSpecificReportOnOrganization(organizationId, reportId)
                        .orElseThrow { NotFoundException("No organization found", "Try other ID") }
                val updatedOrganization = organizationDAO.updateOrganization(Organization(
                        organizationId = prevOrganization.organizationId,
                        version = prevOrganization.version.inc(),
                        votes = prevOrganization.votes,
                        createdBy = report.reportedBy,
                        fullName = report.fullName ?: prevOrganization.fullName,
                        shortName = report.shortName ?: prevOrganization.shortName,
                        contact = report.contact ?: prevOrganization.contact,
                        address = report.address ?: prevOrganization.address
                ))
                organizationDAO.createOrganizationVersion(toOrganizationVersion(updatedOrganization))
                organizationDAO.deleteReportOnOrganization(organizationId, reportId)

                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_STAGE,
                        ORGANIZATION_REPORT_TABLE,
                        report.logId,
                        report.reportedBy,
                        ActionType.ALTER,
                        ORGANIZATION_TABLE,
                        updatedOrganization.logId
                ))
                toOrganizationOutputModel(updatedOrganization)
            }
}