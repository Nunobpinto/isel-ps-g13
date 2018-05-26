package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.ExamInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.WorkAssignmentInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class CourseServiceImpl : CourseService {

    @Autowired
    lateinit var courseDAO: CourseDAO
    @Autowired
    lateinit var examDAO: ExamDAO
    @Autowired
    lateinit var workAssignmentDAO: WorkAssignmentDAO
    @Autowired
    lateinit var classDAO: ClassDAO


    override fun getAllCourses(): List<Course> = courseDAO.getAllCourses()

    override fun getSpecificCourse(courseId: Int) = courseDAO.getSpecificCourse(courseId)

    override fun getAllReportsOnCourse(courseId: Int): List<CourseReport> = courseDAO.getAllReportsOnCourse(courseId)

    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReport = courseDAO.getSpecificReportOfCourse(courseId, reportId)

    override fun getAllCourseStageEntries(): List<CourseStage> = courseDAO.getAllCourseStageEntries()

    override fun getCourseSpecificStageEntry(stageId: Int): CourseStage = courseDAO.getCourseSpecificStageEntry(stageId)

    override fun getTermsOfCourse(courseId: Int): List<Term> = courseDAO.getTermsOfCourse(courseId)

    override fun getSpecificTermOfCourse(courseId: Int, termId: Int): Term = courseDAO.getSpecificTermOfCourse(courseId, termId)

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam> = examDAO.getAllExamsFromSpecificTermOfCourse(courseId, termId)

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Exam = examDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage> = examDAO.getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId)

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStage = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)

    override fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport> = examDAO.getAllReportsOnExamOnSpecificTermOfCourse(examId)

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): ExamReport = examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(reportId)

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment> = workAssignmentDAO.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId)

    override fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int): WorkAssignment = workAssignmentDAO.getSpecificWorkAssignment(workAssignmentId)

    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage> = workAssignmentDAO.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId)

    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStage = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)

    override fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport> = workAssignmentDAO.getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId, termId, workAssignmentId)

    override fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(workAssignmentId: Int, reportId: Int): WorkAssignmentReport = workAssignmentDAO.getSpecificReportOfWorkAssignment(workAssignmentId, reportId)

    override fun getClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class> = classDAO.getClassesOnSpecificTermOfCourse(courseId, termId)

    override fun createCourse(inputCourse: CourseInputModel): Int {
        val course = Course(
                organizationId = inputCourse.organizationId,
                createdBy = inputCourse.createdBy,
                fullName = inputCourse.fullName,
                shortName = inputCourse.shortName,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return courseDAO.createCourse(course)
    }

    override fun voteOnCourse(courseId: Int, inputVote: VoteInputModel): Int = courseDAO.voteOnCourse(courseId, inputVote)

    override fun reportCourse(courseId: Int, inputCourseReport: CourseReportInputModel): Int {
        /*val courseReport = CourseReport(
                courseId = courseId,
                courseFullName = inputCourseReport.fullName,
                courseShortName = inputCourseReport.shortName,
                reportedBy = inputCourseReport.reportedBy
        )
        courseDAO.reportCourse(courseId, courseReport)
        */
        return 0
    }

    override fun voteOnReportedCourse(reportId: Int, inputVote: VoteInputModel): Int = courseDAO.voteOnReportedCourse(reportId, inputVote)

    override fun updateReportedCourse(courseId: Int, reportId: Int) {
        /* val course = courseDAO.getSpecificCourse(courseId)
         val report = courseDAO.getSpecificReportOfExam(courseId, reportId)
         val updatedCourse = Course(
                 id = course.id,
                 version = course.version.inc(),
                 votes = course.votes,
                 createdBy = course.createdBy,
                 fullName = report.courseFullName ?: course.fullName,
                 shortName = report.courseShortName ?: course.shortName,
                 timestamp = Timestamp.valueOf(LocalDateTime.now())
         )
         courseDAO.addToProgrammeVersion(programme)
         courseDAO.updateProgramme(programmeId, updatedProgramme)
         courseDAO.deleteReportOnProgramme(programmeId,reportId)
          */
        NotImplementedError()
    }

    override fun createStagingCourse(inputCourse: CourseInputModel): Int {
        val stage = CourseStage(
                fullName = inputCourse.fullName,
                shortName = inputCourse.shortName,
                createdBy = inputCourse.createdBy,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return courseDAO.createStagingCourse(stage)
    }

    override fun createCourseFromStaged(stageId: Int): Int {
        val courseStage = courseDAO.getCourseSpecificStageEntry(stageId)
        val course = Course(
                organizationId = courseStage.organizationId,
                createdBy = courseStage.createdBy,
                fullName = courseStage.fullName,
                shortName = courseStage.shortName,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        courseDAO.deleteStagedCourse(stageId) //TODO use transaction
        return courseDAO.createCourse(course)    //TODO use transaction
    }

    override fun voteOnStagedCourse(stageId: Int, inputVote: VoteInputModel): Int = courseDAO.voteOnStagedCourse(stageId, inputVote)

    override fun createExamOnCourseInTerm(courseId: Int, termId: Int, inputExam: ExamInputModel): Int {
        val exam = Exam(
                createdBy = inputExam.createdBy,
                sheet = inputExam.sheet,
                dueDate = inputExam.dueDate,
                phase = inputExam.phase,
                location = inputExam.location,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return examDAO.createExamOnCourseInTerm(courseId, termId, exam)
    }

    override fun addReportToExamOnCourseInTerm(examId: Int, inputExamReport: ExamReportInputModel): Int {
        val examReport = ExamReport(
                courseMiscUnitId = examId,
                sheet = inputExamReport.sheet,
                dueDate = inputExamReport.dueDate,
                phase = inputExamReport.phase,
                location = inputExamReport.location,
                reportedBy = inputExamReport.reportedBy,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return examDAO.addReportToExamOnCourseInTerm(examId, examReport)
    }

    override fun voteOnReportToExamOnCourseInTerm(reportId: Int, inputVote: VoteInputModel): Int = examDAO.voteOnReportToExamOnCourseInTerm(reportId, inputVote)

    override fun updateReportedExam(examId: Int, reportId: Int): Int { //TODO mappers type
        val exam = examDAO.getSpecificExam(examId)
        val report = examDAO.getSpecificReportOfExam(examId, reportId)
        val updatedExam = Exam(
                id = exam.id,
                version = exam.version.inc(),
                createdBy = report.reportedBy,
                sheet = report.sheet ?: exam.sheet,
                dueDate = report.dueDate ?: exam.dueDate,
                type = report.type ?: exam.type,
                phase = report.phase ?: exam.phase,
                location = report.location ?: exam.location,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        examDAO.addToExamVersion(exam)
        examDAO.deleteReportOnExam(examId, reportId)
        return examDAO.updateExam(examId, updatedExam)
    }

    override fun createStagingExam(courseId: Int, termId: Int, inputExam: ExamInputModel): Int { //TODO mappers p type
        val stage = ExamStage(
                sheet = inputExam.sheet,
                dueDate = inputExam.dueDate,
                type = inputExam.type,
                phase = inputExam.phase,
                location = inputExam.location,
                createdBy = inputExam.createdBy,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return examDAO.createStagingExam(courseId, termId, stage)
    }

    override fun createExamFromStaged(courseId: Int, termId: Int, stageId: Int): Int { //TODO mappers type?
        val examStage = examDAO.getExamSpecificStageEntry(stageId)
        val exam = Exam(
                createdBy = examStage.createdBy,
                sheet = examStage.sheet,
                dueDate = examStage.dueDate,
                type = examStage.type,
                phase = examStage.phase,
                location = examStage.location,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        examDAO.deleteStagedExam(stageId) //TODO use transaction
        return examDAO.createExam(courseId, termId, exam)    //TODO use transaction
    }

    override fun voteOnStagedExam(stageId: Int, inputVote: VoteInputModel): Int = examDAO.voteOnStagedExam(stageId, inputVote)

    override fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, inputWorkAssignment: WorkAssignmentInputModel): Int {
        val workAssignment = WorkAssignment(
                createdBy = inputWorkAssignment.createdBy,
                sheet = inputWorkAssignment.sheet,
                supplement = inputWorkAssignment.supplement,
                dueDate = inputWorkAssignment.dueDate,
                individual = inputWorkAssignment.individual,
                lateDelivery = inputWorkAssignment.lateDelivery,
                multipleDeliveries = inputWorkAssignment.multipleDeliveries,
                requiresReport = inputWorkAssignment.requiresReport,
                timestamp = inputWorkAssignment.timestamp
        )
        return workAssignmentDAO.createWorkAssignmentOnCourseInTerm(courseId, termId, workAssignment)
    }

    override fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, inputWorkAssignmentReport: WorkAssignmentReportInputModel): Int {
        val workAssignmentReport = WorkAssignmentReport(
                courseMiscUnitId = workAssignmentId,
                sheet = inputWorkAssignmentReport.sheet,
                supplement = inputWorkAssignmentReport.supplement,
                dueDate = inputWorkAssignmentReport.dueDate,
                individual = inputWorkAssignmentReport.individual,
                lateDelivery = inputWorkAssignmentReport.lateDelivery,
                multipleDeliveries = inputWorkAssignmentReport.multipleDeliveries,
                requiresReport = inputWorkAssignmentReport.requiresReport,
                reportedBy = inputWorkAssignmentReport.reportedBy,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return workAssignmentDAO.addReportToWorkAssignmentOnCourseInTerm(workAssignmentId, workAssignmentReport)
    }

    override fun voteOnReportToWorkAssignmentOnCourseInTerm(reportId: Int, inputVote: VoteInputModel): Int = workAssignmentDAO.voteOnReportToWorkAssignmentOnCourseInTerm(reportId, inputVote)

    override fun updateReportedWorkAssignment(workAssignmentId: Int, reportId: Int): Int {
        val workAssignment = workAssignmentDAO.getSpecificWorkAssignment(workAssignmentId)
        val report = workAssignmentDAO.getSpecificReportOfWorkAssignment(workAssignmentId, reportId)
        val updatedWorkAssignment = WorkAssignment(
                id = workAssignmentId,
                version = workAssignment.version.inc(),
                createdBy = report.reportedBy,
                sheet = report.sheet ?: workAssignment.sheet,
                supplement = report.supplement ?: workAssignment.supplement,
                dueDate = report.dueDate ?: workAssignment.dueDate,
                individual = report.individual ?: workAssignment.individual,
                lateDelivery = report.lateDelivery ?: workAssignment.lateDelivery,
                multipleDeliveries = report.multipleDeliveries ?: workAssignment.multipleDeliveries,
                requiresReport = report.requiresReport ?: workAssignment.requiresReport,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        workAssignmentDAO.addToWorkAssignmentVersion(workAssignment)
        workAssignmentDAO.deleteReportOnWorkAssignment(workAssignmentId, reportId)
        return workAssignmentDAO.updateWorkAssignment(workAssignmentId, updatedWorkAssignment)
    }

    override fun createStagingWorkAssignment(courseId: Int, termId: Int, inputWorkAssignment: WorkAssignmentInputModel): Int {
        val stage = WorkAssignmentStage(
                sheet = inputWorkAssignment.sheet,
                supplement = inputWorkAssignment.supplement,
                dueDate = inputWorkAssignment.dueDate,
                individual = inputWorkAssignment.individual,
                lateDelivery = inputWorkAssignment.lateDelivery,
                multipleDeliveries = inputWorkAssignment.multipleDeliveries,
                requiresReport = inputWorkAssignment.requiresReport,
                createdBy = inputWorkAssignment.createdBy,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return workAssignmentDAO.createStagingWorkAssingment(courseId, termId, stage)
    }

    override fun createWorkAssignmentFromStaged(courseId: Int, termId: Int, stageId: Int): Int {
        val workAssignmentStage = workAssignmentDAO.getWorkAssignmentSpecificStageEntry(stageId)
        val workAssignment = WorkAssignment(
                createdBy = workAssignmentStage.createdBy,
                sheet = workAssignmentStage.sheet,
                supplement = workAssignmentStage.supplement,
                dueDate = workAssignmentStage.dueDate,
                individual = workAssignmentStage.individual,
                lateDelivery = workAssignmentStage.lateDelivery,
                multipleDeliveries = workAssignmentStage.multipleDeliveries,
                requiresReport = workAssignmentStage.requiresReport,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        workAssignmentDAO.deleteStagedWorkAssignment(stageId) //TODO use transaction
        return workAssignmentDAO.createWorkAssignmentOnCourseInTerm(courseId, termId, workAssignment)    //TODO use transaction
    }

    override fun voteOnStagedWorkAssignment(stageId: Int, inputVote: VoteInputModel): Int = workAssignmentDAO.voteOnStagedWorkAssignment(stageId, inputVote)
}