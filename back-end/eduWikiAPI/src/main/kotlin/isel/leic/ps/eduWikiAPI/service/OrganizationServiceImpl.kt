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
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganizationServiceImpl : OrganizationService {

    @Autowired
    lateinit var organizationDAO: OrganizationDAO

    @Autowired
    lateinit var handle: Handle

    override fun getSpecificOrganization(organizationId: Int) =
            organizationDAO.getSpecificOrganization(organizationId)

    override fun getAllOrganizations(): List<Organization> = organizationDAO.getAllOrganizations()

    override fun createOrganization(organizationInputModel: OrganizationInputModel): Optional<Organization> {
        handle.begin()
        val organization = organizationDAO.createOrganization(toOrganization(organizationInputModel)).get()
        organizationDAO.createVersion(toOrganizationVersion(organization))
        handle.commit()
        return Optional.of(organization)
    }

    override fun deleteOrganization(organizationId: Int) = organizationDAO.deleteOrganization(organizationId)

    override fun deleteAllOrganizations() = organizationDAO.deleteAllOrganizations()

    override fun updateOrganization(organizationId: Int, organizationInputModel: OrganizationInputModel): Int {
        handle.begin()
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
        val updatedRows = organizationDAO.updateOrganization(organization)

        organizationDAO.createVersion(toOrganizationVersion(organization))
        handle.commit()
        return updatedRows
    }

    override fun voteOnOrganization(organizationId: Int, input: VoteInputModel) =
            organizationDAO.voteOnOrganization(organizationId, Vote.valueOf(input.vote))

    override fun getAllReportsOnOrganization(organizationId: Int) =
            organizationDAO.getAllReportsOnOrganization(organizationId)

    override fun getSpecificReportOnOrganization(organizationId: Int, reportId: Int) =
            organizationDAO.getSpecificReportOnOrganization(organizationId, reportId)

    override fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel): Optional<OrganizationReport> =
            organizationDAO.reportOrganization(toOrganizationReport(organizationId, input))

    override fun deleteAllReportsOnOrganization(organizationId: Int) =
            organizationDAO.deleteAllReportsOnOrganization(organizationId)

    override fun deleteSpecificReportOnOrganization(organizationId: Int, reportId: Int) =
            organizationDAO.deleteReportOnOrganization(reportId)

    override fun voteOnOrganizationReport(organizationId: Int, reportId: Int, input: VoteInputModel) =
            organizationDAO.voteOnOrganizationReport(organizationId, reportId, Vote.valueOf(input.vote))

    override fun getAllVersionsOfOrganization(organizationId: Int): List<OrganizationVersion> =
            organizationDAO.getAllVersionsOfOrganization(organizationId)

    override fun getSpecificVersionOfOrganization(organizationId: Int, version: Int) =
            organizationDAO.getSpecificVersionOfOrganization(organizationId, version)

    override fun deleteAllVersionsOfOrganization(organizationId: Int) =
            organizationDAO.deleteAllVersionsOfOrganization(organizationId)

    override fun deleteSpecificVersionOfOrganization(organizationId: Int, version: Int) =
            organizationDAO.deleteSpecificVersionOfOrganization(organizationId, version)

    override fun updateReportedOrganization(organizationId: Int, reportId: Int) {
        handle.begin()
        val organization = organizationDAO.getSpecificOrganization(organizationId).get()
        val report = organizationDAO.getSpecificReportOnOrganization(organizationId, reportId).get()
        val updatedOrganization = Organization(
                organizationId = organization.organizationId,
                version = organization.version.inc(),
                votes = organization.votes,
                createdBy = report.reportedBy,
                fullName = report.fullName ?: organization.fullName,
                shortName = report.shortName ?: organization.shortName,
                contact = report.contact ?: organization.contact,
                address = report.address ?: organization.address
        )
        organizationDAO.updateOrganization(updatedOrganization)
        organizationDAO.createVersion(toOrganizationVersion(updatedOrganization))
        organizationDAO.deleteReportOnOrganization(reportId)
        handle.commit()
    }

}
