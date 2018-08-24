package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.ResourceStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/resources")
class ResourceController {

    @Autowired
    lateinit var storageService: ResourceStorageService

    @GetMapping("/{sheetId}")
    fun getResource(
            @PathVariable sheetId: UUID
    ): ResponseEntity<ByteArray> {
        val resource = storageService.getResource(sheetId)
        val headers = HttpHeaders()
        val disposition = "attachment; filename=\"${resource.originalFilename}\""
        headers.add("Content-Type", resource.contentType)
        headers.add("Content-Disposition", disposition)
        return ResponseEntity(resource.byteSequence, headers, HttpStatus.OK)
    }

}