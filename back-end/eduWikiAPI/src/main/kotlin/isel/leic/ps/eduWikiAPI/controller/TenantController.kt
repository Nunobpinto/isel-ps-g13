package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.TenantRequestDetails
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.TenantService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/tenants")
class TenantController {

    @Autowired
    lateinit var tenantService: TenantService

    @GetMapping
    fun getAllActiveTenants() = tenantService.getAllActiveTenants()

    @GetMapping("/{tenantUuid}")
    fun getSpecificActiveTenant(@PathVariable tenantUuid: String) = tenantService.findTenantById(tenantUuid)

    @GetMapping("/pending")
    fun getAllPendingTenants() = tenantService.getAllPendingTenants()

    @GetMapping("/pending/{tenantUuid}")
    fun getSpecificPendingTenant(@PathVariable tenantUuid: String) = tenantService.findPendingTenantById(tenantUuid)

    @PostMapping("/pending")
    fun createPendingTenant(@RequestBody requestDetails: TenantRequestDetails) = tenantService.createPendingTenant(requestDetails)

    @PostMapping("/pending/{tenantUuid}")
    fun realizePendingTenant(@PathVariable tenantUuid: String, principal: Principal) = tenantService.realizePendingTenant(tenantUuid, principal)

    @DeleteMapping("/pending/{tenantUuid}")
    fun rejectPendingTenant(@PathVariable tenantUuid: String, principal: Principal) = tenantService.rejectPendingTenant(tenantUuid, principal)

}