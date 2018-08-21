package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ClassCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.UserReportOutputModel
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.UserService
import isel.leic.ps.eduWikiAPI.eventListeners.events.OnRegistrationEvent
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.BadRequestException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.ExceededValidationException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.UnknownDataException
import isel.leic.ps.eduWikiAPI.repository.interfaces.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.regex.Pattern
import java.util.*

@Transactional
@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var eventPublisher: ApplicationEventPublisher
    @Autowired
    lateinit var reputationDAO: ReputationDAO
    @Autowired
    lateinit var userDAO: UserDAO
    @Autowired
    lateinit var programmeDAO: ProgrammeDAO
    @Autowired
    lateinit var classDAO: ClassDAO
    @Autowired
    lateinit var courseDAO: CourseDAO
    @Autowired
    lateinit var termDAO: TermDAO
    @Autowired
    lateinit var tokenDAO: TokenDAO
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun getAuthenticatedUser(username: String): AuthUserOutputModel =
            toAuthUserOutputModel(userDAO.getUser(username).orElseThrow { NotFoundException("User not found", "Try another username") })

    override fun getUser(username: String): UserOutputModel =
            toUserOutputModel(
                    userDAO.getUser(username)
                            .orElseThrow { NotFoundException("User not found", "Try another username") }
            )

    override fun saveUser(inputUser: UserInputModel): AuthUserOutputModel {
        if(!isEmailValid(inputUser.organizationEmail))
            throw BadRequestException("invalid organization email", "Get an actual organization email")

        val user1 = userDAO.createUser(toUser(inputUser))
        reputationDAO.createUserReputation(Reputation(
                reputationPoints = 0,
                reputationRole = ReputationRole.ROLE_UNCONFIRMED.name,
                username = user1.username
        ))
        eventPublisher.publishEvent(OnRegistrationEvent(user1))
        return toAuthUserOutputModel(user1)
    }

    override fun confirmUser(username: String, token: UUID): AuthUserOutputModel {
            val validationToken = tokenDAO.getToken(token)
                    .orElseThrow { NotFoundException("Invalid token", "Verify if the token is the same as the one in the email we sent") }
            val currentTimestamp = Timestamp.valueOf(LocalDateTime.now())
            tokenDAO.deleteToken(token)
            if(currentTimestamp.after(validationToken.date)) {
                userDAO.deleteUser(username)
                throw ExceededValidationException()
            }
            val role = ReputationRole.ROLE_BEGINNER
            reputationDAO.updateUserRole(username, role.minPoints, role.name)
            val userChanged = userDAO.confirmUser(username)
            return toAuthUserOutputModel(userChanged)
    }

    override fun deleteUser(username: String): Int =
            userDAO.deleteUser(username)

    override fun getCoursesOfUser(username: String): CourseCollectionOutputModel {
                val coursesIds = userDAO.getCoursesOfUser(username)
                val courses = coursesIds.map {
                    toCourseOutputModel(courseDAO.getSpecificCourse(it).get())
                }
                return toCourseCollectionOutputModel(courses)
            }

    override fun getClassesOfUser(username: String): ClassCollectionOutputModel {
                val courseClasses = userDAO.getClassesOfUser(username)
                val classes = courseClasses.map {
                    val courseClass = classDAO.getCourseClassFromId(it.courseClassId)
                    val term = termDAO.getTerm(courseClass.termId).get()
                    toClassOutputModel(classDAO.getSpecificClass(it.courseId).get(), term)
                }
                return toClassCollectionOutputModel(classes)
            }

    override fun getProgrammeOfUser(username: String): ProgrammeOutputModel {
                val programmeId = userDAO.getProgrammeOfUser(username)
                return toProgrammeOutput(
                        programmeDAO.getSpecificProgramme(programmeId)
                                .orElseThrow { UnknownDataException("Something happened when accessing your programme", "Try Again Later") }
                )
            }

    override fun addProgrammeToUSer(username: String, input: UserProgrammeInputModel): ProgrammeOutputModel {
                val added = userDAO.addProgrammeToUser(username, input.programmeId)
                return toProgrammeOutput(
                        programmeDAO.getSpecificProgramme(added.programmeId)
                                .orElseThrow {
                                    UnknownDataException(
                                            title = "Can't add programme, maybe you are already following other programme",
                                            detail = "Check if you're following other programme, or try again later"
                                    )
                                }
                )
            }

    override fun addCourseToUser(username: String, input: UserCourseClassInputModel): UserCourseOutputModel {
        val userCourseClass = toUserCourseClass(username, input)
        return toUserCourseOutputModel(userDAO.addCourseToUser(userCourseClass))
    }

    override fun addClassToUser(username: String, input: UserCourseClassInputModel): UserCourseClassOutputModel {
        val userCourseClass = toUserCourseClass(username, input)
        return toUserCourseClassOutputModel(userDAO.addClassToUser(userCourseClass))
    }

    //TODO Support changes of password ???????
    override fun updateUser(input: UserInputModel): AuthUserOutputModel {
            val current = userDAO.getUser(input.username).get()
            val newUser = User(
                    username = current.username,
                    password = input.password ?: current.password,
                    familyName = input.familyName ?: current.familyName,
                    givenName = input.givenName ?: current.givenName,
                    personalEmail = input.personalEmail ?: current.personalEmail

            )
            return toAuthUserOutputModel(userDAO.updateUser(newUser))
        }

    override fun deleteAllCoursesOfUser(username: String): Int =
            userDAO.deleteAllCoursesOfUser(username)

    override fun deleteProgrammeOfUser(username: String): Int =
            userDAO.deleteProgramme(username)

    override fun deleteSpecificCourseOfUser(username: String, courseId: Int): Int =
            userDAO.deleteSpecificCourseOfUser(username, courseId)


    override fun reportUser(username: String, reportedBy: String, reportInput: UserReportInputModel): UserReportOutputModel {
        val report = toUserReport(username, reportedBy, reportInput)
        return toUserReportOutput(userDAO.reportUser(report))
    }

    override fun deleteAllClassesOfUser(username: String): Int =
            userDAO.deleteAllClassesOfUser(username)

    override fun deleteSpecificClassOfUser(username: String, courseClassId: Int): Int =
            userDAO.deleteSpecificClassOfUser(username, courseClassId)

    override fun approveReport(username: String, reportId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllReportsOfUser(username: String): List<UserReportOutputModel> =
            userDAO.getAllReportsOfUser(username).map { toUserReportOutput(it) }

    override fun getSpecificReportOfUser(username: String, reportId: Int): UserReportOutputModel =
            toUserReportOutput(userDAO.getSpecficReportOfUser(username, reportId))


    override fun deleteReportOnUser(username: String, reportId: Int): Int =
            userDAO.deleteSpecificReportOfUser(username, reportId)


    fun isEmailValid(email: String?): Boolean {
        return email != null && Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

}