package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
import java.util.*

interface UserDAO {

    fun getUser(username: String): Optional<User>

    fun createUser(user: User): User

    fun getCoursesOfUser(username: String): List<Course>

    fun getClassesOfUser(username: String): List<UserCourseClass>

    fun getProgrammeOfUser(username: String): Optional<Programme>

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

    fun getSpecificReportOfUser(username: String, reportId: Int): Optional<UserReport>

    fun getAllReportsOfUser(username: String): List<UserReport>

    fun deleteAllClassesOfUser(username: String): Int

    fun deleteSpecificClassOfUser(username: String, courseClassId: Int): Int

    fun getDevs(): List<User>

    fun getUsersByRole(role: String): List<User>

    fun lockUser(username: String): User

    fun deleteAllReportsOnUser(username: String) : Int

    fun getUserByEmail(email: String): Optional<User>
}