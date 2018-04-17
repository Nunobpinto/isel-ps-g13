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

    @GetMapping
    fun getAuthenticatedUser(principal: Principal) = userService.getUser(principal.name).toString()

    @GetMapping("/courses")
    fun getCoursesOfUser(principal: Principal): String {
        throw NotImplementedError()
    }

    @GetMapping("/courses/{courseId}")
    fun getSpecificCourseOfUser(principal: Principal, @PathVariable courseId: Int): String {
        throw NotImplementedError()
    }

    @GetMapping("/classes")
    fun getClassesOfUser(principal: Principal): String {
        throw NotImplementedError()
    }

    @GetMapping("/classes/{classId}")
    fun getSpecificClassOfUser(principal: Principal, @PathVariable classId: Int): String {
        throw NotImplementedError()
    }

    @GetMapping("/programmes")
    fun getProgrammesOfUser(principal: Principal): String {
        throw NotImplementedError()
    }

    @GetMapping("/programmes/{programmeId}")
    fun getSpecificProgrammeOfUser(principal: Principal, @PathVariable programmeId: Int): String {
        throw NotImplementedError()
    }

    @GetMapping("/reputation")
    fun getReputationDetails(principal: Principal): String {
        throw NotImplementedError()
    }

    @PatchMapping("/user")
    fun updateAuthenticatedUser(@RequestBody input: UserInputModel){
        throw NotImplementedError()
    }

    @PostMapping("/courses")
    fun addCourseToUser(principal: Principal,@RequestBody input: CourseInputModel){
        throw NotImplementedError()
    }

    @PostMapping("/classes")
    fun addClassToUser(principal: Principal,@RequestBody input: ClassInputModel){
        throw NotImplementedError()
    }

    @PostMapping("/programmes")
    fun addProgrammeToUser(principal: Principal,@RequestBody input: ProgrammeInputModel){
        throw NotImplementedError()
    }

    @DeleteMapping("/courses")
    fun deleteAllCoursesOfUser(principal: Principal){
        throw NotImplementedError()
    }

    @DeleteMapping("/classes")
    fun deleteAllClassesOfUser(principal: Principal){
        throw NotImplementedError()
    }

    @DeleteMapping("/programmes")
    fun deleteAllProgrammesOfUser(principal: Principal){
        throw NotImplementedError()
    }

    @DeleteMapping("/courses/{courseId}")
    fun deleteSpecificCourseOfUser(principal: Principal,@PathVariable courseId: Int){
        throw NotImplementedError()
    }

    @DeleteMapping("/classes/{classId}")
    fun deleteSpecificClassOfUser(principal: Principal,@PathVariable classId: Int){
        throw NotImplementedError()
    }

    @DeleteMapping("/programmes/{programmeId}")
    fun deleteSpecificProgrammeOfUser(principal: Principal,@PathVariable programmeId: Int){
        throw NotImplementedError()
    }
}