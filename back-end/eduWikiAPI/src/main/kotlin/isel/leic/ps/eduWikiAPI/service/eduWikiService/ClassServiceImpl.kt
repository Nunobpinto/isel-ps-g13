package isel.leic.ps.eduWikiAPI.service.eduWikiService

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
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseClassOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.HomeworkOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.LectureOutputModel
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
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.ForbiddenException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.UnknownDataException
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_TABLE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.HomeworkDAOImpl.Companion.HOMEWORK_TABLE
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.LectureDAOImpl.Companion.LECTURE_TABLE
import isel.leic.ps.eduWikiAPI.repository.interfaces.*
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.ClassService
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.ResourceStorageService
import isel.leic.ps.eduWikiAPI.utils.resolveVote
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@Service
class ClassServiceImpl : ClassService {

    @Autowired
    lateinit var storageService: ResourceStorageService
    @Autowired
    lateinit var publisher: ApplicationEventPublisher
    @Autowired
    lateinit var termDAO: TermDAO
    @Autowired
    lateinit var classDAO: ClassDAO
    @Autowired
    lateinit var courseDAO: CourseDAO
    @Autowired
    lateinit var lectureDAO: LectureDAO
    @Autowired
    lateinit var homeworkDAO: HomeworkDAO
    @Autowired
    lateinit var reputationDAO: ReputationDAO

    /**
     * Class Methods
     */

    fun getTerm(termId: Int): Term = termDAO.getTerm(termId)
            .orElseThrow { UnknownDataException("Can't find term with id $termId", "Try Again Later") }

    fun getCourse(courseId: Int): Course = courseDAO.getSpecificCourse(courseId)
            .orElseThrow { UnknownDataException("Can't find course with id $courseId", "Try Again Later") }

    // ---------- Class ----------

    // ----------------------------
    // Class Methods
    // ----------------------------

    @Transactional
    override fun getAllClasses(): ClassCollectionOutputModel {
        val allClasses = classDAO.getAllClasses()
        val classList = allClasses.map {
            toClassOutputModel(it, getTerm(it.termId))
        }
        return toClassCollectionOutputModel(classList)
    }

    @Transactional
    override fun getSpecificClass(classId: Int): ClassOutputModel {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found with id $classId", "Try other ID") }
        return toClassOutputModel(klass, getTerm(klass.termId))
    }

    @Transactional
    override fun createClass(input: ClassInputModel, principal: Principal): ClassOutputModel {
        val klass = classDAO.createClass(toClass(input, principal.name))
        classDAO.createClassVersion(toClassVersion(klass))
        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                CLASS_TABLE,
                klass.logId
        ))
        return toClassOutputModel(klass, getTerm(klass.termId))
    }

    @Transactional
    override fun voteOnClass(classId: Int, vote: VoteInputModel, principal: Principal): Int {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class with specified id found", "Try another id") }
        resolveVote(principal.name, klass.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, CLASS_TABLE, klass.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) klass.votes.dec() else klass.votes.inc()
        val success = classDAO.updateClassVotes(classId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                klass.createdBy,
                CLASS_TABLE,
                klass.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun partialUpdateOnClass(classId: Int, input: ClassInputModel, principal: Principal): ClassOutputModel {
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
        return toClassOutputModel(newClass, getTerm(newClass.termId))
    }

    @Transactional
    override fun deleteSpecificClass(classId: Int, principal: Principal): Int {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found with id $classId", "Try other ID") }
        val success = classDAO.deleteSpecificClass(classId)
        publisher.publishEvent(ResourceDeletedEvent(
                principal.name,
                CLASS_TABLE,
                klass.logId
        ))
        return success
    }

    // ----------------------------
    // Class Report Methods
    // ----------------------------

    @Transactional
    override fun getAllReportsOfClass(classId: Int): ClassReportCollectionOutputModel {
        val reports = classDAO.getAllReportsFromClass(classId).map { toClassReportOutputModel(it) }
        return toClassReportCollectionOutputModel(reports)
    }

    @Transactional
    override fun getSpecificReportOfClass(classId: Int, reportId: Int): ClassReportOutputModel {
        return toClassReportOutputModel(
                classDAO.getSpecificReportFromClass(classId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try other report Id") }
        )
    }

    @Transactional
    override fun createClassReport(classId: Int, report: ClassReportInputModel, principal: Principal): ClassReportOutputModel {
        val classReport = classDAO.reportClass(classId, toClassReport(classId, report, principal.name))
        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                CLASS_REPORT_TABLE,
                classReport.logId
        ))
        return toClassReportOutputModel(classReport)
    }

    @Transactional
    override fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val classReport = classDAO.getSpecificReportFromClass(classId, reportId)
                .orElseThrow { NotFoundException("No class report found", "Try other ID") }
        resolveVote(principal.name, classReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, CLASS_REPORT_TABLE, classReport.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) classReport.votes.dec() else classReport.votes.inc()
        val success = classDAO.updateReportedClassVotes(classId, reportId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                classReport.reportedBy,
                CLASS_REPORT_TABLE,
                classReport.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun updateClassFromReport(classId: Int, reportId: Int, principal: Principal): ClassOutputModel {
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
        return toClassOutputModel(updatedClass, termDAO.getTerm(updatedClass.termId).get())
    }

    @Transactional
    override fun deleteSpecificReportInClass(classId: Int, reportId: Int, principal: Principal): Int {
        val klass = classDAO.getSpecificReportFromClass(classId, reportId)
                .orElseThrow { NotFoundException("No class report found", "Try other ID") }
        val success = classDAO.deleteSpecificReportInClass(classId, reportId)
        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                klass.reportedBy,
                ActionType.REJECT_REPORT,
                CLASS_REPORT_TABLE,
                klass.logId
        ))
        return success
    }

    // ----------------------------
    // Class Stage Methods
    // ----------------------------

    @Transactional
    override fun getAllStagedClasses(): ClassStageCollectionOutputModel {
        val stagedClasses = classDAO.getAllStagedClasses().map {
            toClassStagedOutputModel(it, getTerm(it.termId))
        }
        return toClassStageCollectionOutputModel(stagedClasses)
    }

    @Transactional
    override fun getSpecificStagedClass(stageId: Int): ClassStageOutputModel {
        val classStage = classDAO.getSpecificStagedClass(stageId)
                .orElseThrow { NotFoundException("No class staged found", "Try other id") }
        return toClassStagedOutputModel(classStage, getTerm(classStage.termId))
    }

    @Transactional
    override fun createStagingClass(classStageInputModel: ClassInputModel, principal: Principal): ClassStageOutputModel {
        val classStage = classDAO.createStagedClass(toClassStage(classStageInputModel, principal.name))
        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                CLASS_STAGE_TABLE,
                classStage.logId
        ))
        return toClassStagedOutputModel(classStage, getTerm(classStage.termId))
    }

    @Transactional
    override fun createClassFromStaged(stageId: Int, principal: Principal): ClassOutputModel {
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
        return toClassOutputModel(createdClass, getTerm(createdClass.termId))
    }

    @Transactional
    override fun voteOnStagedClass(stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val classStage = classDAO.getSpecificStagedClass(stageId)
                .orElseThrow { NotFoundException("No class stage found", "Try other ID") }
        resolveVote(principal.name, classStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, CLASS_STAGE_TABLE, classStage.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) classStage.votes.dec() else classStage.votes.inc()
        val success = classDAO.updateStagedClassVotes(stageId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                classStage.createdBy,
                CLASS_STAGE_TABLE,
                classStage.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun deleteSpecificStagedClass(stageId: Int, principal: Principal): Int {
        val classStage = classDAO.getSpecificStagedClass(stageId)
                .orElseThrow { NotFoundException("No class stage found", "Try other ID") }
        val success = classDAO.deleteSpecificStagedClass(stageId)
        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                classStage.createdBy,
                ActionType.REJECT_STAGE,
                CLASS_STAGE_TABLE,
                classStage.logId
        ))
        return success
    }

    // ----------------------------
    // Class Version Methods
    // ----------------------------

    @Transactional
    override fun getAllVersionsOfClass(classId: Int): ClassVersionCollectionOutputModel {
        val classVersions = classDAO.getAllVersionsOfSpecificClass(classId).map {
            toClassVersionOutputModel(it, getTerm(it.termId))
        }
        return toClassVersionCollectionOutputModel(classVersions)
    }

    @Transactional
    override fun getSpecificVersionOfClass(classId: Int, versionId: Int): ClassVersionOutputModel {
        val classVersion = classDAO.getVersionOfSpecificClass(classId, versionId)
                .orElseThrow { NotFoundException("No version found", "Try with other version number") }
        return toClassVersionOutputModel(classVersion, getTerm(classVersion.termId))
    }

    /**
     * Course Class
     */
    @Transactional
    override fun getAllCoursesOfClass(classId: Int): CourseClassCollectionOutputModel {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found with this id", "Try other id") }
        val courseClasses = classDAO.getAllCoursesOfClass(classId).map {
            toCourseClassOutputModel(getCourse(it.courseId), klass, it, getTerm(it.termId))
        }
        return toCourseClassCollectionOutputModel(courseClasses)
    }

    @Transactional
    override fun getSpecificCourseOfClass(classId: Int, courseId: Int): CourseClassOutputModel {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found with this id", "Try other id") }
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course found with this id for this class", "Try other courseId") }
        val course = getCourse(courseId)
        return toCourseClassOutputModel(course, klass, courseClass, getTerm(courseClass.termId))
    }

    @Transactional
    override fun addCourseToClass(classId: Int, courseId: Int, courseClassInputModel: CourseClassInputModel, principal: Principal): CourseClassOutputModel {

        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found", "Try other id") }
        val term = getTerm(klass.termId)
        val courseClass = classDAO.addCourseToClass(CourseClass(
                courseId = courseId,
                classId = classId,
                termId = term.termId,
                createdBy = principal.name
        ))
        val course = getCourse(courseId)
        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_CLASS_TABLE,
                courseClass.logId
        ))
        return toCourseClassOutputModel(course, klass, courseClass, term)
    }

    @Transactional
    override fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course and class association found", "Try other id") }
        resolveVote(principal.name, courseClass.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_CLASS_TABLE, courseClass.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseClass.votes.dec() else courseClass.votes.inc()
        val success = classDAO.updateCourseClassVotes(classId, courseId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                courseClass.createdBy,
                COURSE_CLASS_TABLE,
                courseClass.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun deleteSpecificCourseInClass(classId: Int, courseId: Int, principal: Principal): Int {
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course and class association found", "Try other id") }
        val success = classDAO.deleteSpecificCourseInClass(classId, courseId)
        publisher.publishEvent(ResourceDeletedEvent(
                principal.name,
                COURSE_CLASS_TABLE,
                courseClass.logId
        ))
        return success
    }

    @Transactional
    override fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): CourseClassReportCollectionOutputModel {
        val courseClass = classDAO.getCourseClass(classId, courseId).get()
        val reports = classDAO.getAllReportsOfCourseInClass(
                courseClass.courseClassId
        ).map { toCourseClassReportOutputModel(it) }
        return toCourseClassReportCollectionOutputModel(reports)
    }

    @Transactional
    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): CourseClassReportOutputModel {
        return toCourseClassReportOutputModel(classDAO.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                .orElseThrow { NotFoundException("No report found", "Try with other report ID") }
        )
    }

    @Transactional
    override fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel, principal: Principal): CourseClassReportOutputModel {
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course and class association found", "Try other id") }
        val report = classDAO.reportCourseInClass(toCourseClassReport(courseClass.courseClassId, courseClassReportInputModel, principal.name))
        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_CLASS_REPORT_TABLE,
                report.logId
        ))
        return toCourseClassReportOutputModel(report)
    }

    @Transactional
    override fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int, principal: Principal): CourseClassOutputModel {
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course in class found", "Try other id") }
        val courseClassReport = classDAO.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                .orElseThrow { NotFoundException("No course in class report found", "Try other id") }

        classDAO.deleteSpecificReportOnCourseClass(courseClass.courseClassId, reportId)
        classDAO.deleteSpecificCourseInClass(classId, courseId)

        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found", "Try other id") }
        val term = getTerm(klass.termId)
        val course = getCourse(courseId)
        val res = if(! courseClassReport.deletePermanently) {
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
        return res
    }

    @Transactional
    override fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseClassReport = classDAO.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                .orElseThrow { NotFoundException("No course in class report found", "Try other id") }
        resolveVote(principal.name, courseClassReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_CLASS_REPORT_TABLE, courseClassReport.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseClassReport.votes.dec() else courseClassReport.votes.inc()
        val success = classDAO.updateReportedCourseClassVotes(classId, courseId, reportId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                courseClassReport.reportedBy,
                COURSE_CLASS_REPORT_TABLE,
                courseClassReport.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun deleteReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, principal: Principal): Int {
        val courseClassReport = classDAO.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                .orElseThrow { NotFoundException("No course in class report found", "Try other id") }

        val success = classDAO.deleteSpecificCourseReportInClass(classId, courseId, reportId)
        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                courseClassReport.reportedBy,
                ActionType.REJECT_REPORT,
                COURSE_CLASS_REPORT_TABLE,
                courseClassReport.logId
        ))
        return success
    }

    @Transactional
    override fun getStageEntriesOfCoursesInClass(classId: Int): CourseClassStageCollectionOutputModel {
        val stageEntries = classDAO.getStageEntriesOfCoursesInClass(classId).map { toCourseClassStageOutputModel(it) }
        return toCourseClassStageCollectionOutputModel(stageEntries)
    }

    @Transactional
    override fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): CourseClassStageOutputModel {
        return toCourseClassStageOutputModel(classDAO.getSpecificStagedCourseInClass(classId, stageId)
                .orElseThrow { NotFoundException("No staged course class", "Try other staged id") })
    }

    @Transactional
    override fun createStagingCourseInClass(classId: Int, courseId: Int, principal: Principal): CourseClassStageOutputModel {
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
        return toCourseClassStageOutputModel(courseClassStage)
    }

    @Transactional
    override fun addCourseInClassFromStaged(classId: Int, stageId: Int, principal: Principal): CourseClassOutputModel {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found", "Try other id") }
        val courseClassStage = classDAO.getSpecificStagedCourseInClass(classId, stageId)
                .orElseThrow { NotFoundException("No staged course in class found", "Try other staged id") }

        val created = classDAO.addCourseToClass(stagedToCourseClass(courseClassStage))
        val term = getTerm(klass.termId)
        val course = courseDAO.getSpecificCourse(created.classId)
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
        return toCourseClassOutputModel(course, klass, created, term)
    }

    @Transactional
    override fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseClassStage = classDAO.getSpecificStagedCourseInClass(classId, stageId)
                .orElseThrow { NotFoundException("No staged course in class found", "Try other staged id") }
        resolveVote(principal.name, courseClassStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_CLASS_STAGE_TABLE, courseClassStage.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseClassStage.votes.dec() else courseClassStage.votes.inc()
        val success = classDAO.updateStagedCourseClassVotes(classId, stageId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                courseClassStage.createdBy,
                COURSE_CLASS_STAGE_TABLE,
                courseClassStage.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int, principal: Principal): Int {
        val courseClassStage = classDAO.getSpecificStagedCourseInClass(classId, stageId)
                .orElseThrow { NotFoundException("No staged course in class found", "Try other staged id") }
        val success = classDAO.deleteSpecificStagedCourseInClass(classId, stageId)
        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                courseClassStage.createdBy,
                ActionType.REJECT_STAGE,
                COURSE_CLASS_STAGE_TABLE,
                courseClassStage.logId
        ))
        return success
    }

    /**
     * Lectures Methods
     */

    @Transactional
    override fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): LectureCollectionOutputModel {
        val lectures = lectureDAO
                .getAllLecturesFromCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId)
                .map { toLectureOutputModel(it) }
        return toLectureCollectionOutputModel(lectures)
    }

    @Transactional
    override fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureOutputModel {
        return toLectureOutputModel(
                lectureDAO.getSpecificLectureFromCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        lectureId
                ).orElseThrow { NotFoundException("No Lecture found", "Try another id") }
        )
    }

    @Transactional
    override fun createLectureOnCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel, principal: Principal): LectureOutputModel {
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
        return toLectureOutputModel(lecture)
    }

    @Transactional
    override fun voteOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, vote: VoteInputModel, principal: Principal): Int {
        val lecture = lectureDAO.getSpecificLectureFromCourseInClass(
                classDAO.getCourseClass(classId, courseId).orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }.courseClassId,
                lectureId
        ).orElseThrow { NotFoundException("No Lecture found", "Try another id") }
        resolveVote(principal.name, lecture.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, LECTURE_TABLE, lecture.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) lecture.votes.dec() else lecture.votes.inc()
        val success = lectureDAO.updateVotesOnLecture(lectureId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                lecture.createdBy,
                LECTURE_TABLE,
                lecture.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun deleteSpecificLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, principal: Principal): Int {
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
        return success
    }

    @Transactional
    override fun getAllReportsOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureReportCollectionOutputModel {
        val reports = lectureDAO.getAllReportsOfLectureFromCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                lectureId
        ).map { toLectureReportOutputModel(it) }
        return toLectureReportCollectionOutputModel(reports)
    }

    @Transactional
    override fun getSpecificReportOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): LectureReportOutputModel {
        return toLectureReportOutputModel(lectureDAO.getSpecificReportOfLectureFromCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                lectureId,
                reportId
        ).orElseThrow { NotFoundException("No report found", "Try other report id") }
        )
    }

    @Transactional
    override fun deleteSpecificReportOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, principal: Principal): Int {
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
        return success
    }

    @Transactional
    override fun createReportOnLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, lectureReportInputModel: LectureReportInputModel, principal: Principal): LectureReport {
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
        return lectureReport
    }

    @Transactional
    override fun voteOnReportOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val lectureReport = lectureDAO.getSpecificReportOfLectureFromCourseInClass(courseClassId, lectureId, reportId)
                .orElseThrow { NotFoundException("No Lecture found", "Try another id") }
        resolveVote(principal.name, lectureReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, LECTURE_REPORT_TABLE, lectureReport.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) lectureReport.votes.dec() else lectureReport.votes.inc()
        val success = lectureDAO.updateVotesOnReportedLecture(lectureId, reportId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                lectureReport.reportedBy,
                LECTURE_REPORT_TABLE,
                lectureReport.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun updateLectureFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int, principal: Principal): LectureOutputModel {
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
        return toLectureOutputModel(res)
    }

    @Transactional
    override fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): LectureStageCollectionOutputModel {
        val stagedLectures = lectureDAO.getAllStagedLecturesOfCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
        ).map { toLectureStageOutputModel(it) }
        return toLectureStageCollectionOutputModel(stagedLectures)
    }

    @Transactional
    override fun getSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): LectureStageOutputModel {
        return toLectureStageOutputModel(lectureDAO.getSpecificStagedLectureOfCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                stageId
        ).orElseThrow { NotFoundException("No staged lecture found", "Try with other id") })
    }

    @Transactional
    override fun createStagingLectureOfCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel, principal: Principal): LectureStageOutputModel {
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
        return toLectureStageOutputModel(lectureStage)
    }

    @Transactional
    override fun deleteSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, principal: Principal): Int {
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
        return success
    }

    @Transactional
    override fun voteOnStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val lectureStage = lectureDAO.getSpecificStagedLectureOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId, stageId)
                .orElseThrow { NotFoundException("No Lecture stage found", "Try another id") }
        resolveVote(principal.name, lectureStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, LECTURE_STAGE_TABLE, lectureStage.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) lectureStage.votes.dec() else lectureStage.votes.inc()
        val success = lectureDAO.updateVotesOnStagedLecture(stageId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                lectureStage.createdBy,
                LECTURE_STAGE_TABLE,
                lectureStage.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun createLectureFromStaged(classId: Int, courseId: Int, stageId: Int, principal: Principal): LectureOutputModel {
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
        return toLectureOutputModel(createdLecture)
    }

    @Transactional
    override fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureVersionCollectionOutputModel {
        val lectureVersions = lectureDAO.getAllVersionsOfLectureOfCourseInclass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                lectureId
        ).map { toLectureVersionOutputModel(it) }
        return toLectureVersionCollectionOutputModel(lectureVersions)
    }

    @Transactional
    override fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): LectureVersionOutputModel {
        return toLectureVersionOutputModel(lectureDAO.getSpecificVersionOfLectureOfCourseInClass(
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

    @Transactional
    override fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): HomeworkCollectionOutputModel {
        val homeworks = homeworkDAO.getAllHomeworksFromCourseInClass(classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId)
        return toHomeworkCollectionOutputModel(homeworks.map { toHomeworkOutputModel(it) })
    }

    @Transactional
    override fun getSpecificHomeworkFromSpecificCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkOutputModel {
        return toHomeworkOutputModel(homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                homeworkId
        ).orElseThrow { NotFoundException("No homework found", "Try other homework id") })
    }

    @Transactional
    override fun createHomeworkOnCourseInClass(
            sheet: MultipartFile,
            classId: Int,
            courseId: Int,
            homeworkInputModel: HomeworkInputModel,
            principal: Principal
    ): HomeworkOutputModel {
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
        return toHomeworkOutputModel(createdHomework)
    }

    @Transactional
    override fun voteOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, vote: VoteInputModel, principal: Principal): Int {
        val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId, homeworkId)
                .orElseThrow { NotFoundException("No homework found", "Try other id") }
        resolveVote(principal.name, homework.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, HOMEWORK_TABLE, homework.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) homework.votes.dec() else homework.votes.inc()
        val success = homeworkDAO.updateVotesOnHomework(homeworkId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                homework.createdBy,
                HOMEWORK_TABLE,
                homework.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun deleteSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, principal: Principal): Int {
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
        return success
    }

    @Transactional
    override fun getAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): HomeworkStageCollectionOutputModel {
        val stagedHomeworks = homeworkDAO.getAllStagedHomeworksOfCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
        ).map { toHomeworkStagedOutputModel(it) }
        return toHomeworkStageCollectionOutputModel(stagedHomeworks)
    }

    @Transactional
    override fun getSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): HomeworkStageOutputModel {
        return toHomeworkStagedOutputModel(
                homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId, stageId)
                        .orElseThrow { NotFoundException("No staged homework", "Try with other stage id") }
        )
    }

    @Transactional
    override fun createStagingHomeworkOnCourseInClass(sheet: MultipartFile, classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel, principal: Principal): HomeworkStageOutputModel {
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
        return toHomeworkStagedOutputModel(stagingHomework)
    }

    @Transactional
    override fun createHomeworkFromStaged(classId: Int, courseId: Int, stageId: Int, principal: Principal): HomeworkOutputModel {
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
        return toHomeworkOutputModel(createdHomework)
    }

    @Transactional
    override fun voteOnStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val homeworkStage = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId, stageId)
                .orElseThrow { NotFoundException("No staged homework", "Try with other stage id") }
        resolveVote(principal.name, homeworkStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, HOMEWORK_STAGE_TABLE, homeworkStage.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) homeworkStage.votes.dec() else homeworkStage.votes.inc()
        val success = homeworkDAO.updateVotesOnStagedHomework(stageId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                homeworkStage.createdBy,
                HOMEWORK_STAGE_TABLE,
                homeworkStage.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun deleteSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, principal: Principal): Int {
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
        return success
    }

    @Transactional
    override fun getAllReportsOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): HomeworkReportCollectionOutputModel {
        val reports = homeworkDAO.getAllReportsOfHomeworkFromCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                homeWorkId
        ).map { toHomeworkReportOutputModel(it) }
        return toHomeworkReportCollectionOutputModel(reports)
    }

    @Transactional
    override fun getSpecificReportOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): HomeworkReportOutputModel {
        return toHomeworkReportOutputModel(
                homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        homeworkId,
                        reportId
                ).orElseThrow { NotFoundException("No report found", "Try with other report id") }
        )
    }

    @Transactional
    override fun createReportOnHomeworkFromCourseInClass(
            classId: Int,
            courseId: Int,
            homeworkId: Int,
            homeworkReportInputModel: HomeworkReportInputModel,
            principal: Principal
    ): HomeworkReportOutputModel {
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
        return toHomeworkReportOutputModel(homeworkReport)
    }

    @Transactional
    override fun updateHomeworkFromReport(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, principal: Principal): HomeworkOutputModel {
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
        return toHomeworkOutputModel(res)
    }

    @Transactional
    override fun voteOnReportOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val homeworkReport = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(courseClassId, homeworkId, reportId)
                .orElseThrow { NotFoundException("No homework report found", "Try other id") }
        resolveVote(principal.name, homeworkReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, HOMEWORK_REPORT_TABLE, homeworkReport.logId))

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) homeworkReport.votes.dec() else homeworkReport.votes.inc()
        val success = homeworkDAO.updateVotesOnReportedHomework(homeworkId, reportId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                homeworkReport.reportedBy,
                HOMEWORK_REPORT_TABLE,
                homeworkReport.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    @Transactional
    override fun deleteSpecificReportOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, principal: Principal): Int {
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
        return success
    }

    @Transactional
    override fun getAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkVersionCollectionOutputModel {
        val homeworkVersions = homeworkDAO.getAllVersionsOfHomeworkOfCourseInclass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                homeworkId
        ).map { toHomeworkVersionOutputModel(it) }
        return toHomeworkVersionCollectionOutputModel(homeworkVersions)
    }

    @Transactional
    override fun getSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): HomeworkVersionOutputModel {
        return toHomeworkVersionOutputModel(
                homeworkDAO.getSpecificVersionOfHomeworkOfCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        homeworkId,
                        version
                ).orElseThrow { NotFoundException("No version found", "Try with other version number") })
    }
}
