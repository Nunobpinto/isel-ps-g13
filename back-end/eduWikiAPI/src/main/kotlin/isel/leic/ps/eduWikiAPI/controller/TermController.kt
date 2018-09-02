package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.TermService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/terms")
class TermController {

    @Autowired
    lateinit var termService: TermService

    @GetMapping
    fun getAllTerms() = termService.getAllTerms()

    @GetMapping("/{termId}")
    fun getSpecificTerm(@PathVariable termId: Int) = termService.getSpecificTerm(termId)


}