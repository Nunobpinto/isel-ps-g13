package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/user")
class AuthUserController {

    @Autowired
    lateinit var userService: UserService

    /**
     * ALL GET Routes
     */

    @GetMapping
    fun getAuthenticatedUser(principal: Principal) = userService.getAuthenticatedUser(principal)

    @GetMapping("/courses")
    fun getCoursesOfUser(principal: Principal) = userService.getCoursesOfUser(principal)

    @GetMapping("/classes")
    fun getClassesOfUser(principal: Principal) = userService.getClassesOfCOurseOfUser(principal)

    @GetMapping("/programme")
    fun getProgrammeOfUser(principal: Principal) = userService.getProgrammeOfUser(principal)

    @GetMapping("/feed")
    fun getUserFeed(principal: Principal) = userService.getUserFeed(principal)

    @GetMapping("/reputation")
    fun getUserReputation(principal: Principal) = userService.getUserReputation(principal)

    @GetMapping("/actions")
    fun getUserActions(principal: Principal) = userService.getUserActions(principal)

    /**
     * ALL PATCH Routes
     */

    @PatchMapping("/user")
    fun updateAuthenticatedUser(
            principal: Principal,
            @RequestBody input: UserUpdateInputModel
    ) = userService.updateUser(input, principal)

    /**
     * All POST Routes
     */

    @PostMapping("/courses")
    fun addCourseToUser(principal: Principal, @RequestBody input: UserCourseClassInputModel) = userService.addCourseToUser(principal, input)

    @PostMapping("/classes")
    fun addClassToUser(principal: Principal, @RequestBody input: UserCourseClassInputModel) = userService.addClassToUser(principal, input)

    @PostMapping("/programme")
    fun addProgrammeToUser(principal: Principal, @RequestBody input: UserProgrammeInputModel) = userService.addProgrammeToUSer(principal, input)


    /**
     * All DELETE Routes
     */

    @DeleteMapping
    fun deleteAuthUser(principal: Principal) = userService.deleteUser(principal)

    @DeleteMapping("/courses")
    fun deleteAllCoursesOfUser(principal: Principal) = userService.deleteAllCoursesOfUser(principal)

    @DeleteMapping("/courses/{courseId}")
    fun deleteSpecificCourseOfUser(
            principal: Principal,
            @PathVariable courseId: Int
    ) = userService.deleteSpecificCourseOfUser(principal, courseId)

    @DeleteMapping("/classes")
    fun deleteAllClassesOfUser(principal: Principal) = userService.deleteAllClassesOfUser(principal)

    @DeleteMapping("/classes/{courseClassId}")
    fun deleteSpecificClassOfUser(
            principal: Principal,
            @PathVariable courseClassId: Int
    ) = userService.deleteSpecificClassOfUser(principal, courseClassId)

    @DeleteMapping("/programme")
    fun deleteProgrammeOfUser(principal: Principal) = userService.deleteProgrammeOfUser(principal)

}