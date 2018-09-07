package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseClassReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.HomeworkReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.LectureReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.*
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
import isel.leic.ps.eduWikiAPI.utils.resolveApproval
import isel.leic.ps.eduWikiAPI.utils.resolveVote
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@Transactional
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
    lateinit var programmeDAO: ProgrammeDAO
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

    fun getProgramme(programmeId: Int): Programme = programmeDAO.getSpecificProgramme(programmeId)
            .orElseThrow { UnknownDataException("Can't find programme with id $programmeId", "Try Again Later") }

    fun getProgrammeFromReport(programmeId: Int?): Programme? {
        if (programmeId != null) return getProgramme(programmeId)
        return null
    }

    fun getClass(classId: Int): Class = classDAO.getSpecificClass(classId)
            .orElseThrow { UnknownDataException("Can't find classs with id $classId", "Try Again Later") }

    fun getCourseNameFromReport(courseId: Int?): String? {
        if (courseId != null) return getCourse(courseId).shortName
        return null
    }

    fun getClassNameFromReport(classId: Int?): String? {
        if (classId != null) return getClass(classId).className
        return null
    }
    // ---------- Class ----------

    // ----------------------------
    // Class Methods
    // ----------------------------

    override fun getAllClasses(): ClassCollectionOutputModel {
        val allClasses = classDAO.getAllClasses()
        val classList = allClasses.map {
            toClassOutputModel(it, getTerm(it.termId), getProgramme(it.programmeId))
        }
        return toClassCollectionOutputModel(classList)
    }

    override fun getSpecificClass(classId: Int): ClassOutputModel {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found with id $classId", "Try other ID") }
        return toClassOutputModel(klass, getTerm(klass.termId), getProgramme(klass.programmeId))
    }

    override fun createClass(input: ClassInputModel, principal: Principal): ClassOutputModel {
        programmeDAO.getSpecificProgramme(input.programmeId)
                .orElseThrow { NotFoundException("No programme found", "Please select a valid programme") }
        termDAO.getTerm(input.termId)
                .orElseThrow { NotFoundException("Term not found", "Please specify a valid term") }
        val klass = classDAO.createClass(toClass(input, principal.name))
        classDAO.createClassVersion(toClassVersion(klass))
        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                CLASS_TABLE,
                klass.logId
        ))
        return toClassOutputModel(klass, getTerm(klass.termId), getProgramme(klass.programmeId))
    }

    override fun voteOnClass(classId: Int, vote: VoteInputModel, principal: Principal): Int {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class with specified id found", "Try another id") }
        resolveVote(principal.name, klass.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, CLASS_TABLE, klass.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) klass.votes.dec() else klass.votes.inc()
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

    override fun partialUpdateOnClass(classId: Int, input: ClassInputModel, principal: Principal): ClassOutputModel {
        if (input.programmeId != 0) programmeDAO.getSpecificProgramme(input.programmeId)
                .orElseThrow { NotFoundException("No programme found", "Please select a valid programme") }
        val oldClass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found with id $classId", "Try other ID") }
        val newClass = classDAO.updateClass(Class(
                classId = classId,
                version = oldClass.version.inc(),
                logId = oldClass.logId,
                createdBy = principal.name,
                className = if (input.className.isEmpty()) oldClass.className else input.className,
                programmeId = if (input.programmeId == 0) oldClass.programmeId else input.programmeId
        ))
        classDAO.createClassVersion(toClassVersion(newClass))
        publisher.publishEvent(ResourceUpdatedEvent(
                principal.name,
                CLASS_TABLE,
                newClass.logId
        ))
        return toClassOutputModel(newClass, getTerm(newClass.termId), getProgramme(newClass.programmeId))
    }

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

    override fun getAllReportsOfClass(classId: Int): ClassReportCollectionOutputModel {
        val reports = classDAO.getAllReportsFromClass(classId).map {

            toClassReportOutputModel(it, getTerm(it.termId), getProgrammeFromReport(it.programmeId))
        }
        return toClassReportCollectionOutputModel(reports)
    }

    override fun getSpecificReportOfClass(classId: Int, reportId: Int): ClassReportOutputModel {
        val report = classDAO.getSpecificReportFromClass(classId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try other report Id") }
        return toClassReportOutputModel(report, getTerm(report.termId), getProgrammeFromReport(report.programmeId))
    }

    override fun createClassReport(classId: Int, report: ClassReportInputModel, principal: Principal): ClassReportOutputModel {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found", "try other Id") }
        val classReport = classDAO.reportClass(classId, toClassReport(klass, report, principal.name))
        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                CLASS_REPORT_TABLE,
                classReport.logId
        ))
        return toClassReportOutputModel(
                classReport,
                getTerm(classReport.termId),
                getProgrammeFromReport(classReport.programmeId)
        )
    }

    override fun voteOnReportClass(classId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val classReport = classDAO.getSpecificReportFromClass(classId, reportId)
                .orElseThrow { NotFoundException("No class report found", "Try other ID") }
        resolveVote(principal.name, classReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, CLASS_REPORT_TABLE, classReport.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) classReport.votes.dec() else classReport.votes.inc()
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

    override fun updateClassFromReport(classId: Int, reportId: Int, principal: Principal): ClassOutputModel {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found", "Try other ID") }
        val report = classDAO.getSpecificReportFromClass(classId, reportId)
                .orElseThrow { NotFoundException("No class report found", "Try other ID") }
        resolveApproval(principal.name, report.reportedBy)

        val updatedClass = classDAO.updateClass(Class(
                classId = classId,
                termId = report.termId,
                version = klass.version.inc(),
                className = report.className ?: klass.className,
                programmeId = report.programmeId ?: klass.programmeId,
                createdBy = report.reportedBy,
                votes = klass.votes
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
        return toClassOutputModel(updatedClass, getTerm(updatedClass.termId), getProgramme(updatedClass.programmeId))
    }

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

    override fun getAllStagedClasses(): ClassStageCollectionOutputModel {
        val stagedClasses = classDAO.getAllStagedClasses().map {
            toClassStagedOutputModel(it, getTerm(it.termId), getProgramme(it.programmeId))
        }
        return toClassStageCollectionOutputModel(stagedClasses)
    }

    override fun getSpecificStagedClass(stageId: Int): ClassStageOutputModel {
        val classStage = classDAO.getSpecificStagedClass(stageId)
                .orElseThrow { NotFoundException("No class staged found", "Try other id") }
        return toClassStagedOutputModel(classStage, getTerm(classStage.termId), getProgramme(classStage.programmeId))
    }

    override fun createStagingClass(classStageInputModel: ClassInputModel, principal: Principal): ClassStageOutputModel {
        val classStage = classDAO.createStagedClass(toClassStage(classStageInputModel, principal.name))
        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                CLASS_STAGE_TABLE,
                classStage.logId
        ))
        return toClassStagedOutputModel(classStage, getTerm(classStage.termId), getProgramme(classStage.programmeId))
    }

    override fun createClassFromStaged(stageId: Int, principal: Principal): ClassOutputModel {
        val classStaged = classDAO.getSpecificStagedClass(stageId)
                .orElseThrow { NotFoundException("No class stage found", "Try other ID") }
        resolveApproval(principal.name, classStaged.createdBy)

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
        return toClassOutputModel(createdClass, getTerm(createdClass.termId), getProgramme(createdClass.programmeId))
    }

    override fun voteOnStagedClass(stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val classStage = classDAO.getSpecificStagedClass(stageId)
                .orElseThrow { NotFoundException("No class stage found", "Try other ID") }
        resolveVote(principal.name, classStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, CLASS_STAGE_TABLE, classStage.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) classStage.votes.dec() else classStage.votes.inc()
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

    override fun getAllVersionsOfClass(classId: Int): ClassVersionCollectionOutputModel {
        val classVersions = classDAO.getAllVersionsOfSpecificClass(classId).map {
            toClassVersionOutputModel(it, getTerm(it.termId), getProgramme(it.programmeId))
        }
        return toClassVersionCollectionOutputModel(classVersions)
    }

    override fun getSpecificVersionOfClass(classId: Int, versionId: Int): ClassVersionOutputModel {
        val classVersion = classDAO.getVersionOfSpecificClass(classId, versionId)
                .orElseThrow { NotFoundException("No version found", "Try with other version number") }
        return toClassVersionOutputModel(
                classVersion,
                getTerm(classVersion.termId),
                getProgramme(classVersion.programmeId)
        )
    }

    /**
     * Course Class
     */
    override fun getAllCoursesOfClass(classId: Int): CourseClassCollectionOutputModel {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found with this id", "Try other id") }
        val courseClasses = classDAO.getAllCoursesOfClass(classId).map {
            toCourseClassOutputModel(getCourse(it.courseId), klass, it, getTerm(it.termId))
        }
        return toCourseClassCollectionOutputModel(courseClasses)
    }

    override fun getSpecificCourseOfClass(classId: Int, courseId: Int): CourseClassOutputModel {
        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found with this id", "Try other id") }
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course found with this id for this class", "Try other courseId") }
        val course = getCourse(courseId)
        return toCourseClassOutputModel(course, klass, courseClass, getTerm(courseClass.termId))
    }

    override fun addCourseToClass(classId: Int, courseId: Int, principal: Principal): CourseClassOutputModel {

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

    override fun voteOnCourseInClass(classId: Int, courseId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course and class association found", "Try other id") }
        resolveVote(principal.name, courseClass.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_CLASS_TABLE, courseClass.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) courseClass.votes.dec() else courseClass.votes.inc()
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

    override fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): CourseClassReportCollectionOutputModel {
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course class found", "Try with other id") }
        val reports = classDAO.getAllReportsOfCourseInClass(
                courseClass.courseClassId
        ).map {
            toCourseClassReportOutputModel(
                    it,
                    getCourseNameFromReport(it.courseId),
                    getTerm(courseClass.termId).shortName,
                    getClassNameFromReport(it.classId)
            )
        }
        return toCourseClassReportCollectionOutputModel(reports)
    }

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): CourseClassReportOutputModel {
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course class found", "Try with other id") }
        val report = classDAO.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                .orElseThrow { NotFoundException("No report found", "Try with other report ID") }
        return toCourseClassReportOutputModel(
                report,
                getCourseNameFromReport(report.courseId),
                getTerm(courseClass.termId).shortName,
                getClassNameFromReport(report.classId)
        )
    }

    override fun reportCourseInClass(classId: Int, courseId: Int, courseClassReportInputModel: CourseClassReportInputModel, principal: Principal): CourseClassReportOutputModel {
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course and class association found", "Try other id") }
        val report = classDAO.reportCourseInClass(toCourseClassReport(courseClass.courseClassId, courseClassReportInputModel, principal.name))
        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_CLASS_REPORT_TABLE,
                report.logId
        ))
        return toCourseClassReportOutputModel(
                report,
                getCourseNameFromReport(report.courseId),
                getTerm(courseClass.termId).shortName,
                getClassNameFromReport(report.classId)
        )
    }

    override fun updateCourseInClassFromReport(classId: Int, courseId: Int, reportId: Int, principal: Principal): CourseClassOutputModel {
        val courseClass = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("No course in class found", "Try other id") }
        val courseClassReport = classDAO.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                .orElseThrow { NotFoundException("No course in class report found", "Try other id") }
        resolveApproval(principal.name, courseClassReport.reportedBy)

        classDAO.deleteSpecificReportOnCourseClass(courseClass.courseClassId, reportId)
        classDAO.deleteSpecificCourseInClass(classId, courseId)

        val klass = classDAO.getSpecificClass(classId)
                .orElseThrow { NotFoundException("No class found", "Try other id") }
        val term = getTerm(klass.termId)
        val course = getCourse(courseId)
        val res = if (!courseClassReport.deletePermanently) {
            toCourseClassOutputModel(
                    course,
                    klass,
                    classDAO.addCourseToClass(CourseClass(
                            courseClassId = courseClass.courseClassId,
                            createdBy = courseClassReport.reportedBy,
                            courseId = courseClassReport.courseId ?: courseClass.courseId,
                            classId = courseClassReport.classId ?: courseClass.courseId,
                            termId = courseClassReport.termId,
                            votes = courseClass.votes
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
                if (courseClassReport.deletePermanently) ActionType.DELETE else ActionType.ALTER,
                COURSE_CLASS_TABLE,
                courseClass.logId
        ))
        return res
    }

    override fun voteOnReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseClassReport = classDAO.getSpecificReportOfCourseInClass(reportId, classId, courseId)
                .orElseThrow { NotFoundException("No course in class report found", "Try other id") }
        resolveVote(principal.name, courseClassReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_CLASS_REPORT_TABLE, courseClassReport.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) courseClassReport.votes.dec() else courseClassReport.votes.inc()
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

    override fun getStageEntriesOfCoursesInClass(classId: Int): CourseClassStageCollectionOutputModel {
        val stageEntries = classDAO.getStageEntriesOfCoursesInClass(classId).map {
            toCourseClassStageOutputModel(
                    it,
                    getCourse(it.courseId),
                    getTerm(it.termId),
                    getClass(it.classId)
            )
        }
        return toCourseClassStageCollectionOutputModel(stageEntries)
    }

    override fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): CourseClassStageOutputModel {
        val staged = classDAO.getSpecificStagedCourseInClass(classId, stageId)
                .orElseThrow { NotFoundException("No staged course class", "Try other staged id") }
        return toCourseClassStageOutputModel(
                staged,
                getCourse(staged.courseId),
                getTerm(staged.termId),
                getClass(staged.classId)
        )
    }

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
        return toCourseClassStageOutputModel(
                courseClassStage,
                getCourse(courseId),
                getTerm(termId),
                getClass(classId)
        )
    }

    override fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseClassStage = classDAO.getSpecificStagedCourseInClass(classId, stageId)
                .orElseThrow { NotFoundException("No staged course in class found", "Try other staged id") }
        resolveVote(principal.name, courseClassStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_CLASS_STAGE_TABLE, courseClassStage.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) courseClassStage.votes.dec() else courseClassStage.votes.inc()
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

    override fun getAllLecturesFromCourseInClass(classId: Int, courseId: Int): LectureCollectionOutputModel {
        val klass = getClass(classId)
        val course = getCourse(courseId)
        val term = getTerm(klass.termId)
        val lectures = lectureDAO
                .getAllLecturesFromCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId)
                .map { toLectureOutputModel(it, course, klass, term) }
        return toLectureCollectionOutputModel(lectures)
    }

    override fun getSpecificLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureOutputModel {
        val klass = getClass(classId)
        val course = getCourse(courseId)
        val term = getTerm(klass.termId)
        return toLectureOutputModel(
                lectureDAO.getSpecificLectureFromCourseInClass(
                        classDAO.getCourseClass(classId, courseId)
                                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                                .courseClassId,
                        lectureId
                ).orElseThrow { NotFoundException("No Lecture found", "Try another id") }
                , course, klass, term
        )
    }

    override fun createLectureOnCourseInClass(classId: Int, courseId: Int, lectureInputModel: LectureInputModel, principal: Principal): LectureOutputModel {
        val klass = getClass(classId)
        val course = getCourse(courseId)
        val term = getTerm(klass.termId)
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
        return toLectureOutputModel(lecture, course, klass, term)
    }

    override fun voteOnLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, vote: VoteInputModel, principal: Principal): Int {
        val lecture = lectureDAO.getSpecificLectureFromCourseInClass(
                classDAO.getCourseClass(classId, courseId).orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }.courseClassId,
                lectureId
        ).orElseThrow { NotFoundException("No Lecture found", "Try another id") }
        resolveVote(principal.name, lecture.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, LECTURE_TABLE, lecture.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) lecture.votes.dec() else lecture.votes.inc()
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

    override fun getAllReportsOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureReportCollectionOutputModel {
        val klass = getClass(classId)
        val course = getCourse(courseId)
        val term = getTerm(klass.termId)
        val reports = lectureDAO.getAllReportsOfLectureFromCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                lectureId
        ).map { toLectureReportOutputModel(it, course, klass, term) }
        return toLectureReportCollectionOutputModel(reports)
    }

    override fun getSpecificReportOfLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int): LectureReportOutputModel {
        val klass = getClass(classId)
        val course = getCourse(courseId)
        val term = getTerm(klass.termId)
        return toLectureReportOutputModel(lectureDAO.getSpecificReportOfLectureFromCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                lectureId,
                reportId
        ).orElseThrow { NotFoundException("No report found", "Try other report id") }, course, klass, term
        )
    }

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

    override fun createReportOnLectureFromCourseInClass(classId: Int, courseId: Int, lectureId: Int, lectureReportInputModel: LectureReportInputModel, principal: Principal): LectureReportOutputModel {
        val klass = getClass(classId)
        val course = getCourse(courseId)
        val term = getTerm(klass.termId)
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
        return toLectureReportOutputModel(lectureReport, course, klass, term)
    }

    override fun voteOnReportOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val lectureReport = lectureDAO.getSpecificReportOfLectureFromCourseInClass(courseClassId, lectureId, reportId)
                .orElseThrow { NotFoundException("No Lecture found", "Try another id") }
        resolveVote(principal.name, lectureReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, LECTURE_REPORT_TABLE, lectureReport.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) lectureReport.votes.dec() else lectureReport.votes.inc()
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

    override fun updateLectureFromReport(classId: Int, courseId: Int, lectureId: Int, reportId: Int, principal: Principal): LectureOutputModel {
        val klass = getClass(classId)
        val course = getCourse(courseId)
        val term = getTerm(klass.termId)
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val lecture = lectureDAO.getSpecificLectureFromCourseInClass(courseClassId, lectureId)
                .orElseThrow { NotFoundException("No Lecture found", "Try another id") }
        val lectureReport = lectureDAO.getSpecificReportOfLectureFromCourseInClass(courseClassId, lectureId, reportId)
                .orElseThrow { NotFoundException("No Lecture report found", "Try another id") }
        resolveApproval(principal.name, lectureReport.reportedBy)

        val res = lectureDAO.updateLecture(Lecture(
                lectureId = lectureId,
                createdBy = lectureReport.reportedBy,
                version = lecture.version.inc(),
                weekDay = lectureReport.weekDay ?: lecture.weekDay,
                begins = lectureReport.begins ?: lecture.begins,
                duration = lectureReport.duration ?: lecture.duration,
                location = lectureReport.location ?: lecture.location,
                votes = lecture.votes
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
        return toLectureOutputModel(res, course, klass, term)
    }

    override fun getAllStagedLecturesOfCourseInClass(classId: Int, courseId: Int): LectureStageCollectionOutputModel {
        val stagedLectures = lectureDAO.getAllStagedLecturesOfCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
        ).map { toLectureStageOutputModel(it) }
        return toLectureStageCollectionOutputModel(stagedLectures)
    }

    override fun getSpecificStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int): LectureStageOutputModel {
        return toLectureStageOutputModel(lectureDAO.getSpecificStagedLectureOfCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                stageId
        ).orElseThrow { NotFoundException("No staged lecture found", "Try with other id") })
    }

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

    override fun voteOnStagedLectureOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val lectureStage = lectureDAO.getSpecificStagedLectureOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId, stageId)
                .orElseThrow { NotFoundException("No Lecture stage found", "Try another id") }
        resolveVote(principal.name, lectureStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, LECTURE_STAGE_TABLE, lectureStage.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) lectureStage.votes.dec() else lectureStage.votes.inc()
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

    override fun createLectureFromStaged(classId: Int, courseId: Int, stageId: Int, principal: Principal): LectureOutputModel {
        val klass = getClass(classId)
        val course = getCourse(courseId)
        val term = getTerm(klass.termId)
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val stagedLecture = lectureDAO.getSpecificStagedLectureOfCourseInClass(courseClassId, stageId)
                .orElseThrow { NotFoundException("No Lecture stage found", "Try another id") }
        resolveApproval(principal.name, stagedLecture.createdBy)

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
        return toLectureOutputModel(createdLecture, course, klass, term)
    }

    override fun getAllVersionsOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int): LectureVersionCollectionOutputModel {
        val klass = getClass(classId)
        val course = getCourse(courseId)
        val term = getTerm(klass.termId)
        val lectureVersions = lectureDAO.getAllVersionsOfLectureOfCourseInclass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                lectureId
        ).map { toLectureVersionOutputModel(it, course, klass, term) }
        return toLectureVersionCollectionOutputModel(lectureVersions)
    }

    override fun getSpecificVersionOfLectureOfCourseInClass(classId: Int, courseId: Int, lectureId: Int, version: Int): LectureVersionOutputModel {
        val klass = getClass(classId)
        val course = getCourse(courseId)
        val term = getTerm(klass.termId)
        return toLectureVersionOutputModel(lectureDAO.getSpecificVersionOfLectureOfCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                lectureId,
                version
        ).orElseThrow { NotFoundException("No version for this lecture", "Try with other version number") }, course, klass, term)
    }

    /**
     * Homeworks Methods
     */

    override fun getAllHomeworksOfCourseInClass(classId: Int, courseId: Int): HomeworkCollectionOutputModel {
        val course = getCourse(courseId)
        val klass: Class = getClass(classId)
        val term: Term = getTerm(klass.termId)
        val homeworks = homeworkDAO.getAllHomeworksFromCourseInClass(classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId)
        return toHomeworkCollectionOutputModel(homeworks.map { toHomeworkOutputModel(it, course, klass, term) })
    }

    override fun getSpecificHomeworkFromSpecificCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkOutputModel {
        val course = getCourse(courseId)
        val klass: Class = getClass(classId)
        val term: Term = getTerm(klass.termId)
        val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                homeworkId
        ).orElseThrow { NotFoundException("No homework found", "Try other homework id") }
        return toHomeworkOutputModel(homework, course, klass, term)
    }

    override fun createHomeworkOnCourseInClass(
            sheet: MultipartFile?,
            classId: Int,
            courseId: Int,
            homeworkInputModel: HomeworkInputModel,
            principal: Principal
    ): HomeworkOutputModel {
        val course = getCourse(courseId)
        val klass: Class = getClass(classId)
        val term: Term = getTerm(klass.termId)
        val createdHomework = homeworkDAO.createHomeworkOnCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                toHomework(homeworkInputModel, principal.name, sheet)
        )
        homeworkDAO.createHomeworkVersion(toHomeworkVersion(createdHomework))

        if (sheet != null && createdHomework.sheetId != null)
            storageService.storeResource(createdHomework.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                HOMEWORK_TABLE,
                createdHomework.logId
        ))
        return toHomeworkOutputModel(createdHomework, course, klass, term)
    }

    override fun voteOnHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, vote: VoteInputModel, principal: Principal): Int {
        val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId, homeworkId)
                .orElseThrow { NotFoundException("No homework found", "Try other id") }
        resolveVote(principal.name, homework.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, HOMEWORK_TABLE, homework.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) homework.votes.dec() else homework.votes.inc()
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

    override fun deleteSpecificHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, principal: Principal): Int {
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(courseClassId, homeworkId)
                .orElseThrow { NotFoundException("No homework found", "Try other id") }

        val success = homeworkDAO.deleteSpecificHomeworkOfCourseInClass(courseClassId, homeworkId)
        if (homework.sheetId != null) storageService.deleteSpecificResource(homework.sheetId)

        publisher.publishEvent(ResourceDeletedEvent(
                principal.name,
                HOMEWORK_TABLE,
                homework.logId
        ))
        return success
    }

    override fun getAllStagedHomeworksOfCourseInClass(classId: Int, courseId: Int): HomeworkStageCollectionOutputModel {
        val stagedHomeworks = homeworkDAO.getAllStagedHomeworksOfCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId
        ).map { toHomeworkStagedOutputModel(it) }
        return toHomeworkStageCollectionOutputModel(stagedHomeworks)
    }

    override fun getSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int): HomeworkStageOutputModel {
        return toHomeworkStagedOutputModel(
                homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId, stageId)
                        .orElseThrow { NotFoundException("No staged homework", "Try with other stage id") }
        )
    }

    override fun createStagingHomeworkOnCourseInClass(sheet: MultipartFile?, classId: Int, courseId: Int, homeworkInputModel: HomeworkInputModel, principal: Principal): HomeworkStageOutputModel {
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val stagingHomework = homeworkDAO.createStagingHomeworkOnCourseInClass(
                courseClassId,
                toHomeworkStage(
                        homeworkInputModel, sheet, principal.name
                )
        )

        if (sheet != null && stagingHomework.sheetId != null)
            storageService.storeResource(stagingHomework.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                HOMEWORK_STAGE_TABLE,
                stagingHomework.logId
        ))
        return toHomeworkStagedOutputModel(stagingHomework)
    }

    override fun createHomeworkFromStaged(classId: Int, courseId: Int, stageId: Int, principal: Principal): HomeworkOutputModel {
        val course = getCourse(courseId)
        val klass: Class = getClass(classId)
        val term: Term = getTerm(klass.termId)
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val stagedHomework = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)
                .orElseThrow { NotFoundException("No staged homework", "Try with other stage id") }
        resolveApproval(principal.name, stagedHomework.createdBy)


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
        return toHomeworkOutputModel(createdHomework, course, klass, term)
    }

    override fun voteOnStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val homeworkStage = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId, stageId)
                .orElseThrow { NotFoundException("No staged homework", "Try with other stage id") }
        resolveVote(principal.name, homeworkStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, HOMEWORK_STAGE_TABLE, homeworkStage.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) homeworkStage.votes.dec() else homeworkStage.votes.inc()
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

    override fun deleteSpecificStagedHomeworkOfCourseInClass(classId: Int, courseId: Int, stageId: Int, principal: Principal): Int {
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val stagedHomework = homeworkDAO.getSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)
                .orElseThrow { NotFoundException("No staged homework", "Try with other stage id") }

        val success = homeworkDAO.deleteSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)

        if (stagedHomework.sheetId != null) storageService.deleteSpecificResource(stagedHomework.sheetId)
        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                stagedHomework.createdBy,
                ActionType.REJECT_STAGE,
                HOMEWORK_STAGE_TABLE,
                stagedHomework.logId
        ))
        return success
    }

    override fun getAllReportsOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeWorkId: Int): HomeworkReportCollectionOutputModel {
        val course = getCourse(courseId)
        val klass: Class = getClass(classId)
        val term: Term = getTerm(klass.termId)
        val reports = homeworkDAO.getAllReportsOfHomeworkFromCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                homeWorkId
        ).map { toHomeworkReportOutputModel(it, course, klass, term) }
        return toHomeworkReportCollectionOutputModel(reports)
    }

    override fun getSpecificReportOfHomeworkFromCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int): HomeworkReportOutputModel {
        val course = getCourse(courseId)
        val klass: Class = getClass(classId)
        val term: Term = getTerm(klass.termId)
        val report = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                homeworkId,
                reportId
        ).orElseThrow { NotFoundException("No report found", "Try with other report id") }
        return toHomeworkReportOutputModel(report, course, klass, term)
    }

    override fun createReportOnHomeworkFromCourseInClass(
            classId: Int,
            courseId: Int,
            homeworkId: Int,
            homeworkReportInputModel: HomeworkReportInputModel,
            principal: Principal
    ): HomeworkReportOutputModel {
        val course = getCourse(courseId)
        val klass: Class = getClass(classId)
        val term: Term = getTerm(klass.termId)
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
        return toHomeworkReportOutputModel(homeworkReport, course, klass, term)
    }

    override fun updateHomeworkFromReport(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, principal: Principal): HomeworkOutputModel {
        val course = getCourse(courseId)
        val klass: Class = getClass(classId)
        val term: Term = getTerm(klass.termId)
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val homework = homeworkDAO.getSpecificHomeworkFromSpecificCourseInClass(courseClassId, homeworkId)
                .orElseThrow { NotFoundException("No homework found", "Try other id") }
        val homeworkReport = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(courseClassId, homeworkId, reportId)
                .orElseThrow { NotFoundException("No homework report found", "Try other id") }
        resolveApproval(principal.name, homeworkReport.reportedBy)

        val res = homeworkDAO.updateHomework(Homework(
                homeworkId = homeworkId,
                createdBy = homeworkReport.reportedBy,
                version = homework.version.inc(),
                sheetId = homeworkReport.sheetId ?: homework.sheetId,
                dueDate = homeworkReport.dueDate ?: homework.dueDate,
                lateDelivery = homeworkReport.lateDelivery ?: homework.lateDelivery,
                multipleDeliveries = homeworkReport.multipleDeliveries ?: homework.multipleDeliveries,
                votes = homework.votes,
                homeworkName = homeworkReport.homeworkName ?: homework.homeworkName
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
        return toHomeworkOutputModel(res, course, klass, term)
    }

    override fun voteOnReportOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseClassId = classDAO.getCourseClass(classId, courseId)
                .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                .courseClassId
        val homeworkReport = homeworkDAO.getSpecificReportOfHomeworkFromCourseInClass(courseClassId, homeworkId, reportId)
                .orElseThrow { NotFoundException("No homework report found", "Try other id") }
        resolveVote(principal.name, homeworkReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, HOMEWORK_REPORT_TABLE, homeworkReport.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) homeworkReport.votes.dec() else homeworkReport.votes.inc()
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

    override fun getAllVersionsOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int): HomeworkVersionCollectionOutputModel {
        val course = getCourse(courseId)
        val klass: Class = getClass(classId)
        val term: Term = getTerm(klass.termId)
        val homeworkVersions = homeworkDAO.getAllVersionsOfHomeworkOfCourseInclass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                homeworkId
        ).map { toHomeworkVersionOutputModel(it, course, klass, term) }
        return toHomeworkVersionCollectionOutputModel(homeworkVersions)
    }

    override fun getSpecificVersionOfHomeworkOfCourseInClass(classId: Int, courseId: Int, homeworkId: Int, version: Int): HomeworkVersionOutputModel {
        val course = getCourse(courseId)
        val klass: Class = getClass(classId)
        val term: Term = getTerm(klass.termId)
        val homeworkVersion = homeworkDAO.getSpecificVersionOfHomeworkOfCourseInClass(
                classDAO.getCourseClass(classId, courseId)
                        .orElseThrow { NotFoundException("this course in class does not exist", "try other ids") }
                        .courseClassId,
                homeworkId,
                version
        ).orElseThrow { NotFoundException("No version found", "Try with other version number") }
        return toHomeworkVersionOutputModel(homeworkVersion, course, klass, term)

    }
}
