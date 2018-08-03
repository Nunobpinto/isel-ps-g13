package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Resource
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface ResourceStorageService {

    fun storeResource(sheetId: UUID, sheet: MultipartFile): Resource

    fun getResource(sheetId: UUID): Resource

}