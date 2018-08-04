package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Resource
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface ResourceStorageService {

    fun storeResource(uuId: UUID, sheet: MultipartFile): Resource

    fun getResource(uuId: UUID): Resource

    fun batchDeleteResource(uuIds: List<UUID>): IntArray

    fun deleteSpecificResource(uuid: UUID): Int

}