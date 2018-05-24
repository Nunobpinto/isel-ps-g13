package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.ExamInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
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
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.service.interfaces.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class CourseServiceImpl : CourseService {

    @Autowired
    lateinit var courseDAO: CourseDAO

    override fun getAllCourses(): List<Course> = courseDAO.getAllCourses()

    override fun getSpecificCourse(courseId: Int) = courseDAO.getSpecificCourse(courseId)

    override fun getAllReportsOnCourse(courseId: Int): List<CourseReport> = courseDAO.getAllReportsOnCourse(courseId)

    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReport = courseDAO.getSpecificReportOfCourse(courseId, reportId)

    override fun getAllCourseStageEntries(): List<CourseStage> = courseDAO.getAllCourseStageEntries()

    override fun getCourseSpecificStageEntry(stageId: Int): CourseStage = courseDAO.getCourseSpecificStageEntry(stageId)

    override fun getTermsOfCourse(courseId: Int): List<Term> = courseDAO.getTermsOfCourse(courseId)

    override fun getSpecificTermOfCourse(courseId: Int, termId: Int): Term = courseDAO.getSpecificTermOfCourse(courseId, termId)

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam> = courseDAO.getAllExamsFromSpecificTermOfCourse(courseId, termId)

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Exam = courseDAO.getSpecificExamFromSpecificTermOfCourse(courseId, termId, examId)

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage> = courseDAO.getStageEntriesFromExamOnSpecificTermOfCourse(courseId, termId)

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStage = courseDAO.getStageEntryFromExamOnSpecificTermOfCourse(courseId, termId, stageId)

    override fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport> = courseDAO.getAllReportsOnExamOnSpecificTermOfCourse(examId)

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): ExamReport = courseDAO.getSpecificReportOnExamOnSpecificTermOfCourse(reportId)

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment> = courseDAO.getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId)

    override fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int): WorkAssignment = courseDAO.getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId)

    override fun getStageEntriesFromWorkItemOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage> = courseDAO.getStageEntriesFromWorkItemOnSpecificTermOfCourse(courseId, termId)

    override fun getStageEntryFromWorkItemOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStage = courseDAO.getStageEntryFromWorkItemOnSpecificTermOfCourse(courseId, termId, stageId)

    override fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport> = courseDAO.getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId, termId, workAssignmentId)

    override fun getSpecificReportFromWorkItemOnSpecificTermOfCourse(reportId: Int): WorkAssignmentReport = courseDAO.getSpecificReportFromWorkItemOnSpecificTermOfCourse(reportId)

    override fun getClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class> = courseDAO.getClassesOnSpecificTermOfCourse(courseId, termId)

    override fun createCourse(inputCourse: CourseInputModel) {
        val course = Course(
                organizationId = inputCourse.organizationId,
                createdBy = inputCourse.createdBy,
                fullName = inputCourse.fullName,
                shortName = inputCourse.shortName,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        courseDAO.createCourse(course)
    }

    override fun voteOnCourse(courseId: Int, inputVote: VoteInputModel) = courseDAO.voteOnCourse(courseId, inputVote)

    override fun reportCourse(courseId: Int, inputReportCourse: CourseReportInputModel) {
        /*val courseReport = CourseReport(
                courseId = courseId,
                courseFullName = inputReportCourse.fullName,
                courseShortName = inputReportCourse.shortName,
                reportedBy = inputReportCourse.reportedBy
        )
        courseDAO.reportCourse(courseId, courseReport)
        */
        NotImplementedError()
    }

    override fun voteOnReportedCourse(reportId: Int, inputVote: VoteInputModel) = courseDAO.voteOnReportedCourse(reportId, inputVote)

    override fun updateReportedCourse(courseId: Int, reportId: Int) {
        /* val course = courseDAO.getSpecificCourse(courseId)
         val report = courseDAO.getSpecificReportOfCourse(courseId, reportId)
         val updatedCourse = Course(
                 id = course.id,
                 version = course.version + 1,
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

    override fun createStagingCourse(inputCourse: CourseInputModel) {
        val stage = CourseStage(
                fullName = inputCourse.fullName,
                shortName = inputCourse.shortName,
                createdBy = inputCourse.createdBy,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        courseDAO.createStagingCourse(stage)
    }

    override fun createCourseFromStaged(stageId: Int) {
        val courseStage = courseDAO.getCourseSpecificStageEntry(stageId)
        val course = Course(
                organizationId = courseStage.organizationId,
                createdBy = courseStage.createdBy,
                fullName = courseStage.fullName,
                shortName = courseStage.shortName,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        courseDAO.deleteStagedCourse(stageId) //TODO use transaction
        courseDAO.createCourse(course)    //TODO use transaction
    }

    override fun voteOnStagedCourse(stageId: Int, inputVote: VoteInputModel) = courseDAO.voteOnStagedCourse(stageId, inputVote)

    override fun createExamOnCourseInTerm(courseId: Int, termId: Int, inputExam: ExamInputModel) {
        val exam = Exam(
                createdBy = inputExam.createdBy,
                sheet = inputExam.sheet,
                dueDate = inputExam.dueDate,
                phase = inputExam.phase,
                location = inputExam.location
                )
        courseDAO.createExamOnCourseInTerm(courseId, termId, exam)
    }
}