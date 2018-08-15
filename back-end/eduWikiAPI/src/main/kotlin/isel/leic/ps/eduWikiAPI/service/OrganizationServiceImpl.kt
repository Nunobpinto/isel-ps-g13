package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.eventListeners.events.*
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_TABLE
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.OrganizationCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.OrganizationOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.OrganizationReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.OrganizationVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.OrganizationReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.OrganizationVersionOutputModel
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION_TABLE
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.context.ApplicationEventPublisher
import org.jdbi.v3.core.Jdbi
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

@Service
class OrganizationServiceImpl : OrganizationService {

    @Autowired
    lateinit var organizationDAO: OrganizationDAO
    @Autowired
    lateinit var publisher: ApplicationEventPublisher

    /**
     * Organization Methods
     */

    @Transactional
    override fun getSpecificOrganization(organizationId: Int): OrganizationOutputModel =
            toOrganizationOutputModel(organizationDAO.getSpecificOrganization(organizationId)
                    .orElseThrow { NotFoundException("No organization found", "Try other ID") }
            )


    @Transactional
    override fun getAllOrganizations(): OrganizationCollectionOutputModel {
        val organizations = organizationDAO.getAllOrganizations().map { toOrganizationOutputModel(it) }
        return toOrganizationCollectionOutputModel(organizations)
    }

    @Transactional
    override fun createOrganization(organizationInputModel: OrganizationInputModel, principal: Principal): OrganizationOutputModel {
        val organization = organizationDAO.createOrganization(toOrganization(organizationInputModel, principal.name))

        organizationDAO.createOrganizationVersion(toOrganizationVersion(organization))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                ORGANIZATION_TABLE,
                organization.logId
        ))
        return toOrganizationOutputModel(organization)
    }

    @Transactional
    override fun deleteOrganization(organizationId: Int): Int =
            organizationDAO.deleteOrganization(organizationId)

    @Transactional
    override fun updateOrganization(organizationId: Int, organizationInputModel: OrganizationInputModel, principal: Principal): OrganizationOutputModel {
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
        return toOrganizationOutputModel(updatedOrganization)
    }

    @Transactional
    override fun voteOnOrganization(organizationId: Int, vote: VoteInputModel, principal: Principal): Int {
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
        return success
    }

    @Transactional
    override fun getAllReportsOnOrganization(organizationId: Int): OrganizationReportCollectionOutputModel {
        val reports = organizationDAO.getAllReportsOnOrganization(organizationId).map { toOrganizationReportOutputModel(it) }
        return toOrganizationReportCollectionOutputModel(reports)
    }

    @Transactional
    override fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int): OrganizationReportOutputModel {
        return toOrganizationReportOutputModel(
                organizationDAO.getSpecificReportOnOrganization(organizationId, reportId)
                        .orElseThrow { NotFoundException("No report found for this organization", "Try other report ID") }
        )
    }

    @Transactional
    override fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel, principal: Principal): OrganizationReportOutputModel {
        val reportOrganization = organizationDAO.reportOrganization(toOrganizationReport(organizationId, input, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                ORGANIZATION_REPORT_TABLE,
                reportOrganization.logId
        ))
        return toOrganizationReportOutputModel(reportOrganization)
    }

    @Transactional
    override fun deleteSpecificReportOnOrganization(organizationId: Int, reportId: Int, principal: Principal): Int {
        val report = organizationDAO.getSpecificReportOnOrganization(organizationId, reportId)
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

    @Transactional
    override fun voteOnOrganizationReport(organizationId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
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
        return success
    }

    @Transactional
    override fun getAllVersionsOfOrganization(organizationId: Int): OrganizationVersionCollectionOutputModel {
        val organizationVersions = organizationDAO.getAllVersionsOfOrganization(organizationId).map { toOrganizationVersionOutputModel(it) }
        return toOrganizationVersionCollectionOutputModel(organizationVersions)
    }

    @Transactional
    override fun getSpecificVersionOfOrganization(organizationId: Int, version: Int): OrganizationVersionOutputModel {
        return toOrganizationVersionOutputModel(
                organizationDAO.getSpecificVersionOfOrganization(organizationId, version)
                        .orElseThrow { NotFoundException("No version found for this organization", "Try another version number") }
        )
    }

    @Transactional
    override fun updateReportedOrganization(organizationId: Int, reportId: Int, principal: Principal): OrganizationOutputModel {
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
        return toOrganizationOutputModel(updatedOrganization)
    }
}