package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Resource
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface ResourceDAO {

    fun storeResource(
            sheetId: UUID,
            byteSequence: ByteArray,
            contentType: String,
            originalFilename: String,
            size: Long
    ): Resource

    fun getResource(sheetId: UUID): Optional<Resource>

   // fun confirm(examId: Int)

    fun createResourceValidatorEntry(sheetId: UUID, valid: Int)
}