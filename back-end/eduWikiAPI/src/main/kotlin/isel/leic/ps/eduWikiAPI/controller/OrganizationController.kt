package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.OrganizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/organization")
class OrganizationController {

    @Autowired
    lateinit var organizationService: OrganizationService

    // ------ ORGANIZATION --------

    // ----------------------------
    // Organization Endpoints
    // ----------------------------

    @GetMapping
    fun getOrganization() = organizationService.getOrganization()

    @PatchMapping("")
    fun partialUpdateOnOrganization(
            @RequestBody input: OrganizationInputModel,
            principal: Principal
    ) = organizationService.updateOrganization(input, principal)

    // ----------------------------
    // Organization Report Endpoints
    // ----------------------------

    @GetMapping("/reports")
    fun getAllReportsOnOrganization() = organizationService.getAllReportsOnOrganization()

    @GetMapping("/reports/{reportId}")
    fun getSpecificReportOnOrganization(@PathVariable reportId: Int) = organizationService.getSpecificReportOnOrganization(reportId)

    @PostMapping("/reports")
    fun reportOrganization(@RequestBody input: OrganizationReportInputModel, principal: Principal) = organizationService.reportOrganization(input, principal)

    @PostMapping("/reports/{reportId}")
    fun updateReportedOrganization(@PathVariable reportId: Int, principal: Principal)
            = organizationService.updateReportedOrganization(reportId, principal)

    @PostMapping("/reports/{reportId}/vote")
    fun voteOrganizationReport(@PathVariable reportId: Int, @RequestBody input: VoteInputModel, principal: Principal) = organizationService.voteOnOrganizationReport(reportId, input, principal)

    @DeleteMapping("/reports/{reportId}")
    fun deleteSpecificReport(@PathVariable reportId: Int, principal: Principal) = organizationService.deleteSpecificReportOnOrganization(reportId, principal)

    // ----------------------------
    // Organization Version Endpoints
    // ----------------------------

    @GetMapping("/versions")
    fun getAllVersionsOfOrganization() = organizationService.getAllVersionsOfOrganization()

    @GetMapping("/versions/{versionId}")
    fun getSpecificVersionOfOrganization(@PathVariable versionId: Int) = organizationService.getSpecificVersionOfOrganization(versionId)
}