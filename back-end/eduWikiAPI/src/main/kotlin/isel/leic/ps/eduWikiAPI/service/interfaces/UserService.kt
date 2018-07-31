package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport

interface UserService {

    fun getAuthenticatedUser(username: String): User

    fun saveUser(inputUser: UserInputModel): User

    fun deleteUser(username: String): Int

    fun getCoursesOfUser(username: String): List<Course>

    fun getClassesOfUser(username: String): List<Class>

    fun getProgrammeOfUser(username: String): Programme

    fun addCourseToUser(username: String, input: UserCourseClassInputModel): UserCourseClass

    fun updateUser(input: UserInputModel): User

    fun addClassToUser(username: String, input: UserCourseClassInputModel): UserCourseClass

    fun addProgrammeToUSer(username: String, input: UserProgrammeInputModel): UserProgramme

    fun deleteAllCoursesOfUser(username: String): Int

    fun deleteProgrammeOfUser(username: String): Int

    fun deleteSpecificCourseOfUser(username: String, courseId: Int): Int

    fun reportUser(username: String, reportedBy: String, reportInput: UserReportInputModel): UserReport

    fun deleteSpecificClassOfUser(name: String?, classId: Int): Int

    fun deleteAllClassesOfUser(username: String?): Int

}