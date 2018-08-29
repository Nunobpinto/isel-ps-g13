package isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserUpdateInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.UserReportOutputModel
import java.security.Principal
import java.util.*

interface UserService {

    fun getAuthenticatedUser(principal: Principal): AuthUserOutputModel

    fun saveUser(inputUser: UserInputModel): AuthUserOutputModel

    fun deleteUser(principal: Principal): Int

    fun getCoursesOfUser(principal: Principal): CourseCollectionOutputModel

    fun getClassesOfCOurseOfUser(principal: Principal): CourseClassCollectionOutputModel

    fun getProgrammeOfUser(principal: Principal): ProgrammeOutputModel

    fun addCourseToUser(principal: Principal, input: UserCourseClassInputModel): UserCourseOutputModel

    fun updateUser(input: UserUpdateInputModel, principal: Principal): AuthUserOutputModel

    fun addClassToUser(principal: Principal, input: UserCourseClassInputModel): UserCourseClassOutputModel

    fun addProgrammeToUSer(principal: Principal, input: UserProgrammeInputModel): ProgrammeOutputModel

    fun deleteAllCoursesOfUser(principal: Principal): Int

    fun deleteProgrammeOfUser(principal: Principal): Int

    fun deleteSpecificCourseOfUser(principal: Principal, courseId: Int): Int

    fun reportUser(username: String, reportInput: UserReportInputModel, principal: Principal): UserReportOutputModel

    fun deleteSpecificClassOfUser(principal: Principal, courseClassId: Int): Int

    fun deleteAllClassesOfUser(principal: Principal): Int

    fun getUser(username: String): UserOutputModel

    fun approveReport(username: String, reportId: Int, principal: Principal): UserReportOutputModel

    fun getAllReportsOfUser(username: String): UserReportCollectionOutputModel

    fun getSpecificReportOfUser(username: String, reportId: Int): UserReportOutputModel

    fun deleteReportOnUser(username: String, reportId: Int): Int

    fun confirmUser(username: String, token: UUID): String

    fun getUserFeed(principal: Principal): UserActionCollectionOutputModel

    fun getUserReputation(principal: Principal): UserReputationCollectionOutputModel

    fun getUserActions(principal: Principal): UserActionCollectionOutputModel
}