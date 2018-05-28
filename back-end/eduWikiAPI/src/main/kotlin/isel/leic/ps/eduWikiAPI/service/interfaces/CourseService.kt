package isel.leic.ps.eduWikiAPI.service.interfaces

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
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion

interface CourseService {

    /**
     * Main entities queries
     */

    fun getAllCourses(): List<Course>

    fun getSpecificCourse(courseId: Int): Course

    fun getTermsOfCourse(courseId: Int): List<Term>

    fun getSpecificTermOfCourse(courseId: Int, termId: Int): Term

    fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam>

    fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Exam

    fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment>

    fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int): WorkAssignment

    fun getAllClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class>

    fun getClassOnSpecificTermOfCourse(courseId: Int, termId: Int, classId: Int): Class

    fun createCourse(inputCourse: CourseInputModel): Int

    fun createCourseFromStaged(stageId: Int): Int

    fun createExamOnCourseInTerm(courseId: Int, termId: Int, inputExam: ExamInputModel): Int

    fun createExamFromStaged(courseId: Int, termId: Int, stageId: Int): Int

    fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, inputWorkAssignment: WorkAssignmentInputModel): Int

    fun createWorkAssignmentFromStaged(courseId: Int, termId: Int, stageId: Int): Int

    fun voteOnCourse(courseId: Int, inputVote: VoteInputModel): Int

    fun partialUpdateOnCourse(courseId: Int, inputCourse: CourseInputModel): Int

    fun deleteAllCourses(): Int

    fun deleteSpecificCourse(courseId: Int): Int

    fun deleteExamsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun deleteExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int): Int

    fun deleteWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun deleteWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int): Int

    /**
     * Stage entities queries
     */

    fun getAllCourseStageEntries(): List<CourseStage>

    fun getCourseSpecificStageEntry(stageId: Int): CourseStage

    fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage>

    fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStage

    fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage>

    fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStage

    fun createStagingCourse(inputCourse: CourseInputModel): Int

    fun createStagingExam(courseId: Int, termId: Int, inputExam: ExamInputModel): Int

    fun createStagingWorkAssignment(courseId: Int, termId: Int, inputWorkAssignment: WorkAssignmentInputModel): Int

    fun voteOnStagedCourse(stageId: Int, inputVote: VoteInputModel): Int

    fun voteOnStagedExam(stageId: Int, inputVote: VoteInputModel): Int

    fun voteOnStagedWorkAssignment(stageId: Int, inputVote: VoteInputModel): Int

    fun deleteStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun deleteStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int

    fun deleteAllStagedCourses(): Int

    fun deleteSpecificStagedCourse(stageId: Int): Int

    fun deleteStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun deleteStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion>

    fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): CourseVersion

    fun getAllVersionsOfSpecificExam(examId: Int): List<ExamVersion>

    fun getVersionOfSpecificExam(examId: Int, versionId: Int): ExamVersion

    fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int): List<WorkAssignmentVersion>

    fun getVersionOfSpecificWorkAssignment(workAssignmentId: Int, versionId: Int): WorkAssignmentVersion

    fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion>

    fun getVersionOfSpecificClass(classId: Int, versionId: Int): ClassVersion

    fun deleteAllVersionsOfCourse(courseId: Int): Int

    fun deleteVersionOfCourse(courseId: Int, version: Int): Int

    fun deleteAllVersionsOfWorkAssignment(courseId: Int, termId: Int, workAssignmentId: Int): Int

    fun deleteVersionOfWorkAssignment(courseId: Int, termId: Int, workAssignmentId: Int, version: Int): Int

    fun deleteAllVersionsOfExam(courseId: Int, termId: Int, examId: Int): Int

    fun deleteVersionOfExam(courseId: Int, termId: Int, examId: Int, version: Int): Int

    /**
     * Report entities queries
     */

    fun getAllReportsOnCourse(courseId: Int): List<CourseReport>

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReport

    fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport>

    fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): ExamReport

    fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport>

    fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(workAssignmentId: Int, reportId: Int): WorkAssignmentReport

    fun reportCourse(courseId: Int, inputCourseReport: CourseReportInputModel): Int

    fun voteOnReportedCourse(reportId: Int, inputVote: VoteInputModel): Int

    fun voteOnReportToExamOnCourseInTerm(reportId: Int, inputVote: VoteInputModel): Int

    fun voteOnReportToWorkAssignmentOnCourseInTerm(reportId: Int, inputVote: VoteInputModel): Int

    fun updateReportedCourse(courseId: Int, reportId: Int) : Int

    fun updateReportedExam(examId: Int, reportId: Int): Int

    fun updateReportedWorkAssignment(workAssignmentId: Int, reportId: Int): Int

    fun addReportToExamOnCourseInTerm(examId: Int, inputExamReport: ExamReportInputModel): Int

    fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, inputWorkAssignmentReport: WorkAssignmentReportInputModel): Int

    fun deleteAllReportsOnCourse(courseId: Int): Int

    fun deleteReportOnCourse(courseId: Int, reportId: Int): Int

    fun deleteAllReportsOnExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int): Int

    fun deleteReportOnExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int, reportId: Int): Int

    fun deleteAllReportsOfWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int): Int

    fun deleteReportOfWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int, reportId: Int): Int

}