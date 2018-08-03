package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.service.interfaces.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/user")
class AuthUserController {

    //TODO implementation of Authenticated User Controller methods
    @Autowired
    lateinit var userService: UserService

    /**
     * ALL GET Routes
     */

    @GetMapping
    fun getAuthenticatedUser(principal: Principal) = userService.getAuthenticatedUser(principal.name).toString()

    @GetMapping("/courses")
    fun getCoursesOfUser(principal: Principal) = userService.getCoursesOfUser(principal.name)

    @GetMapping("/classes")
    fun getClassesOfUser(principal: Principal) = userService.getClassesOfUser(principal.name)

    @GetMapping("/programme")
    fun getProgrammeOfUser(principal: Principal) = userService.getProgrammeOfUser(principal.name)

    /**
     * ALL PATCH Routes
     */

    @PatchMapping("/user")
    fun updateAuthenticatedUser(@RequestBody input: UserInputModel) = userService.updateUser(input)

    /**
     * All POST Routes
     */

    @PostMapping("/courses")
    fun addCourseToUser(principal: Principal, @RequestBody input: UserCourseClassInputModel) = userService.addCourseToUser(principal.name, input)

    @PostMapping("/classes")
    fun addClassToUser(principal: Principal, @RequestBody input: UserCourseClassInputModel) = userService.addClassToUser(principal.name, input)

    @PostMapping("/programme")
    fun addProgrammeToUser(principal: Principal, @RequestBody input: UserProgrammeInputModel) = userService.addProgrammeToUSer(principal.name, input)


    /**
     * All DELETE Routes
     */

    @DeleteMapping
    fun deleteAuthUser(principal: Principal) = userService.deleteUser(principal.name)

    @DeleteMapping("/courses")
    fun deleteAllCoursesOfUser(principal: Principal) = userService.deleteAllCoursesOfUser(principal.name)

    @DeleteMapping("/courses/{courseId}")
    fun deleteSpecificCourseOfUser(principal: Principal, @PathVariable courseId: Int) =
            userService.deleteSpecificCourseOfUser(principal.name, courseId)

    @DeleteMapping("/classes")
    fun deleteAllClassesOfUser(principal: Principal) = userService.deleteAllClassesOfUser(principal.name)
    @DeleteMapping("/classes/{classId}")
    fun deleteSpecificClassOfUser(principal: Principal, @PathVariable classId: Int) =
            userService.deleteSpecificClassOfUser(principal.name, classId)

    @DeleteMapping("/programme")
    fun deleteProgrammeOfUser(principal: Principal) = userService.deleteProgrammeOfUser(principal.name)

}