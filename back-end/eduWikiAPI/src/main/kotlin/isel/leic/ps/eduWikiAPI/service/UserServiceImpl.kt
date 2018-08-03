package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.service.interfaces.UserService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.UserReportOutputModel
import isel.leic.ps.eduWikiAPI.exceptions.BadRequestException
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.exceptions.UnknownDataException
import isel.leic.ps.eduWikiAPI.repository.*
import java.util.regex.Pattern

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAuthenticatedUser(username: String): AuthUserOutputModel =
            jdbi.withExtension<AuthUserOutputModel, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                toAuthUserOutputModel(it.getUser(username).get())
            }

    override fun getUser(username: String): UserOutputModel =
            jdbi.withExtension<UserOutputModel, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                toUserOutputModel(
                        it.getUser(username)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "Can't find User",
                                            action = "Check username again or try again later"
                                    )
                                }
                )
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

    override fun saveUser(inputUser: UserInputModel): AuthUserOutputModel {
        val user = toUser(inputUser)
        val valid = isEmailValid(user.organizationEmail)
        if (!valid) {
            throw BadRequestException(msg = "invalid organization email", action = "Get an actual organization email")
        }
        return jdbi.inTransaction<AuthUserOutputModel, Exception> {
            val userDao = it.attach(UserDAOJdbi::class.java)
            val repDao = it.attach(ReputationDAOJdbi::class.java)
            val user1 = userDao.createUser(user)
            val reputation = Reputation(
                    reputationPoints = 1,
                    reputationRole = "ROLE_BEGINNER",
                    username = user1.username
            )
            repDao.saveNewUser(reputation)
            toAuthUserOutputModel(user1)
        }
    }

    override fun deleteUser(username: String): Int =
            jdbi.withExtension<Int, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                it.deleteUser(username)
            }

    override fun getCoursesOfUser(username: String): List<CourseOutputModel> =
            jdbi.inTransaction<List<CourseOutputModel>, Exception> {
                val userDao = it.attach(UserDAOJdbi::class.java)
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val coursesIds = userDao.getCoursesOfUser(username)
                coursesIds.map {
                    toCourseOutputModel(courseDAO.getSpecificCourse(it).get())
                }
            }

    override fun getClassesOfUser(username: String): List<ClassOutputModel> =
            jdbi.inTransaction<List<ClassOutputModel>, NotFoundException> {
                val userDao = it.attach(UserDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAO = it.attach(TermDAOJdbi::class.java)
                val courseClasses = userDao.getClassesOfUser(username)
                courseClasses.map {
                    val courseClass = classDAO.getCourseCLassFromId(it.courseClassId)
                    val term = termDAO.getTerm(courseClass.termId).get()
                    toClassOutputModel(classDAO.getSpecificClass(it.courseId).get(), term)
                }
            }

    override fun getProgrammeOfUser(username: String): ProgrammeOutputModel =
            jdbi.inTransaction<ProgrammeOutputModel, Exception> {
                val userDao = it.attach(UserDAOJdbi::class.java)
                val programmeDAO = it.attach(ProgrammeDAOJdbi::class.java)
                val programmeId = userDao.getProgrammeOfUser(username)
                toProgrammeOutput(
                        programmeDAO.getSpecificProgramme(programmeId)
                                .orElseThrow {
                                    UnknownDataException(
                                            msg = "Something happened when accessing your programme",
                                            action = "Try Again Later"
                                    )
                                }
                )
            }

    override fun addProgrammeToUSer(username: String, input: UserProgrammeInputModel): ProgrammeOutputModel =
            jdbi.inTransaction<ProgrammeOutputModel, Exception> {
                val userDAOJdbi = it.attach(UserDAOJdbi::class.java)
                val programmeDAOJdbi = it.attach(ProgrammeDAOJdbi::class.java)
                val added = userDAOJdbi.addProgrammeToUser(username, input.programmeId)
                toProgrammeOutput(
                        programmeDAOJdbi.getSpecificProgramme(added.programmeId)
                                .orElseThrow {
                                    UnknownDataException(
                                            msg = "Can't add programme, maybe you are already following other programme",
                                            action = "Check if you're following other programme, or try again later"

                                    )
                                }
                )
            }

    override fun addCourseToUser(username: String, input: UserCourseClassInputModel): UserCourseOutputModel {
        val userCourseClass = toUserCourseClass(username, input)
        return jdbi.withExtension<UserCourseOutputModel, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
            toUserCourseOutputModel(it.addCourseToUser(userCourseClass))
        }
    }

    override fun addClassToUser(username: String, input: UserCourseClassInputModel): UserCourseClassOutputModel {
        val userCourseClass = toUserCourseClass(username, input)
        return jdbi.withExtension<UserCourseClassOutputModel, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
            toUserCourseClassOutputModel(it.addClassToUser(userCourseClass))
        }
    }

    //TODO Support changes of password ???????
    override fun updateUser(input: UserInputModel): AuthUserOutputModel {
        return jdbi.inTransaction<AuthUserOutputModel, Exception> {
            val userDao = it.attach(UserDAOJdbi::class.java)
            val current = userDao.getUser(input.username).get()
            val newUser = User(
                    username = current.username,
                    password = input.password ?: current.password,
                    familyName = input.familyName ?: current.familyName,
                    givenName = input.givenName ?: current.givenName,
                    personalEmail = input.personalEmail ?: current.personalEmail

            )
            toAuthUserOutputModel(userDao.updateUser(newUser))
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

    override fun reportUser(username: String, reportedBy: String, reportInput: UserReportInputModel): UserReportOutputModel {
        val report = toUserReport(username, reportedBy, reportInput)
        return jdbi.withExtension<UserReportOutputModel, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
            toUserReportOutput(it.reportUser(report))
        }
    }

    override fun deleteAllClassesOfUser(username: String): Int =
        jdbi.withExtension<Int, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
            it.deleteAllClassesOfUser(username)
        }

    override fun deleteSpecificClassOfUser(username: String, courseClassId: Int): Int =
        jdbi.withExtension<Int, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
            it.deleteSpecificClassOfUser(username, courseClassId)
        }

    override fun approveReport(username: String, reportId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllReportsOfUser(username: String): List<UserReportOutputModel> =
            jdbi.withExtension<List<UserReportOutputModel>, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                it.getAllReportsOfUser(username).map { toUserReportOutput(it) }
            }

    override fun getSpecificReportOfUser(username: String, reportId: Int): UserReportOutputModel =
            jdbi.withExtension<UserReportOutputModel, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                toUserReportOutput(it.getSpecficReportOfUser(username, reportId))
            }

    override fun deleteReportOnUser(username: String, reportId: Int): Int =
            jdbi.withExtension<Int, UserDAOJdbi, Exception>(UserDAOJdbi::class.java) {
                it.deleteSpecificReportOfUser(username, reportId)
            }

}