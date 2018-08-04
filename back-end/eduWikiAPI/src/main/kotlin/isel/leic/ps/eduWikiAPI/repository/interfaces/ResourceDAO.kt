package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Resource
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface ResourceDAO {

    fun storeResource(
            uuId: UUID,
            byteSequence: ByteArray,
            contentType: String,
            originalFilename: String,
            size: Long
    ): Resource

    fun getResource(uuId: UUID): Optional<Resource>

   // fun confirm(examId: Int)

    fun batchDeleteResources(uuIds: List<UUID>): IntArray

    fun deleteSpecificResource(uuId: UUID): Int
}