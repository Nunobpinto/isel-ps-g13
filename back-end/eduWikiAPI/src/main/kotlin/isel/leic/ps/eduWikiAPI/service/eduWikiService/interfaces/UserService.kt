package isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.UserReportOutputModel
import java.security.Principal
import java.util.*

interface UserService {

    fun getAuthenticatedUser(username: String): AuthUserOutputModel

    fun saveUser(inputUser: UserInputModel): AuthUserOutputModel

    fun deleteUser(username: String): Int

    fun getCoursesOfUser(username: String): CourseCollectionOutputModel

    fun getClassesOfCOurseOfUser(username: String): CourseClassCollectionOutputModel

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

    fun confirmUser(username: String, token: UUID): String

    fun getUserFeed(principal: Principal): UserActionCollectionOutputModel

    fun getUserReputation(principal: Principal): UserReputationCollectionOutputModel

    fun getUserActions(principal: Principal): UserActionCollectionOutputModel
}