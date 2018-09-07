package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/users")
class UsersController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/{username}")
    fun getUser(@PathVariable username: String) = userService.getUser(username)

    @GetMapping("/{username}/reports")
    fun getReportsOfUser(@PathVariable username: String) = userService.getAllReportsOfUser(username)

    @GetMapping("/{username}/reports/{reportId}")
    fun getSpecificReportOfUser(
            @PathVariable username: String,
            @PathVariable reportId: Int
    ) = userService.getSpecificReportOfUser(username, reportId)

    @GetMapping("/{username}/confirm/{token}")
    fun confirmUser(
            @PathVariable username: String,
            @PathVariable token: UUID
    ) = userService.confirmUser(username, token)

    @PostMapping
    fun registerUser(
            @RequestBody inputUser: UserInputModel
    ) = userService.saveUser(inputUser)

    @PostMapping("/{username}/reports")
    fun reportUser(
            principal: Principal,
            @PathVariable username: String,
            @RequestBody report: UserReportInputModel
    ) = userService.reportUser(username, report, principal)

    @PostMapping("/{username}/reports/{reportId}")
    fun approveUserReport(
            principal: Principal,
            @PathVariable username: String,
            @PathVariable reportId: Int
    ) = userService.approveReport(username, reportId, principal)

    @DeleteMapping("/{username}/reports/{reportId}")
    fun deleteReportOnUser(
            principal: Principal,
            @PathVariable username: String,
            @PathVariable reportId: Int
    ) = userService.deleteReportOnUser(username, reportId)
}