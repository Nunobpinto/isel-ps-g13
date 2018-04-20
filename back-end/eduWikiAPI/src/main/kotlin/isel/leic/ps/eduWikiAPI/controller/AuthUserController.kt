package isel.leic.ps.eduWikiAPI.controller

import isel.leic.ps.eduWikiAPI.inputModel.ClassInputModel
import isel.leic.ps.eduWikiAPI.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.inputModel.ProgrammeInputModel
import isel.leic.ps.eduWikiAPI.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.service.UserService
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
    fun getAuthenticatedUser(principal: Principal) = userService.getUser(principal.name).toString()

    @GetMapping("/courses")
    fun getCoursesOfUser(principal: Principal) = NotImplementedError()

    @GetMapping("/courses/{courseId}")
    fun getSpecificCourseOfUser(principal: Principal, @PathVariable courseId: Int) = NotImplementedError()

    @GetMapping("/classes")
    fun getClassesOfUser(principal: Principal) = NotImplementedError()

    @GetMapping("/classes/{classId}")
    fun getSpecificClassOfUser(principal: Principal, @PathVariable classId: Int) = NotImplementedError()

    @GetMapping("/programme")
    fun getProgrammeOfUser(principal: Principal) = NotImplementedError()

    @GetMapping("/reputation")
    fun getReputationDetails(principal: Principal) = NotImplementedError()

    /**
     * ALL PATCH Routes
     */
    @PatchMapping("/user")
    fun updateAuthenticatedUser(@RequestBody input: UserInputModel) = NotImplementedError()

    @PostMapping("/courses")
    fun addCourseToUser(principal: Principal, @RequestBody input: CourseInputModel) = NotImplementedError()


    @PostMapping("/classes")
    fun addClassToUser(principal: Principal, @RequestBody input: ClassInputModel) = NotImplementedError()


    @PostMapping("/programmes")
    fun addProgrammeToUser(principal: Principal, @RequestBody input: ProgrammeInputModel) = NotImplementedError()


    @DeleteMapping("/courses")
    fun deleteAllCoursesOfUser(principal: Principal) = NotImplementedError()


    @DeleteMapping("/classes")
    fun deleteAllClassesOfUser(principal: Principal) = NotImplementedError()


    @DeleteMapping("/programme")
    fun deleteProgrammeOfUser(principal: Principal) = NotImplementedError()

    @DeleteMapping("/courses/{courseId}")
    fun deleteSpecificCourseOfUser(principal: Principal, @PathVariable courseId: Int) = NotImplementedError()


    @DeleteMapping("/classes/{classId}")
    fun deleteSpecificClassOfUser(principal: Principal, @PathVariable classId: Int) = NotImplementedError()

}