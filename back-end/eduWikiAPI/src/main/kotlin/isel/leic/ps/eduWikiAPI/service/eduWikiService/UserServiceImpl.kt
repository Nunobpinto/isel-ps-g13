package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.UserReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.UserActionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.UserReputationCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.UserReportOutputModel
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.UserService
import isel.leic.ps.eduWikiAPI.eventListeners.events.OnRegistrationEvent
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.BadRequestException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.ExceededValidationException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.UnknownDataException
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_PROGRAMME_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_TABLE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_TABLE
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_TABLE
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_TABLE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl.Companion.PROGRAMME_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_TABLE
import isel.leic.ps.eduWikiAPI.repository.interfaces.*
import isel.leic.ps.eduWikiAPI.utils.isEmailValid
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import java.util.Objects.*

@Transactional
@Service
class UserServiceImpl : UserService {

    val entityToAction = mapOf<String, (ActionLog) -> UserActionOutputModel>(
            CLASS_TABLE to { actionLog -> classDAO.getClassByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            CLASS_REPORT_TABLE to { actionLog -> classDAO.getClassReportByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            CLASS_STAGE_TABLE to { actionLog -> classDAO.getClassStageByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },

            HOMEWORK_TABLE to { actionLog -> homeworkDAO.getHomeworkByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            HOMEWORK_REPORT_TABLE to { actionLog -> homeworkDAO.getHomeworkReportByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            HOMEWORK_STAGE_TABLE to { actionLog -> homeworkDAO.getHomeworkStageByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },

            LECTURE_TABLE to { actionLog -> lectureDAO.getLectureByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            LECTURE_REPORT_TABLE to { actionLog -> lectureDAO.getLectureReportByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            LECTURE_STAGE_TABLE to { actionLog -> lectureDAO.getLectureStageByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },

            COURSE_TABLE to { actionLog -> courseDAO.getCourseByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            COURSE_REPORT_TABLE to { actionLog -> courseDAO.getCourseReportByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            COURSE_STAGE_TABLE to { actionLog -> courseDAO.getCourseStageByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },

            COURSE_CLASS_TABLE to { actionLog -> classDAO.getCourseClassByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            COURSE_CLASS_REPORT_TABLE to { actionLog -> classDAO.getCourseClassReportByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            COURSE_CLASS_STAGE_TABLE to { actionLog -> classDAO.getCourseClassStageByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },

            COURSE_PROGRAMME_TABLE to { actionLog -> courseDAO.getCourseProgrammeByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            COURSE_PROGRAMME_REPORT_TABLE to { actionLog -> courseDAO.getCourseProgrammeReportByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            COURSE_PROGRAMME_STAGE_TABLE to { actionLog -> courseDAO.getCourseProgrammeStageByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },

            WORK_ASSIGNMENT_TABLE to { actionLog -> workAssignmentDAO.getWorkAssignmentByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            WORK_ASSIGNMENT_REPORT_TABLE to { actionLog -> workAssignmentDAO.getWorkAssignmentReportByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            WORK_ASSIGNMENT_STAGE_TABLE to { actionLog -> workAssignmentDAO.getWorkAssignmentStageByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },

            EXAM_TABLE to { actionLog -> examDAO.getExamByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            EXAM_REPORT_TABLE to { actionLog -> examDAO.getExamStageByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            EXAM_STAGE_TABLE to { actionLog -> examDAO.getExamReportByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },

            PROGRAMME_TABLE to { actionLog -> programmeDAO.getProgrammeByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            PROGRAMME_REPORT_TABLE to { actionLog -> programmeDAO.getProgrammeReportByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            PROGRAMME_STAGE_TABLE to { actionLog -> programmeDAO.getProgrammeStageByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },

            ORGANIZATION_TABLE to { actionLog -> organizationDAO.getOrganization().orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) },
            ORGANIZATION_REPORT_TABLE to { actionLog -> organizationDAO.getOrganizationReportByLogId(actionLog.logId).orElseThrow { noActionException() }.toUserActionOutputModel(actionLog) }
    )

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
    lateinit var homeworkDAO: HomeworkDAO
    @Autowired
    lateinit var lectureDAO: LectureDAO
    @Autowired
    lateinit var workAssignmentDAO: WorkAssignmentDAO
    @Autowired
    lateinit var examDAO: ExamDAO
    @Autowired
    lateinit var organizationDAO: OrganizationDAO
    @Autowired
    lateinit var tokenDAO: TokenDAO
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun getAuthenticatedUser(username: String): AuthUserOutputModel =
            toAuthUserOutputModel(
                    userDAO.getUser(username).orElseThrow { NotFoundException("User not found", "Try another username") },
                    reputationDAO.getUserReputationDetails(username).orElseThrow { NotFoundException("No reputation details found", "There was a problem while retrieving this user's reputation details, try again later") }
            )

    override fun getUser(username: String): UserOutputModel =
            toUserOutputModel(userDAO.getUser(username).orElseThrow { NotFoundException("User not found", "Try another username") })

    override fun saveUser(inputUser: UserInputModel): AuthUserOutputModel {
        if(! isEmailValid(inputUser.organizationEmail))
            throw BadRequestException("invalid organization email", "Get an actual organization email")

        val user = userDAO.createUser(toUser(inputUser))
        val reputation = reputationDAO.createUserReputation(Reputation(
                role = ROLE_UNCONFIRMED.name,
                username = user.username
        ))
        eventPublisher.publishEvent(OnRegistrationEvent(user))
        return toAuthUserOutputModel(user, reputation)
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

        val role = reputationDAO.updateUserRole(username, ROLE_BEGINNER.minPoints, ROLE_BEGINNER.name)
        val userChanged = userDAO.confirmUser(username)
        return toAuthUserOutputModel(userChanged, role)
    }

    override fun deleteUser(username: String): Int =
            userDAO.deleteUser(username)

    override fun getCoursesOfUser(username: String): CourseCollectionOutputModel {
        val courses = userDAO.getCoursesOfUser(username)
        return toCourseCollectionOutputModel(courses.map {
            toCourseOutputModel(it)
        })
    }

    override fun getClassesOfCOurseOfUser(username: String): CourseClassCollectionOutputModel {
        val courseClasses = userDAO.getClassesOfUser(username)
        val classes = courseClasses.map {
            val courseClass = classDAO.getCourseClassFromId(it.courseClassId)
            val course = courseDAO.getSpecificCourse(it.courseId).get()
            val term = termDAO.getTerm(courseClass.termId).get()
            toCourseClassOutputModel(course, classDAO.getSpecificClass(it.courseId).get(), courseClass, term)
        }
        return toCourseClassCollectionOutputModel(classes)
    }

    override fun getProgrammeOfUser(username: String): ProgrammeOutputModel {
        val programme = userDAO.getProgrammeOfUser(username)
                .orElseThrow { UnknownDataException("Something happened when accessing your programme", "Try Again Later") }
        return toProgrammeOutput(programme)
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
        val current = userDAO.getUser(input.username)
                .orElseThrow { NotFoundException("User not found", "Try another username") }
        val newUser = User(
                username = current.username,
                password = input.password ?: current.password,
                familyName = input.familyName ?: current.familyName,
                givenName = input.givenName ?: current.givenName,
                personalEmail = input.personalEmail ?: current.personalEmail

        )
        return toAuthUserOutputModel(
                userDAO.updateUser(newUser),
                reputationDAO.getUserReputationDetails(input.username)
                        .orElseThrow { NotFoundException("No reputation details found", "There was a problem while retrieving this user's reputation details, try again later") }
        )
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

    override fun getUserActions(principal: Principal): UserActionCollectionOutputModel {
        val user = userDAO.getUser(principal.name)
                .orElseThrow { NotFoundException("User not found", "There is not user with the username ${principal.name} in this tenant") }

        return UserActionCollectionOutputModel(
                username = user.username,
                actions = reputationDAO.getActionLogsByUser(user.username)
                        .map { entityToAction[it.entity]?.invoke(it) ?: noActionException() }
        )
    }

    override fun getUserFeed(principal: Principal): UserActionCollectionOutputModel {
        val user = userDAO.getUser(principal.name)
                .orElseThrow { NotFoundException("User not found", "There is not user with the username ${principal.name} in this tenant") }

        // Get everything followed by user
        val programme = userDAO.getProgrammeOfUser(user.username)
        val followedCourses = userDAO.getCoursesOfUser(user.username).toMutableList()
        val followedClasses = userDAO.getClassesOfUser(user.username)
                .filter { nonNull(it.courseClassId) }
                .toMutableList()

        // Extract actions from all entities
        val actions: MutableList<UserActionOutputModel> = mutableListOf()
        actions.addAll(getActionsOnFollowedCourses(followedCourses))
        actions.addAll(getActionsOnFollowedClasses(followedClasses))
        actions.addAll(if(programme.isPresent) getActionsOnFollowedProgramme(programme.get()) else mutableListOf())
        return UserActionCollectionOutputModel(
                username = user.username,
                actions = actions.sortedBy { it.timestamp }
        )
    }

    private fun getActionsOnFollowedProgramme(programme: Programme): MutableList<UserActionOutputModel> {
        val res: MutableList<UserActionOutputModel> = mutableListOf()

        res.addAll(courseDAO.getAllCoursesOnSpecificProgramme(programme.programmeId)
                .flatMap { courseProg -> reputationDAO.getActionLogsByResource(COURSE_PROGRAMME_TABLE, courseProg.logId)
                        .map { courseProg.toUserActionOutputModel(it) }
                }
        )
        res.addAll(courseDAO.getAllCourseStageEntriesOfSpecificProgramme(programme.programmeId)
                .flatMap { courseProg -> reputationDAO.getActionLogsByResource(COURSE_PROGRAMME_STAGE_TABLE, courseProg.logId)
                        .map { courseProg.toUserActionOutputModel(it) }
                }
        )
        res.addAll(programmeDAO.getAllReportsOfSpecificProgramme(programme.programmeId)
                .flatMap { prog -> reputationDAO.getActionLogsByResource(COURSE_PROGRAMME_STAGE_TABLE, prog.logId)
                        .map { prog.toUserActionOutputModel(it) }
                }
        )
        return res
    }

    private fun getActionsOnFollowedClasses(courseClasses: MutableList<UserCourseClass>): MutableList<UserActionOutputModel> {
        val res: MutableList<UserActionOutputModel> = mutableListOf()
        courseClasses.forEach { courseClass ->
            res.addAll(lectureDAO.getAllLecturesFromCourseInClass(courseClass.courseClassId!!)
                    .flatMap { lecture -> reputationDAO.getActionLogsByResource(LECTURE_TABLE, lecture.logId)
                            .map { lecture.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(lectureDAO.getAllStagedLecturesOfCourseInClass(courseClass.courseClassId)
                    .flatMap { lecture -> reputationDAO.getActionLogsByResource(LECTURE_STAGE_TABLE, lecture.logId)
                            .map { lecture.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(homeworkDAO.getAllHomeworksFromCourseInClass(courseClass.courseClassId)
                    .flatMap { homework -> reputationDAO.getActionLogsByResource(HOMEWORK_TABLE, homework.logId)
                            .map { homework.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(homeworkDAO.getAllStagedHomeworksOfCourseInClass(courseClass.courseClassId)
                    .flatMap { homework -> reputationDAO.getActionLogsByResource(HOMEWORK_STAGE_TABLE, homework.logId)
                            .map { homework.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(classDAO.getAllReportsOfCourseInClass(courseClass.courseClassId)
                    .flatMap { courseClassRep -> reputationDAO.getActionLogsByResource(HOMEWORK_STAGE_TABLE, courseClassRep.logId)
                            .map { courseClassRep.toUserActionOutputModel(it) }
                    }
            )
        }
        return res
    }

    private fun getActionsOnFollowedCourses(followedCourses: MutableList<Course>): MutableList<UserActionOutputModel> {
        val res: MutableList<UserActionOutputModel> = mutableListOf()
        followedCourses.forEach {course ->
            res.addAll(workAssignmentDAO.getAllWorkAssignmentsOfSpecificCourse(course.courseId)
                    .flatMap { wrk -> reputationDAO.getActionLogsByResource(WORK_ASSIGNMENT_TABLE, wrk.logId)
                            .map { wrk.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(workAssignmentDAO.getAllStagedWorkAssignmentOnSpecificCourse(course.courseId)
                    .flatMap { wrk -> reputationDAO.getActionLogsByResource(WORK_ASSIGNMENT_STAGE_TABLE, wrk.logId)
                            .map { wrk.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(examDAO.getAllExamsFromSpecificCourse(course.courseId)
                    .flatMap { exam -> reputationDAO.getActionLogsByResource(EXAM_TABLE, exam.logId)
                            .map { exam.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(examDAO.getAllStagedExamOnSpecificCourse(course.courseId)
                    .flatMap { exam -> reputationDAO.getActionLogsByResource(EXAM_STAGE_TABLE, exam.logId)
                            .map { exam.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(courseDAO.getAllReportsOnCourse(course.courseId)
                    .flatMap { report -> reputationDAO.getActionLogsByResource(COURSE_REPORT_TABLE, report.logId)
                            .map { report.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(reputationDAO.getActionLogsByResource(COURSE_TABLE, course.logId)
                    .map { course.toUserActionOutputModel(it) }
            )
        }
        return res
    }

    override fun getUserReputation(principal: Principal): UserReputationCollectionOutputModel {
        val user = userDAO.getUser(principal.name)
                .orElseThrow { NotFoundException("User not found", "There is not user with the username ${principal.name} in this tenant") }

        val reputationLogs = reputationDAO
                .getReputationLogsByUser(user.username)
                .map {
                    val actionLog = reputationDAO.getActionLogById(it.repActionId)
                            .orElseThrow { noActionException() }
                    UserReputationOutputModel(
                            givenBy = it.givenBy,
                            pointsGiven = it.points,
                            action = entityToAction[actionLog.entity]?.invoke(actionLog) ?: noActionException()
                    )
                }
        return UserReputationCollectionOutputModel(reputationLogs)

    }

    private fun noActionException(): Nothing = throw UnknownDataException("An error occured while loading the actions", "Try again alter or contact the EduWiki team")

}
