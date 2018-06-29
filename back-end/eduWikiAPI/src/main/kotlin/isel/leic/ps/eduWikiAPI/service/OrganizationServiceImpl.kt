package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Service
class OrganizationServiceImpl : OrganizationService {

    @Autowired
    lateinit var organizationDAO: OrganizationDAO

    @Autowired
    lateinit var handle: Handle

    override fun getSpecificOrganization(organizationId: Int) = organizationDAO.getSpecificOrganization(organizationId)

    override fun getAllOrganizations(): List<Organization> = organizationDAO.getAllOrganizations()

    override fun createOrganization(organizationInputModel: OrganizationInputModel) : Optional<Organization> {
        handle.begin()
        var organization = Organization(
                fullName = organizationInputModel.fullName,
                shortName = organizationInputModel.shortName,
                address = organizationInputModel.address,
                createdBy = organizationInputModel.createdBy,
                contact = organizationInputModel.contact,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        organization = organizationDAO.createOrganization(organization).get()

        organizationDAO.createVersion(OrganizationVersion(
                organizationId = organization.id,
                version = organization.version,
                createdBy = organization.createdBy,
                fullName = organization.fullName,
                shortName = organization.shortName,
                contact = organization.contact,
                address = organization.address,
                timestamp = organization.timestamp
        ))
        handle.commit()
        return Optional.of(organization)
    }

    override fun deleteOrganization(organizationId: Int) = organizationDAO.deleteOrganization(organizationId)

    override fun deleteAllOrganizations() = organizationDAO.deleteAllOrganizations()

    override fun updateOrganization(organizationId: Int, organizationInputModel: OrganizationInputModel): Int {
        handle.begin()
        val prevOrganization = organizationDAO.getSpecificOrganization(organizationId).get()
        val organization = Organization(
                id = prevOrganization.id,
                version = prevOrganization.version.inc(),
                createdBy = organizationInputModel.createdBy,
                fullName = if(organizationInputModel.fullName.isEmpty()) prevOrganization.fullName else organizationInputModel.fullName,
                shortName = if(organizationInputModel.shortName.isEmpty()) prevOrganization.shortName else organizationInputModel.shortName,
                address = if(organizationInputModel.address.isEmpty()) prevOrganization.address else organizationInputModel.address,
                contact = if(organizationInputModel.contact.isEmpty()) prevOrganization.contact else organizationInputModel.contact,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        val updatedRows = organizationDAO.updateOrganization(organization)

        organizationDAO.createVersion(OrganizationVersion(
                organizationId = organization.id,
                version = organization.version,
                createdBy = organization.createdBy,
                fullName = organization.fullName,
                shortName = organization.shortName,
                contact = organization.contact,
                address = organization.address,
                timestamp = organization.timestamp
        ))
        handle.commit()
        return updatedRows
    }

    override fun voteOnOrganization(organizationId: Int, input: VoteInputModel) = organizationDAO.voteOnOrganization(organizationId, Vote.valueOf(input.vote))

    override fun getAllReportsOnOrganization(organizationId: Int) = organizationDAO.getAllReportsOnOrganization(organizationId)

    override fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int) = organizationDAO.getSpecificReportOnOrganization(organizationId, reportId)

    override fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel): Optional<OrganizationReport> {
        val report = OrganizationReport(
                fullName = input.fullName,
                shortName = input.shortName,
                address = input.address,
                contact = input.contact,
                reportedBy = input.reportedBy,
                organization_id = organizationId,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return organizationDAO.reportOrganization(report)
    }

    override fun deleteAllReportsOnOrganization(organizationId: Int) = organizationDAO.deleteAllReportsOnOrganization(organizationId)

    override fun deleteSpecificReportOnOrganization(organizationId: Int, reportId: Int) = organizationDAO.deleteReportOnOrganization(reportId)

    override fun voteOnReport(organizationId: Int, reportId: Int, input: VoteInputModel) = organizationDAO.voteOnReport(organizationId, reportId, Vote.valueOf(input.vote))

    override fun getAllVersionsOfOrganization(organizationId: Int): List<OrganizationVersion> = organizationDAO.getAllVersionsOfOrganization(organizationId)

    override fun getSpecificVersionOfOrganization(organizationId: Int, version: Int) = organizationDAO.getSpecificVersionOfOrganization(organizationId, version)

    override fun deleteAllVersionsOfOrganization(organizationId: Int) = organizationDAO.deleteAllVersionsOfOrganization(organizationId)

    override fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int) = organizationDAO.deleteSpecificVersionOfOrganization(organizationId, version)

    override fun updateReportedOrganization(organizationId: Int, reportId: Int) {
        handle.begin()
        val organization = organizationDAO.getSpecificOrganization(organizationId).get()
        val report = organizationDAO.getSpecificReportOnOrganization(organizationId, reportId).get()

        val updatedOrganization = Organization(
                id = organization.id,
                version = organization.version.inc(),
                votes = organization.votes,
                createdBy = organization.createdBy,
                fullName = report.fullName ?: organization.fullName,
                shortName = report.shortName ?: organization.shortName,
                contact = report.contact ?: organization.contact,
                address = report.address ?: organization.address,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        organizationDAO.updateOrganization(updatedOrganization)

        organizationDAO.createVersion(OrganizationVersion(
                organizationId = updatedOrganization.id,
                version = updatedOrganization.version,
                createdBy = updatedOrganization.createdBy,
                fullName = updatedOrganization.fullName,
                shortName = updatedOrganization.shortName,
                contact = updatedOrganization.contact,
                address = updatedOrganization.address,
                timestamp = updatedOrganization.timestamp
        ))
        organizationDAO.deleteReportOnOrganization(reportId)
        handle.commit()
    }

}
