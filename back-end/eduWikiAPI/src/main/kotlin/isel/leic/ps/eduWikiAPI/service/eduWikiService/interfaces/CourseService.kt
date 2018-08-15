package isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.CourseInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.ExamInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.WorkAssignmentInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ExamReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.WorkAssignmentReportInputModel
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
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

interface CourseService {

    /**
     * Main entities queries
     */

    fun getAllCourses(): CourseCollectionOutputModel

    fun getSpecificCourse(courseId: Int): CourseOutputModel

    fun getTermsOfCourse(courseId: Int): List<TermOutputModel>

    fun getSpecificTermOfCourse(courseId: Int, termId: Int): TermOutputModel

    fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): ExamCollectionOutputModel

    fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): ExamOutputModel

    fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): WorkAssignmentCollectionOutputModel

    fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int, courseId: Int, termId: Int): WorkAssignmentOutputModel

    fun createCourse(inputCourse: CourseInputModel, principal: Principal): CourseOutputModel

    fun createCourseFromStaged(stageId: Int, principal: Principal): CourseOutputModel

    fun createExamOnCourseInTerm(courseId: Int, termId: Int, sheet: MultipartFile, inputExam: ExamInputModel, principal: Principal): ExamOutputModel

    fun createExamFromStaged(courseId: Int, termId: Int, stageId: Int, principal: Principal): ExamOutputModel

    fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, inputWorkAssignment: WorkAssignmentInputModel, sheet: MultipartFile, principal: Principal): WorkAssignmentOutputModel

    fun createWorkAssignmentFromStaged(courseId: Int, termId: Int, stageId: Int, principal: Principal): WorkAssignmentOutputModel

    fun voteOnCourse(courseId: Int, vote: VoteInputModel, principal: Principal): Int

    fun voteOnExam(termId: Int, courseId: Int, examId: Int, inputVote: VoteInputModel, principal: Principal): Int

    fun voteOnWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, vote: VoteInputModel, principal: Principal): Int

    fun partialUpdateOnCourse(courseId: Int, inputCourse: CourseInputModel, principal: Principal): CourseOutputModel

    fun deleteSpecificCourse(courseId: Int, principal: Principal): Int

    fun deleteSpecificExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int, principal: Principal): Int

    fun deleteSpecificWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int, principal: Principal): Int

    /**
     * Stage entities queries
     */

    fun getAllCourseStageEntries(): CourseStageCollectionOutputModel

    fun getCourseSpecificStageEntry(stageId: Int): CourseStageOutputModel

    fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStageOutputModel>

    fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStageOutputModel

    fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStageOutputModel>

    fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStageOutputModel

    fun createStagingCourse(inputCourse: CourseInputModel, principal: Principal): CourseStageOutputModel

    fun createStagingExam(courseId: Int, termId: Int, examInputModel: ExamInputModel, sheet: MultipartFile, principal: Principal): ExamStageOutputModel

    fun createStagingWorkAssignment(sheet: MultipartFile, courseId: Int, termId: Int, inputWorkAssignment: WorkAssignmentInputModel, principal: Principal): WorkAssignmentStageOutputModel

    fun voteOnStagedCourse(stageId: Int, vote: VoteInputModel, principal: Principal): Int

    fun voteOnStagedExam(termId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int

    fun voteOnStagedWorkAssignment(termId: Int, courseId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int

    fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int, principal: Principal): Int

    fun deleteSpecificStagedCourse(stageId: Int, principal: Principal): Int

    fun deleteSpecificStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int, principal: Principal): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificCourse(courseId: Int): CourseVersionCollectionOutputModel

    fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): CourseVersionOutputModel

    fun getAllVersionsOfSpecificExam(termId: Int, courseId: Int, examId: Int): ExamVersionCollectionOutputModel

    fun getVersionOfSpecificExam(termId: Int, courseId: Int, examId: Int, versionId: Int): ExamVersionOutputModel

    fun getAllVersionsOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int): WorkAssignmentVersionCollectionOutputModel

    fun getVersionOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, versionId: Int): WorkAssignmentVersionOutputModel

    /**
     * Report entities queries
     */

    fun getAllReportsOnCourse(courseId: Int): CourseReportCollectionOutputModel

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReportOutputModel

    fun getAllReportsOnExamOnSpecificTermOfCourse(termId: Int, courseId: Int, examId: Int): ExamReportCollectionOutputModel

    fun getSpecificReportOnExamOnSpecificTermOfCourse(termId: Int, courseId: Int, examId: Int, reportId: Int): ExamReportOutputModel

    fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): WorkAssignmentReportCollectionOutputModel

    fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int): WorkAssignmentReportOutputModel

    fun reportCourse(courseId: Int, inputCourseReport: CourseReportInputModel, principal: Principal): CourseReportOutputModel

    fun voteOnReportedCourse(courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int

    fun voteOnReportedExamOnCourseInTerm(termId: Int, courseId: Int, examId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int

    fun voteOnReportedWorkAssignmentOnCourseInTerm(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int, inputVote: VoteInputModel, principal: Principal): Int

    fun updateReportedCourse(courseId: Int, reportId: Int, principal: Principal) : CourseOutputModel

    fun updateReportedExam(examId: Int, reportId: Int, courseId: Int, termId: Int, principal: Principal): ExamOutputModel

    fun updateWorkAssignmentBasedOnReport(workAssignmentId: Int, reportId: Int, courseId: Int, termId: Int, principal: Principal): WorkAssignmentOutputModel

    fun addReportToExamOnCourseInTerm(termId: Int, courseId: Int, examId: Int, inputExamReport: ExamReportInputModel, principal: Principal): ExamReportOutputModel

    fun addReportToWorkAssignmentOnCourseInTerm(termId: Int, courseId: Int, workAssignmentId: Int, inputWorkAssignmentReport: WorkAssignmentReportInputModel, principal: Principal): WorkAssignmentReportOutputModel

    fun deleteReportOnCourse(courseId: Int, reportId: Int, principal: Principal): Int

    fun deleteReportOnExam(termId: Int, courseId: Int, examId: Int, reportId: Int, principal: Principal): Int

    fun deleteReportOnWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int, principal: Principal): Int

}