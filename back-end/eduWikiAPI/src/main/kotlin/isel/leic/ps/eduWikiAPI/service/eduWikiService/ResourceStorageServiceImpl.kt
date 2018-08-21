package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.domain.model.Resource
import isel.leic.ps.eduWikiAPI.repository.interfaces.ResourceDAO
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.ResourceStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Transactional
@Service
class ResourceStorageServiceImpl : ResourceStorageService {

    @Autowired
    lateinit var resourceDAO: ResourceDAO

    override fun storeResource(uuId: UUID, sheet: MultipartFile): Resource =
            resourceDAO.storeResource(
                    uuId,
                    sheet.bytes,
                    sheet.contentType !!,
                    sheet.originalFilename !!,
                    sheet.size
            )

    override fun getResource(uuId: UUID): Resource =
            resourceDAO.getResource(uuId).orElseGet { Resource() }

    override fun batchDeleteResource(uuIds: List<UUID>): IntArray =
            resourceDAO.batchDeleteResources(uuIds)

    override fun deleteSpecificResource(uuid: UUID): Int =
            resourceDAO.deleteSpecificResource(uuid)

}