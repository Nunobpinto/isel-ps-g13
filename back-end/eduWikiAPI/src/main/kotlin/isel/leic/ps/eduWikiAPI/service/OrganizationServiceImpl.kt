package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.versions.OrganizationVersionInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class OrganizationServiceImpl : OrganizationService {

    @Autowired
    lateinit var organizationRepo: OrganizationDAO

    override fun getSpecificOrganization(organizationId: Int) = organizationRepo.getSpecificOrganization(organizationId)

    override fun createOrganization(organizationInputModel: OrganizationInputModel) {
        val organization = Organization(
                fullName = organizationInputModel.fullName,
                shortName = organizationInputModel.shortName,
                address = organizationInputModel.address,
                createdBy = organizationInputModel.createdBy,
                contact = organizationInputModel.contact,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        organizationRepo.createOrganization(organization)
    }

    override fun deleteOrganization(organizationId: Int) = organizationRepo.deleteOrganization(organizationId)

    override fun deleteAllOrganizations() = organizationRepo.deleteAllOrganizations()

    override fun updateOrganization(organization: Organization) = organizationRepo.updateOrganization(organization)

    override fun getAllOrganizations(): List<Organization> = organizationRepo.getAllOrganizations()

    override fun getAllOrganizationReports(organizationId: Int) = organizationRepo.getAllOrganizationReports(organizationId)

    override fun getSpecificReport(organizationId: Int, reportId: Int) = organizationRepo.getSpecificReportOfOrganization(organizationId, reportId)

    override fun reportOrganization(organizationId: Int, input: OrganizationReportInputModel) {
        val report = OrganizationReport(
                fullName = input.fullName,
                shortName = input.shortName,
                address = input.address,
                contact = input.contact,
                reportedBy = input.createdBy,
                id = organizationId,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        organizationRepo.reportOrganization(report)
    }

    override fun deleteAllReports(organizationId: Int) = organizationRepo.deleteAllReportsOnOrganization(organizationId)

    override fun deleteSpecificReport(organizationId: Int, reportId: Int) = organizationRepo.deleteReportOnOrganization(reportId)

    override fun voteOrganization(organizationId: Int, input: VoteInputModel)
            = organizationRepo.voteOnOrganization(organizationId, Vote.valueOf(input.vote))

    override fun voteOnReport(organizationId: Int, reportId: Int, input: VoteInputModel)
            = organizationRepo.voteOnReport(organizationId,reportId, Vote.valueOf(input.vote))

    override fun getAllVersions(organizationId: Int): List<OrganizationVersion> = organizationRepo.getAllVersions(organizationId)

    override fun getVersion(organizationId: Int, version: Int): OrganizationVersion = organizationRepo.getVersion(organizationId, version)

    override fun createVersion(organizationId: Int, input: OrganizationVersionInputModel) {
        val version = OrganizationVersion(
                fullName = input.fullName,
                shortName = input.shortName,
                address = input.address,
                contact = input.contact,
                createdBy = input.createdBy,
                version = input.version,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        organizationRepo.createVersion(version)
    }

    override fun deleteAllVersions(organizationId: Int) = organizationRepo.deleteAllVersions(organizationId)

    override fun deleteSpecificVersion(organizationId: Int, version: Int) = organizationRepo.deleteVersion(organizationId, version)

    override fun updateReportedOrganization(organizationId: Int, reportId: Int) {
        val organization = organizationRepo.getSpecificOrganization(organizationId)
        val report = organizationRepo.getSpecificReportOfOrganization(organizationId, reportId)
        val updatedOrganization = Organization(
                id = organization.id,
                version = organization.version.inc(),
                votes = organization.votes,
                createdBy = organization.createdBy,
                fullName = report.fullName ?: organization.fullName,
                shortName =report.shortName ?: organization.shortName,
                contact = report.contact ?: organization.contact,
                address = report.address ?: organization.address,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        organizationRepo.addToOrganizationVersion(organization)
        organizationRepo.updateOrganization(updatedOrganization)
        organizationRepo.deleteReportOnOrganization(reportId)
    }

}
