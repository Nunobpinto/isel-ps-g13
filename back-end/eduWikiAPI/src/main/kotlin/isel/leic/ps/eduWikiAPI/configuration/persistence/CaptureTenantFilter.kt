package isel.leic.ps.eduWikiAPI.configuration.persistence

import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.interfaces.TenantRepository
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.TenantService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.*

@Component
class CaptureTenantFilter : OncePerRequestFilter() {

    companion object {
        const val TENANT_HEADER = "tenant-uuid"
        const val NO_TENANT_FOUND = "notfound"
        const val NO_TENANT_PROVIDED = "notprovided"
    }

    @Autowired
    lateinit var tenantService: TenantService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val tenantUuid = request.getHeader(TENANT_HEADER)
        // Check if tenant provided is valid. If,
        //   1) No tenant is provided, ThreadLocal is affected with NO_TENANT_PROVIDED
        //   2) Tenant is provided but is invalid, ThreadLocal is affected with NO_TENANT_FOUND
        //   3) A valid tenant is provided, ThreadLocal is affected with its schema
        val tenantSchema =
                if(Objects.nonNull(tenantUuid))
                    try {
                        tenantService.findTenantById(tenantUuid).schemaName
                    } catch(ex: Exception) {
                        if(ex is NotFoundException) NO_TENANT_FOUND
                        else throw ex
                    }
                else
                    NO_TENANT_PROVIDED
        TenantContext.setTenantSchema(tenantSchema)
        filterChain.doFilter(request, response)
        // Reset tenant after processing request
        TenantContext.resetTenantSchema()
    }

}