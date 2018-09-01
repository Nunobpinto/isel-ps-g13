package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.ExamInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.WorkAssignmentInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ClassCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ExamCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.TermCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.WorkAssignmentCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.CourseReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.ExamReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.WorkAssignmentReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.CourseStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.ExamStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.CourseVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.ExamVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.WorkAssignmentVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.*
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ExamReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.WorkAssignmentReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.ExamStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.WorkAssignmentStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.CourseVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ExamVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.WorkAssignmentVersionOutputModel
import isel.leic.ps.eduWikiAPI.eventListeners.events.*
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_TABLE
import isel.leic.ps.eduWikiAPI.repository.interfaces.*
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.CourseService
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
class CourseServiceImpl : CourseService {

    @Autowired
    lateinit var storageService: ResourceStorageService
    @Autowired
    lateinit var publisher: ApplicationEventPublisher
    @Autowired
    lateinit var courseDAO: CourseDAO
    @Autowired
    lateinit var examDAO: ExamDAO
    @Autowired
    lateinit var termDAO: TermDAO
    @Autowired
    lateinit var reputationDAO: ReputationDAO
    @Autowired
    lateinit var workAssignmentDAO: WorkAssignmentDAO

    // ---------- Course ----------

    // ----------------------------
    // Course Methods
    // ----------------------------

    override fun getAllCourses(): CourseCollectionOutputModel {
        val courses = courseDAO.getAllCourses().map { toCourseOutputModel(it) }
        return toCourseCollectionOutputModel(courses)
    }

    override fun getSpecificCourse(courseId: Int): CourseOutputModel =
            toCourseOutputModel(
                    courseDAO.getSpecificCourse(courseId)
                            .orElseThrow { NotFoundException("No course found", "Try again with other course id") }
            )


    override fun getClassesOfSpecificCourseInTerm(courseId: Int, termId: Int): ClassCollectionOutputModel {
        val classes =
                courseDAO.getClassesOfSpecificCourseInTerm(courseId, termId)
                        .map { toClassOutputModel(it, termDAO.getTerm(termId).orElseThrow { NotFoundException("No term found", "Try again with other term id") }) }
        return toClassCollectionOutputModel(classes)
    }

    override fun getSpecificClassOfSpecificCourseInTerm(courseId: Int, termId: Int, classId: Int): ClassOutputModel =
            toClassOutputModel(
                    courseDAO.getSpecificClassOfSpecificCourseInTerm(courseId, termId, classId)
                            .orElseThrow { NotFoundException("No class found", "Try again with other class id") },
                    termDAO.getTerm(termId).orElseThrow { NotFoundException("No term found", "Try again with other term id") }
            )

    override fun createCourse(inputCourse: CourseInputModel, principal: Principal): CourseOutputModel {
        val course = courseDAO.createCourse(toCourse(inputCourse, principal.name))
        courseDAO.createCourseVersion(toCourseVersion(course))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_TABLE,
                course.logId
        ))
        return toCourseOutputModel(course)
    }

    override fun voteOnCourse(courseId: Int, vote: VoteInputModel, principal: Principal): Int {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No Course found", "Try another id") }
        resolveVote(principal.name, course.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_TABLE, course.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) course.votes.dec() else course.votes.inc()
        val success = courseDAO.updateVotesOnCourse(courseId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                course.createdBy,
                COURSE_TABLE,
                course.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    override fun partialUpdateOnCourse(courseId: Int, inputCourse: CourseInputModel, principal: Principal): CourseOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No Course found", "Try another id") }

        val updatedCourse = courseDAO.updateCourse(Course(
                courseId = courseId,
                createdBy = principal.name,
                version = course.version.inc(),
                fullName = if (inputCourse.fullName.isEmpty()) course.fullName else inputCourse.fullName,
                shortName = if (inputCourse.shortName.isEmpty()) course.shortName else inputCourse.shortName
        ))
        courseDAO.createCourseVersion(toCourseVersion(updatedCourse))

        publisher.publishEvent(ResourceUpdatedEvent(
                principal.name,
                COURSE_TABLE,
                updatedCourse.logId
        ))
        return toCourseOutputModel(updatedCourse)
    }

    override fun deleteSpecificCourse(courseId: Int, principal: Principal): Int {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No Course found", "Try another id") }

        val success = courseDAO.deleteSpecificCourse(courseId)

        publisher.publishEvent(ResourceDeletedEvent(
                principal.name,
                COURSE_TABLE,
                course.logId
        ))
        return success
    }

    // ----------------------------
    // Course Stage Methods
    // ----------------------------

    override fun getAllCourseStageEntries(): CourseStageCollectionOutputModel {
        val stageEntries = courseDAO.getAllCourseStageEntries().map { toCourseStageOutputModel(it) }
        return toCourseStageCollectionOutputModel(stageEntries)
    }

    override fun getCourseSpecificStageEntry(stageId: Int): CourseStageOutputModel {
        return toCourseStageOutputModel(
                courseDAO.getCourseSpecificStageEntry(stageId)
                        .orElseThrow { NotFoundException("No staged course found", "Try again with other stage id") }
        )
    }

    override fun createStagingCourse(inputCourse: CourseInputModel, principal: Principal): CourseStageOutputModel {
        val courseStage = courseDAO.createStagingCourse(toCourseStage(inputCourse, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_STAGE_TABLE,
                courseStage.logId
        ))
        return toCourseStageOutputModel(courseStage)
    }

    override fun createCourseFromStaged(stageId: Int, principal: Principal): CourseOutputModel {
        val courseStage = courseDAO.getCourseSpecificStageEntry(stageId)
                .orElseThrow { NotFoundException("No staged course found", "Try again with other stage id") }
        resolveApproval(principal.name, courseStage.createdBy)

        val createdCourse = courseDAO.createCourse(stagedToCourse(courseStage))
        courseDAO.deleteStagedCourse(stageId)
        courseDAO.createCourseVersion(toCourseVersion(createdCourse))

        publisher.publishEvent(ResourceApprovedEvent(
                principal.name,
                ActionType.APPROVE_STAGE,
                COURSE_STAGE_TABLE,
                courseStage.logId,
                courseStage.createdBy,
                ActionType.CREATE,
                COURSE_TABLE,
                createdCourse.logId
        ))
        return toCourseOutputModel(createdCourse)
    }

    override fun voteOnStagedCourse(stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseStage = courseDAO.getCourseSpecificStageEntry(stageId)
                .orElseThrow { NotFoundException("No staged course found", "Try again with other stage id") }
        resolveVote(principal.name, courseStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_STAGE_TABLE, courseStage.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) courseStage.votes.dec() else courseStage.votes.inc()
        val success = courseDAO.updateVotesOnStagedCourse(stageId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                courseStage.createdBy,
                COURSE_STAGE_TABLE,
                courseStage.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    override fun deleteSpecificStagedCourse(stageId: Int, principal: Principal): Int {
        val courseStage = courseDAO.getCourseSpecificStageEntry(stageId)
                .orElseThrow { NotFoundException("No staged course found", "Try again with other stage id") }
        val success = courseDAO.deleteStagedCourse(stageId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                courseStage.createdBy,
                ActionType.REJECT_STAGE,
                COURSE_STAGE_TABLE,
                courseStage.logId
        ))
        return success
    }

    // ----------------------------
    // Course Report Methods
    // ----------------------------

    override fun getAllReportsOnCourse(courseId: Int): CourseReportCollectionOutputModel {
        val reports = courseDAO.getAllReportsOnCourse(courseId).map { toCourseReportOutputModel(it) }
        return toCourseReportCollectionOutputModel(reports)
    }

    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReportOutputModel {
        return toCourseReportOutputModel(
                courseDAO.getSpecificReportOfCourse(courseId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        )
    }

    override fun reportCourse(courseId: Int, inputCourseReport: CourseReportInputModel, principal: Principal): CourseReportOutputModel {
        val courseReport = courseDAO.reportCourse(courseId, toCourseReport(courseId, inputCourseReport, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_REPORT_TABLE,
                courseReport.logId
        ))
        return toCourseReportOutputModel(courseReport)
    }

    override fun voteOnReportedCourse(courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseReport = courseDAO.getSpecificReportOfCourse(courseId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        resolveVote(principal.name, courseReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, COURSE_REPORT_TABLE, courseReport.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) courseReport.votes.dec() else courseReport.votes.inc()
        val success = courseDAO.updateVotesOnReportedCourse(reportId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                courseReport.reportedBy,
                COURSE_REPORT_TABLE,
                courseReport.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    override fun updateReportedCourse(courseId: Int, reportId: Int, principal: Principal): CourseOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No course found", "Try another id") }
        val report = courseDAO.getSpecificReportOfCourse(courseId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        resolveApproval(principal.name, report.reportedBy)

        val updatedCourse = courseDAO.updateCourse(Course(
                courseId = courseId,
                version = course.version.inc(),
                createdBy = report.reportedBy,
                fullName = report.fullName ?: course.fullName,
                shortName = report.shortName ?: course.shortName,
                votes = course.votes
        ))
        courseDAO.createCourseVersion(toCourseVersion(updatedCourse))
        courseDAO.deleteReportOnCourse(reportId)

        publisher.publishEvent(ResourceApprovedEvent(
                principal.name,
                ActionType.APPROVE_REPORT,
                COURSE_REPORT_TABLE,
                report.logId,
                report.reportedBy,
                ActionType.ALTER,
                COURSE_TABLE,
                updatedCourse.logId
        ))
        return toCourseOutputModel(updatedCourse)
    }

    override fun deleteReportOnCourse(courseId: Int, reportId: Int, principal: Principal): Int {
        val courseReport = courseDAO.getSpecificReportOfCourse(courseId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }

        val success = courseDAO.deleteReportOnCourse(reportId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                courseReport.reportedBy,
                ActionType.REJECT_REPORT,
                COURSE_REPORT_TABLE,
                courseReport.logId
        ))
        return success
    }

    // ----------------------------
    // Course Version Methods
    // ----------------------------

    override fun getAllVersionsOfSpecificCourse(courseId: Int): CourseVersionCollectionOutputModel {
        val courseVersions = courseDAO.getAllVersionsOfSpecificCourse(courseId).map { toCourseVersionOutputModel(it) }
        return toCourseVersionCollectionOutputModel(courseVersions)
    }

    override fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): CourseVersionOutputModel {
        return toCourseVersionOutputModel(
                courseDAO.getVersionOfSpecificCourse(courseId, versionId)
                        .orElseThrow { NotFoundException("No version found", "Try again with other version number") }
        )
    }

    // ----------- TERM -----------

    // ----------------------------
    // Term Methods
    // ----------------------------

    override fun getTermsOfCourse(courseId: Int): TermCollectionOutputModel {
        return toTermCollectionOutputModel(courseDAO.getTermsOfCourse(courseId).map { toTermOutputModel(it) })
    }

    override fun getSpecificTermOfCourse(courseId: Int, termId: Int): TermOutputModel {
        return toTermOutputModel(
                courseDAO.getSpecificTermOfCourse(courseId, termId)
                        .orElseThrow { NotFoundException("No term found related to this course", "Try again with other term id or course id") }
        )
    }

    // ----------- EXAM -----------

    // ----------------------------
    // Exam Methods
    // ----------------------------

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): ExamCollectionOutputModel {
        val exams = examDAO.getAllExamsFromSpecificTermOfCourse(courseId, termId).map {
            val course = courseDAO.getSpecificCourse(courseId)
                    .orElseThrow { NotFoundException("No course found", "Try with other id") }
            val term = termDAO.getTerm(termId)
                    .orElseThrow { NotFoundException("No term found", "Try with other id") }
            toExamOutputModel(it, course, term)
        }
        return toExamCollectionOutputModel(exams)
    }

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): ExamOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No course found", "Try with other id") }
        val term = termDAO.getTerm(termId)
                .orElseThrow { NotFoundException("No term found", "Try with other id") }
        return toExamOutputModel(
                examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                        .orElseThrow { NotFoundException("No exam found", "Try again with other exam id") },
                course, term
        )
    }

    override fun createExamOnCourseInTerm(
            courseId: Int,
            termId: Int,
            sheet: MultipartFile?,
            inputExam: ExamInputModel,
            principal: Principal
    ): ExamOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No course found", "Try with other id") }
        val term = termDAO.getTerm(termId)
                .orElseThrow { NotFoundException("No term found", "Try with other id") }
        val exam = toExam(inputExam, sheet, principal.name)
        val createdExam = examDAO.createExamOnCourseInTerm(courseId, termId, exam)
        examDAO.createExamVersion(toExamVersion(createdExam))

        if (sheet != null && createdExam.sheetId != null)
            storageService.storeResource(createdExam.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                EXAM_TABLE,
                createdExam.logId
        ))
        return toExamOutputModel(createdExam, course, term)
    }

    override fun voteOnExam(termId: Int, courseId: Int, examId: Int, inputVote: VoteInputModel, principal: Principal): Int {
        val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                .orElseThrow { NotFoundException("No exam found", "Try again with other exam id") }
        resolveVote(principal.name, exam.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, EXAM_TABLE, exam.logId))

        val votes = if (Vote.valueOf(inputVote.vote) == Vote.Down) exam.votes.dec() else exam.votes.inc()
        val success = examDAO.updateVotesOnExam(examId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                exam.createdBy,
                EXAM_TABLE,
                exam.logId,
                Vote.valueOf(inputVote.vote)
        ))
        return success
    }

    override fun deleteSpecificExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int, principal: Principal): Int {
        val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                .orElseThrow { NotFoundException("No exam found", "Try again with other exam id") }

        if (exam.sheetId != null) storageService.deleteSpecificResource(exam.sheetId)
        val success = examDAO.deleteSpecificExamOfCourseInTerm(termId, courseId, examId)

        publisher.publishEvent(ResourceDeletedEvent(
                principal.name,
                EXAM_TABLE,
                exam.logId
        ))
        return success
    }

    // ----------------------------
    // Exam Stage Methods
    // ----------------------------

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): ExamStageCollectionOutputModel {
        return toExamStageCollectionOutputModel(examDAO.getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId).map { toExamStageOutputModel(it) })
    }

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStageOutputModel {
        return toExamStageOutputModel(
                examDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
                        .orElseThrow { NotFoundException("No exam staged found", "Try again with other staged id") }
        )
    }

    override fun createStagingExam(
            courseId: Int,
            termId: Int,
            examInputModel: ExamInputModel,
            sheet: MultipartFile?,
            principal: Principal
    ): ExamStageOutputModel {
        val stagingExam = examDAO.createStagingExamOnCourseInTerm(courseId, termId, toStageExam(examInputModel, sheet, principal.name))

        if (sheet != null && stagingExam.sheetId != null)
            storageService.storeResource(stagingExam.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                EXAM_STAGE_TABLE,
                stagingExam.logId
        ))
        return toExamStageOutputModel(stagingExam)
    }

    override fun createExamFromStaged(courseId: Int, termId: Int, stageId: Int, principal: Principal): ExamOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No course found", "Try with other id") }
        val term = termDAO.getTerm(termId)
                .orElseThrow { NotFoundException("No term found", "Try with other id") }
        val examStage = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No exam staged found", "Try again with other staged id") }
        resolveApproval(principal.name, examStage.createdBy)

        val exam = examDAO.createExamOnCourseInTerm(courseId, termId, stagedToExam(examStage))
        examDAO.createExamVersion(toExamVersion(exam))
        examDAO.deleteSpecificStagedExamOfCourseInTerm(courseId, termId, stageId)

        publisher.publishEvent(ResourceApprovedEvent(
                principal.name,
                ActionType.APPROVE_STAGE,
                EXAM_STAGE_TABLE,
                examStage.logId,
                examStage.createdBy,
                ActionType.CREATE,
                EXAM_TABLE,
                exam.logId
        ))
        return toExamOutputModel(exam, course, term)
    }

    override fun voteOnStagedExam(termId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val examStage = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No exam staged found", "Try again with other staged id") }
        resolveVote(principal.name, examStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, EXAM_STAGE_TABLE, examStage.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) examStage.votes.dec() else examStage.votes.inc()
        val success = examDAO.updateVotesOnStagedExam(stageId, votes)
        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                examStage.createdBy,
                EXAM_STAGE_TABLE,
                examStage.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    override fun deleteSpecificStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int, principal: Principal): Int {
        val stagedExam = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No exam staged found", "Try again with other staged id") }

        if (stagedExam.sheetId != null) storageService.deleteSpecificResource(stagedExam.sheetId)

        val success = examDAO.deleteSpecificStagedExamOfCourseInTerm(courseId, termId, stageId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                stagedExam.createdBy,
                ActionType.REJECT_STAGE,
                EXAM_STAGE_TABLE,
                stagedExam.logId
        ))
        return success
    }

    // ----------------------------
    // Exam Report Methods
    // ----------------------------

    override fun getAllReportsOnExamOnSpecificTermOfCourse(termId: Int, courseId: Int, examId: Int): ExamReportCollectionOutputModel {
        val reports = examDAO.getAllReportsOnExamOnSpecificTermOfCourse(courseId, termId, examId).map {
            val course = courseDAO.getSpecificCourse(courseId)
                    .orElseThrow { NotFoundException("No course found", "Try with other id") }
            val term = termDAO.getTerm(termId)
                    .orElseThrow { NotFoundException("No term found", "Try with other id") }
            toExamReportOutputModel(it, course, term)
        }
        return toExamReportCollectionOutputModel(reports)
    }

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(termId: Int, courseId: Int, examId: Int, reportId: Int): ExamReportOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No course found", "Try with other id") }
        val term = termDAO.getTerm(termId)
                .orElseThrow { NotFoundException("No term found", "Try with other id") }
        return toExamReportOutputModel(
                examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(courseId, termId, examId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try again with other report id") },
                course, term
        )
    }

    override fun addReportToExamOnCourseInTerm(termId: Int, courseId: Int, examId: Int, inputExamReport: ExamReportInputModel, principal: Principal): ExamReportOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No course found", "Try with other id") }
        val term = termDAO.getTerm(termId)
                .orElseThrow { NotFoundException("No term found", "Try with other id") }
        examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                .orElseThrow { NotFoundException("No exam found", "Try again") }

        val reportExam = examDAO.reportExam(toExamReport(examId, inputExamReport, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                EXAM_REPORT_TABLE,
                reportExam.logId
        ))
        return toExamReportOutputModel(reportExam, course, term)
    }

    override fun voteOnReportedExamOnCourseInTerm(termId: Int, courseId: Int, examId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val examReport = examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(courseId, termId, examId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        resolveVote(principal.name, examReport.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, EXAM_REPORT_TABLE, examReport.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) examReport.votes.dec() else examReport.votes.inc()
        val success = examDAO.updateVotesOnReportedExam(reportId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                examReport.reportedBy,
                EXAM_REPORT_TABLE,
                examReport.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    override fun updateReportedExam(examId: Int, reportId: Int, courseId: Int, termId: Int, principal: Principal): ExamOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No course found", "Try with other id") }
        val term = termDAO.getTerm(termId)
                .orElseThrow { NotFoundException("No term found", "Try with other id") }
        val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                .orElseThrow { NotFoundException("No exam found", "Try again with other ids") }
        val report = examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(courseId, termId, examId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other ids") }
        resolveApproval(principal.name, report.reportedBy)

        val updatedExam = examDAO.updateExam(examId, Exam(
                examId = exam.examId,
                version = exam.version.inc(),
                createdBy = report.reportedBy,
                sheetId = report.sheetId ?: exam.sheetId,
                dueDate = report.dueDate ?: exam.dueDate,
                type = report.type ?: exam.type,
                phase = report.phase ?: exam.phase,
                location = report.location ?: exam.location,
                votes = exam.votes
        ))
        examDAO.createExamVersion(toExamVersion(updatedExam))
        examDAO.deleteReportOnExam(courseId, termId, examId, reportId)

        publisher.publishEvent(ResourceApprovedEvent(
                principal.name,
                ActionType.APPROVE_REPORT,
                EXAM_REPORT_TABLE,
                report.logId,
                report.reportedBy,
                ActionType.ALTER,
                EXAM_TABLE,
                updatedExam.logId
        ))
        return toExamOutputModel(updatedExam, course, term)
    }


    override fun deleteReportOnExam(termId: Int, courseId: Int, examId: Int, reportId: Int, principal: Principal): Int {
        val examReport = examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(courseId, termId, examId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other ids") }

        val success = examDAO.deleteReportOnExam(courseId, termId, examId, reportId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                examReport.reportedBy,
                ActionType.REJECT_REPORT,
                EXAM_REPORT_TABLE,
                examReport.logId
        ))
        return success
    }

    // ----------------------------
    // Exam Version Methods
    // ----------------------------

    override fun getAllVersionsOfSpecificExam(termId: Int, courseId: Int, examId: Int): ExamVersionCollectionOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No course found", "Try with other id") }
        val term = termDAO.getTerm(termId)
                .orElseThrow { NotFoundException("No term found", "Try with other id") }
        val examVersions = examDAO.getAllVersionsOfSpecificExam(termId, courseId, examId).map {
            toExamVersionOutputModel(it, course, term)
        }
        return toExamVersionCollectionOutputModel(examVersions)
    }

    override fun getVersionOfSpecificExam(termId: Int, courseId: Int, examId: Int, versionId: Int): ExamVersionOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No course found", "Try with other id") }
        val term = termDAO.getTerm(termId)
                .orElseThrow { NotFoundException("No term found", "Try with other id") }
        return toExamVersionOutputModel(
                examDAO.getVersionOfSpecificExam(termId, courseId, examId, versionId)
                        .orElseThrow { NotFoundException("No version found", "Try again with other version number") },
                course, term
        )
    }

    // ------ WORK ASSIGNMENT -----

    // ----------------------------
    // Work Assignment Methods
    // ----------------------------

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): WorkAssignmentCollectionOutputModel {
        val workAssignments = workAssignmentDAO.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId).map { toWorkAssignmentOutputModel(it) }
        return toWorkAssignmentCollectionOutputModel(workAssignments)
    }

    override fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int, courseId: Int, termId: Int): WorkAssignmentOutputModel {
        return toWorkAssignmentOutputModel(
                workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(workAssignmentId, courseId, termId)
                        .orElseThrow { NotFoundException("No Work Assignment found", "Try again with other work assignment id") }
        )
    }

    override fun createWorkAssignmentOnCourseInTerm(
            courseId: Int,
            termId: Int,
            inputWorkAssignment: WorkAssignmentInputModel,
            sheet: MultipartFile?,
            principal: Principal,
            supplement: MultipartFile?
    ): WorkAssignmentOutputModel {
        val createdWorkAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(courseId, termId, toWorkAssignment(inputWorkAssignment, sheet, supplement, principal.name))

        workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(createdWorkAssignment))
        if (sheet != null && createdWorkAssignment.sheetId != null)
            storageService.storeResource(createdWorkAssignment.sheetId, sheet)
        if (supplement != null && createdWorkAssignment.supplementId != null)
            storageService.storeResource(createdWorkAssignment.supplementId, supplement)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                WORK_ASSIGNMENT_TABLE,
                createdWorkAssignment.logId
        ))
        return toWorkAssignmentOutputModel(createdWorkAssignment)
    }

    override fun voteOnWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, vote: VoteInputModel, principal: Principal): Int {
        val workAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(courseId, termId, workAssignmentId)
                .orElseThrow { NotFoundException("No Work Assignment found", "Try again with other work assignment id") }
        resolveVote(principal.name, workAssignment.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, WORK_ASSIGNMENT_TABLE, workAssignment.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) workAssignment.votes.dec() else workAssignment.votes.inc()
        val success = workAssignmentDAO.updateVotesOnWorkAssignment(workAssignmentId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                workAssignment.createdBy,
                WORK_ASSIGNMENT_TABLE,
                workAssignment.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    override fun deleteSpecificWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int, principal: Principal): Int {
        val workAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(workAssignmentId, courseId, termId)
                .orElseThrow { NotFoundException("No Work Assignment found", "Try again with other work assignment id") }

        if (workAssignment.sheetId != null) storageService.deleteSpecificResource(workAssignment.sheetId)
        if (workAssignment.supplementId != null) storageService.deleteSpecificResource(workAssignment.supplementId)

        val success = workAssignmentDAO.deleteSpecificWorkAssignment(courseId, termId, workAssignmentId)

        publisher.publishEvent(ResourceDeletedEvent(
                principal.name,
                WORK_ASSIGNMENT_TABLE,
                workAssignment.logId
        ))
        return success
    }

    // ----------------------------
    // Work Assignment Stage Methods
    // ----------------------------

    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStageOutputModel> {
        return workAssignmentDAO.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId).map { toWorkAssignmentStageOutputModel(it) }
    }

    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStageOutputModel {
        return toWorkAssignmentStageOutputModel(
                workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
                        .orElseThrow { NotFoundException("No Work Assignment Staged found", "Try again with other stage id") }
        )
    }

    override fun createStagingWorkAssignment(
            sheet: MultipartFile?,
            supplement: MultipartFile?,
            courseId: Int,
            termId: Int,
            inputWorkAssignment: WorkAssignmentInputModel,
            principal: Principal
    ): WorkAssignmentStageOutputModel {
        val stagingWorkAssignment = workAssignmentDAO.createStagingWorkAssingment(courseId, termId, toStageWorkAssignment(inputWorkAssignment, sheet, supplement, principal.name))

        if (sheet != null && stagingWorkAssignment.sheetId != null)
            storageService.storeResource(stagingWorkAssignment.sheetId, sheet)
        if (supplement != null && stagingWorkAssignment.supplementId != null)
            storageService.storeResource(stagingWorkAssignment.supplementId, supplement)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                WORK_ASSIGNMENT_STAGE_TABLE,
                stagingWorkAssignment.logId
        ))
        return toWorkAssignmentStageOutputModel(stagingWorkAssignment)
    }

    override fun createWorkAssignmentFromStaged(courseId: Int, termId: Int, stageId: Int, principal: Principal): WorkAssignmentOutputModel {
        val workAssignmentStage = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No Work Assignment Staged found", "Try again with other stage id") }
        resolveApproval(principal.name, workAssignmentStage.createdBy)

        val workAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(courseId, termId, stagedToWorkAssignment(workAssignmentStage))
        workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(workAssignment))
        workAssignmentDAO.deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId, termId, stageId)

        publisher.publishEvent(ResourceApprovedEvent(
                principal.name,
                ActionType.APPROVE_STAGE,
                WORK_ASSIGNMENT_STAGE_TABLE,
                workAssignmentStage.logId,
                workAssignmentStage.createdBy,
                ActionType.CREATE,
                WORK_ASSIGNMENT_TABLE,
                workAssignment.logId
        ))
        return toWorkAssignmentOutputModel(workAssignment)
    }

    override fun voteOnStagedWorkAssignment(termId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val workAssignmentStage = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No Work Assignment Staged found", "Try again with other stage id") }
        resolveVote(principal.name, workAssignmentStage.createdBy, reputationDAO.getActionLogsByUserAndResource(principal.name, WORK_ASSIGNMENT_STAGE_TABLE, workAssignmentStage.logId))

        val votes = if (Vote.valueOf(vote.vote) == Vote.Down) workAssignmentStage.votes.dec() else workAssignmentStage.votes.inc()
        val success = workAssignmentDAO.updateStagedWorkAssignmentVotes(stageId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                workAssignmentStage.createdBy,
                WORK_ASSIGNMENT_STAGE_TABLE,
                workAssignmentStage.logId,
                Vote.valueOf(vote.vote)
        ))
        return success
    }

    override fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int, principal: Principal): Int {
        val stagedWorkAssignment = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No Work Assignment Staged found", "Try again with other stage id") }

        if (stagedWorkAssignment.sheetId != null) storageService.deleteSpecificResource(stagedWorkAssignment.sheetId)
        if (stagedWorkAssignment.supplementId != null) storageService.deleteSpecificResource(stagedWorkAssignment.supplementId)

        val success = workAssignmentDAO.deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId, termId, stageId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                stagedWorkAssignment.createdBy,
                ActionType.REJECT_STAGE,
                WORK_ASSIGNMENT_STAGE_TABLE,
                stagedWorkAssignment.logId
        ))
        return success
    }

    // ----------------------------
    // Work Assignment Report Methods
    // ----------------------------

    override fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): WorkAssignmentReportCollectionOutputModel {
        val reports = workAssignmentDAO.getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId, termId, workAssignmentId).map { toWorkAssignmentReportOutputModel(it) }
        return toWorkAssignmentReportCollectionOutputModel(reports)
    }

    override fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int): WorkAssignmentReportOutputModel {
        return toWorkAssignmentReportOutputModel(
                workAssignmentDAO.getSpecificReportOfWorkAssignment(termId, courseId, workAssignmentId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        )
    }

    override fun addReportToWorkAssignmentOnCourseInTerm(
            termId: Int,
            courseId: Int,
            workAssignmentId: Int,
            inputWorkAssignmentReport: WorkAssignmentReportInputModel,
            principal: Principal
    ): WorkAssignmentReportOutputModel {
        val workAssignmentReport = workAssignmentDAO.addReportToWorkAssignmentOnCourseInTerm(workAssignmentId, toWorkAssignmentReport(workAssignmentId, inputWorkAssignmentReport, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                WORK_ASSIGNMENT_REPORT_TABLE,
                workAssignmentReport.logId
        ))
        return toWorkAssignmentReportOutputModel(workAssignmentReport)
    }

    override fun voteOnReportedWorkAssignmentOnCourseInTerm(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int, inputVote: VoteInputModel, principal: Principal): Int {
        val report = workAssignmentDAO.getSpecificReportOfWorkAssignment(termId, courseId, workAssignmentId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        resolveVote(principal.name, report.reportedBy, reputationDAO.getActionLogsByUserAndResource(principal.name, WORK_ASSIGNMENT_REPORT_TABLE, report.logId))

        val votes = if (Vote.valueOf(inputVote.vote) == Vote.Down) report.votes.dec() else report.votes.inc()
        val success = workAssignmentDAO.updateVotesOnReportedWorkAssignment(reportId, votes)

        publisher.publishEvent(VoteOnResourceEvent(
                principal.name,
                report.reportedBy,
                WORK_ASSIGNMENT_REPORT_TABLE,
                report.logId,
                Vote.valueOf(inputVote.vote)
        ))
        return success
    }

    override fun updateWorkAssignmentBasedOnReport(
            workAssignmentId: Int,
            reportId: Int,
            courseId: Int,
            termId: Int,
            principal: Principal
    ): WorkAssignmentOutputModel {
        val workAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(courseId, termId, workAssignmentId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        val report = workAssignmentDAO.getSpecificReportOfWorkAssignment(termId, courseId, workAssignmentId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        resolveApproval(principal.name, report.reportedBy)

        val updatedWorkAssignment = workAssignmentDAO.updateWorkAssignment(workAssignmentId, WorkAssignment(
                workAssignmentId = workAssignmentId,
                version = workAssignment.version.inc(),
                createdBy = report.reportedBy,
                sheetId = report.sheetId,
                supplementId = report.supplementId,
                dueDate = report.dueDate ?: workAssignment.dueDate,
                individual = report.individual ?: workAssignment.individual,
                lateDelivery = report.lateDelivery ?: workAssignment.lateDelivery,
                multipleDeliveries = report.multipleDeliveries ?: workAssignment.multipleDeliveries,
                requiresReport = report.requiresReport ?: workAssignment.requiresReport,
                votes = workAssignment.votes
        ))
        workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(updatedWorkAssignment))
        workAssignmentDAO.deleteReportOnWorkAssignment(termId, courseId, workAssignmentId, reportId)

        publisher.publishEvent(ResourceApprovedEvent(
                principal.name,
                ActionType.APPROVE_REPORT,
                WORK_ASSIGNMENT_REPORT_TABLE,
                report.logId,
                report.reportedBy,
                ActionType.ALTER,
                WORK_ASSIGNMENT_TABLE,
                updatedWorkAssignment.logId
        ))
        return toWorkAssignmentOutputModel(updatedWorkAssignment)
    }

    override fun deleteReportOnWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int, principal: Principal): Int {
        val report = workAssignmentDAO.getSpecificReportOfWorkAssignment(termId, courseId, workAssignmentId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        val success = workAssignmentDAO.deleteReportOnWorkAssignment(termId, courseId, workAssignmentId, reportId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                report.reportedBy,
                ActionType.REJECT_REPORT,
                WORK_ASSIGNMENT_REPORT_TABLE,
                report.logId
        ))
        return success
    }

    // ----------------------------
    // Work Assignment Version Methods
    // ----------------------------

    override fun getAllVersionsOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int): WorkAssignmentVersionCollectionOutputModel {
        val workAssignmentVersions = workAssignmentDAO.getAllVersionsOfSpecificWorkAssignment(termId, courseId, workAssignmentId).map { toWorkAssignmentVersionOutputModel(it) }
        return toWorkAssignmentVersionCollectionOutputModel(workAssignmentVersions)
    }

    override fun getVersionOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, versionId: Int): WorkAssignmentVersionOutputModel {
        return toWorkAssignmentVersionOutputModel(
                workAssignmentDAO.getVersionOfSpecificWorkAssignment(termId, courseId, workAssignmentId, versionId)
                        .orElseThrow { NotFoundException("No version found", "Try again with other version number") }
        )
    }

}