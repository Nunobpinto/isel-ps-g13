package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
import isel.leic.ps.eduWikiAPI.domain.outputModel.ClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.CourseClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.HomeworkOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.LectureOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ClassCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseClassCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.HomeworkCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.LectureCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.ClassReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.CourseClassReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.HomeworkReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.LectureReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.ClassStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.CourseClassStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.HomeworkStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.LectureStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.ClassVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.HomeworkVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.LectureVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ClassReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseClassReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.HomeworkReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.LectureReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.ClassStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseClassStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.HomeworkStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.LectureStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ClassVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.HomeworkVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.LectureVersionOutputModel
import isel.leic.ps.eduWikiAPI.eventListeners.events.*
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.exceptions.UnknownDataException
import isel.leic.ps.eduWikiAPI.repository.*
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_TABLE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOJdbi.Companion.HOMEWORK_TABLE
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.LectureDAOJdbi.Companion.LECTURE_TABLE
import isel.leic.ps.eduWikiAPI.service.interfaces.ClassService
import isel.leic.ps.eduWikiAPI.service.interfaces.ResourceStorageService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@Service
class ClassServiceImpl : ClassService {

    @Autowired
    lateinit var jdbi: Jdbi
    @Autowired
    lateinit var storageService: ResourceStorageService
    @Autowired
    lateinit var publisher: ApplicationEventPublisher

    /**
     * Class Methods
     */

    fun getTerm(termDAOJdbi: TermDAOJdbi, termId: Int): Term = termDAOJdbi.getTerm(termId)
            .orElseThrow { UnknownDataException("Can't find term with id $termId", "Try Again Later") }

    fun getCourse(courseDAOJdbi: CourseDAOJdbi, courseId: Int): Course = courseDAOJdbi.getSpecificCourse(courseId)
            .orElseThrow { UnknownDataException("Can't find course with id $courseId", "Try Again Later") }

    // ---------- Class ----------

    // ----------------------------
    // Class Methods
    // ----------------------------

    override fun getAllClasses(): ClassCollectionOutputModel =
            jdbi.inTransaction<ClassCollectionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val allClasses = classDAO.getAllClasses()
                val classList = allClasses.map {
                    toClassOutputModel(it, getTerm(termDAOJdbi, it.termId))
                }
                toClassCollectionOutputModel(classList)
            }

    override fun getSpecificClass(classId: Int): ClassOutputModel =
            jdbi.inTransaction<ClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow {
                            NotFoundException(
                                    msg = "No class found with id $classId",
                                    action = "Try other ID"
                            )
                        }
                toClassOutputModel(klass, getTerm(termDAOJdbi, klass.termId))
            }

    override fun createClass(input: ClassInputModel, principal: Principal): ClassOutputModel =
            jdbi.inTransaction<ClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val klass = classDAO.createClass(toClass(input, principal.name))
                classDAO.createClassVersion(toClassVersion(klass))
                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        CLASS_TABLE,
                        klass.logId
                ))
                toClassOutputModel(klass, getTerm(termDAOJdbi, klass.termId))
            }

    override fun voteOnClass(classId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow { NotFoundException("No class with specified id found", "Try another id") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) klass.votes.dec() else klass.votes.inc()
                val success = classDAO.updateClassVotes(classId, votes)
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        klass.createdBy,
                        CLASS_TABLE,
                        klass.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun partialUpdateOnClass(classId: Int, input: ClassInputModel, principal: Principal): ClassOutputModel =
            jdbi.inTransaction<ClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)

                val oldClass = classDAO.getSpecificClass(classId)
                        .orElseThrow { NotFoundException("No class found with id $classId", "Try other ID") }
                val newClass = classDAO.updateClass(Class(
                        classId = classId,
                        version = oldClass.version.inc(),
                        logId = oldClass.logId,
                        createdBy = principal.name,
                        className = if(input.className.isEmpty()) oldClass.className else input.className
                ))
                classDAO.createClassVersion(toClassVersion(newClass))
                publisher.publishEvent(ResourceUpdatedEvent(
                        principal.name,
                        CLASS_TABLE,
                        newClass.logId
                ))
                toClassOutputModel(newClass, getTerm(termDAOJdbi, newClass.termId))
            }

    override fun deleteSpecificClass(classId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                val klass = it.getSpecificClass(classId)
                        .orElseThrow { NotFoundException("No class found with id $classId", "Try other ID") }
                val success = it.deleteSpecificClass(classId)
                publisher.publishEvent(ResourceDeletedEvent(
                        principal.name,
                        CLASS_TABLE,
                        klass.logId
                ))
                success
            }

    // ----------------------------
    // Class Report Methods
    // ----------------------------

    override fun getAllReportsOfClass(classId: Int): ClassReportCollectionOutputModel =
            jdbi.withExtension<ClassReportCollectionOutputModel, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                val reports = it.getAllReportsFromClass(classId).map { toClassReportOutputModel(it) }
                toClassReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOfClass(classId: Int, reportId: Int): ClassReportOutputModel =
            jdbi.withExtension<ClassReportOutputModel, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                toClassReportOutputModel(
                        it.getSpecificReportFromClass(classId, reportId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No report found",
                                            action = "Try other report Id"
                                    )
                                }
                )
            }

    override fun createClassReport(classId: Int, report: ClassReportInputModel, principal: Principal): ClassReportOutputModel =
            jdbi.withExtension<ClassReportOutputModel, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                val classReport = it.reportClass(classId, toClassReport(classId, report, principal.name))
                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        CLASS_REPORT_TABLE,
                        classReport.logId
                ))
                toClassReportOutputModel(classReport)
            }

    override fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val classReport = classDAO.getSpecificReportFromClass(classId, reportId)
                        .orElseThrow { NotFoundException("No class report found", "Try other ID") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) classReport.votes.dec() else classReport.votes.inc()
                val success = classDAO.updateReportedClassVotes(classId, reportId, votes)
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        classReport.reportedBy,
                        CLASS_REPORT_TABLE,
                        classReport.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun updateClassFromReport(classId: Int, reportId: Int, principal: Principal): ClassOutputModel =
            jdbi.inTransaction<ClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                //TODO: Add option to delete Class
                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow { NotFoundException("No class found", "Try other ID") }
                val report = classDAO.getSpecificReportFromClass(classId, reportId)
                        .orElseThrow { NotFoundException("No class report found", "Try other ID") }

                val updatedClass = classDAO.updateClass(Class(
                        classId = classId,
                        termId = report.termId,
                        version = klass.version.inc(),
                        className = report.className ?: klass.className,
                        createdBy = report.reportedBy
                ))
                classDAO.createClassVersion(toClassVersion(updatedClass))
                classDAO.deleteSpecificReportInClass(classId, reportId)
                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_REPORT,
                        CLASS_REPORT_TABLE,
                        report.logId,
                        report.reportedBy,
                        ActionType.ALTER,
                        CLASS_TABLE,
                        klass.logId
                ))
                toClassOutputModel(updatedClass, termDAOJdbi.getTerm(updatedClass.termId).get())
            }

    override fun deleteSpecificReportInClass(classId: Int, reportId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                val klass = it.getSpecificReportFromClass(classId, reportId)
                        .orElseThrow { NotFoundException("No class report found", "Try other ID") }
                val success = it.deleteSpecificReportInClass(classId, reportId)
                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        klass.reportedBy,
                        ActionType.REJECT_REPORT,
                        CLASS_REPORT_TABLE,
                        klass.logId
                ))
                success
            }

    // ----------------------------
    // Class Stage Methods
    // ----------------------------

    override fun getAllStagedClasses(): ClassStageCollectionOutputModel =
            jdbi.inTransaction<ClassStageCollectionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val stagedClasses = classDAO.getAllStagedClasses().map {
                    toClassStagedOutputModel(it, getTerm(termDAOJdbi, it.termId))
                }
                toClassStageCollectionOutputModel(stagedClasses)
            }

    override fun getSpecificStagedClass(stageId: Int): ClassStageOutputModel =
            jdbi.inTransaction<ClassStageOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val classStage = classDAO.getSpecificStagedClass(stageId)
                        .orElseThrow {
                            NotFoundException(
                                    msg = "No class staged found",
                                    action = "Try other id"
                            )
                        }
                toClassStagedOutputModel(classStage, getTerm(termDAOJdbi, classStage.termId))
            }

    override fun createStagingClass(classStageInputModel: ClassInputModel, principal: Principal): ClassStageOutputModel =
            jdbi.inTransaction<ClassStageOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)

                val classStage = classDAO.createStagedClass(toClassStage(classStageInputModel, principal.name))
                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        CLASS_STAGE_TABLE,
                        classStage.logId
                ))
                toClassStagedOutputModel(classStage, getTerm(termDAOJdbi, classStage.termId))
            }

    override fun createClassFromStaged(stageId: Int, principal: Principal): ClassOutputModel =
            jdbi.inTransaction<ClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                //TODO: Don't delete stage, flag it!
                val classStaged = classDAO.getSpecificStagedClass(stageId)
                        .orElseThrow { NotFoundException("No class stage found", "Try other ID") }
                val createdClass = classDAO.createClass(stagedToClass(classStaged))
                classDAO.deleteSpecificStagedClass(stageId)
                classDAO.createClassVersion(toClassVersion(createdClass))
                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_STAGE,
                        CLASS_STAGE_TABLE,
                        classStaged.logId,
                        classStaged.createdBy,
                        ActionType.CREATE,
                        CLASS_TABLE,
                        createdClass.logId
                ))
                toClassOutputModel(createdClass, getTerm(termDAOJdbi, createdClass.termId))
            }

    override fun voteOnStagedClass(stageId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val classStage = classDAO.getSpecificStagedClass(stageId)
                        .orElseThrow { NotFoundException("No class stage found", "Try other ID") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) classStage.votes.dec() else classStage.votes.inc()
                val success = classDAO.updateStagedClassVotes(stageId, votes)
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        classStage.createdBy,
                        CLASS_STAGE_TABLE,
                        classStage.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun deleteSpecificStagedClass(stageId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                val classStage = it.getSpecificStagedClass(stageId)
                        .orElseThrow { NotFoundException("No class stage found", "Try other ID") }
                val success = it.deleteSpecificStagedClass(stageId)
                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        classStage.createdBy,
                        ActionType.REJECT_STAGE,
                        CLASS_STAGE_TABLE,
                        classStage.logId
                ))
                success
            }

    // ----------------------------
    // Class Version Methods
    // ----------------------------

    override fun getAllVersionsOfClass(classId: Int): ClassVersionCollectionOutputModel =
            jdbi.inTransaction<ClassVersionCollectionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val classVersions = classDAO.getAllVersionsOfSpecificClass(classId).map {
                    toClassVersionOutputModel(it, getTerm(termDAOJdbi, it.termId))
                }
                toClassVersionCollectionOutputModel(classVersions)
            }

    override fun getSpecificVersionOfClass(classId: Int, versionId: Int): ClassVersionOutputModel =
            jdbi.inTransaction<ClassVersionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val classVersion = classDAO.getVersionOfSpecificClass(classId, versionId)
                        .orElseThrow { NotFoundException("No version found", "Try with other version number") }
                toClassVersionOutputModel(classVersion, getTerm(termDAOJdbi, classVersion.termId))
            }

    /**
     * Course Class
     */
    override fun getAllCoursesOfClass(classId: Int): CourseClassCollectionOutputModel =
            jdbi.inTransaction<CourseClassCollectionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAOJdbi = it.attach(CourseDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow { NotFoundException("No class found with this id", "Try other id") }
                val courseClasses = classDAO.getAllCoursesOfClass(classId).map {
                    toCourseClassOutputModel(getCourse(courseDAOJdbi, it.courseId), klass, it, getTerm(termDAOJdbi, it.termId))
                }
                toCourseClassCollectionOutputModel(courseClasses)
            }

    override fun getSpecificCourseOfClass(classId: Int, courseId: Int): CourseClassOutputModel =
            jdbi.inTransaction<CourseClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAOJdbi = it.attach(CourseDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)
                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow { NotFoundException("No class found with this id", "Try other id") }
                val courseClass = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("No course found with this id for this class", "Try other courseId") }
                val course = getCourse(courseDAOJdbi, courseId)
                toCourseClassOutputModel(course, klass, courseClass, getTerm(termDAOJdbi, courseClass.termId))
            }

    override fun addCourseToClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel, principal: Principal): CourseClassOutputModel =
            jdbi.inTransaction<CourseClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAOJdbi = it.attach(CourseDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)

                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow { NotFoundException("No class found", "Try other id") }
                val term = getTerm(termDAOJdbi, klass.termId)
                val courseClass = classDAO.addCourseToClass(CourseClass(
                        courseId = courseId,
                        classId = classId,
                        termId = term.termId,
                        createdBy = principal.name
                ))
                val course = getCourse(courseDAOJdbi, courseId)
                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        COURSE_CLASS_TABLE,
                        courseClass.logId
                ))
                toCourseClassOutputModel(course, klass, courseClass, term)
            }

    override fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClass = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("No course and class association found", "Try other id") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseClass.votes.dec() else courseClass.votes.inc()
                val success = classDAO.updateCourseClassVotes(classId, courseId, votes)
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        courseClass.createdBy,
                        COURSE_CLASS_TABLE,
                        courseClass.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun deleteSpecificCourseInClass(classId: Int, courseId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                val courseClass = it.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("No course and class association found", "Try other id") }
                val success = it.deleteSpecificCourseInClass(classId, courseId)
                publisher.publishEvent(ResourceDeletedEvent(
                        principal.name,
                        COURSE_CLASS_TABLE,
                        courseClass.logId
                ))
                success
            }

    override fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): CourseClassReportCollectionOutputModel =
            jdbi.inTransaction<CourseClassReportCollectionOutputModel, Exception> {
                val classDAOJdbi = it.attach(ClassDAOJdbi::class.java)
                val courseClass = classDAOJdbi.getCourseClass(classId, courseId).get()
                val reports = classDAOJdbi.getAllReportsOfCourseInClass(
                        courseClass.courseClassId
                ).map { toCourseClassReportOutputModel(it) }
                toCourseClassReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): CourseClassReportOutputModel =
            jdbi.withExtension<CourseClassReportOutputModel, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                toCourseClassReportOutputModel(it.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                        .orElseThrow { NotFoundException("No report found", "Try with other report ID") }
                )
            }

    override fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel, principal: Principal): CourseClassReportOutputModel =
            jdbi.inTransaction<CourseClassReportOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClass = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("No course and class association found", "Try other id") }
                val report = classDAO.reportCourseInClass(toCourseClassReport(courseClass.courseClassId, courseClassReportInputModel, principal.name))
                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        COURSE_CLASS_REPORT_TABLE,
                        report.logId
                ))
                toCourseClassReportOutputModel(report)
            }

    override fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int, principal: Principal): CourseClassOutputModel =
            jdbi.inTransaction<CourseClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAOJdbi = it.attach(CourseDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)

                val courseClass = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("No course in class found", "Try other id") }
                val courseClassReport = classDAO.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                        .orElseThrow { NotFoundException("No course in class report found", "Try other id") }

                classDAO.deleteSpecificReportOnCourseClass(courseClass.courseClassId, reportId)
                classDAO.deleteSpecificCourseInClass(classId, courseId)

                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow { NotFoundException("No class found", "Try other id") }
                val term = getTerm(termDAOJdbi, klass.termId)
                val course = getCourse(courseDAOJdbi, courseId)
                val res = if(!courseClassReport.deletePermanently) {
                    toCourseClassOutputModel(
                            course,
                            klass,
                            classDAO.addCourseToClass(CourseClass(
                                    courseClassId = courseClass.courseClassId,
                                    createdBy = courseClassReport.reportedBy,
                                    courseId = courseClassReport.courseId ?: courseClass.courseId,
                                    classId = courseClassReport.classId ?: courseClass.courseId,
                                    termId = courseClassReport.termId ?: courseClass.termId
                            )),
                            term
                    )
                } else toCourseClassOutputModel(course, klass, courseClass, term)

                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_REPORT,
                        COURSE_CLASS_REPORT_TABLE,
                        courseClassReport.logId,
                        courseClassReport.reportedBy,
                        if(courseClassReport.deletePermanently) ActionType.DELETE else ActionType.ALTER,
                        COURSE_CLASS_TABLE,
                        courseClass.logId
                ))
                res
            }

    override fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassReport = classDAO.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                        .orElseThrow { NotFoundException("No course in class report found", "Try other id") }

                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseClassReport.votes.dec() else courseClassReport.votes.inc()
                val success = classDAO.updateReportedCourseClassVotes(classId, courseId, reportId, votes)
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        courseClassReport.reportedBy,
                        COURSE_CLASS_REPORT_TABLE,
                        courseClassReport.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun deleteReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                val courseClassReport = it.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                        .orElseThrow { NotFoundException("No course in class report found", "Try other id") }

                val success = it.deleteSpecificCourseReportInClass(classId, courseId, reportId)
                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        courseClassReport.reportedBy,
                        ActionType.REJECT_REPORT,
                        COURSE_CLASS_REPORT_TABLE,
                        courseClassReport.logId
                ))
                success
            }

    override fun getStageEntriesOfCoursesInClass(classId: Int): CourseClassStageCollectionOutputModel =
            jdbi.withExtension<CourseClassStageCollectionOutputModel, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                val stageEntries = it.getStageEntriesOfCoursesInClass(classId).map { toCourseClassStageOutputModel(it) }
                toCourseClassStageCollectionOutputModel(stageEntries)
            }

    override fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): CourseClassStageOutputModel =
            jdbi.withExtension<CourseClassStageOutputModel, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                toCourseClassStageOutputModel(it.getSpecificStagedCourseInClass(classId, stageId)
                        .orElseThrow { NotFoundException("No staged course class", "Try other staged id") })
            }

    override fun createStagingCourseInClass(classId: Int, courseId: Int, principal: Principal): CourseClassStageOutputModel =
            jdbi.inTransaction<CourseClassStageOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

                val termId = classDAO.getTermIdFromSpecificClass(classId)
                courseDAO.getSpecificCourse(courseId)
                        .orElseThrow { NotFoundException("Course does not exist", "Try other id") }
                val courseClassStage = classDAO.createStagingCourseInClass(CourseClassStage(
                        courseId = courseId,
                        classId = classId,
                        termId = termId,
                        createdBy = principal.name
                ))
                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        COURSE_CLASS_STAGE_TABLE,
                        courseClassStage.logId
                ))
                toCourseClassStageOutputModel(courseClassStage)
            }

    override fun addCourseInClassFromStaged(classId: Int, stageId: Int, principal: Principal): CourseClassOutputModel =
            jdbi.inTransaction<CourseClassOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseDAOJdbi = it.attach(CourseDAOJdbi::class.java)
                val termDAOJdbi = it.attach(TermDAOJdbi::class.java)

                val klass = classDAO.getSpecificClass(classId)
                        .orElseThrow { NotFoundException("No class found", "Try other id") }
                val courseClassStage = classDAO.getSpecificStagedCourseInClass(classId, stageId)
                        .orElseThrow { NotFoundException("No staged course in class found", "Try other staged id") }

                val created = classDAO.addCourseToClass(stagedToCourseClass(courseClassStage))
                val term = getTerm(termDAOJdbi, klass.termId)
                val course = courseDAOJdbi.getSpecificCourse(created.classId)
                        .orElseThrow { NotFoundException("No course found", "Try other id") }
                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_STAGE,
                        COURSE_CLASS_STAGE_TABLE,
                        courseClassStage.logId,
                        courseClassStage.createdBy,
                        ActionType.CREATE,
                        COURSE_CLASS_TABLE,
                        created.logId
                ))
                toCourseClassOutputModel(course, klass, created, term)
            }

    override fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val courseClassStage = classDAO.getSpecificStagedCourseInClass(classId, stageId)
                        .orElseThrow { NotFoundException("No staged course in class found", "Try other staged id") }

                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseClassStage.votes.dec() else courseClassStage.votes.inc()
                val success = classDAO.updateStagedCourseClassVotes(classId, stageId, votes)
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        courseClassStage.createdBy,
                        COURSE_CLASS_STAGE_TABLE,
                        courseClassStage.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                val courseClassStage = it.getSpecificStagedCourseInClass(classId, stageId)
                        .orElseThrow { NotFoundException("No staged course in class found", "Try other staged id") }
                val success = it.deleteSpecificStagedCourseInClass(classId, stageId)
                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        courseClassStage.createdBy,
                        ActionType.REJECT_STAGE,
                        COURSE_CLASS_STAGE_TABLE,
                        courseClassStage.logId
                ))
                success
            }

    /**
     * Lectures Methods
     */

    override fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): LectureCollectionOutputModel =
            jdbi.inTransaction<LectureCollectionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)

                val lectures = lectureDAO
                        .getAllLecturesFromCourseInClass(classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId)
                        .map { toLectureOutputModel(it) }
                toLectureCollectionOutputModel(lectures)
            }

    override fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureOutputModel =
            jdbi.inTransaction<LectureOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)

                toLectureOutputModel(lectureDAO.getSpecificLectureFromCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        lectureId).orElseThrow { NotFoundException("No Lecture found", "Try another id") }
                )
            }

    override fun createLectureOnCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel, principal: Principal): LectureOutputModel =
            jdbi.inTransaction<LectureOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val lecture = lectureDAO.createLectureOnCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        toLecture(lectureInputModel, principal.name)
                )
                lectureDAO.createLectureVersion(toLectureVersion(lecture))
                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        LECTURE_TABLE,
                        lecture.logId
                ))
                toLectureOutputModel(lecture)
            }

    override fun voteOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val lecture = lectureDAO.getSpecificLectureFromCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId, lectureId)
                        .orElseThrow { NotFoundException("No Lecture found", "Try another id") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) lecture.votes.dec() else lecture.votes.inc()
                val success = lectureDAO.updateVotesOnLecture(lectureId, votes)
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        lecture.createdBy,
                        LECTURE_TABLE,
                        lecture.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun deleteSpecificLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val lecture = lectureDAO.getSpecificLectureFromCourseInClass(courseClassId, lectureId)
                        .orElseThrow { NotFoundException("No Lecture found", "Try another id") }
                val success = lectureDAO.deleteSpecificLectureOfCourseInClass(courseClassId, lectureId)
                publisher.publishEvent(ResourceDeletedEvent(
                        principal.name,
                        LECTURE_TABLE,
                        lecture.logId
                ))
                success
            }

    override fun getAllReportsOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureReportCollectionOutputModel =
            jdbi.inTransaction<LectureReportCollectionOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val reports = lectureDAO.getAllReportsOfLectureFromCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        lectureId
                ).map { toLectureReportOutputModel(it) }
                toLectureReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): LectureReportOutputModel =
            jdbi.inTransaction<LectureReportOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toLectureReportOutputModel(lectureDAO.getSpecificReportOfLectureFromCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        lectureId,
                        reportId
                ).orElseThrow { NotFoundException(msg = "No report found", action = "Try other report id") }
                )
            }

    override fun deleteSpecificReportOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val lectureReport = lectureDAO.getSpecificReportOfLectureFromCourseInClass(courseClassId, lectureId, reportId)
                        .orElseThrow { NotFoundException("No Lecture found", "Try another id") }
                val success = lectureDAO.deleteSpecificReportOnLectureOfCourseInClass(courseClassId, lectureId, reportId)
                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        lectureReport.reportedBy,
                        ActionType.REJECT_STAGE,
                        LECTURE_REPORT_TABLE,
                        lectureReport.logId
                ))
                success
            }

    override fun createReportOnLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, lectureReportInputModel: LectureReportInputModel, principal: Principal): LectureReport =
            jdbi.inTransaction<LectureReport, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val lecture = lectureDAO.getSpecificLectureFromCourseInClass(courseClassId, lectureId)
                        .orElseThrow { NotFoundException("No Lecture found", "Try another id") }
                val lectureReport = lectureDAO.createReportOnLecture(toLectureReport(lectureReportInputModel, lecture.lectureId, principal.name))
                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        LECTURE_REPORT_TABLE,
                        lectureReport.logId
                ))
                lectureReport
            }

    override fun voteOnReportOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val lectureReport = lectureDAO.getSpecificReportOfLectureFromCourseInClass(courseClassId, lectureId, reportId)
                        .orElseThrow { NotFoundException("No Lecture found", "Try another id") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) lectureReport.votes.dec() else lectureReport.votes.inc()
                val success = lectureDAO.updateVotesOnReportedLecture(lectureId, reportId, votes)
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        lectureReport.reportedBy,
                        LECTURE_REPORT_TABLE,
                        lectureReport.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun updateLectureFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int, principal: Principal): LectureOutputModel =
            jdbi.inTransaction<LectureOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val lecture = lectureDAO.getSpecificLectureFromCourseInClass(courseClassId, lectureId)
                        .orElseThrow { NotFoundException("No Lecture found", "Try another id") }
                val lectureReport = lectureDAO.getSpecificReportOfLectureFromCourseInClass(courseClassId, lectureId, reportId)
                        .orElseThrow { NotFoundException("No Lecture report found", "Try another id") }

                val res = lectureDAO.updateLecture(Lecture(
                        lectureId = lectureId,
                        createdBy = lectureReport.reportedBy,
                        version = lecture.version.inc(),
                        weekDay = lectureReport.weekDay ?: lecture.weekDay,
                        begins = lectureReport.begins ?: lecture.begins,
                        duration = lectureReport.duration ?: lecture.duration,
                        location = lectureReport.location ?: lecture.location
                ))
                lectureDAO.createLectureVersion(toLectureVersion(res))
                lectureDAO.deleteSpecificReportOnLectureOfCourseInClass(courseClassId, lectureId, reportId)
                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_REPORT,
                        LECTURE_REPORT_TABLE,
                        lectureReport.logId,
                        lectureReport.reportedBy,
                        ActionType.ALTER,
                        LECTURE_TABLE,
                        lecture.logId
                ))
                toLectureOutputModel(res)
            }

    override fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): LectureStageCollectionOutputModel =
            jdbi.inTransaction<LectureStageCollectionOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val stagedLectures = lectureDAO.getAllStagedLecturesOfCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId
                ).map { toLectureStageOutputModel(it) }
                toLectureStageCollectionOutputModel(stagedLectures)
            }

    override fun getSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): LectureStageOutputModel =
            jdbi.inTransaction<LectureStageOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toLectureStageOutputModel(lectureDAO.getSpecificStagedLectureOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId, stageId)
                        .orElseThrow { NotFoundException("No staged lecture found", "Try with other id") })
            }

    override fun createStagingLectureOfCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel, principal: Principal): LectureStageOutputModel =
            jdbi.inTransaction<LectureStageOutputModel, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val lectureStage = lectureDAO.createStagingLectureOnCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        toLectureStage(lectureInputModel, principal.name)
                )
                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        LECTURE_STAGE_TABLE,
                        lectureStage.logId
                ))
                toLectureStageOutputModel(lectureStage)
            }

    override fun deleteSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val lectureStage = lectureDAO.getSpecificStagedLectureOfCourseInClass(courseClassId, stageId)
                        .orElseThrow { NotFoundException("No Lecture stage found", "Try another id") }
                val success = lectureDAO.deleteSpecificStagedLectureOfCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        stageId
                )
                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        lectureStage.createdBy,
                        ActionType.REJECT_STAGE,
                        LECTURE_STAGE_TABLE,
                        lectureStage.logId
                ))
                success
            }

    override fun voteOnStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val lectureStage = lectureDAO.getSpecificStagedLectureOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId, stageId)
                        .orElseThrow { NotFoundException("No Lecture stage found", "Try another id") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) lectureStage.votes.dec() else lectureStage.votes.inc()
                val success = lectureDAO.updateVotesOnStagedLecture(stageId, votes)
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        lectureStage.createdBy,
                        LECTURE_STAGE_TABLE,
                        lectureStage.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun createLectureFromStaged(classId: Int, courseId: Int, stageId: Int, principal: Principal): LectureOutputModel =
            jdbi.inTransaction<LectureOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val stagedLecture = lectureDAO.getSpecificStagedLectureOfCourseInClass(courseClassId, stageId)
                        .orElseThrow { NotFoundException("No Lecture stage found", "Try another id") }
                val createdLecture = lectureDAO.createLectureOnCourseInClass(courseClassId, stagedToLecture(stagedLecture))
                lectureDAO.deleteSpecificStagedLectureOfCourseInClass(courseClassId, stageId)
                lectureDAO.createLectureVersion(toLectureVersion(createdLecture))
                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_STAGE,
                        LECTURE_STAGE_TABLE,
                        stagedLecture.logId,
                        stagedLecture.createdBy,
                        ActionType.CREATE,
                        LECTURE_TABLE,
                        createdLecture.logId
                ))
                toLectureOutputModel(createdLecture)
            }

    override fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureVersionCollectionOutputModel =
            jdbi.inTransaction<LectureVersionCollectionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                val lectureVersions = lectureDAO.getAllVersionsOfLectureOfCourseInclass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        lectureId
                ).map { toLectureVersionOutputModel(it) }
                toLectureVersionCollectionOutputModel(lectureVersions)
            }

    override fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): LectureVersionOutputModel =
            jdbi.inTransaction<LectureVersionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val lectureDAO = it.attach(LectureDAOJdbi::class.java)
                toLectureVersionOutputModel(lectureDAO.getSpecificVersionOfLectureOfCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        lectureId,
                        version
                ).orElseThrow { NotFoundException("No version for this lecture", "Try with other version number") })
            }

    /**
     * Homeworks Methods
     */

    override fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): HomeworkCollectionOutputModel =
            jdbi.inTransaction<HomeworkCollectionOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)

                val homeworks = homeworkDAO.getAllHomeworksFromCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId)
                toHomeworkCollectionOutputModel(homeworks.map { toHomeworkOutputModel(it) })
            }

    override fun getSpecificHomeworkFromSpecificCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkOutputModel =
            jdbi.inTransaction<HomeworkOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                toHomeworkOutputModel(homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        homeworkId
                ).orElseThrow { NotFoundException("No homework found", "Try other homework id") })
            }

    override fun createHomeworkOnCourseInClass(
            sheet: MultipartFile,
            classId: Int,
            courseId: Int,
            homeworkInputModel: HomeworkInputModel,
            principal: Principal
    ): HomeworkOutputModel = jdbi.inTransaction<HomeworkOutputModel, Exception> {
        val classDAO = it.attach(ClassDAOJdbi::class.java)
        val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)

        val createdHomework = homeworkDAO.createHomeworkOnCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                toHomework(homeworkInputModel, principal.name)
        )
        homeworkDAO.createHomeworkVersion(toHomeworkVersion(createdHomework))
        storageService.storeResource(createdHomework.sheetId, sheet)
        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                HOMEWORK_TABLE,
                createdHomework.logId
        ))
        toHomeworkOutputModel(createdHomework)
    }

    override fun voteOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId, homeworkId)
                        .orElseThrow { NotFoundException("No homework found", "Try other id") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) homework.votes.dec() else homework.votes.inc()
                val success = homeworkDAO.updateVotesOnHomework(homeworkId, votes)
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        homework.createdBy,
                        HOMEWORK_TABLE,
                        homework.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun deleteSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(courseClassId, homeworkId)
                        .orElseThrow { NotFoundException("No homework found", "Try other id") }

                val success = homeworkDAO.deleteSpecificHomeworkOfCourseInClass(courseClassId, homeworkId)
                storageService.deleteSpecificResource(homework.sheetId)
                publisher.publishEvent(ResourceDeletedEvent(
                        principal.name,
                        HOMEWORK_TABLE,
                        homework.logId
                ))
                success
            }

    override fun getAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): HomeworkStageCollectionOutputModel =
            jdbi.inTransaction<HomeworkStageCollectionOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val stagedHomeworks = homeworkDAO.getAllStagedHomeworksOfCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId
                ).map { toHomeworkStagedOutputModel(it) }
                toHomeworkStageCollectionOutputModel(stagedHomeworks)
            }

    override fun getSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): HomeworkStageOutputModel =
            jdbi.inTransaction<HomeworkStageOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toHomeworkStagedOutputModel(
                        homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId, stageId)
                                .orElseThrow { NotFoundException("No staged homework", "Try with other stage id") }
                )
            }

    override fun createStagingHomeworkOnCourseInClass(sheet: MultipartFile, classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel, principal: Principal): HomeworkStageOutputModel =
            jdbi.inTransaction<HomeworkStageOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val stagingHomework = homeworkDAO.createStagingHomeworkOnCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        toHomeworkStage(homeworkInputModel, principal.name)
                )
                storageService.storeResource(stagingHomework.sheetId, sheet)
                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        HOMEWORK_STAGE_TABLE,
                        stagingHomework.logId
                ))
                toHomeworkStagedOutputModel(stagingHomework)
            }

    override fun createHomeworkFromStaged(classId: Int, courseId: Int, stageId: Int, principal: Principal): HomeworkOutputModel =
            jdbi.inTransaction<HomeworkOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val stagedHomework = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)
                        .orElseThrow { NotFoundException("No staged homework", "Try with other stage id") }

                val createdHomework = homeworkDAO.createHomeworkOnCourseInClass(courseClassId, stagedToHomework(stagedHomework))
                homeworkDAO.deleteSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)
                homeworkDAO.createHomeworkVersion(toHomeworkVersion(createdHomework))

                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_STAGE,
                        HOMEWORK_STAGE_TABLE,
                        stagedHomework.logId,
                        stagedHomework.createdBy,
                        ActionType.CREATE,
                        HOMEWORK_TABLE,
                        createdHomework.logId
                ))
                toHomeworkOutputModel(createdHomework)
            }

    override fun voteOnStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)

                val homeworkStage = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId, stageId)
                        .orElseThrow { NotFoundException("No staged homework", "Try with other stage id") }
                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) homeworkStage.votes.dec() else homeworkStage.votes.inc()

                val success = homeworkDAO.updateVotesOnStagedHomework(stageId, votes)

                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        homeworkStage.createdBy,
                        HOMEWORK_STAGE_TABLE,
                        homeworkStage.logId,
                        Vote.valueOf(vote.vote)
                ))
                success
            }

    override fun deleteSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val stagedHomework = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)
                        .orElseThrow { NotFoundException("No staged homework", "Try with other stage id") }

                val success = homeworkDAO.deleteSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)
                storageService.deleteSpecificResource(stagedHomework.sheetId)
                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        stagedHomework.createdBy,
                        ActionType.REJECT_STAGE,
                        HOMEWORK_STAGE_TABLE,
                        stagedHomework.logId
                ))
                success
            }

    override fun getAllReportsOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): HomeworkReportCollectionOutputModel =
            jdbi.inTransaction<HomeworkReportCollectionOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val reports = homeworkDAO.getAllReportsOfHomeworkFromCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        homeWorkId
                ).map { toHomeworkReportOutputModel(it) }
                toHomeworkReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): HomeworkReportOutputModel =
            jdbi.inTransaction<HomeworkReportOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toHomeworkReportOutputModel(
                        homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(
                                classDAO.getCourseClass(classId, courseId)
                                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                        .courseClassId,
                                homeworkId,
                                reportId
                        ).orElseThrow { NotFoundException("No report found","Try with other report id") }
                )
            }

    override fun createReportOnHomeworkFromCourseInClass(
            classId: Int,
            courseId: Int,
            homeworkId: Int,
            homeworkReportInputModel: HomeworkReportInputModel,
            principal: Principal
    ): HomeworkReportOutputModel =
            jdbi.inTransaction<HomeworkReportOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(courseClassId, homeworkId)
                        .orElseThrow { NotFoundException("No homework found", "Try other id") }
                val homeworkReport = homeworkDAO.createReportOnHomework(toHomeworkReport(homeworkReportInputModel, homework.homeworkId, principal.name))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        HOMEWORK_REPORT_TABLE,
                        homeworkReport.logId
                ))
                toHomeworkReportOutputModel(homeworkReport)
            }

    override fun updateHomeworkFromReport(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, principal: Principal): HomeworkOutputModel =
            jdbi.inTransaction<HomeworkOutputModel, Exception> {
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(courseClassId, homeworkId)
                        .orElseThrow { NotFoundException("No homework found", "Try other id") }
                val homeworkReport = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(courseClassId, homeworkId, reportId)
                        .orElseThrow { NotFoundException("No homework report found", "Try other id") }

                val res = homeworkDAO.updateHomework(Homework(
                        homeworkId = homeworkId,
                        createdBy = homeworkReport.reportedBy,
                        version = homework.version.inc(),
                        sheetId = homeworkReport.sheetId ?: homework.sheetId,
                        dueDate = homeworkReport.dueDate ?: homework.dueDate,
                        lateDelivery = homeworkReport.lateDelivery ?: homework.lateDelivery,
                        multipleDeliveries = homeworkReport.multipleDeliveries ?: homework.multipleDeliveries
                ))
                homeworkDAO.createHomeworkVersion(toHomeworkVersion(res))
                homeworkDAO.deleteSpecificReportOnHomeworkOfCourseInClass(courseClassId, homeworkId, reportId)

                publisher.publishEvent(ResourceApprovedEvent(
                        principal.name,
                        ActionType.APPROVE_REPORT,
                        HOMEWORK_REPORT_TABLE,
                        homeworkReport.logId,
                        homeworkReport.reportedBy,
                        ActionType.ALTER,
                        HOMEWORK_TABLE,
                        res.logId
                ))
                toHomeworkOutputModel(res)
            }

    override fun voteOnReportOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val homeworkReport = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(courseClassId, homeworkId, reportId)
                        .orElseThrow { NotFoundException("No homework report found", "Try other id") }

                val votes = if(Vote.valueOf(vote.vote) == Vote.Down) homeworkReport.votes.dec() else homeworkReport.votes.inc()
                publisher.publishEvent(VoteOnResourceEvent(
                        principal.name,
                        homeworkReport.reportedBy,
                        HOMEWORK_REPORT_TABLE,
                        homeworkReport.logId,
                        Vote.valueOf(vote.vote)
                ))
                homeworkDAO.updateVotesOnReportedLecture(homeworkId, reportId, votes)
            }

    override fun deleteSpecificReportOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)

                val courseClassId = classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
                val homeworkReport = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(courseClassId, homeworkId, reportId)
                        .orElseThrow { NotFoundException("No homework report found", "Try other id") }
                val success = homeworkDAO.deleteSpecificReportOnHomeworkOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId, homeworkId, reportId)
                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        homeworkReport.reportedBy,
                        ActionType.REJECT_REPORT,
                        HOMEWORK_REPORT_TABLE,
                        homeworkReport.logId
                ))
                success
            }

    override fun getAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkVersionCollectionOutputModel =
            jdbi.inTransaction<HomeworkVersionCollectionOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                val homeworkVersions = homeworkDAO.getAllVersionsOfHomeworkOfCourseInclass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        homeworkId
                ).map { toHomeworkVersionOutputModel(it) }
                toHomeworkVersionCollectionOutputModel(homeworkVersions)
            }

    override fun getSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): HomeworkVersionOutputModel =
            jdbi.inTransaction<HomeworkVersionOutputModel, Exception> {
                val homeworkDAO = it.attach(HomeworkDAOJdbi::class.java)
                val classDAO = it.attach(ClassDAOJdbi::class.java)
                toHomeworkVersionOutputModel(
                        homeworkDAO.getSpecificVersionOfHomeworkOfCourseInClass(
                                classDAO.getCourseClass(classId, courseId)
                                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                        .courseClassId,
                                homeworkId,
                                version
                        ).orElseThrow { NotFoundException("No version found", "Try with other version number") })
            }
}
