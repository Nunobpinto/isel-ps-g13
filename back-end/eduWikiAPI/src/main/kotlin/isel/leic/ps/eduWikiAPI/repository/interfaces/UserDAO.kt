package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.domain.model.UserCourseClass
import isel.leic.ps.eduWikiAPI.domain.model.UserProgramme
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
import java.util.*

interface UserDAO {

    fun getUser(username: String): Optional<User>

    fun createUser(user: User): User

    fun confirmUser(username: String): User

    fun getCoursesOfUser(username: String): List<Int>

    fun getClassesOfUser(username: String): List<UserCourseClass>

    fun getProgrammeOfUser(username: String): Int

    fun addProgrammeToUser(username: String, programmeId: Int): UserProgramme

    fun addCourseToUser(userCourseClass: UserCourseClass): UserCourseClass

    fun addClassToUser(userCourseClass: UserCourseClass): UserCourseClass

    fun deleteAllCoursesOfUser(username: String): Int

    fun deleteProgramme(username: String): Int

    fun deleteSpecificCourseOfUser(username: String, courseId: Int): Int

    fun reportUser(report: UserReport): UserReport

    fun updateUser(newUser: User): User

    fun deleteUser(username: String): Int

    fun deleteSpecificReportOfUser(username: String, reportId: Int): Int

    fun getSpecficReportOfUser(username: String, reportId: Int): UserReport

    fun getAllReportsOfUser(username: String): List<UserReport>

    fun deleteAllClassesOfUser(username: String): Int

    fun deleteSpecificClassOfUser(username: String, courseClassId: Int): Int

    fun getDevs(): List<User>
}