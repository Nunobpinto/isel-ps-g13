package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/organizations")
class OrganizationController {

    @Autowired
    lateinit var organizationService: OrganizationService

    // ------ ORGANIZATION --------

    // ----------------------------
    // Organization Endpoints
    // ----------------------------

    @GetMapping
    fun getAllOrganizations() = organizationService.getAllOrganizations()

    @GetMapping("/{organizationId}")
    fun getSpecificOrganization(@PathVariable organizationId: Int) = organizationService.getSpecificOrganization(organizationId)

    @PostMapping()
    fun createOrganization(@RequestBody input: OrganizationInputModel) = organizationService.createOrganization(input)

    @PostMapping("/{organizationId}/vote")
    fun voteOrganization(@PathVariable organizationId: Int, @RequestBody input: VoteInputModel) = organizationService.voteOnOrganization(organizationId, input)

    @PatchMapping("/{organizationId}")
    fun partialUpdateOnOrganization(
            @PathVariable organizationId: Int,
            @RequestBody input: OrganizationInputModel
    ) = organizationService.updateOrganization(organizationId, input)

    @DeleteMapping
    fun deleteAllOrganizations() = organizationService.deleteAllOrganizations()

    @DeleteMapping("/{organizationId}")
    fun deleteSpecificOrganization(@PathVariable organizationId: Int) = organizationService.deleteOrganization(organizationId)

    // ----------------------------
    // Organization Report Endpoints
    // ----------------------------

    @GetMapping("/{organizationId}/reports")
    fun getAllReportsOnOrganization(@PathVariable organizationId: Int) = organizationService.getAllReportsOnOrganization(organizationId)

    @GetMapping("/{organizationId}/reports/{reportId}")
    fun getSpecificReportOnOrganization(@PathVariable organizationId: Int, @PathVariable reportId: Int) = organizationService.getSpecificReportOnOrganization(organizationId, reportId)

    @PostMapping("/{organizationId}/reports")
    fun reportOrganization(@PathVariable organizationId: Int, @RequestBody input: OrganizationReportInputModel) = organizationService.reportOrganization(organizationId, input)

    @PostMapping("/{organizationId}/reports/{reportId}")
    fun updateReportedOrganization(@PathVariable organizationId: Int, @PathVariable reportId: Int)
            = organizationService.updateReportedOrganization(organizationId, reportId)

    @PostMapping("/{organizationId}/reports/{reportId}/vote")
    fun voteOrganizationReport(@PathVariable organizationId: Int, @PathVariable reportId: Int, @RequestBody input: VoteInputModel) = organizationService.voteOnOrganizationReport(organizationId, reportId, input)

    @DeleteMapping("/{organizationId}/reports")
    fun deleteAllReports(@PathVariable organizationId: Int) = organizationService.deleteAllReportsOnOrganization(organizationId)

    @DeleteMapping("/{organizationId}/reports/{reportId}")
    fun deleteSpecificReport(@PathVariable organizationId: Int, @PathVariable reportId: Int) = organizationService.deleteSpecificReportOnOrganization(organizationId, reportId)

    // ----------------------------
    // Organization Version Endpoints
    // ----------------------------

    @GetMapping("/{organizationId}/versions")
    fun getAllVersionsOfOrganization(@PathVariable organizationId: Int) = organizationService.getAllVersionsOfOrganization(organizationId)

    @GetMapping("/{organizationId}/versions/{versionId}")
    fun getSpecificVersionOfOrganization(@PathVariable organizationId: Int, @PathVariable versionId: Int) = organizationService.getSpecificVersionOfOrganization(organizationId, versionId)

    @DeleteMapping("/{organizationId}/versions")
    fun deleteAllVersions(@PathVariable organizationId: Int) = organizationService.deleteAllVersionsOfOrganization(organizationId)

    @DeleteMapping("/{organizationId}/versions/{versionId}")
    fun deleteSpecificVersion(@PathVariable organizationId: Int, @PathVariable versionId: Int) = organizationService.deleteSpecificVersionOfOrganization(organizationId, versionId)

}