package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.ExamInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.WorkAssignmentInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
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

    override fun getAllClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class> = classDAO.getAllClassesOnSpecificTermOfCourse(courseId, termId)

    override fun getClassOnSpecificTermOfCourse(courseId: Int, termId: Int, classId: Int): Class = classDAO.getClassOnSpecificTermOfCourse(courseId, termId, classId)

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
        val courseReport = CourseReport(
                courseId = courseId,
                courseFullName = inputCourseReport.fullName,
                courseShortName = inputCourseReport.shortName,
                reportedBy = inputCourseReport.reportedBy,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        return courseDAO.reportCourse(courseId, courseReport)
    }

    override fun voteOnReportedCourse(reportId: Int, inputVote: VoteInputModel): Int = courseDAO.voteOnReportedCourse(reportId, Vote.valueOf(inputVote.vote))

    override fun updateReportedCourse(courseId: Int, reportId: Int) : Int {
         val course = courseDAO.getSpecificCourse(courseId)
         val report = courseDAO.getSpecificReportOfCourse(courseId, reportId)
         val updatedCourse = Course(
                 id = courseId,
                 organizationId = course.organizationId,
                 version = course.version.inc(),
                 createdBy = report.reportedBy,
                 fullName = report.courseFullName,
                 shortName = report.courseShortName,
                 timestamp = Timestamp.valueOf(LocalDateTime.now())
         )
         courseDAO.addToCourseVersion(updatedCourse)
         courseDAO.deleteReportOnCourse(reportId)
         return courseDAO.updateCourse(updatedCourse)
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

    override fun voteOnStagedCourse(stageId: Int, inputVote: VoteInputModel): Int = courseDAO.voteOnStagedCourse(stageId, Vote.valueOf(inputVote.vote))

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

    override fun partialUpdateOnCourse(courseId: Int, inputCourse: CourseInputModel): Int {
        val course = courseDAO.getSpecificCourse(courseId)
        val updatedCourse = Course(
                id = courseId,
                version = course.version.inc(),
                createdBy = inputCourse.createdBy,
                fullName = inputCourse.fullName ?: course.fullName,
                shortName = inputCourse.shortName ?: course.shortName,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        courseDAO.addToCourseVersion(updatedCourse)
        return courseDAO.updateCourse(updatedCourse)
    }

    override fun voteOnStagedWorkAssignment(stageId: Int, inputVote: VoteInputModel): Int = workAssignmentDAO.voteOnStagedWorkAssignment(stageId, inputVote)

    override fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion> = courseDAO.getAllVersionsOfSpecificCourse(courseId)

    override fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): CourseVersion = courseDAO.getVersionOfSpecificCourse(courseId, versionId)

    override fun getAllVersionsOfSpecificExam(examId: Int): List<ExamVersion> = examDAO.getAllVersionsOfSpecificExam(examId)

    override fun getVersionOfSpecificExam(examId: Int, versionId: Int): ExamVersion = examDAO.getVersionOfSpecificExam(examId, versionId)

    override fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int): List<WorkAssignmentVersion> =
            workAssignmentDAO.getAllVersionsOfSpecificWorkAssignment(workAssignmentId)

    override fun getVersionOfSpecificWorkAssignment(workAssignmentId: Int, versionId: Int): WorkAssignmentVersion =
            workAssignmentDAO.getVersionOfSpecificWorkAssignment(workAssignmentId, versionId)

    override fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion> = classDAO.getAllVersionsOfSpecificClass(classId)

    override fun getVersionOfSpecificClass(classId: Int, versionId: Int): ClassVersion = classDAO.getVersionOfSpecificClass(classId, versionId)

    override fun deleteAllCourses(): Int = courseDAO.deleteAllCourses()

    override fun deleteSpecificCourse(courseId: Int): Int = courseDAO.deleteCourse(courseId)

    override fun deleteAllStagedCourses(): Int = courseDAO.deleteAllCourseStages()

    override fun deleteSpecificStagedCourse(stageId: Int): Int = courseDAO.deleteStagedCourse(stageId)

    override fun deleteAllReportsOnCourse(courseId: Int): Int = courseDAO.deleteAllReportsOnCourse(courseId)

    override fun deleteReportOnCourse(courseId: Int, reportId: Int): Int = courseDAO.deleteReportOnCourse(reportId)

    override fun deleteExamsOfCourseInTerm(courseId: Int, termId: Int): Int = examDAO.deleteAllExamsOfCourseInTerm(courseId, termId)

    override fun deleteExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int): Int = examDAO.deleteExam(examId)

    override fun deleteStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int = examDAO.deleteAllStagedExamsOfCourseInTerm(courseId, termId)

    override fun deleteStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int = examDAO.deleteStagedExam(stageId)

    override fun deleteAllReportsOnExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int): Int = examDAO.deleteAllReportsOnExam(examId)

    override fun deleteReportOnExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int, reportId: Int): Int = examDAO.deleteReportOnExam(examId, reportId)

    override fun deleteWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int = workAssignmentDAO.deleteWorkAssignmentsOfCourseInTerm(courseId, termId)

    override fun deleteWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int): Int = workAssignmentDAO.deleteWorkAssignment(workAssignmentId)

    override fun deleteStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int = workAssignmentDAO.deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId, termId)

    override fun deleteStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int = workAssignmentDAO.deleteStagedWorkAssignment(stageId)

    override fun deleteAllReportsOfWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int): Int = workAssignmentDAO.deleteAllReportsOnWorkAssignment(workAssignmentId)

    override fun deleteReportOfWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int, reportId: Int): Int = workAssignmentDAO.deleteReportOnWorkAssignment(workAssignmentId, reportId)

    override fun deleteAllVersionsOfCourse(courseId: Int): Int = courseDAO.deleteAllVersionCourses(courseId)

    override fun deleteVersionOfCourse(courseId: Int, version: Int): Int = courseDAO.deleteVersionCourse(courseId, version)

    override fun deleteAllVersionsOfWorkAssignment(courseId: Int, termId: Int, workAssignmentId: Int): Int = workAssignmentDAO.deleteAllVersionWorkAssignments(workAssignmentId)

    override fun deleteVersionOfWorkAssignment(courseId: Int, termId: Int, workAssignmentId: Int, version: Int): Int = workAssignmentDAO.deleteVersionWorkAssignment(workAssignmentId, version)

    override fun deleteAllVersionsOfExam(courseId: Int, termId: Int, examId: Int): Int = examDAO.deleteAllVersionExams(examId)

    override fun deleteVersionOfExam(courseId: Int, termId: Int, examId: Int, version: Int): Int = examDAO.deleteVersionExam(examId, version)

}