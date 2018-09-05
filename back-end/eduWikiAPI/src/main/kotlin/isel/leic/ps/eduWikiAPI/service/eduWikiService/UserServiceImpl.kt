package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole
import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserCourseClassInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.UserUpdateInputModel
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
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.*
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
import isel.leic.ps.eduWikiAPI.service.mailSender.EmailService
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
            CLASS_TABLE to { actionLog -> classDAO.getClassByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            CLASS_REPORT_TABLE to { actionLog -> classDAO.getClassReportByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            CLASS_STAGE_TABLE to { actionLog -> classDAO.getClassStageByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },

            HOMEWORK_TABLE to { actionLog -> homeworkDAO.getHomeworkByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            HOMEWORK_REPORT_TABLE to { actionLog -> homeworkDAO.getHomeworkReportByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            HOMEWORK_STAGE_TABLE to { actionLog -> homeworkDAO.getHomeworkStageByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },

            LECTURE_TABLE to { actionLog -> lectureDAO.getLectureByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            LECTURE_REPORT_TABLE to { actionLog -> lectureDAO.getLectureReportByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            LECTURE_STAGE_TABLE to { actionLog -> lectureDAO.getLectureStageByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },

            COURSE_TABLE to { actionLog -> courseDAO.getCourseByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            COURSE_REPORT_TABLE to { actionLog -> courseDAO.getCourseReportByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            COURSE_STAGE_TABLE to { actionLog -> courseDAO.getCourseStageByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },

            COURSE_CLASS_TABLE to { actionLog -> classDAO.getCourseClassByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            COURSE_CLASS_REPORT_TABLE to { actionLog -> classDAO.getCourseClassReportByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            COURSE_CLASS_STAGE_TABLE to { actionLog -> classDAO.getCourseClassStageByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },

            COURSE_PROGRAMME_TABLE to { actionLog -> courseDAO.getCourseProgrammeByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            COURSE_PROGRAMME_REPORT_TABLE to { actionLog -> courseDAO.getCourseProgrammeReportByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            COURSE_PROGRAMME_STAGE_TABLE to { actionLog -> courseDAO.getCourseProgrammeStageByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },

            WORK_ASSIGNMENT_TABLE to { actionLog -> workAssignmentDAO.getWorkAssignmentByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            WORK_ASSIGNMENT_REPORT_TABLE to { actionLog -> workAssignmentDAO.getWorkAssignmentReportByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            WORK_ASSIGNMENT_STAGE_TABLE to { actionLog -> workAssignmentDAO.getWorkAssignmentStageByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },

            EXAM_TABLE to { actionLog -> examDAO.getExamByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            EXAM_REPORT_TABLE to { actionLog -> examDAO.getExamStageByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            EXAM_STAGE_TABLE to { actionLog -> examDAO.getExamReportByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },

            PROGRAMME_TABLE to { actionLog -> programmeDAO.getProgrammeByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            PROGRAMME_REPORT_TABLE to { actionLog -> programmeDAO.getProgrammeReportByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            PROGRAMME_STAGE_TABLE to { actionLog -> programmeDAO.getProgrammeStageByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },

            ORGANIZATION_TABLE to { actionLog -> organizationDAO.getOrganization().map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } },
            ORGANIZATION_REPORT_TABLE to { actionLog -> organizationDAO.getOrganizationReportByLogId(actionLog.logId).map { it.toUserActionOutputModel(actionLog) }.orElseGet { actionLog.toUserActionOutputModel() } }
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
    lateinit var emailService: EmailService
    @Autowired
    lateinit var tenantDAO: TenantDAO
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun getAuthenticatedUser(principal: Principal): AuthUserOutputModel =
            toAuthUserOutputModel(
                    userDAO.getUser(principal.name).orElseThrow { NotFoundException("User not found", "Try another username") },
                    reputationDAO.getUserReputationDetails(principal.name).orElseThrow { NotFoundException("No reputation details found", "There was a problem while retrieving this user's reputation details, try again later") }
            )

    override fun getUser(username: String): UserOutputModel =
            toUserOutputModel(
                    userDAO.getUser(username)
                            .orElseThrow { NotFoundException("User not found", "Try another username") }
            )

    override fun saveUser(inputUser: UserInputModel): AuthUserOutputModel {
        // Get current tenant
        val tenantDetails = tenantDAO.getCurrentTenantDetails()//TODO implement getting current tenant
                .orElseThrow { BadRequestException("Bad tenant", "You must provide a valid tenant to register yourself in to") }
        // Check if email is valid
        if(! isEmailValid(inputUser.email) || ! inputUser.email.endsWith(tenantDetails.emailPattern))
            throw BadRequestException("invalid email", "Get an email from your organization")
        // Check if username was taken
        if(tenantDAO.getRegisteredUserByUsername(inputUser.username).isPresent)
            throw ConflictException("Username already picked", "Please try another username")
        // Check if email was taken
        if(userDAO.getUserByEmail(inputUser.email).isPresent)
            throw ConflictException("Email already taken", "Please choose another email")

        val user = userDAO.createUser(toUser(inputUser))
        val reputation = reputationDAO.createUserReputation(Reputation(
                role = ROLE_UNCONFIRMED.name,
                username = user.username
        ))
        // Send confirmation email
        val token = ValidationToken(token = UUID.randomUUID())
        tokenDAO.saveToken(token)
        val message =
                "Please follow this link to confirm your account " +
                        "" + "http://localhost:8080/users/" + user.username + "/confirm/" + token.token
        emailService.sendSimpleMessage(
                to = user.email,
                subject = "Verify your Eduwiki account",
                text = message
        )
        return toAuthUserOutputModel(user, reputation)
    }

    override fun confirmUser(username: String, token: UUID): String {
        // Check if user exists
        val user = userDAO.getUser(username)
                .orElseThrow { NotFoundException("User not found", "Try another username") }
        // Check if user already is confirmed
        if(user.confirmed)
            throw ForbiddenException("User already confirmed", "This account has already been confirmed")
        // Get validation token and curent timestamp
        val validationToken = tokenDAO.getToken(token)
                .orElseThrow { NotFoundException("Invalid token", "Verify if the token is the same as the one in the email we sent") }
        val currentTimestamp = Timestamp.valueOf(LocalDateTime.now())

        // Check if user confirmed his account in time
        tokenDAO.deleteToken(token)
        if(currentTimestamp.after(validationToken.date)) {
            userDAO.deleteUser(username)
            throw ExceededValidationException()
        }

        // Create user
        val role = reputationDAO.updateUserRole(username, ROLE_BEGINNER.minPoints, ROLE_BEGINNER.name)
        val changedUser = userDAO.confirmUser(user.username)
        return "User Confirmed!!"
    }

    override fun deleteUser(principal: Principal): Int =
            userDAO.deleteUser(principal.name)

    override fun getCoursesOfUser(principal: Principal): CourseCollectionOutputModel {
        val courses = userDAO.getCoursesOfUser(principal.name)
        return toCourseCollectionOutputModel(courses.map {
            toCourseOutputModel(it)
        })
    }

    override fun getClassesOfCOurseOfUser(principal: Principal): CourseClassCollectionOutputModel {
        val courseClasses = userDAO.getClassesOfUser(principal.name)
        val classes = courseClasses
                .filter { it.courseClassId != null }
                .map {
                    val courseClass = classDAO.getCourseClassFromId(it.courseClassId !!)
                    val course = courseDAO.getSpecificCourse(it.courseId).get()
                    val term = termDAO.getTerm(courseClass.termId).get()
                    toCourseClassOutputModel(course, classDAO.getSpecificClass(it.courseId).get(), courseClass, term)
                }
        return toCourseClassCollectionOutputModel(classes)
    }

    override fun getProgrammeOfUser(principal: Principal): ProgrammeOutputModel {
        val programme = userDAO.getProgrammeOfUser(principal.name)
                .orElseThrow { UnknownDataException("Something happened when accessing your programme", "Try Again Later") }
        return toProgrammeOutput(programme)
    }

    override fun addProgrammeToUSer(principal: Principal, input: UserProgrammeInputModel): ProgrammeOutputModel {
        val added = userDAO.addProgrammeToUser(principal.name, input.programmeId)
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

    override fun addCourseToUser(principal: Principal, input: UserCourseClassInputModel): UserCourseOutputModel {
        courseDAO.getSpecificCourse(input.courseId)
                .orElseThrow { NotFoundException("Course not found", "Please choose a valid course") }
        val userCourseClass = toUserCourseClass(principal.name, input)
        return toUserCourseOutputModel(userDAO.addCourseToUser(userCourseClass))
    }

    override fun addClassToUser(principal: Principal, input: UserCourseClassInputModel): UserCourseClassOutputModel {
        val course = courseDAO.getSpecificCourse(input.courseId)
                .orElseThrow { NotFoundException("Course not found", "Please choose a valid course") }

        val userCourseClass = toUserCourseClass(principal.name, input)
        return toUserCourseClassOutputModel(userDAO.addClassToUser(userCourseClass))
    }

    override fun updateUser(input: UserUpdateInputModel, principal: Principal): AuthUserOutputModel {
        val current = userDAO.getUser(principal.name)
                .orElseThrow { NotFoundException("User not found", "Try another username") }
        val newUser = User(
                username = current.username,
                familyName = input.familyName ?: current.familyName,
                givenName = input.givenName ?: current.givenName
        )
        return toAuthUserOutputModel(
                userDAO.updateUser(newUser),
                reputationDAO.getUserReputationDetails(principal.name)
                        .orElseThrow { NotFoundException("No reputation details found", "There was a problem while retrieving this user's reputation details, try again later") }
        )
    }

    override fun deleteAllCoursesOfUser(principal: Principal): Int =
            userDAO.deleteAllCoursesOfUser(principal.name)

    override fun deleteProgrammeOfUser(principal: Principal): Int =
            userDAO.deleteProgramme(principal.name)

    override fun deleteSpecificCourseOfUser(principal: Principal, courseId: Int): Int =
            userDAO.deleteSpecificCourseOfUser(principal.name, courseId)


    override fun reportUser(username: String, reportInput: UserReportInputModel, principal: Principal): UserReportOutputModel {
        // Get reported user
        val reported = userDAO.getUser(username)
                .orElseThrow { NotFoundException("User not found", "The reported user does not exist") }
        // Check if user was already banned
        if(reported.locked)
            throw ForbiddenException("Can't report this user", "The user was already banned")

        // Report user
        val report = userDAO.reportUser(toUserReport(reported.username, principal.name, reportInput))

        // Notify admins that an report has occurred
        val admins = userDAO.getUsersByRole(ReputationRole.ROLE_ADMIN.name)
        admins.forEach {
            emailService.sendUserReportedEmailToAdmin(report, it)
        }
        return toUserReportOutput(userDAO.reportUser(report))
    }

    override fun deleteAllClassesOfUser(principal: Principal): Int =
            userDAO.deleteAllClassesOfUser(principal.name)

    override fun deleteSpecificClassOfUser(principal: Principal, courseClassId: Int): Int =
            userDAO.deleteSpecificClassOfUser(principal.name, courseClassId)

    override fun approveReport(username: String, reportId: Int, principal: Principal): UserReportOutputModel {
        // Get reported user and the report
        val reportedUser = userDAO.getUser(username)
                .orElseThrow { NotFoundException("User not found", "The reported user does not exist") }
        val report = userDAO.getSpecificReportOfUser(username, reportId)
                .orElseThrow { NotFoundException("Report not found", "The reported does not exist") }

        // Check if user is already banned
        if(reportedUser.locked)
            throw ForbiddenException("User already banned", "No use in reporting an already banned user")

        // Change user status to locked and delete the reports
        userDAO.lockUser(reportedUser.username)
        userDAO.deleteAllReportsOnUser(reportedUser.username)

        // Notify banned user
        emailService.sendUserBannedEmail(reportedUser, report)
        return toUserReportOutput(report)
    }

    override fun getAllReportsOfUser(username: String): UserReportCollectionOutputModel {
        val user = userDAO.getUser(username)
                .orElseThrow { NotFoundException("User not found", "There is not user with the username $username in this tenant") }
        return toUserReportCollectionOutputModel(userDAO.getAllReportsOfUser(user.username))
    }

    override fun getSpecificReportOfUser(username: String, reportId: Int): UserReportOutputModel =
            toUserReportOutput(
                    userDAO.getSpecificReportOfUser(username, reportId)
                            .orElseThrow { NotFoundException("Report not found", "The reported does not exist") }
            )

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

    private fun getActionsOnFollowedProgramme(programme: Programme): MutableList<UserActionOutputModel> {
        val res: MutableList<UserActionOutputModel> = mutableListOf()

        res.addAll(courseDAO.getAllCoursesOnSpecificProgramme(programme.programmeId)
                .flatMap { courseProg ->
                    reputationDAO.getActionLogsByResource(COURSE_PROGRAMME_TABLE, courseProg.logId)
                            .map { courseProg.toUserActionOutputModel(it) }
                }
        )
        res.addAll(courseDAO.getAllCourseStageEntriesOfSpecificProgramme(programme.programmeId)
                .flatMap { courseProg ->
                    reputationDAO.getActionLogsByResource(COURSE_PROGRAMME_STAGE_TABLE, courseProg.logId)
                            .map { courseProg.toUserActionOutputModel(it) }
                }
        )
        res.addAll(programmeDAO.getAllReportsOfSpecificProgramme(programme.programmeId)
                .flatMap { prog ->
                    reputationDAO.getActionLogsByResource(COURSE_PROGRAMME_STAGE_TABLE, prog.logId)
                            .map { prog.toUserActionOutputModel(it) }
                }
        )
        return res
    }

    private fun getActionsOnFollowedClasses(courseClasses: MutableList<UserCourseClass>): MutableList<UserActionOutputModel> {
        val res: MutableList<UserActionOutputModel> = mutableListOf()
        courseClasses.forEach { courseClass ->
            res.addAll(lectureDAO.getAllLecturesFromCourseInClass(courseClass.courseClassId !!)
                    .flatMap { lecture ->
                        reputationDAO.getActionLogsByResource(LECTURE_TABLE, lecture.logId)
                                .map { lecture.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(lectureDAO.getAllStagedLecturesOfCourseInClass(courseClass.courseClassId)
                    .flatMap { lecture ->
                        reputationDAO.getActionLogsByResource(LECTURE_STAGE_TABLE, lecture.logId)
                                .map { lecture.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(homeworkDAO.getAllHomeworksFromCourseInClass(courseClass.courseClassId)
                    .flatMap { homework ->
                        reputationDAO.getActionLogsByResource(HOMEWORK_TABLE, homework.logId)
                                .map { homework.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(homeworkDAO.getAllStagedHomeworksOfCourseInClass(courseClass.courseClassId)
                    .flatMap { homework ->
                        reputationDAO.getActionLogsByResource(HOMEWORK_STAGE_TABLE, homework.logId)
                                .map { homework.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(classDAO.getAllReportsOfCourseInClass(courseClass.courseClassId)
                    .flatMap { courseClassRep ->
                        reputationDAO.getActionLogsByResource(HOMEWORK_STAGE_TABLE, courseClassRep.logId)
                                .map { courseClassRep.toUserActionOutputModel(it) }
                    }
            )
        }
        return res
    }

    private fun getActionsOnFollowedCourses(followedCourses: MutableList<Course>): MutableList<UserActionOutputModel> {
        val res: MutableList<UserActionOutputModel> = mutableListOf()
        followedCourses.forEach { course ->
            res.addAll(workAssignmentDAO.getAllWorkAssignmentsOfSpecificCourse(course.courseId)
                    .flatMap { wrk ->
                        reputationDAO.getActionLogsByResource(WORK_ASSIGNMENT_TABLE, wrk.logId)
                                .map { wrk.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(workAssignmentDAO.getAllStagedWorkAssignmentOnSpecificCourse(course.courseId)
                    .flatMap { wrk ->
                        reputationDAO.getActionLogsByResource(WORK_ASSIGNMENT_STAGE_TABLE, wrk.logId)
                                .map { wrk.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(examDAO.getAllExamsFromSpecificCourse(course.courseId)
                    .flatMap { exam ->
                        reputationDAO.getActionLogsByResource(EXAM_TABLE, exam.logId)
                                .map { exam.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(examDAO.getAllStagedExamOnSpecificCourse(course.courseId)
                    .flatMap { exam ->
                        reputationDAO.getActionLogsByResource(EXAM_STAGE_TABLE, exam.logId)
                                .map { exam.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(courseDAO.getAllReportsOnCourse(course.courseId)
                    .flatMap { report ->
                        reputationDAO.getActionLogsByResource(COURSE_REPORT_TABLE, report.logId)
                                .map { report.toUserActionOutputModel(it) }
                    }
            )
            res.addAll(reputationDAO.getActionLogsByResource(COURSE_TABLE, course.logId)
                    .map { course.toUserActionOutputModel(it) }
            )
        }
        return res
    }

    private fun noActionException(): Nothing = throw UnknownDataException("An error occured while loading the actions", "Try again alter or contact the EduWiki team")

}
