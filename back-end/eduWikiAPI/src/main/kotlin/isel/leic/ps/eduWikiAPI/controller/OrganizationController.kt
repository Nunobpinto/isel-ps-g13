package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.versions.OrganizationVersionInputModel
import isel.leic.ps.eduWikiAPI.service.interfaces.OrganizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/organizations")
class OrganizationController {

    //TODO implementation of Programme Controller methods
    @Autowired
    lateinit var organizationService: OrganizationService

    /**
     * All GET Routes
     */
    @GetMapping
    fun getAllOrganizations() = organizationService.getAllOrganizations()

    @GetMapping("/{organizationId}")
    fun getSpecificOrganization(@PathVariable organizationId: Int) = organizationService.getSpecificOrganization(organizationId)

    @GetMapping("/{organizationId}/reports")
    fun getAllOrganizationReporta(@PathVariable organizationId: Int) = organizationService.getAllOrganizationReports(organizationId)

    @GetMapping("/{organizationId}/reports/{reportId}")
    fun getSpecificReport(@PathVariable organizationId: Int, @PathVariable reportId: Int) = organizationService.getSpecificReport(organizationId, reportId)

    @GetMapping("/{organizationId}/versions")
    fun getAllVersionsOfOrganization(@PathVariable organizationId: Int) = organizationService.getAllVersions(organizationId)

    @GetMapping("/{organizationId}/versions/{versionId]")
    fun getVersionOfOrganization(@PathVariable organizationId: Int, @PathVariable versionId: Int) = organizationService.getVersion(organizationId, versionId)

    /**
     * All POST Routes
     */
    @PostMapping()
    fun createOrganization(@RequestBody input: OrganizationInputModel) = organizationService.createOrganization(input)

    @PostMapping("/{organizationId}/reports")
    fun reportOrganization(@PathVariable organizationId: Int, @RequestBody input: OrganizationReportInputModel) = organizationService.reportOrganization(organizationId, input)

    @PostMapping("/{organizationId}/vote")
    fun voteOrganization(@PathVariable organizationId: Int, @RequestBody input: VoteInputModel) = organizationService.voteOrganization(organizationId, input)

    @PostMapping("/{organizationId}/reports/{reportId}/vote")
    fun voteOrganizationReport(@PathVariable organizationId: Int, @PathVariable reportId: Int, @RequestBody input: VoteInputModel) = organizationService.voteOnReport(organizationId, reportId, input)

    @PostMapping("/{organizationId}/versions")
    fun createVersion(@PathVariable organizationId: Int, @RequestBody input: OrganizationVersionInputModel)
            = organizationService.createVersion(organizationId, input)

    /**
     * ALL PATCH Routes
     */
    @PatchMapping("/{organizationId}")
    fun partialUpdateOnOrganization(
            @PathVariable organizationId: Int,
            @RequestBody input: OrganizationInputModel
    ) = NotImplementedError()

    /**
     * ALL DELETE Routes
     */
    @DeleteMapping
    fun deleteAllOrganizations() = organizationService.deleteAllOrganizations()

    @DeleteMapping("/{organizationId}")
    fun deleteSpecificOrganization(@PathVariable organizationId: Int) = organizationService.deleteOrganization(organizationId)

    @DeleteMapping("/{organizationId}/reports")
    fun deleteAllReports(@PathVariable organizationId: Int) = organizationService.deleteAllReports(organizationId)

    @DeleteMapping("/{organizationId}/reports/{reportId}")
    fun deleteSpecificReport(@PathVariable organizationId: Int, @PathVariable reportId: Int) = organizationService.deleteSpecificReport(organizationId, reportId)

    @DeleteMapping("/{organizationId}/versions")
    fun deleteAllVersions(@PathVariable organizationId: Int) = organizationService.deleteAllVersions(organizationId)

    @DeleteMapping("/{organizationId}/versions/{versionId}")
    fun deleteSpecificVersion(@PathVariable organizationId: Int, @PathVariable versionId: Int) = organizationService.deleteSpecificVersion(organizationId, versionId)
}