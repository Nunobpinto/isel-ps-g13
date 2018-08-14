package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.ExamInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.WorkAssignmentInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
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
import isel.leic.ps.eduWikiAPI.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.*
import isel.leic.ps.eduWikiAPI.service.interfaces.CourseService
import isel.leic.ps.eduWikiAPI.service.interfaces.ResourceStorageService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class CourseServiceImpl : CourseService {

    @Autowired
    lateinit var jdbi: Jdbi

    @Autowired
    lateinit var storageService: ResourceStorageService

    /**
     * Course Methods
     */

    override fun getAllCourses(): CourseCollectionOutputModel =
            jdbi.withExtension<CourseCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courses = it.getAllCourses().map { toCourseOutputModel(it) }
                toCourseCollectionOutputModel(courses)
            }

    override fun getSpecificCourse(courseId: Int): CourseOutputModel =
            jdbi.withExtension<CourseOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseOutputModel(
                        it.getSpecificCourse(courseId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No course found",
                                            action = "Try again with other course id"
                                    )
                                }
                )
            }

    override fun getAllReportsOnCourse(courseId: Int): CourseReportCollectionOutputModel =
            jdbi.withExtension<CourseReportCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val reports = it.getAllReportsOnCourse(courseId).map { toCourseReportOutputModel(it) }
                toCourseReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReportOutputModel =
            jdbi.withExtension<CourseReportOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseReportOutputModel(
                        it.getSpecificReportOfCourse(courseId, reportId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No report found",
                                            action = "Try again with other report id"
                                    )
                                }
                )
            }

    override fun getAllCourseStageEntries(): CourseStageCollectionOutputModel =
            jdbi.withExtension<CourseStageCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val stageEntries = it.getAllCourseStageEntries().map { toCourseStageOutputModel(it) }
                toCourseStageCollectionOutputModel(stageEntries)
            }

    override fun getCourseSpecificStageEntry(stageId: Int): CourseStageOutputModel =
            jdbi.withExtension<CourseStageOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseStageOutputModel(
                        it.getCourseSpecificStageEntry(stageId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No staged course found",
                                            action = "Try again with other stage id"
                                    )
                                }
                )
            }

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
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No term found related to this course",
                                            action = "Try again with other term id or course id"
                                    )
                                }
                )
            }

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): ExamCollectionOutputModel =
            jdbi.withExtension<ExamCollectionOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                val exams = it.getAllExamsFromSpecificTermOfCourse(courseId, termId).map { toExamOutputModel(it) }
                toExamCollectionOutputModel(exams)
            }

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): ExamOutputModel =
            jdbi.withExtension<ExamOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                toExamOutputModel(
                        it.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No exam found",
                                            action = "Try again with other exam id"
                                    )
                                }
                )
            }

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStageOutputModel> =
            jdbi.withExtension<List<ExamStageOutputModel>, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId).map { toExamStageOutputModel(it) }
            }

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStageOutputModel =
            jdbi.withExtension<ExamStageOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                toExamStageOutputModel(
                        it.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No exam staged found",
                                            action = "Try again with other staged id"
                                    )
                                }
                )
            }

    override fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): ExamReportCollectionOutputModel =
            jdbi.withExtension<ExamReportCollectionOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                val reports = it.getAllReportsOnExamOnSpecificTermOfCourse(examId).map { toExamReportOutputModel(it) }
                toExamReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): ExamReportOutputModel =
            jdbi.withExtension<ExamReportOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                toExamReportOutputModel(
                        it.getSpecificReportOnExamOnSpecificTermOfCourse(reportId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No report found",
                                            action = "Try again with other report id"
                                    )
                                }
                )
            }

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): WorkAssignmentCollectionOutputModel =
            jdbi.withExtension<WorkAssignmentCollectionOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                val workAssignments = it.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId).map { toWorkAssignmentOutputModel(it) }
                toWorkAssignmentCollectionOutputModel(workAssignments)
            }

    override fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int, courseId: Int, termId: Int): WorkAssignmentOutputModel =
            jdbi.withExtension<WorkAssignmentOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                toWorkAssignmentOutputModel(
                        it.getSpecificWorkAssignmentOfCourseInTerm(workAssignmentId, courseId, termId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No Work Assignment found",
                                            action = "Try again with other work assignment id"
                                    )
                                }
                )
            }

    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStageOutputModel> =
            jdbi.withExtension<List<WorkAssignmentStageOutputModel>, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId).map { toWorkAssignmentStageOutputModel(it) }
            }

    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStageOutputModel =
            jdbi.withExtension<WorkAssignmentStageOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                toWorkAssignmentStageOutputModel(
                        it.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No Work Assignment Staged found",
                                            action = "Try again with other stage id"
                                    )
                                }
                )
            }

    override fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): WorkAssignmentReportCollectionOutputModel =
            jdbi.withExtension<WorkAssignmentReportCollectionOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                val reports = it.getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId, termId, workAssignmentId).map { toWorkAssignmentReportOutputModel(it) }
                toWorkAssignmentReportCollectionOutputModel(reports)
            }

    override fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(workAssignmentId: Int, reportId: Int): WorkAssignmentReportOutputModel =
            jdbi.withExtension<WorkAssignmentReportOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                toWorkAssignmentReportOutputModel(
                        it.getSpecificReportOfWorkAssignment(workAssignmentId, reportId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No report found",
                                            action = "Try again with other report id"
                                    )
                                }
                )
            }

    override fun createCourse(inputCourse: CourseInputModel): CourseOutputModel =
            jdbi.inTransaction<CourseOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val course = courseDAO.createCourse(toCourse(inputCourse))
                courseDAO.createCourseVersion(toCourseVersion(course))
                toCourseOutputModel(course)
            }

    override fun voteOnCourse(courseId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnCourse(courseId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnCourse(courseId, votes)
            }

    override fun reportCourse(courseId: Int, inputCourseReport: CourseReportInputModel): CourseReportOutputModel =
            jdbi.withExtension<CourseReportOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseReportOutputModel(
                        it.reportCourse(courseId, toCourseReport(courseId, inputCourseReport))
                )
            }

    override fun voteOnReportedCourse(reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnReportedCourse(reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnReportedCourse(reportId, votes)
            }

    override fun updateReportedCourse(courseId: Int, reportId: Int): CourseOutputModel =
            jdbi.inTransaction<CourseOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val course = courseDAO.getSpecificCourse(courseId).get()
                val report = courseDAO.getSpecificReportOfCourse(courseId, reportId).get()
                val updatedCourse = Course(
                        courseId = courseId,
                        organizationId = course.organizationId,
                        version = course.version.inc(),
                        createdBy = report.reportedBy,
                        fullName = report.fullName ?: course.fullName,
                        shortName = report.shortName ?: course.shortName
                )
                val res = courseDAO.updateCourse(updatedCourse)
                courseDAO.createCourseVersion(toCourseVersion(updatedCourse))
                courseDAO.deleteReportOnCourse(reportId)
                toCourseOutputModel(res)
            }

    override fun createStagingCourse(inputCourse: CourseInputModel): CourseStageOutputModel =
            jdbi.withExtension<CourseStageOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseStageOutputModel(it.createStagingCourse(toCourseStage(inputCourse)))
            }

    override fun createCourseFromStaged(stageId: Int): CourseOutputModel =
            jdbi.inTransaction<CourseOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val courseStage = courseDAO.getCourseSpecificStageEntry(stageId).get()
                val createdCourse = courseDAO.createCourse(stagedToCourse(courseStage))
                courseDAO.deleteStagedCourse(stageId)
                courseDAO.createCourseVersion(toCourseVersion(createdCourse))
                toCourseOutputModel(createdCourse)
            }

    override fun partialUpdateOnCourse(courseId: Int, inputCourse: CourseInputModel): CourseOutputModel =
            jdbi.inTransaction<CourseOutputModel, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val course = courseDAO.getSpecificCourse(courseId).get()
                val updatedCourse = Course(
                        courseId = courseId,
                        version = course.version.inc(),
                        organizationId = course.organizationId,
                        createdBy = inputCourse.createdBy,
                        fullName = if (inputCourse.fullName.isEmpty()) course.fullName else inputCourse.fullName,
                        shortName = if (inputCourse.shortName.isEmpty()) course.shortName else inputCourse.shortName
                )
                val res = courseDAO.updateCourse(updatedCourse)
                courseDAO.createCourseVersion(toCourseVersion(updatedCourse))
                toCourseOutputModel(res)
            }

    override fun deleteAllCourses(): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteAllCourses()
            }

    override fun deleteSpecificCourse(courseId: Int): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteSpecificCourse(courseId)
            }

    override fun deleteAllStagedCourses(): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteAllStagedCourses()
            }

    override fun deleteSpecificStagedCourse(stageId: Int): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteStagedCourse(stageId)
            }

    override fun deleteAllReportsOnCourse(courseId: Int): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteAllReportsOnCourse(courseId)
            }

    override fun deleteReportOnCourse(courseId: Int, reportId: Int): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteReportOnCourse(reportId)
            }

    override fun voteOnStagedCourse(stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnStagedCourse(stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnStagedCourse(stageId, votes)
            }

    override fun createExamOnCourseInTerm(
            sheet: MultipartFile,
            courseId: Int,
            termId: Int,
            inputExam: ExamInputModel
    ): ExamOutputModel =
            jdbi.inTransaction<ExamOutputModel, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val createdExam = examDAO.createExamOnCourseInTerm(courseId, termId, toExam(inputExam))
                examDAO.createExamVersion(toExamVersion(createdExam))
                storageService.storeResource(createdExam.sheetId, sheet)
                toExamOutputModel(createdExam)
            }


    override fun addReportToExamOnCourseInTerm(examId: Int, inputExamReport: ExamReportInputModel): ExamReportOutputModel =
            jdbi.withExtension<ExamReportOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                toExamReportOutputModel(it.reportExam(toExamReport(examId, inputExamReport)))
            }

    override fun voteOnReportedExamOnCourseInTerm(reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                var votes = examDAO.getVotesOnReportedExam(reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                examDAO.updateVotesOnReportedExam(reportId, votes)
            }

    override fun updateReportedExam(examId: Int, reportId: Int, courseId: Int, termId: Int): ExamOutputModel =
            jdbi.inTransaction<ExamOutputModel, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId).get()
                val report = examDAO.getSpecificReportOfExam(examId, reportId).get()
                val updatedExam = Exam(
                        examId = exam.examId,
                        version = exam.version.inc(),
                        createdBy = report.reportedBy,
                        sheetId = report.sheetId ?: exam.sheetId,
                        dueDate = report.dueDate ?: exam.dueDate,
                        type = report.type ?: exam.type,
                        phase = report.phase ?: exam.phase,
                        location = report.location ?: exam.location
                )
                val res = examDAO.updateExam(examId, updatedExam)
                examDAO.createExamVersion(toExamVersion(updatedExam))
                examDAO.deleteReportOnExam(examId, reportId)
                toExamOutputModel(res)
            }

    override fun createStagingExam(
            sheet: MultipartFile,
            courseId: Int,
            termId: Int,
            examInputModel: ExamInputModel
    ): ExamStageOutputModel = jdbi.inTransaction<ExamStageOutputModel, Exception> {
        val examDAO = it.attach(ExamDAOJdbi::class.java)
        val stagingExam = examDAO.createStagingExamOnCourseInTerm(courseId, termId, toStageExam(examInputModel))
        storageService.storeResource(stagingExam.sheetId, sheet)
        toExamStageOutputModel(stagingExam)
    }

    override fun createExamFromStaged(courseId: Int, termId: Int, stageId: Int): ExamOutputModel =
            jdbi.inTransaction<ExamOutputModel, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val examStage = examDAO.getExamSpecificStageEntry(stageId).get()
                val exam = examDAO.createExamOnCourseInTerm(courseId, termId, stagedToExam(examStage))
                examDAO.createExamVersion(toExamVersion(exam))
                examDAO.deleteSpecificStagedExamOfCourseInTerm(courseId, termId, stageId)
                toExamOutputModel(exam)
            }

    override fun voteOnStagedExam(stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                var votes = examDAO.getVotesOnStagedExam(stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                examDAO.updateVotesOnStagedExam(stageId, votes)
            }

    override fun createWorkAssignmentOnCourseInTerm(
            sheet: MultipartFile,
            courseId: Int,
            termId: Int,
            inputWorkAssignment: WorkAssignmentInputModel
    ): WorkAssignmentOutputModel = jdbi.inTransaction<WorkAssignmentOutputModel, Exception> {
        val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
        val createdWorkAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(
                courseId,
                termId,
                toWorkAssignment(inputWorkAssignment)
        )
        workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(createdWorkAssignment))
        storageService.storeResource(createdWorkAssignment.sheetId, sheet)
        toWorkAssignmentOutputModel(createdWorkAssignment)
    }

    override fun addReportToWorkAssignmentOnCourseInTerm(
            workAssignmentId: Int,
            inputWorkAssignmentReport: WorkAssignmentReportInputModel
    ): WorkAssignmentReportOutputModel =
            jdbi.withExtension<WorkAssignmentReportOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                toWorkAssignmentReportOutputModel(it.addReportToWorkAssignmentOnCourseInTerm(
                        workAssignmentId,
                        toWorkAssignmentReport(workAssignmentId, inputWorkAssignmentReport)
                ))
            }

    override fun voteOnReportedWorkAssignmentOnCourseInTerm(reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                var votes = workAssignmentDAO.getVotesOnReportedWorkAssignment(reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                workAssignmentDAO.updateVotesOnReportedWorkAssignment(reportId, votes)
            }

    override fun voteOnExam(examId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                var votes = examDAO.getVotesOnExam(examId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                examDAO.updateVotesOnExam(examId, votes)
            }

    override fun voteOnWorkAssignment(workAssignmentId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                var votes = workAssignmentDAO.getVotesOnWorkAssignment(workAssignmentId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                workAssignmentDAO.updateVotesOnWorkAssignment(workAssignmentId, votes)
            }

    override fun updateWorkAssignmentBasedOnReport(
            workAssignmentId: Int,
            reportId: Int,
            courseId: Int,
            termId: Int
    ): WorkAssignmentOutputModel =
            jdbi.inTransaction<WorkAssignmentOutputModel, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                val workAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(workAssignmentId, courseId, termId).get()
                val report = workAssignmentDAO.getSpecificReportOfWorkAssignment(workAssignmentId, reportId).get()
                val updatedWorkAssignment = WorkAssignment(
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
                )
                val res = workAssignmentDAO.updateWorkAssignment(workAssignmentId, updatedWorkAssignment)
                workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(updatedWorkAssignment))
                workAssignmentDAO.deleteReportOnWorkAssignment(reportId)
                toWorkAssignmentOutputModel(res)
            }

    override fun createStagingWorkAssignment(
            sheet: MultipartFile,
            courseId: Int,
            termId: Int,
            inputWorkAssignment: WorkAssignmentInputModel
    ): WorkAssignmentStageOutputModel = jdbi.inTransaction<WorkAssignmentStageOutputModel, Exception> {
        val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
        val stagingWorkAssignment = workAssignmentDAO.createStagingWorkAssingment(
                courseId,
                termId,
                toStageWorkAssignment(inputWorkAssignment)
        )
        storageService.storeResource(stagingWorkAssignment.sheetId, sheet)
        toWorkAssignmentStageOutputModel(stagingWorkAssignment)
    }

    override fun createWorkAssignmentFromStaged(courseId: Int, termId: Int, stageId: Int): WorkAssignmentOutputModel =
            jdbi.inTransaction<WorkAssignmentOutputModel, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                val workAssignmentStage = workAssignmentDAO.getWorkAssignmentSpecificStageEntry(stageId).get()
                val workAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(
                        courseId,
                        termId,
                        stagedToWorkAssignment(workAssignmentStage)
                )
                workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(workAssignment))
                workAssignmentDAO.deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId, termId, stageId)
                toWorkAssignmentOutputModel(workAssignment)
            }

    override fun voteOnStagedWorkAssignment(stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                var votes = workAssignmentDAO.getVotesOnStagedWorkAssignment(stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                workAssignmentDAO.updateStagedWorkAssignmentVotes(stageId, votes)
            }

    override fun getAllVersionsOfSpecificCourse(courseId: Int): CourseVersionCollectionOutputModel =
            jdbi.withExtension<CourseVersionCollectionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                val courseVersions = it.getAllVersionsOfSpecificCourse(courseId).map { toCourseVersionOutputModel(it) }
                toCourseVersionCollectionOutputModel(courseVersions)
            }

    override fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): CourseVersionOutputModel =
            jdbi.withExtension<CourseVersionOutputModel, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                toCourseVersionOutputModel(
                        it.getVersionOfSpecificCourse(courseId, versionId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No version found",
                                            action = "Try again with other version number"
                                    )
                                }

                )
            }

    override fun getAllVersionsOfSpecificExam(examId: Int): ExamVersionCollectionOutputModel =
            jdbi.withExtension<ExamVersionCollectionOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                val examVersions = it.getAllVersionsOfSpecificExam(examId).map { toExamVersionOutputModel(it) }
                toExamVersionCollectionOutputModel(examVersions)
            }

    override fun getVersionOfSpecificExam(examId: Int, versionId: Int): ExamVersionOutputModel =
            jdbi.withExtension<ExamVersionOutputModel, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                toExamVersionOutputModel(
                        it.getVersionOfSpecificExam(examId, versionId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No version found",
                                            action = "Try again with other version number"
                                    )
                                }
                )
            }

    override fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int): WorkAssignmentVersionCollectionOutputModel =
            jdbi.withExtension<WorkAssignmentVersionCollectionOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                val workAssignmentVersions = it.getAllVersionsOfSpecificWorkAssignment(workAssignmentId).map { toWorkAssignmentVersionOutputModel(it) }
                toWorkAssignmentVersionCollectionOutputModel(workAssignmentVersions)
            }

    override fun getVersionOfSpecificWorkAssignment(workAssignmentId: Int, versionId: Int): WorkAssignmentVersionOutputModel =
            jdbi.withExtension<WorkAssignmentVersionOutputModel, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                toWorkAssignmentVersionOutputModel(
                        it.getVersionOfSpecificWorkAssignment(workAssignmentId, versionId)
                                .orElseThrow {
                                    NotFoundException(
                                            msg = "No version found",
                                            action = "Try again with other version number"
                                    )
                                }
                )
            }

    override fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val exams = examDAO.getAllExamsFromSpecificTermOfCourse(courseId, termId)
                storageService.batchDeleteResource(exams.map(Exam::sheetId))
                examDAO.deleteAllExamsOfCourseInTerm(courseId, termId)
            }

    override fun deleteSpecificExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId).get()
                storageService.deleteSpecificResource(exam.sheetId)
                examDAO.deleteSpecificExamOfCourseInTerm(examId)
            }

    override fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val stagedExams = examDAO.getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId)
                storageService.batchDeleteResource(stagedExams.map(ExamStage::sheetId))
                examDAO.deleteAllStagedExamsOfCourseInTerm(courseId, termId)
            }

    override fun deleteSpecificStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val stagedExam = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(
                        courseId,
                        termId,
                        stageId
                ).get()
                storageService.deleteSpecificResource(stagedExam.sheetId)
                examDAO.deleteSpecificStagedExamOfCourseInTerm(courseId, termId, stageId)
            }

    override fun deleteAllReportsOnExam(examId: Int): Int =
            jdbi.withExtension<Int, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.deleteAllReportsOnExam(examId)
            }

    override fun deleteReportOnExam(examId: Int, reportId: Int): Int =
            jdbi.withExtension<Int, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.deleteReportOnExam(examId, reportId)
            }

    override fun deleteAllWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                val workAssignments = workAssignmentDAO.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId)
                storageService.batchDeleteResource(workAssignments.map(WorkAssignment::sheetId))
                workAssignmentDAO.deleteAllWorkAssignmentsOfCourseInTerm(courseId, termId)
            }

    override fun deleteSpecificWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                val workAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(workAssignmentId, courseId, termId).get()
                storageService.deleteSpecificResource(workAssignment.sheetId)
                workAssignmentDAO.deleteSpecificWorkAssignment(workAssignmentId)
            }

    override fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                val workAssignments = workAssignmentDAO.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId)
                storageService.batchDeleteResource(workAssignments.map(WorkAssignmentStage::sheetId))
                workAssignmentDAO.deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId, termId)
            }

    override fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                val stagedWorkAssignment = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId).get()
                storageService.deleteSpecificResource(stagedWorkAssignment.sheetId)
                workAssignmentDAO.deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId, termId, stageId)
            }

    override fun deleteAllReportsOnWorkAssignment(workAssignmentId: Int): Int =
            jdbi.withExtension<Int, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.deleteAllReportsOnWorkAssignment(workAssignmentId)
            }

    override fun deleteReportOnWorkAssignment(workAssignmentId: Int, reportId: Int): Int =
            jdbi.withExtension<Int, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.deleteReportOnWorkAssignment(reportId)
            }

    override fun deleteAllVersionsOfCourse(courseId: Int): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteAllVersionsOfCourse(courseId)
            }

    override fun deleteVersionOfCourse(courseId: Int, version: Int): Int =
            jdbi.withExtension<Int, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.deleteVersionOfCourse(courseId, version)
            }

    override fun deleteAllVersionsOfWorkAssignment(workAssignmentId: Int): Int =
            jdbi.withExtension<Int, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.deleteAllVersionOfWorkAssignments(workAssignmentId)
            }

    override fun deleteVersionOfWorkAssignment(workAssignmentId: Int, version: Int): Int =
            jdbi.withExtension<Int, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.deleteVersionWorkAssignment(workAssignmentId, version)
            }

    override fun deleteAllVersionsOfExam(examId: Int): Int =
            jdbi.withExtension<Int, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.deleteAllVersionOfExam(examId)
            }

    override fun deleteVersionOfExam(examId: Int, version: Int): Int =
            jdbi.withExtension<Int, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.deleteVersionOfExam(examId, version)
            }

}