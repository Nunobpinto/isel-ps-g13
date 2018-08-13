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
import isel.leic.ps.eduWikiAPI.domain.outputModel.CourseOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.ExamOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.TermOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.WorkAssignmentOutputModel
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
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi.Companion.EXAM_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOJdbi.Companion.WORK_ASSIGNMENT_REPORT_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOJdbi.Companion.WORK_ASSIGNMENT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOJdbi.Companion.WORK_ASSIGNMENT_TABLE
import isel.leic.ps.eduWikiAPI.service.interfaces.CourseService
import isel.leic.ps.eduWikiAPI.service.interfaces.ResourceStorageService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@Service
class CourseServiceImpl : CourseService {

    @Autowired
    lateinit var jdbi: Jdbi
    @Autowired
    lateinit var storageService: ResourceStorageService
    @Autowired
    lateinit var publisher: ApplicationEventPublisher

    // ---------- Course ----------

    // ----------------------------
    // Course Methods
    // ----------------------------

    override fun getAllCourses(): CourseCollectionOutputModel =
            jdbi.withExtension<CourseCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courses = it.getAllCourses().map { toCourseOutputModel(it) }
                toCourseCollectionOutputModel(courses)
            }

    override fun getSpecificCourse(courseId: Int): CourseOutputModel =
            jdbi.withExtension<CourseOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseOutputModel(
                        it.getSpecificCourse(courseId)
                                .orElseThrow { NotFoundException("No course found", "Try again with other course id") }
                )
            }

    override fun createCourse(inputCourse: CourseInputModel, principal: Principal): CourseOutputModel =
            jdbi.inTransaction<CourseOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

                val course = courseDAO.createCourse(toCourse(inputCourse, principal.name))
                courseDAO.createCourseVersion(toCourseVersion(course))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        COURSE_TABLE,
                        course.logId
                ))
                toCourseOutputModel(course)
            }

    override fun voteOnCourse(courseId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

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
                success
            }

    override fun partialUpdateOnCourse(courseId: Int, inputCourse: CourseInputModel, principal: Principal): CourseOutputModel =
            jdbi.inTransaction<CourseOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

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
                toCourseOutputModel(updatedCourse)
            }

    override fun deleteSpecificCourse(courseId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val course = it.getSpecificCourse(courseId)
                        .orElseThrow { NotFoundException("No Course found", "Try another id") }

                val success = it.deleteSpecificCourse(courseId)

                publisher.publishEvent(ResourceDeletedEvent(
                        principal.name,
                        COURSE_TABLE,
                        course.logId
                ))
                success
            }

    // ----------------------------
    // Course Stage Methods
    // ----------------------------

    override fun getAllCourseStageEntries(): CourseStageCollectionOutputModel =
            jdbi.withExtension<CourseStageCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val stageEntries = it.getAllCourseStageEntries().map { toCourseStageOutputModel(it) }
                toCourseStageCollectionOutputModel(stageEntries)
            }

    override fun getCourseSpecificStageEntry(stageId: Int): CourseStageOutputModel =
            jdbi.withExtension<CourseStageOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseStageOutputModel(
                        it.getCourseSpecificStageEntry(stageId)
                                .orElseThrow { NotFoundException("No staged course found", "Try again with other stage id") }
                )
            }

    override fun createStagingCourse(inputCourse: CourseInputModel, principal: Principal): CourseStageOutputModel =
            jdbi.withExtension<CourseStageOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseStage = it.createStagingCourse(toCourseStage(inputCourse, principal.name))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        COURSE_STAGE_TABLE,
                        courseStage.logId
                ))
                toCourseStageOutputModel(courseStage)
            }

    override fun createCourseFromStaged(stageId: Int, principal: Principal): CourseOutputModel =
            jdbi.inTransaction<CourseOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

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
                toCourseOutputModel(createdCourse)
            }

    override fun voteOnStagedCourse(stageId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

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
                success
            }

    override fun deleteSpecificStagedCourse(stageId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseStage = it.getCourseSpecificStageEntry(stageId)
                        .orElseThrow { NotFoundException("No staged course found", "Try again with other stage id") }
                val success = it.deleteStagedCourse(stageId)

                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        courseStage.createdBy,
                        ActionType.REJECT_STAGE,
                        COURSE_STAGE_TABLE,
                        courseStage.logId
                ))
                success
            }

    // ----------------------------
    // Course Report Methods
    // ----------------------------

    override fun getAllReportsOnCourse(courseId: Int): CourseReportCollectionOutputModel =
            jdbi.withExtension<CourseReportCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val reports = it.getAllReportsOnCourse(courseId).map { toCourseReportOutputModel(it) }
                toCourseReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReportOutputModel =
            jdbi.withExtension<CourseReportOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseReportOutputModel(
                        it.getSpecificReportOfCourse(courseId, reportId)
                                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
                )
            }

    override fun reportCourse(courseId: Int, inputCourseReport: CourseReportInputModel, principal: Principal): CourseReportOutputModel =
            jdbi.withExtension<CourseReportOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseReport = it.reportCourse(courseId, toCourseReport(courseId, inputCourseReport, principal.name))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        COURSE_REPORT_TABLE,
                        courseReport.logId
                ))
                toCourseReportOutputModel(courseReport)
            }

    override fun voteOnReportedCourse(courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

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
                success
            }

    override fun updateReportedCourse(courseId: Int, reportId: Int, principal: Principal): CourseOutputModel =
            jdbi.inTransaction<CourseOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)

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
                toCourseOutputModel(updatedCourse)
            }

    override fun deleteReportOnCourse(courseId: Int, reportId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseReport= it.getSpecificReportOfCourse(courseId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try again with other report id") }

                val success = it.deleteReportOnCourse(reportId)

                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        courseReport.reportedBy,
                        ActionType.APPROVE_REPORT,
                        COURSE_REPORT_TABLE,
                        courseReport.logId
                ))
                success
            }

    // ----------------------------
    // Course Version Methods
    // ----------------------------

    override fun getAllVersionsOfSpecificCourse(courseId: Int): CourseVersionCollectionOutputModel =
            jdbi.withExtension<CourseVersionCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseVersions = it.getAllVersionsOfSpecificCourse(courseId).map { toCourseVersionOutputModel(it) }
                toCourseVersionCollectionOutputModel(courseVersions)
            }

    override fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): CourseVersionOutputModel =
            jdbi.withExtension<CourseVersionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseVersionOutputModel(
                        it.getVersionOfSpecificCourse(courseId, versionId)
                                .orElseThrow { NotFoundException("No version found", "Try again with other version number") }
                )
            }

    // ----------- TERM -----------

    // ----------------------------
    // Term Methods
    // ----------------------------

    override fun getTermsOfCourse(courseId: Int): List<TermOutputModel> =
            jdbi.withExtension<List<TermOutputModel>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getTermsOfCourse(courseId).map {
                    toTermOutputModel(it)
                }
            }

    override fun getSpecificTermOfCourse(courseId: Int, termId: Int): TermOutputModel =
            jdbi.withExtension<TermOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toTermOutputModel(
                        it.getSpecificTermOfCourse(courseId, termId)
                                .orElseThrow { NotFoundException("No term found related to this course","Try again with other term id or course id") }
                )
            }

    // ----------- EXAM -----------

    // ----------------------------
    // Exam Methods
    // ----------------------------

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): ExamCollectionOutputModel =
            jdbi.withExtension<ExamCollectionOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                val exams = it.getAllExamsFromSpecificTermOfCourse(courseId, termId).map { toExamOutputModel(it) }
                toExamCollectionOutputModel(exams)
            }

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): ExamOutputModel =
            jdbi.withExtension<ExamOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                toExamOutputModel(
                        it.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                                .orElseThrow { NotFoundException("No exam found", "Try again with other exam id") }
                )
            }

    override fun createExamOnCourseInTerm(
            courseId: Int,
            termId: Int,
            sheet: MultipartFile,
            inputExam: ExamInputModel,
            principal: Principal
    ): ExamOutputModel =
            jdbi.inTransaction<ExamOutputModel, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)

                val createdExam = examDAO.createExamOnCourseInTerm(courseId, termId, toExam(inputExam, principal.name))
                examDAO.createExamVersion(toExamVersion(createdExam))
                storageService.storeResource(createdExam.sheetId, sheet)

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        EXAM_TABLE,
                        createdExam.logId
                ))
                toExamOutputModel(createdExam)
            }

    override fun voteOnExam(termId: Int, courseId: Int, examId: Int, inputVote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)

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
                success
            }

    override fun deleteSpecificExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                        .orElseThrow { NotFoundException("No exam found", "Try again with other exam id") }
                storageService.deleteSpecificResource(exam.sheetId)
                val success = examDAO.deleteSpecificExamOfCourseInTerm(termId, courseId, examId)

                publisher.publishEvent(ResourceDeletedEvent(
                        principal.name,
                        EXAM_TABLE,
                        exam.logId
                ))
                success
            }

    // ----------------------------
    // Exam Stage Methods
    // ----------------------------

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStageOutputModel> =
            jdbi.withExtension<List<ExamStageOutputModel>, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId).map { toExamStageOutputModel(it) }
            }

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStageOutputModel =
            jdbi.withExtension<ExamStageOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                toExamStageOutputModel(
                        it.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
                                .orElseThrow { NotFoundException("No exam staged found", "Try again with other staged id") }
                )
            }

    override fun createStagingExam(
            courseId: Int,
            termId: Int,
            examInputModel: ExamInputModel,
            sheet: MultipartFile,
            principal: Principal
    ): ExamStageOutputModel = jdbi.inTransaction<ExamStageOutputModel, Exception> {
        val examDAO = it.attach(ExamDAOJdbi::class.java)

        val stagingExam = examDAO.createStagingExamOnCourseInTerm(courseId, termId, toStageExam(examInputModel, principal.name))
        storageService.storeResource(stagingExam.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                EXAM_STAGE_TABLE,
                stagingExam.logId
        ))
        toExamStageOutputModel(stagingExam)
    }

    override fun createExamFromStaged(courseId: Int, termId: Int, stageId: Int, principal: Principal): ExamOutputModel =
            jdbi.inTransaction<ExamOutputModel, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)

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
                toExamOutputModel(exam)
            }

    override fun voteOnStagedExam(termId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
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
                success
            }

    override fun deleteSpecificStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)

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
                success
            }

    // ----------------------------
    // Exam Report Methods
    // ----------------------------

    override fun getAllReportsOnExamOnSpecificTermOfCourse(termId: Int, courseId: Int, examId: Int): ExamReportCollectionOutputModel =
            jdbi.withExtension<ExamReportCollectionOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                val reports = it.getAllReportsOnExamOnSpecificTermOfCourse(courseId, termId, examId).map { toExamReportOutputModel(it) }
                toExamReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(termId: Int, courseId: Int, examId: Int, reportId: Int): ExamReportOutputModel =
            jdbi.withExtension<ExamReportOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                toExamReportOutputModel(
                        it.getSpecificReportOnExamOnSpecificTermOfCourse(courseId, termId, examId, reportId)
                                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
                )
            }

    override fun addReportToExamOnCourseInTerm(termId: Int, courseId: Int, examId: Int, inputExamReport: ExamReportInputModel, principal: Principal): ExamReportOutputModel =
            jdbi.withExtension<ExamReportOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                // Check if exam exists before reporting
                it.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                        .orElseThrow { NotFoundException("No exam found", "Try again") }

                val reportExam = it.reportExam(toExamReport(examId, inputExamReport, principal.name))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        EXAM_REPORT_TABLE,
                        reportExam.logId
                ))
                toExamReportOutputModel(reportExam)
            }

    override fun voteOnReportedExamOnCourseInTerm(termId: Int, courseId: Int, examId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
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
                success
            }

    override fun updateReportedExam(examId: Int, reportId: Int, courseId: Int, termId: Int, principal: Principal): ExamOutputModel =
            jdbi.inTransaction<ExamOutputModel, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)

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
                toExamOutputModel(updatedExam)
            }


    override fun deleteReportOnExam(termId: Int, courseId: Int, examId: Int, reportId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                val examReport = it.getSpecificReportOnExamOnSpecificTermOfCourse(courseId, termId, examId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try again with other ids") }

                val success = it.deleteReportOnExam(courseId, termId, examId, reportId)

                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        examReport.reportedBy,
                        ActionType.REJECT_REPORT,
                        EXAM_REPORT_TABLE,
                        examReport.logId
                ))
                success
            }

    // ----------------------------
    // Exam Version Methods
    // ----------------------------

    override fun getAllVersionsOfSpecificExam(termId: Int, courseId: Int, examId: Int): ExamVersionCollectionOutputModel =
            jdbi.withExtension<ExamVersionCollectionOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                val examVersions = it.getAllVersionsOfSpecificExam(termId, courseId, examId).map { toExamVersionOutputModel(it) }
                toExamVersionCollectionOutputModel(examVersions)
            }

    override fun getVersionOfSpecificExam(termId: Int, courseId: Int, examId: Int, versionId: Int): ExamVersionOutputModel =
            jdbi.withExtension<ExamVersionOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                toExamVersionOutputModel(
                        it.getVersionOfSpecificExam(termId, courseId, examId, versionId)
                                .orElseThrow { NotFoundException("No version found", "Try again with other version number") }
                )
            }

    // ------ WORK ASSIGNMENT -----

    // ----------------------------
    // Work Assignment Methods
    // ----------------------------

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): WorkAssignmentCollectionOutputModel =
            jdbi.withExtension<WorkAssignmentCollectionOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                val workAssignments = it.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId).map { toWorkAssignmentOutputModel(it) }
                toWorkAssignmentCollectionOutputModel(workAssignments)
            }

    override fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int, courseId: Int, termId: Int): WorkAssignmentOutputModel =
            jdbi.withExtension<WorkAssignmentOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                toWorkAssignmentOutputModel(
                        it.getSpecificWorkAssignmentOfCourseInTerm(workAssignmentId, courseId, termId)
                                .orElseThrow { NotFoundException("No Work Assignment found", "Try again with other work assignment id") }
                )
            }

    override fun createWorkAssignmentOnCourseInTerm(
            courseId: Int,
            termId: Int,
            inputWorkAssignment: WorkAssignmentInputModel,
            sheet: MultipartFile,
            principal: Principal
    ): WorkAssignmentOutputModel = jdbi.inTransaction<WorkAssignmentOutputModel, Exception> {
        val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)

        val createdWorkAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(courseId, termId, toWorkAssignment(inputWorkAssignment, principal.name))
        workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(createdWorkAssignment))
        storageService.storeResource(createdWorkAssignment.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                WORK_ASSIGNMENT_TABLE,
                createdWorkAssignment.logId
        ))
        toWorkAssignmentOutputModel(createdWorkAssignment)
    }

    override fun voteOnWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
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
                success
            }

    override fun deleteSpecificWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)

                val workAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(workAssignmentId, courseId, termId)
                        .orElseThrow { NotFoundException("No Work Assignment found", "Try again with other work assignment id") }
                storageService.deleteSpecificResource(workAssignment.sheetId)
                val success = workAssignmentDAO.deleteSpecificWorkAssignment(courseId, termId, workAssignmentId)

                publisher.publishEvent(ResourceDeletedEvent(
                        principal.name,
                        WORK_ASSIGNMENT_TABLE,
                        workAssignment.logId
                ))
                success
            }

    // ----------------------------
    // Work Assignment Stage Methods
    // ----------------------------

    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStageOutputModel> =
            jdbi.withExtension<List<WorkAssignmentStageOutputModel>, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId).map { toWorkAssignmentStageOutputModel(it) }
            }

    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStageOutputModel =
            jdbi.withExtension<WorkAssignmentStageOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                toWorkAssignmentStageOutputModel(
                        it.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
                                .orElseThrow { NotFoundException("No Work Assignment Staged found", "Try again with other stage id") }
                )
            }

    override fun createStagingWorkAssignment(
            sheet: MultipartFile,
            courseId: Int,
            termId: Int,
            inputWorkAssignment: WorkAssignmentInputModel,
            principal: Principal
    ): WorkAssignmentStageOutputModel = jdbi.inTransaction<WorkAssignmentStageOutputModel, Exception> {
        val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)

        val stagingWorkAssignment = workAssignmentDAO.createStagingWorkAssingment(courseId, termId, toStageWorkAssignment(inputWorkAssignment, principal.name))
        storageService.storeResource(stagingWorkAssignment.sheetId, sheet)

        publisher.publishEvent(ResourceCreatedEvent(
                principal.name,
                WORK_ASSIGNMENT_STAGE_TABLE,
                stagingWorkAssignment.logId
        ))
        toWorkAssignmentStageOutputModel(stagingWorkAssignment)
    }

    override fun createWorkAssignmentFromStaged(courseId: Int, termId: Int, stageId: Int, principal: Principal): WorkAssignmentOutputModel =
            jdbi.inTransaction<WorkAssignmentOutputModel, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
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
                toWorkAssignmentOutputModel(workAssignment)
            }

    override fun voteOnStagedWorkAssignment(termId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)

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
                success
            }

    override fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)

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
                success
            }

    // ----------------------------
    // Work Assignment Report Methods
    // ----------------------------

    override fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): WorkAssignmentReportCollectionOutputModel =
            jdbi.withExtension<WorkAssignmentReportCollectionOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                val reports = it.getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId, termId, workAssignmentId).map { toWorkAssignmentReportOutputModel(it) }
                toWorkAssignmentReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int): WorkAssignmentReportOutputModel =
            jdbi.withExtension<WorkAssignmentReportOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                toWorkAssignmentReportOutputModel(
                        it.getSpecificReportOfWorkAssignment(termId, courseId, workAssignmentId, reportId)
                                .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
                )
            }

    override fun addReportToWorkAssignmentOnCourseInTerm(
            termId: Int,
            courseId: Int,
            workAssignmentId: Int,
            inputWorkAssignmentReport: WorkAssignmentReportInputModel,
            principal: Principal
    ): WorkAssignmentReportOutputModel =
            jdbi.withExtension<WorkAssignmentReportOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                val workAssignmentReport = it.addReportToWorkAssignmentOnCourseInTerm(workAssignmentId, toWorkAssignmentReport(workAssignmentId, inputWorkAssignmentReport, principal.name))

                publisher.publishEvent(ResourceCreatedEvent(
                        principal.name,
                        WORK_ASSIGNMENT_REPORT_TABLE,
                        workAssignmentReport.logId
                ))
                toWorkAssignmentReportOutputModel(workAssignmentReport)
            }

    override fun voteOnReportedWorkAssignmentOnCourseInTerm(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int, inputVote: VoteInputModel, principal: Principal): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)

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
                success
            }

    override fun updateWorkAssignmentBasedOnReport(
            workAssignmentId: Int,
            reportId: Int,
            courseId: Int,
            termId: Int,
            principal: Principal
    ): WorkAssignmentOutputModel =
            jdbi.inTransaction<WorkAssignmentOutputModel, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)

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
                toWorkAssignmentOutputModel(updatedWorkAssignment)
            }

    override fun deleteReportOnWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int, principal: Principal): Int =
            jdbi.withExtension<Int, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                val report = it.getSpecificReportOfWorkAssignment(termId, courseId, workAssignmentId, reportId)
                        .orElseThrow { NotFoundException("No report found", "Try again with other report id") }
                val success = it.deleteReportOnWorkAssignment(termId, courseId, workAssignmentId, reportId)

                publisher.publishEvent(ResourceRejectedEvent(
                        principal.name,
                        report.reportedBy,
                        ActionType.REJECT_REPORT,
                        WORK_ASSIGNMENT_REPORT_TABLE,
                        report.logId
                ))
                success
            }

    // ----------------------------
    // Work Assignment Version Methods
    // ----------------------------

    override fun getAllVersionsOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int): WorkAssignmentVersionCollectionOutputModel =
            jdbi.withExtension<WorkAssignmentVersionCollectionOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                val workAssignmentVersions = it.getAllVersionsOfSpecificWorkAssignment(termId, courseId, workAssignmentId).map { toWorkAssignmentVersionOutputModel(it) }
                toWorkAssignmentVersionCollectionOutputModel(workAssignmentVersions)
            }

    override fun getVersionOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, versionId: Int): WorkAssignmentVersionOutputModel =
            jdbi.withExtension<WorkAssignmentVersionOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                toWorkAssignmentVersionOutputModel(
                        it.getVersionOfSpecificWorkAssignment(termId, courseId, workAssignmentId, versionId)
                                .orElseThrow { NotFoundException("No version found", "Try again with other version number") }
                )
            }

}