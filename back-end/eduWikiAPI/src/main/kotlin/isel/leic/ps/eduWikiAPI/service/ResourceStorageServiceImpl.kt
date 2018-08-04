package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.model.Resource
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.ResourceDAOJdbi
import isel.leic.ps.eduWikiAPI.service.interfaces.ResourceStorageService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.reflect.Executable
import java.util.*

@Service
class ResourceStorageServiceImpl : ResourceStorageService{

    @Autowired
    lateinit var jdbi: Jdbi

    override fun storeResource(uuId: UUID, sheet: MultipartFile): Resource =
            jdbi.withExtension<Resource, ResourceDAOJdbi, Exception>(ResourceDAOJdbi::class.java) {
                it.storeResource(
                        uuId,
                        sheet.bytes,
                        sheet.contentType!!,
                        sheet.originalFilename!!,
                        sheet.size
                )
            }

    override fun getResource(uuId: UUID): Resource =
            jdbi.withExtension<Resource, ResourceDAOJdbi, Exception>(ResourceDAOJdbi::class.java) {
                it.getResource(uuId).get()
            }

    override fun batchDeleteResource(uuIds: List<UUID>): IntArray =
            jdbi.withExtension<IntArray, ResourceDAOJdbi, Exception>(ResourceDAOJdbi::class.java) {
                it.batchDeleteResources(uuIds)
            }

    override fun deleteSpecificResource(uuid: UUID): Int =
            jdbi.withExtension<Int, ResourceDAOJdbi, Exception>(ResourceDAOJdbi::class.java){
                it.deleteSpecificResource(uuid)
            }
}