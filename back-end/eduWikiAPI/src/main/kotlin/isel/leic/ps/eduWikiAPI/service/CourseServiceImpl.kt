package isel.leic.ps.eduWikiAPI.service

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
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ExamOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TermOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.WorkAssignmentOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ExamCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.WorkAssignmentCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.CourseReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.ExamReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.WorkAssignmentReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.CourseStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.CourseVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.ExamVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.WorkAssignmentVersionCollectionOutputModel
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
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.*
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOImpl.Companion.EXAM_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOImpl.Companion.WORK_ASSIGNMENT_TABLE
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.CourseService
import isel.leic.ps.eduWikiAPI.service.interfaces.ResourceStorageService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

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
    lateinit var workAssignmentDAO: WorkAssignmentDAO

    // ---------- Course ----------

    // ----------------------------
    // Course Methods
    // ----------------------------

    @Transactional
    override fun getAllCourses(): CourseCollectionOutputModel {
        val courses = courseDAO.getAllCourses().map { toCourseOutputModel(it) }
        return toCourseCollectionOutputModel(courses)
    }

    @Transactional
    override fun getSpecificCourse(courseId: Int): CourseOutputModel {
        return toCourseOutputModel(
                courseDAO.getSpecificCourse(courseId)
                        .orElseThrow { NotFoundException("No course found", "Try again with other course id") }
        )
    }

    @Transactional
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

    @Transactional
    override fun voteOnCourse(courseId: Int, vote: VoteInputModel, principal: Principal): Int {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No Course found", "Try another id") }

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) course.votes.dec() else course.votes.inc()
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

    @Transactional
    override fun partialUpdateOnCourse(courseId: Int, inputCourse: CourseInputModel, principal: Principal): CourseOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No Course found", "Try another id") }

        val updatedCourse = courseDAO.updateCourse(Course(
                courseId = courseId,
                createdBy = principal.name,
                version = course.version.inc(),
                organizationId = course.organizationId,
                fullName = if(inputCourse.fullName.isEmpty()) course.fullName else inputCourse.fullName,
                shortName = if(inputCourse.shortName.isEmpty()) course.shortName else inputCourse.shortName
        ))
        courseDAO.createCourseVersion(toCourseVersion(updatedCourse))

        publisher.publishEvent(ResourceUpdatedEvent(
                principal.name,
                COURSE_TABLE,
                updatedCourse.logId
        ))
        return toCourseOutputModel(updatedCourse)
    }

    @Transactional
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

    @Transactional
    override fun getAllCourseStageEntries(): CourseStageCollectionOutputModel {
        val stageEntries = courseDAO.getAllCourseStageEntries().map { toCourseStageOutputModel(it) }
        return toCourseStageCollectionOutputModel(stageEntries)
    }

    @Transactional
    override fun getCourseSpecificStageEntry(stageId: Int): CourseStageOutputModel {
        return toCourseStageOutputModel(
                courseDAO.getCourseSpecificStageEntry(stageId)
                        .orElseThrow { NotFoundException("No staged course found", "Try again with other stage id") }
        )
    }

    @Transactional
    override fun createStagingCourse(inputCourse: CourseInputModel, principal: Principal): CourseStageOutputModel {
        val courseStage = courseDAO.createStagingCourse(toCourseStage(inputCourse, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_STAGE_TABLE,
                courseStage.logId
        ))
        return toCourseStageOutputModel(courseStage)
    }

    @Transactional
    override fun createCourseFromStaged(stageId: Int, principal: Principal): CourseOutputModel {
        val courseStage = courseDAO.getCourseSpecificStageEntry(stageId)
                .orElseThrow { NotFoundException("No staged course found", "Try again with other stage id") }
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

    @Transactional
    override fun voteOnStagedCourse(stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseStage = courseDAO.getCourseSpecificStageEntry(stageId)
                .orElseThrow { NotFoundException("No staged course found", "Try again with other stage id") }
        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseStage.votes.dec() else courseStage.votes.inc()
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

    @Transactional
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

    @Transactional
    override fun getAllReportsOnCourse(courseId: Int): CourseReportCollectionOutputModel {
        val reports = courseDAO.getAllReportsOnCourse(courseId).map { toCourseReportOutputModel(it) }
        return toCourseReportCollectionOutputModel(reports)
    }

    @Transactional
    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReportOutputModel {
        return toCourseReportOutputModel(
                courseDAO.getSpecificReportOfCourse(courseId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        )
    }

    @Transactional
    override fun reportCourse(courseId: Int, inputCourseReport: CourseReportInputModel, principal: Principal): CourseReportOutputModel {
        val courseReport = courseDAO.reportCourse(courseId, toCourseReport(courseId, inputCourseReport, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                COURSE_REPORT_TABLE,
                courseReport.logId
        ))
        return toCourseReportOutputModel(courseReport)
    }

    @Transactional
    override fun voteOnReportedCourse(courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val courseReport = courseDAO.getSpecificReportOfCourse(courseId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) courseReport.votes.dec() else courseReport.votes.inc()
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

    @Transactional
    override fun updateReportedCourse(courseId: Int, reportId: Int, principal: Principal): CourseOutputModel {
        val course = courseDAO.getSpecificCourse(courseId)
                .orElseThrow { NotFoundException("No course found", "Try another id") }
        val report = courseDAO.getSpecificReportOfCourse(courseId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }

        val updatedCourse = courseDAO.updateCourse(Course(
                courseId = courseId,
                organizationId = course.organizationId,
                version = course.version.inc(),
                createdBy = report.reportedBy,
                fullName = report.fullName ?: course.fullName,
                shortName = report.shortName ?: course.shortName
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

    @Transactional
    override fun deleteReportOnCourse(courseId: Int, reportId: Int, principal: Principal): Int {
        val courseReport = courseDAO.getSpecificReportOfCourse(courseId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }

        val success = courseDAO.deleteReportOnCourse(reportId)

        publisher.publishEvent(ResourceRejectedEvent(
                principal.name,
                courseReport.reportedBy,
                ActionType.APPROVE_REPORT,
                COURSE_REPORT_TABLE,
                courseReport.logId
        ))
        return success
    }

    // ----------------------------
    // Course Version Methods
    // ----------------------------

    @Transactional
    override fun getAllVersionsOfSpecificCourse(courseId: Int): CourseVersionCollectionOutputModel {
        val courseVersions = courseDAO.getAllVersionsOfSpecificCourse(courseId).map { toCourseVersionOutputModel(it) }
        return toCourseVersionCollectionOutputModel(courseVersions)
    }

    @Transactional
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

    @Transactional
    override fun getTermsOfCourse(courseId: Int): List<TermOutputModel> {
        return courseDAO.getTermsOfCourse(courseId).map {
            toTermOutputModel(it)
        }
    }

    @Transactional
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

    @Transactional
    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): ExamCollectionOutputModel {
        val exams = examDAO.getAllExamsFromSpecificTermOfCourse(courseId, termId).map { toExamOutputModel(it) }
        return toExamCollectionOutputModel(exams)
    }

    @Transactional
    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): ExamOutputModel {
        return toExamOutputModel(
                examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                        .orElseThrow { NotFoundException("No exam found", "Try again with other exam id") }
        )
    }

    @Transactional
    override fun createExamOnCourseInTerm(
            courseId: Int,
            termId: Int,
            sheet: MultipartFile,
            inputExam: ExamInputModel,
            principal: Principal
    ): ExamOutputModel {
        val createdExam = examDAO.createExamOnCourseInTerm(courseId, termId, toExam(inputExam, principal.name))
        examDAO.createExamVersion(toExamVersion(createdExam))
        storageService.storeResource(createdExam.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                EXAM_TABLE,
                createdExam.logId
        ))
        return toExamOutputModel(createdExam)
    }

    @Transactional
    override fun voteOnExam(termId: Int, courseId: Int, examId: Int, inputVote: VoteInputModel, principal: Principal): Int {
        val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                .orElseThrow { NotFoundException("No exam found", "Try again with other exam id") }
        val votes = if(Vote.valueOf(inputVote.vote) == Vote.Down) exam.votes.dec() else exam.votes.inc()
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

    @Transactional
    override fun deleteSpecificExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int, principal: Principal): Int {
        val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                .orElseThrow { NotFoundException("No exam found", "Try again with other exam id") }
        storageService.deleteSpecificResource(exam.sheetId)
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

    @Transactional
    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStageOutputModel> {
        return examDAO.getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId).map { toExamStageOutputModel(it) }
    }

    @Transactional
    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStageOutputModel {
        return toExamStageOutputModel(
                examDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
                        .orElseThrow { NotFoundException("No exam staged found", "Try again with other staged id") }
        )
    }

    @Transactional
    override fun createStagingExam(
            courseId: Int,
            termId: Int,
            examInputModel: ExamInputModel,
            sheet: MultipartFile,
            principal: Principal
    ): ExamStageOutputModel {
        val stagingExam = examDAO.createStagingExamOnCourseInTerm(courseId, termId, toStageExam(examInputModel, principal.name))
        storageService.storeResource(stagingExam.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                EXAM_STAGE_TABLE,
                stagingExam.logId
        ))
        return toExamStageOutputModel(stagingExam)
    }

    @Transactional
    override fun createExamFromStaged(courseId: Int, termId: Int, stageId: Int, principal: Principal): ExamOutputModel {
        val examStage = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No exam staged found", "Try again with other staged id") }

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
        return toExamOutputModel(exam)
    }

    @Transactional
    override fun voteOnStagedExam(termId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val examStage = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No exam staged found", "Try again with other staged id") }
        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) examStage.votes.dec() else examStage.votes.inc()
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

    @Transactional
    override fun deleteSpecificStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int, principal: Principal): Int {
        val stagedExam = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No exam staged found", "Try again with other staged id") }
        storageService.deleteSpecificResource(stagedExam.sheetId)
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

    @Transactional
    override fun getAllReportsOnExamOnSpecificTermOfCourse(termId: Int, courseId: Int, examId: Int): ExamReportCollectionOutputModel {
        val reports = examDAO.getAllReportsOnExamOnSpecificTermOfCourse(courseId, termId, examId).map { toExamReportOutputModel(it) }
        return toExamReportCollectionOutputModel(reports)
    }

    @Transactional
    override fun getSpecificReportOnExamOnSpecificTermOfCourse(termId: Int, courseId: Int, examId: Int, reportId: Int): ExamReportOutputModel {
        return toExamReportOutputModel(
                examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(courseId, termId, examId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        )
    }

    @Transactional
    override fun addReportToExamOnCourseInTerm(termId: Int, courseId: Int, examId: Int, inputExamReport: ExamReportInputModel, principal: Principal): ExamReportOutputModel {
        examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                .orElseThrow { NotFoundException("No exam found", "Try again") }

        val reportExam = examDAO.reportExam(toExamReport(examId, inputExamReport, principal.name))

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                EXAM_REPORT_TABLE,
                reportExam.logId
        ))
        return toExamReportOutputModel(reportExam)
    }

    @Transactional
    override fun voteOnReportedExamOnCourseInTerm(termId: Int, courseId: Int, examId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int {
        val examReport = examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(courseId, termId, examId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) examReport.votes.dec() else examReport.votes.inc()
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

    @Transactional
    override fun updateReportedExam(examId: Int, reportId: Int, courseId: Int, termId: Int, principal: Principal): ExamOutputModel {
        val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                .orElseThrow { NotFoundException("No exam found", "Try again with other ids") }
        val report = examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(courseId, termId, examId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other ids") }

        val updatedExam = examDAO.updateExam(examId, Exam(
                examId = exam.examId,
                version = exam.version.inc(),
                createdBy = report.reportedBy,
                sheetId = report.sheetId ?: exam.sheetId,
                dueDate = report.dueDate ?: exam.dueDate,
                type = report.type ?: exam.type,
                phase = report.phase ?: exam.phase,
                location = report.location ?: exam.location
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
        return toExamOutputModel(updatedExam)
    }


    @Transactional
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

    @Transactional
    override fun getAllVersionsOfSpecificExam(termId: Int, courseId: Int, examId: Int): ExamVersionCollectionOutputModel {
        val examVersions = examDAO.getAllVersionsOfSpecificExam(termId, courseId, examId).map { toExamVersionOutputModel(it) }
        return toExamVersionCollectionOutputModel(examVersions)
    }

    @Transactional
    override fun getVersionOfSpecificExam(termId: Int, courseId: Int, examId: Int, versionId: Int): ExamVersionOutputModel {
        return toExamVersionOutputModel(
                examDAO.getVersionOfSpecificExam(termId, courseId, examId, versionId)
                        .orElseThrow { NotFoundException("No version found", "Try again with other version number") }
        )
    }

    // ------ WORK ASSIGNMENT -----

    // ----------------------------
    // Work Assignment Methods
    // ----------------------------

    @Transactional
    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): WorkAssignmentCollectionOutputModel {
        val workAssignments = workAssignmentDAO.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId).map { toWorkAssignmentOutputModel(it) }
        return toWorkAssignmentCollectionOutputModel(workAssignments)
    }

    @Transactional
    override fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int, courseId: Int, termId: Int): WorkAssignmentOutputModel {
        return toWorkAssignmentOutputModel(
                workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(workAssignmentId, courseId, termId)
                        .orElseThrow { NotFoundException("No Work Assignment found", "Try again with other work assignment id") }
        )
    }

    @Transactional
    override fun createWorkAssignmentOnCourseInTerm(
            courseId: Int,
            termId: Int,
            inputWorkAssignment: WorkAssignmentInputModel,
            sheet: MultipartFile,
            principal: Principal
    ): WorkAssignmentOutputModel {
        val createdWorkAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(courseId, termId, toWorkAssignment(inputWorkAssignment, principal.name))
        workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(createdWorkAssignment))
        storageService.storeResource(createdWorkAssignment.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                WORK_ASSIGNMENT_TABLE,
                createdWorkAssignment.logId
        ))
        return toWorkAssignmentOutputModel(createdWorkAssignment)
    }

    @Transactional
    override fun voteOnWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, vote: VoteInputModel, principal: Principal): Int {
        val workAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(courseId, termId, workAssignmentId)
                .orElseThrow { NotFoundException("No Work Assignment found", "Try again with other work assignment id") }

        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) workAssignment.votes.dec() else workAssignment.votes.inc()
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

    @Transactional
    override fun deleteSpecificWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int, principal: Principal): Int {
        val workAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(workAssignmentId, courseId, termId)
                .orElseThrow { NotFoundException("No Work Assignment found", "Try again with other work assignment id") }
        storageService.deleteSpecificResource(workAssignment.sheetId)
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

    @Transactional
    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStageOutputModel> {
        return workAssignmentDAO.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId).map { toWorkAssignmentStageOutputModel(it) }
    }

    @Transactional
    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStageOutputModel {
        return toWorkAssignmentStageOutputModel(
                workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
                        .orElseThrow { NotFoundException("No Work Assignment Staged found", "Try again with other stage id") }
        )
    }

    @Transactional
    override fun createStagingWorkAssignment(
            sheet: MultipartFile,
            courseId: Int,
            termId: Int,
            inputWorkAssignment: WorkAssignmentInputModel,
            principal: Principal
    ): WorkAssignmentStageOutputModel {
        val stagingWorkAssignment = workAssignmentDAO.createStagingWorkAssingment(courseId, termId, toStageWorkAssignment(inputWorkAssignment, principal.name))
        storageService.storeResource(stagingWorkAssignment.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                WORK_ASSIGNMENT_STAGE_TABLE,
                stagingWorkAssignment.logId
        ))
        return toWorkAssignmentStageOutputModel(stagingWorkAssignment)
    }

    @Transactional
    override fun createWorkAssignmentFromStaged(courseId: Int, termId: Int, stageId: Int, principal: Principal): WorkAssignmentOutputModel {
        val workAssignmentStage = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No Work Assignment Staged found", "Try again with other stage id") }

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

    @Transactional
    override fun voteOnStagedWorkAssignment(termId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int {
        val workAssignmentStage = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No Work Assignment Staged found", "Try again with other stage id") }
        val votes = if(Vote.valueOf(vote.vote) == Vote.Down) workAssignmentStage.votes.dec() else workAssignmentStage.votes.inc()
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

    @Transactional
    override fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int, principal: Principal): Int {
        val stagedWorkAssignment = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
                .orElseThrow { NotFoundException("No Work Assignment Staged found", "Try again with other stage id") }
        storageService.deleteSpecificResource(stagedWorkAssignment.sheetId)
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

    @Transactional
    override fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): WorkAssignmentReportCollectionOutputModel {
        val reports = workAssignmentDAO.getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId, termId, workAssignmentId).map { toWorkAssignmentReportOutputModel(it) }
        return toWorkAssignmentReportCollectionOutputModel(reports)
    }

    @Transactional
    override fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int): WorkAssignmentReportOutputModel {
        return toWorkAssignmentReportOutputModel(
                workAssignmentDAO.getSpecificReportOfWorkAssignment(termId, courseId, workAssignmentId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        )
    }

    @Transactional
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

    @Transactional
    override fun voteOnReportedWorkAssignmentOnCourseInTerm(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int, inputVote: VoteInputModel, principal: Principal): Int {
        val report = workAssignmentDAO.getSpecificReportOfWorkAssignment(termId, courseId, workAssignmentId, reportId)
                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
        val votes = if(Vote.valueOf(inputVote.vote) == Vote.Down) report.votes.dec() else report.votes.inc()
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

    @Transactional
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
                requiresReport = report.requiresReport ?: workAssignment.requiresReport
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

    @Transactional
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

    @Transactional
    override fun getAllVersionsOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int): WorkAssignmentVersionCollectionOutputModel {
        val workAssignmentVersions = workAssignmentDAO.getAllVersionsOfSpecificWorkAssignment(termId, courseId, workAssignmentId).map { toWorkAssignmentVersionOutputModel(it) }
        return toWorkAssignmentVersionCollectionOutputModel(workAssignmentVersions)
    }

    @Transactional
    override fun getVersionOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, versionId: Int): WorkAssignmentVersionOutputModel {
        return toWorkAssignmentVersionOutputModel(
                workAssignmentDAO.getVersionOfSpecificWorkAssignment(termId, courseId, workAssignmentId, versionId)
                        .orElseThrow { NotFoundException("No version found", "Try again with other version number") }
        )
    }

}