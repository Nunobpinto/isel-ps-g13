package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal

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

    @PostMapping
    fun createOrganization(
            @RequestBody input: OrganizationInputModel,
            principal: Principal
    ) = organizationService.createOrganization(input, principal)

    @PostMapping("/{organizationId}/vote")
    fun voteOrganization(@PathVariable organizationId: Int, @RequestBody input: VoteInputModel, principal: Principal) = organizationService.voteOnOrganization(organizationId, input, principal)

    @PatchMapping("/{organizationId}")
    fun partialUpdateOnOrganization(
            @PathVariable organizationId: Int,
            @RequestBody input: OrganizationInputModel,
            principal: Principal
    ) = organizationService.updateOrganization(organizationId, input, principal)

    @DeleteMapping("/{organizationId}")
    fun deleteSpecificOrganization(@PathVariable organizationId: Int, principal: Principal) = organizationService.deleteOrganization(organizationId)

    // ----------------------------
    // Organization Report Endpoints
    // ----------------------------

    @GetMapping("/{organizationId}/reports")
    fun getAllReportsOnOrganization(@PathVariable organizationId: Int) = organizationService.getAllReportsOnOrganization(organizationId)

    @GetMapping("/{organizationId}/reports/{reportId}")
    fun getSpecificReportOnOrganization(@PathVariable organizationId: Int, @PathVariable reportId: Int) = organizationService.getSpecificReportOnOrganization(organizationId, reportId)

    @PostMapping("/{organizationId}/reports")
    fun reportOrganization(@PathVariable organizationId: Int, @RequestBody input: OrganizationReportInputModel, principal: Principal) = organizationService.reportOrganization(organizationId, input, principal)

    @PostMapping("/{organizationId}/reports/{reportId}")
    fun updateReportedOrganization(@PathVariable organizationId: Int, @PathVariable reportId: Int, principal: Principal)
            = organizationService.updateReportedOrganization(organizationId, reportId, principal)

    @PostMapping("/{organizationId}/reports/{reportId}/vote")
    fun voteOrganizationReport(@PathVariable organizationId: Int, @PathVariable reportId: Int, @RequestBody input: VoteInputModel, principal: Principal) = organizationService.voteOnOrganizationReport(organizationId, reportId, input, principal)

    @DeleteMapping("/{organizationId}/reports/{reportId}")
    fun deleteSpecificReport(@PathVariable organizationId: Int, @PathVariable reportId: Int, principal: Principal) = organizationService.deleteSpecificReportOnOrganization(organizationId, reportId, principal)

    // ----------------------------
    // Organization Version Endpoints
    // ----------------------------

    @GetMapping("/{organizationId}/versions")
    fun getAllVersionsOfOrganization(@PathVariable organizationId: Int) = organizationService.getAllVersionsOfOrganization(organizationId)

    @GetMapping("/{organizationId}/versions/{versionId}")
    fun getSpecificVersionOfOrganization(@PathVariable organizationId: Int, @PathVariable versionId: Int) = organizationService.getSpecificVersionOfOrganization(organizationId, versionId)
}