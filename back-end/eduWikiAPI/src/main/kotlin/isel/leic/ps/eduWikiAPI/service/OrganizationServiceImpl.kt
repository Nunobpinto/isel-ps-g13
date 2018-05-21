package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.ReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrganizationServiceImpl : OrganizationService {

    @Autowired
    lateinit var organizationRepo: OrganizationDAO

    override fun getSpecificOrganization(organizationId: Int) = organizationRepo.getOrganization(organizationId)

    override fun createOrganization(organizationInputModel: OrganizationInputModel) {
        val organization = Organization(
                fullName = organizationInputModel.fullName,
                shortName = organizationInputModel.shortName,
                address = organizationInputModel.address,
                createdBy = organizationInputModel.createdBy,
                contact = organizationInputModel.contact
        )
        organizationRepo.createOrganization(organization)
    }

    override fun deleteOrganization(organizationId: Int) = organizationRepo.deleteOrganization(organizationId)

    override fun deleteAllOrganizations() = organizationRepo.deleteAllOrganizations()

    override fun updateOrganization() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllOrganizations(): List<Organization> = organizationRepo.getAllOrganizations()

    override fun getAllOrganizationReports(organizationId: Int) = organizationRepo.getAllOrganizationReports(organizationId)

    override fun getSpecificReport(organizationId: Int, reportId: Int) = organizationRepo.getSpecificReport(organizationId, reportId)

    override fun reportOrganization(organizationId: Int, input: ReportInputModel) {
        val report = when(input.reportedField){
            "full_name" -> OrganizationReport(fullName = input.suggestedValue, reporter = input.reporter, id = organizationId)
            "short_name" -> OrganizationReport(shortName = input.suggestedValue, reporter = input.reporter, id = organizationId)
            "address" -> OrganizationReport(address = input.suggestedValue, reporter = input.reporter, id = organizationId)
            "contact" -> OrganizationReport(contact = input.suggestedValue, reporter = input.reporter, id = organizationId)
            else -> OrganizationReport(createdBy = input.suggestedValue, reporter = input.reporter, id = organizationId )
        }
        organizationRepo.reportOrganization(report)
    }

    override fun deleteAllReports(organizationId: Int) = organizationRepo.deleteAllReportsOnOrganization(organizationId)

    override fun deleteSpecificReport(organizationId: Int, reportId: Int) = organizationRepo.deleteReportOnOrganization(reportId)

    override fun voteOrganization(organizationId: Int, input: VoteInputModel)
            = organizationRepo.voteOnOrganization(organizationId, Vote.valueOf(input.vote))

    override fun voteOnReport(organizationId: Int, reportId: Int, input: VoteInputModel)
            = organizationRepo.voteOnReport(organizationId,reportId, Vote.valueOf(input.vote))

}
