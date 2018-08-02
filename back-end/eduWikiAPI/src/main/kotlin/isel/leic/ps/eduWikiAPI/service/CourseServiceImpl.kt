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
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.ExamDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.WorkAssignmentDAOJdbi
import isel.leic.ps.eduWikiAPI.service.interfaces.CourseService
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CourseServiceImpl : CourseService {

    @Autowired
    lateinit var jdbi: Jdbi

    /**
     * Course Methods
     */

    override fun getAllCourses(): List<Course> =
            jdbi.withExtension<List<Course>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getAllCourses()
            }

    override fun getSpecificCourse(courseId: Int): Optional<Course> =
            jdbi.withExtension<Optional<Course>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getSpecificCourse(courseId)
            }

    override fun getAllReportsOnCourse(courseId: Int): List<CourseReport> =
            jdbi.withExtension<List<CourseReport>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getAllReportsOnCourse(courseId)
            }

    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int): Optional<CourseReport> =
            jdbi.withExtension<Optional<CourseReport>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getSpecificReportOfCourse(courseId, reportId)
            }

    override fun getAllCourseStageEntries(): List<CourseStage> =
            jdbi.withExtension<List<CourseStage>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getAllCourseStageEntries()
            }

    override fun getCourseSpecificStageEntry(stageId: Int): Optional<CourseStage> =
            jdbi.withExtension<Optional<CourseStage>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getCourseSpecificStageEntry(stageId)
            }

    override fun getTermsOfCourse(courseId: Int): List<Term> =
            jdbi.withExtension<List<Term>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getTermsOfCourse(courseId)
            }

    override fun getSpecificTermOfCourse(courseId: Int, termId: Int): Optional<Term> =
            jdbi.withExtension<Optional<Term>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getSpecificTermOfCourse(courseId, termId)
            }

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam> =
            jdbi.withExtension<List<Exam>, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.getAllExamsFromSpecificTermOfCourse(courseId, termId)
            }

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Optional<Exam> =
            jdbi.withExtension<Optional<Exam>, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)
            }

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage> =
            jdbi.withExtension<List<ExamStage>, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId)
            }

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<ExamStage> =
            jdbi.withExtension<Optional<ExamStage>, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)
            }

    override fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport> =
            jdbi.withExtension<List<ExamReport>, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.getAllReportsOnExamOnSpecificTermOfCourse(examId)
            }

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): Optional<ExamReport> =
            jdbi.withExtension<Optional<ExamReport>, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.getSpecificReportOnExamOnSpecificTermOfCourse(reportId)
            }

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment> =
            jdbi.withExtension<List<WorkAssignment>, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId)
            }

    override fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int, courseId: Int, termId: Int): Optional<WorkAssignment> =
            jdbi.withExtension<Optional<WorkAssignment>, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.getSpecificWorkAssignment(workAssignmentId, courseId, termId)
            }

    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage> =
            jdbi.withExtension<List<WorkAssignmentStage>, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId)
            }

    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<WorkAssignmentStage> =
            jdbi.withExtension<Optional<WorkAssignmentStage>, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)
            }

    override fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport> =
            jdbi.withExtension<List<WorkAssignmentReport>, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId, termId, workAssignmentId)
            }

    override fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(workAssignmentId: Int, reportId: Int): Optional<WorkAssignmentReport> =
            jdbi.withExtension<Optional<WorkAssignmentReport>, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.getSpecificReportOfWorkAssignment(workAssignmentId, reportId)
            }

    override fun getAllClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class> =
            jdbi.withExtension<List<Class>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getAllClassesOnSpecificTermOfCourse(courseId, termId)
            }

    override fun getClassOnSpecificTermOfCourse(courseId: Int, termId: Int, classId: Int): Optional<Class> =
            jdbi.withExtension<Optional<Class>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getClassOnSpecificTermOfCourse(courseId, termId, classId)
            }

    override fun createCourse(inputCourse: CourseInputModel): Course =
            jdbi.inTransaction<Course, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val course = courseDAO.createCourse(toCourse(inputCourse))
                courseDAO.createCourseVersion(toCourseVersion(course))
                course
            }

    override fun voteOnCourse(courseId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnCourse(courseId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnCourse(courseId, votes)
            }

    override fun reportCourse(courseId: Int, inputCourseReport: CourseReportInputModel): CourseReport =
            jdbi.withExtension<CourseReport, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.reportCourse(courseId, toCourseReport(courseId, inputCourseReport))
            }

    override fun voteOnReportedCourse(reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                var votes = courseDAO.getVotesOnReportedCourse(reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                courseDAO.updateVotesOnReportedCourse(reportId, votes)
            }

    override fun updateReportedCourse(courseId: Int, reportId: Int): Course =
            jdbi.inTransaction<Course, Exception> {
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
                res
            }

    override fun createStagingCourse(inputCourse: CourseInputModel): CourseStage =
            jdbi.withExtension<CourseStage, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.createStagingCourse(toCourseStage(inputCourse))
            }

    override fun createCourseFromStaged(stageId: Int): Course =
            jdbi.inTransaction<Course, Exception> {
                val courseDAO = it.attach(CourseDAOJdbi::class.java)
                val courseStage = courseDAO.getCourseSpecificStageEntry(stageId).get()
                val createdCourse = courseDAO.createCourse(stagedToCourse(courseStage))
                courseDAO.deleteStagedCourse(stageId)
                courseDAO.createCourseVersion(toCourseVersion(createdCourse))
                createdCourse
            }

    override fun partialUpdateOnCourse(courseId: Int, inputCourse: CourseInputModel): Course =
            jdbi.inTransaction<Course, Exception> {
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
                res
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

    override fun createExamOnCourseInTerm(courseId: Int, termId: Int, inputExam: ExamInputModel): Exam =
            jdbi.inTransaction<Exam, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val exam = examDAO.createExamOnCourseInTerm(courseId, termId, toExam(inputExam))
                examDAO.createExamVersion(toExamVersion(exam))
                exam
            }

    override fun addReportToExamOnCourseInTerm(examId: Int, inputExamReport: ExamReportInputModel): ExamReport =
            jdbi.withExtension<ExamReport, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.reportExam(toExamReport(examId, inputExamReport))
            }

    override fun voteOnReportedExamOnCourseInTerm(reportId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                var votes = examDAO.getVotesOnReportedExam(reportId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                examDAO.updateVotesOnReportedExam(reportId, votes)
            }

    override fun updateReportedExam(examId: Int, reportId: Int, courseId: Int, termId: Int): Exam =
            jdbi.inTransaction<Exam, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId).get()
                val report = examDAO.getSpecificReportOfExam(examId, reportId).get()
                val updatedExam = Exam(
                        examId = exam.examId,
                        version = exam.version.inc(),
                        createdBy = report.reportedBy,
                        sheet = report.sheet ?: exam.sheet,
                        dueDate = report.dueDate ?: exam.dueDate,
                        type = report.type ?: exam.type,
                        phase = report.phase ?: exam.phase,
                        location = report.location ?: exam.location
                )
                val res = examDAO.updateExam(examId, updatedExam)
                examDAO.createExamVersion(toExamVersion(updatedExam))
                examDAO.deleteReportOnExam(examId, reportId)
                res
            }

    override fun createStagingExam(courseId: Int, termId: Int, inputExam: ExamInputModel): ExamStage =
            jdbi.withExtension<ExamStage, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.createStagingExamOnCourseInTerm(courseId, termId, toStageExam(inputExam))
            }

    override fun createExamFromStaged(courseId: Int, termId: Int, stageId: Int): Exam =
            jdbi.inTransaction<Exam, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                val examStage = examDAO.getExamSpecificStageEntry(stageId).get()
                val exam = examDAO.createExamOnCourseInTerm(courseId, termId, stagedToExam(examStage))
                examDAO.createExamVersion(toExamVersion(exam))
                examDAO.deleteStagedExam(stageId)
                exam
            }

    override fun voteOnStagedExam(stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val examDAO = it.attach(ExamDAOJdbi::class.java)
                var votes = examDAO.getVotesOnStagedExam(stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                examDAO.updateVotesOnStagedExam(stageId, votes)
            }

    override fun createWorkAssignmentOnCourseInTerm(
            courseId: Int,
            termId: Int,
            inputWorkAssignment: WorkAssignmentInputModel
    ): WorkAssignment =
            jdbi.inTransaction<WorkAssignment, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                val workAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(
                        courseId,
                        termId,
                        toWorkAssignment(inputWorkAssignment)
                )
                workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(workAssignment))
                workAssignment
            }

    override fun addReportToWorkAssignmentOnCourseInTerm(
            workAssignmentId: Int,
            inputWorkAssignmentReport: WorkAssignmentReportInputModel
    ): WorkAssignmentReport =
            jdbi.withExtension<WorkAssignmentReport, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.addReportToWorkAssignmentOnCourseInTerm(
                        workAssignmentId,
                        toWorkAssignmentReport(workAssignmentId, inputWorkAssignmentReport)
                )
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
    ): WorkAssignment =
            jdbi.inTransaction<WorkAssignment, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                val workAssignment = workAssignmentDAO.getSpecificWorkAssignment(workAssignmentId, courseId, termId).get()
                val report = workAssignmentDAO.getSpecificReportOfWorkAssignment(workAssignmentId, reportId).get()
                val updatedWorkAssignment = WorkAssignment(
                        workAssignmentId = workAssignmentId,
                        version = workAssignment.version.inc(),
                        createdBy = report.reportedBy,
                        sheet = report.sheet ?: workAssignment.sheet,
                        supplement = report.supplement ?: workAssignment.supplement,
                        dueDate = report.dueDate ?: workAssignment.dueDate,
                        individual = report.individual ?: workAssignment.individual,
                        lateDelivery = report.lateDelivery ?: workAssignment.lateDelivery,
                        multipleDeliveries = report.multipleDeliveries ?: workAssignment.multipleDeliveries,
                        requiresReport = report.requiresReport ?: workAssignment.requiresReport
                )
                val res = workAssignmentDAO.updateWorkAssignment(workAssignmentId, updatedWorkAssignment)
                workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(updatedWorkAssignment))
                workAssignmentDAO.deleteReportOnWorkAssignment(reportId)
                res
            }

    override fun createStagingWorkAssignment(
            courseId: Int,
            termId: Int,
            inputWorkAssignment: WorkAssignmentInputModel
    ): WorkAssignmentStage =
            jdbi.inTransaction<WorkAssignmentStage, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                workAssignmentDAO.createStagingWorkAssingment(courseId, termId, toStageWorkAssignment(inputWorkAssignment))
            }

    override fun createWorkAssignmentFromStaged(courseId: Int, termId: Int, stageId: Int): WorkAssignment =
            jdbi.inTransaction<WorkAssignment, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                val workAssignmentStage = workAssignmentDAO.getWorkAssignmentSpecificStageEntry(stageId).get()
                val workAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(
                        courseId,
                        termId,
                        stagedToWorkAssignment(workAssignmentStage)
                )
                workAssignmentDAO.createWorkAssignmentVersion(toWorkAssignmentVersion(workAssignment))
                workAssignmentDAO.deleteSpecificStagedWorkAssignment(stageId)
                workAssignment
            }

    override fun voteOnStagedWorkAssignment(stageId: Int, vote: VoteInputModel): Int =
            jdbi.inTransaction<Int, Exception> {
                val workAssignmentDAO = it.attach(WorkAssignmentDAOJdbi::class.java)
                var votes = workAssignmentDAO.getVotesOnStagedWorkAssignment(stageId)
                votes = if (Vote.valueOf(vote.vote) == Vote.Down) --votes else ++votes
                workAssignmentDAO.updateStagedWorkAssignmentVotes(stageId, votes)
            }

    override fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion> =
            jdbi.withExtension<List<CourseVersion>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getAllVersionsOfSpecificCourse(courseId)
            }

    override fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): Optional<CourseVersion> =
            jdbi.withExtension<Optional<CourseVersion>, CourseDAOJdbi, Exception>(CourseDAOJdbi::class.java) {
                it.getVersionOfSpecificCourse(courseId, versionId)
            }

    override fun getAllVersionsOfSpecificExam(examId: Int): List<ExamVersion> =
            jdbi.withExtension<List<ExamVersion>, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.getAllVersionsOfSpecificExam(examId)
            }

    override fun getVersionOfSpecificExam(examId: Int, versionId: Int): Optional<ExamVersion> =
            jdbi.withExtension<Optional<ExamVersion>, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.getVersionOfSpecificExam(examId, versionId)
            }

    override fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int): List<WorkAssignmentVersion> =
            jdbi.withExtension<List<WorkAssignmentVersion>, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.getAllVersionsOfSpecificWorkAssignment(workAssignmentId)
            }

    override fun getVersionOfSpecificWorkAssignment(workAssignmentId: Int, versionId: Int): Optional<WorkAssignmentVersion> =
            jdbi.withExtension<Optional<WorkAssignmentVersion>, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.getVersionOfSpecificWorkAssignment(workAssignmentId, versionId)
            }

    override fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion> =
            jdbi.withExtension<List<ClassVersion>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getAllVersionsOfSpecificClass(classId)
            }

    override fun getVersionOfSpecificClass(classId: Int, versionId: Int): Optional<ClassVersion> =
            jdbi.withExtension<Optional<ClassVersion>, ClassDAOJdbi, Exception>(ClassDAOJdbi::class.java) {
                it.getVersionOfSpecificClass(classId, versionId)
            }

    override fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int): Int =
            jdbi.withExtension<Int, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.deleteAllExamsOfCourseInTerm(courseId, termId)
            }

    override fun deleteSpecificExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int): Int =
            jdbi.withExtension<Int, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.deleteSpecificExamOfCourseInTerm(examId)
            }

    override fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int =
            jdbi.withExtension<Int, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.deleteAllStagedExamsOfCourseInTerm(courseId, termId)
            }

    override fun deleteStagedExam(stageId: Int): Int =
            jdbi.withExtension<Int, ExamDAOJdbi, Exception>(ExamDAOJdbi::class.java) {
                it.deleteStagedExam(stageId)
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
            jdbi.withExtension<Int, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.deleteAllWorkAssignmentsOfCourseInTerm(courseId, termId)
            }

    override fun deleteSpecificWorkAssignment(workAssignmentId: Int): Int =
            jdbi.withExtension<Int, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.deleteSpecificWorkAssignment(workAssignmentId)
            }

    override fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int =
            jdbi.withExtension<Int, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId, termId)
            }

    override fun deleteSpecificStagedWorkAssignment(stageId: Int): Int =
            jdbi.withExtension<Int, WorkAssignmentDAOJdbi, Exception>(WorkAssignmentDAOJdbi::class.java) {
                it.deleteSpecificStagedWorkAssignment(stageId)
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