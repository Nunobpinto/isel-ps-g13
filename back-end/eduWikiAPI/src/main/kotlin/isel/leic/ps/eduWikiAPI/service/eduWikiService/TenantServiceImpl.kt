package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.domain.mappers.toTenantDetailsOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TenantDetailsOutputModel
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.interfaces.TenantRepository
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.TenantService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class TenantServiceImpl : TenantService {

    @Autowired
    lateinit var tenantRepository: TenantRepository

    override fun findTenantById(tenantId: String): TenantDetailsOutputModel =
            toTenantDetailsOutputModel(tenantRepository.findById(tenantId).orElseThrow { NotFoundException("Tenant not found", "Try a valid tenant id") })
}