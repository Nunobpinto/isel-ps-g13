package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.UserReportOutputModel

interface UserService {

    fun getAuthenticatedUser(username: String): AuthUserOutputModel

    fun saveUser(inputUser: UserInputModel): AuthUserOutputModel

    fun deleteUser(username: String): Int

    fun getCoursesOfUser(username: String): List<CourseOutputModel>

    fun getClassesOfUser(username: String): List<ClassOutputModel>

    fun getProgrammeOfUser(username: String): ProgrammeOutputModel

    fun addCourseToUser(username: String, input: UserCourseClassInputModel): UserCourseOutputModel

    fun updateUser(input: UserInputModel): AuthUserOutputModel

    fun addClassToUser(username: String, input: UserCourseClassInputModel): UserCourseClassOutputModel

    fun addProgrammeToUSer(username: String, input: UserProgrammeInputModel): ProgrammeOutputModel

    fun deleteAllCoursesOfUser(username: String): Int

    fun deleteProgrammeOfUser(username: String): Int

    fun deleteSpecificCourseOfUser(username: String, courseId: Int): Int

    fun reportUser(username: String, reportedBy: String, reportInput: UserReportInputModel): UserReportOutputModel

    fun deleteSpecificClassOfUser(username: String, courseClassId: Int): Int

    fun deleteAllClassesOfUser(username: String): Int

    fun getUser(username: String): UserOutputModel

    fun approveReport(username: String, reportId: Int): Int

    fun getAllReportsOfUser(username: String): List<UserReportOutputModel>

    fun getSpecificReportOfUser(username: String, reportId: Int): UserReportOutputModel

    fun deleteReportOnUser(username: String, reportId: Int): Int

}