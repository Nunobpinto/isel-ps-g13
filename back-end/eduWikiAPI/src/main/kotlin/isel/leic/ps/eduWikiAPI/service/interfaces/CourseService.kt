package isel.leic.ps.eduWikiAPI.service.interfaces

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

interface CourseService {

    fun getAllCourses() : List<Course>

    fun getSpecificCourse(courseId: Int) : Course

    fun getAllReportsOnCourse(courseId: Int) : List<CourseReport>

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReport

    fun getAllCourseStageEntries(): List<CourseStage>

    fun getCourseSpecificStageEntry(stageId: Int): CourseStage

    fun getTermsOfCourse(courseId: Int): List<Term>

    fun getSpecificTermOfCourse(courseId: Int, termId: Int): Term

    fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam>

    fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Exam

    fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage>

    fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStage

    fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport>

    fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): ExamReport

    fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment>

    fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int): WorkAssignment

    fun getStageEntriesFromWorkItemOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage>

    fun getStageEntryFromWorkItemOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStage

    fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport>

    fun getSpecificReportFromWorkItemOnSpecificTermOfCourse(reportId: Int): WorkAssignmentReport

    fun getClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class>

    fun createCourse(inputCourse: CourseInputModel)

    fun voteOnCourse(courseId: Int, inputVote: VoteInputModel)

    fun reportCourse(courseId: Int, inputReportCourse: CourseReportInputModel)

    fun voteOnReportedCourse(reportId: Int, inputVote: VoteInputModel)

    fun updateReportedCourse(courseId: Int, reportId: Int)

    fun createStagingCourse(inputCourse: CourseInputModel)

    fun createCourseFromStaged(stageId: Int)

    fun voteOnStagedCourse(stageId: Int, inputVote: VoteInputModel)

    fun createExamOnCourseInTerm(courseId: Int, termId: Int, inputExam: ExamInputModel)

}