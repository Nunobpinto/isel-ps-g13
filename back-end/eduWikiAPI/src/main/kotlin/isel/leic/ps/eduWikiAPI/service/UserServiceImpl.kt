package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.service.interfaces.UserService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
import isel.leic.ps.eduWikiAPI.exceptions.BadRequestException
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.*
import java.util.regex.Pattern

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAuthenticatedUser(username: String): User =
            jdbi.withExtension<User, UserDAOJdbi,Exception>(UserDAOJdbi::class.java) {
                it.getUser(username).get()
            }

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    override fun saveUser(inputUser: UserInputModel): User {
        val user = toUser(inputUser)
        val valid = isEmailValid(user.organizationEmail)
        if (!valid) {
            throw BadRequestException(msg = "invalid organization email", action = "Get an actual organization email")
        }
        return jdbi.inTransaction<User, Exception> {
            val userDao = it.attach(UserDAOJdbi::class.java)
            val repDao = it.attach(ReputationDAOJdbi::class.java)
            val user1 = userDao.createUser(user)
            val reputation = Reputation(
                    reputationPoints = 1,
                    reputationRole = "ROLE_BEGINNER",
                    username = user1.username
            )
            repDao.saveNewUser(reputation)
            user1
        }
    }

    override fun deleteUser(username: String): Int =
            jdbi.withExtension<Int, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                it.deleteUser(username)
            }

    override fun getCoursesOfUser(username: String): List<Course> = jdbi.inTransaction<List<Course>, Exception> {
        val userDao = it.attach(UserDAOJdbi::class.java)
        val courseDAO = it.attach(CourseDAOJdbi::class.java)
        val coursesIds = userDao.getCoursesOfUser(username)
        coursesIds.map {
            courseDAO.getSpecificCourse(it).get()
        }
    }

    override fun getClassesOfUser(username: String): List<Class> =
            jdbi.inTransaction<List<Class>, NotFoundException> {
                val userDao = it.attach(UserDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClasses = userDao.getClassesOfUser(username)
                courseClasses.map {
                    classDAO.getSpecificClass(it.courseId).get()
                }
            }

    override fun getProgrammeOfUser(username: String): Programme =
            jdbi.inTransaction<Programme, Exception> {
                val userDao = it.attach(UserDAOJdbi::class.java)
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                val programmeId = userDao.getProgrammeOfUser(username)
                programmeDAO.getSpecificProgramme(programmeId).get()
            }

    override fun addProgrammeToUSer(username: String, input: UserProgrammeInputModel): UserProgramme =
            jdbi.withExtension<UserProgramme, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                it.addProgrammeToUser(username, input.programmeId)
            }

    override fun addCourseToUser(username: String, input: UserCourseClassInputModel): UserCourseClass {
        val userCourseClass = toUserCourseClass(username, input)
        return jdbi.withExtension<UserCourseClass, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
            it.addCourseToUser(userCourseClass)
        }
    }

    override fun addClassToUser(username: String, input: UserCourseClassInputModel): UserCourseClass {
        val userCourseClass = toUserCourseClass(username, input)
        return jdbi.withExtension<UserCourseClass, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
            it.addClassToUser(userCourseClass)
        }
    }

    override fun updateUser(input: UserInputModel): User {
        return  jdbi.inTransaction<User, Exception> {
            val userDao = it.attach(UserDAOJdbi::class.java)
            val current = userDao.getUser(input.username).get()
            val newUser = User(
                    username = current.username,
                    password = input.password ?: current.password,
                    familyName = input.familyName ?: current.familyName,
                    givenName = input.givenName ?: current.givenName,
                    personalEmail = input.personalEmail ?: current.personalEmail

            )
            userDao.updateUser(newUser)
        }
    }

    override fun deleteAllCoursesOfUser(username: String): Int =
            jdbi.withExtension<Int, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                it.deleteAllCoursesOfUser(username)
            }

    override fun deleteProgrammeOfUser(username: String): Int =
            jdbi.withExtension<Int, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                it.deleteProgramme(username)
            }

    override fun deleteSpecificCourseOfUser(username: String, courseId: Int): Int =
            jdbi.withExtension<Int, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                it.deleteSpecificCourseOfUser(username, courseId)
            }

    override fun reportUser(username: String, reportedBy: String, reportInput: UserReportInputModel): UserReport {
        val report = toUserReport(username, reportedBy, reportInput)
        return jdbi.withExtension<UserReport, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
            it.reportUser(report)
        }
    }

    override fun deleteAllClassesOfUser(username: String?): Int {
        TODO("How to Delete Classes ???")
    }

    override fun deleteSpecificClassOfUser(name: String?, classId: Int): Int {
        TODO("Delete classes on specific term ???") //To change body of created functions use File | Settings | File Templates.
    }

}