package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.model.Resource
import isel.leic.ps.eduWikiAPI.repository.ResourceDAOJdbi
import isel.leic.ps.eduWikiAPI.service.interfaces.ResourceStorageService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ResourceStorageServiceImpl : ResourceStorageService{

    @Autowired
    lateinit var jdbi: Jdbi

    override fun storeResource(sheetId: UUID, sheet: MultipartFile): Resource =
            jdbi.withExtension<Resource, ResourceDAOJdbi, Exception>(ResourceDAOJdbi::class.java) {
                it.storeResource(
                        sheetId,
                        sheet.bytes,
                        sheet.contentType!!,
                        sheet.originalFilename!!,
                        sheet.size
                )
                //TODO confirm(exam.examId)
            }

    override fun getResource(sheetId: UUID): Resource =
            jdbi.withExtension<Resource, ResourceDAOJdbi, Exception>(ResourceDAOJdbi::class.java) {
                it.getResource(sheetId).get()
            }
}