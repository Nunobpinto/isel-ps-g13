package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.eventListeners.events.*
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_TABLE
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.OrganizationService
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.OrganizationOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.OrganizationReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.OrganizationVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.OrganizationReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.OrganizationVersionOutputModel
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION_TABLE
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import isel.leic.ps.eduWikiAPI.utils.resolveApproval
import isel.leic.ps.eduWikiAPI.utils.resolveVote
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

@Transactional
@Service
class OrganizationServiceImpl : OrganizationService {

    @Autowired
    lateinit var organizationDAO: OrganizationDAO
    @Autowired
    lateinit var reputationDAO: ReputationDAO
    @Autowired
    lateinit var publisher: ApplicationEventPublisher

    /**
     * Organization Methods
     */

    override fun getOrganization(): OrganizationOutputModel =
            toOrganizationOutputModel(organizationDAO.getOrganization()
                    .orElseThrow { NotFoundException("No organization found", "Try other ID") }
            )

    override fun updateOrganization(organizationInputModel: OrganizationInputModel, principal: Principal): OrganizationOutputModel {
        val prevOrganization = organizationDAO.getOrganization()
                .orElseThrow { NotFoundException("No organization found", "Try other ID") }
        val updatedOrganization = organizationDAO.updateOrganization(Organization(
                organizationId = prevOrganization.organizationId,
                version = prevOrganization.version.inc(),
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
        return toOrganizationOutputModel(updatedOrganization)
    }

    override fun getAllReportsOnOrganization(): OrganizationReportCollectionOutputModel {
        val reports = organizationDAO.getAllReportsOnOrganization().map { toOrganizationReportOutputModel(it) }
        return toOrganizationReportCollectionOutputModel(reports)
    }

    override fun getSpecificReportOnOrganization(reportId: Int): OrganizationReportOutputModel {
        return toOrganizationReportOutputModel(
                organizationDAO.getSpecificReportOnOrganization(reportId)
                        .orElseThrow { NotFoundException("No report found for this organization", "Try other report ID") }
        )
    }

    override fun reportOrganization(input: OrganizationReportInputModel, principal: Principal): OrganizationReportOutputModel {
        val reportOrganization = organizationDAO.reportOrganization(toOrganizationReport(input, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                ORGANIZATION_REPORT_TABLE,
                reportOrganization.logId
        ))
        return toOrganizationReportOutputModel(reportOrganization)
    }

    override fun deleteSpecificReportOnOrganization(reportId: Int, principal: Principal): Int {
        val report = organizationDAO.getSpecificReportOnOrganization(reportId)
                .orElseThrow { NotFoundException("No report found", "Try other ID") }
        val success = organizationDAO.deleteSpecificReportOnOrganization(reportId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                report.reportedBy,
                ActionType.REJECT_REPORT,
                ORGANIZATION_REPORT_TABLE,
                report.logId
        ))
        return success

    }

    override fun voteOnOrganizationReport(reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val organizationReport = organizationDAO.getSpecificReportOnOrganization(reportId)
                .orElseThrow { NotFoundException("No report found", "Try other ID") }
        resolveVote(principal.name, organizationReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, ORGANIZATION_REPORT_TABLE, organizationReport.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) organizationReport.votes - 1 else organizationReport.votes + 1
        val success = organizationDAO.updateVotesOnOrganizationReport(reportId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                organizationReport.reportedBy,
                ORGANIZATION_REPORT_TABLE,
                organizationReport.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    override fun getAllVersionsOfOrganization(): OrganizationVersionCollectionOutputModel {
        val organizationVersions = organizationDAO.getAllVersionsOfOrganization().map { toOrganizationVersionOutputModel(it) }
        return toOrganizationVersionCollectionOutputModel(organizationVersions)
    }

    override fun getSpecificVersionOfOrganization(version: Int): OrganizationVersionOutputModel {
        return toOrganizationVersionOutputModel(
                organizationDAO.getSpecificVersionOfOrganization(version)
                        .orElseThrow { NotFoundException("No version found for this organization", "Try another version number") }
        )
    }

    override fun updateReportedOrganization(reportId: Int, principal: Principal): OrganizationOutputModel {
        val prevOrganization = organizationDAO.getOrganization()
                .orElseThrow { NotFoundException("No organization found", "Try other ID") }
        val report = organizationDAO.getSpecificReportOnOrganization(reportId)
                .orElseThrow { NotFoundException("No organization found", "Try other ID") }
        resolveApproval(principal.name, report.reportedBy)

        val updatedOrganization = organizationDAO.updateOrganization(Organization(
                organizationId = prevOrganization.organizationId,
                version = prevOrganization.version.inc(),
                fullName = report.fullName ?: prevOrganization.fullName,
                shortName = report.shortName ?: prevOrganization.shortName,
                contact = report.contact ?: prevOrganization.contact,
                address = report.address ?: prevOrganization.address
        ))
        organizationDAO.createOrganizationVersion(toOrganizationVersion(updatedOrganization))
        organizationDAO.deleteReportOnOrganization(reportId)

        publisher.publishEvent(ResourceApprovedEvent(
                principal.name,
                ActionType.APPROVE_REPORT,
                ORGANIZATION_REPORT_TABLE,
                report.logId,
                report.reportedBy,
                ActionType.ALTER,
                ORGANIZATION_TABLE,
                updatedOrganization.logId
        ))
        return toOrganizationOutputModel(updatedOrganization)
    }
}