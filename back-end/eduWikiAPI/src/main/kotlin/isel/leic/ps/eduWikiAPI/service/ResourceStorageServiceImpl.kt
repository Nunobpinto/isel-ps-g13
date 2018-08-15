package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.model.Resource
import isel.leic.ps.eduWikiAPI.repository.interfaces.ResourceDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.ResourceStorageService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.lang.reflect.Executable
import java.util.*

@Service
class ResourceStorageServiceImpl : ResourceStorageService {

    @Autowired
    lateinit var resourceDAO: ResourceDAO

    @Transactional
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

    @Transactional
    override fun batchDeleteResource(uuIds: List<UUID>): IntArray =
            resourceDAO.batchDeleteResources(uuIds)

    @Transactional
    override fun deleteSpecificResource(uuid: UUID): Int =
            resourceDAO.deleteSpecificResource(uuid)

}