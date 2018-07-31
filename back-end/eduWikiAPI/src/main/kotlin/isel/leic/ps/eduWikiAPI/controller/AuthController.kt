package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.mailSender.EmailService
import isel.leic.ps.eduWikiAPI.service.interfaces.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/users")
class AuthController {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var emailService: EmailService

    @PostMapping
    fun registerUser(@RequestBody inputUser: UserInputModel) = userService.saveUser(inputUser)

    @PostMapping("/{username}/report")
    fun reportUser(
            principal: Principal,
            @PathVariable username: String,
            @RequestBody report: UserReportInputModel
    ) = userService.reportUser(username,principal.name, report)
}