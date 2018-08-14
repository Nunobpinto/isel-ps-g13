package isel.leic.ps.eduWikiAPI.service.interfaces

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

    fun createCourse(inputCourse: CourseInputModel): CourseOutputModel

    fun createCourseFromStaged(stageId: Int): CourseOutputModel

    fun createExamOnCourseInTerm(sheet: MultipartFile, courseId: Int, termId: Int, inputExam: ExamInputModel): ExamOutputModel

    fun createExamFromStaged(courseId: Int, termId: Int, stageId: Int): ExamOutputModel

    fun createWorkAssignmentOnCourseInTerm(sheet: MultipartFile, courseId: Int, termId: Int, inputWorkAssignment: WorkAssignmentInputModel): WorkAssignmentOutputModel

    fun createWorkAssignmentFromStaged(courseId: Int, termId: Int, stageId: Int): WorkAssignmentOutputModel

    fun voteOnCourse(courseId: Int, vote: VoteInputModel): Int

    fun voteOnExam(examId: Int, vote: VoteInputModel): Int

    fun voteOnWorkAssignment(workAssignmentId: Int, vote: VoteInputModel): Int

    fun partialUpdateOnCourse(courseId: Int, inputCourse: CourseInputModel): CourseOutputModel

    fun deleteAllCourses(): Int

    fun deleteSpecificCourse(courseId: Int): Int

    fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun deleteSpecificExamOfCourseInTerm(courseId: Int, termId: Int, examId: Int): Int

    fun deleteAllWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun deleteSpecificWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int): Int

    /**
     * Stage entities queries
     */

    fun getAllCourseStageEntries(): CourseStageCollectionOutputModel

    fun getCourseSpecificStageEntry(stageId: Int): CourseStageOutputModel

    fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStageOutputModel>

    fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStageOutputModel

    fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStageOutputModel>

    fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStageOutputModel

    fun createStagingCourse(inputCourse: CourseInputModel): CourseStageOutputModel

    fun createStagingExam(sheet: MultipartFile, courseId: Int, termId: Int, examInputModel: ExamInputModel): ExamStageOutputModel

    fun createStagingWorkAssignment(sheet: MultipartFile, courseId: Int, termId: Int, inputWorkAssignment: WorkAssignmentInputModel): WorkAssignmentStageOutputModel

    fun voteOnStagedCourse(stageId: Int, vote: VoteInputModel): Int

    fun voteOnStagedExam(stageId: Int, vote: VoteInputModel): Int

    fun voteOnStagedWorkAssignment(stageId: Int, vote: VoteInputModel): Int

    fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int

    fun deleteAllStagedCourses(): Int

    fun deleteSpecificStagedCourse(stageId: Int): Int

    fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun deleteSpecificStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificCourse(courseId: Int): CourseVersionCollectionOutputModel

    fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): CourseVersionOutputModel

    fun getAllVersionsOfSpecificExam(examId: Int): ExamVersionCollectionOutputModel

    fun getVersionOfSpecificExam(examId: Int, versionId: Int): ExamVersionOutputModel

    fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int): WorkAssignmentVersionCollectionOutputModel

    fun getVersionOfSpecificWorkAssignment(workAssignmentId: Int, versionId: Int): WorkAssignmentVersionOutputModel

    fun deleteAllVersionsOfCourse(courseId: Int): Int

    fun deleteVersionOfCourse(courseId: Int, version: Int): Int

    fun deleteAllVersionsOfWorkAssignment(workAssignmentId: Int): Int

    fun deleteVersionOfWorkAssignment(workAssignmentId: Int, version: Int): Int

    fun deleteAllVersionsOfExam(examId: Int): Int

    fun deleteVersionOfExam(examId: Int, version: Int): Int

    /**
     * Report entities queries
     */

    fun getAllReportsOnCourse(courseId: Int): CourseReportCollectionOutputModel

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReportOutputModel

    fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): ExamReportCollectionOutputModel

    fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): ExamReportOutputModel

    fun getAllReportsOnWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): WorkAssignmentReportCollectionOutputModel

    fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(workAssignmentId: Int, reportId: Int): WorkAssignmentReportOutputModel

    fun reportCourse(courseId: Int, inputCourseReport: CourseReportInputModel): CourseReportOutputModel

    fun voteOnReportedCourse(reportId: Int, vote: VoteInputModel): Int

    fun voteOnReportedExamOnCourseInTerm(reportId: Int, vote: VoteInputModel): Int

    fun voteOnReportedWorkAssignmentOnCourseInTerm(reportId: Int, vote: VoteInputModel): Int

    fun updateReportedCourse(courseId: Int, reportId: Int) : CourseOutputModel

    fun updateReportedExam(examId: Int, reportId: Int, courseId: Int, termId: Int): ExamOutputModel

    fun updateWorkAssignmentBasedOnReport(workAssignmentId: Int, reportId: Int, courseId: Int, termId: Int): WorkAssignmentOutputModel

    fun addReportToExamOnCourseInTerm(examId: Int, inputExamReport: ExamReportInputModel): ExamReportOutputModel

    fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, inputWorkAssignmentReport: WorkAssignmentReportInputModel): WorkAssignmentReportOutputModel

    fun deleteAllReportsOnCourse(courseId: Int): Int

    fun deleteReportOnCourse(courseId: Int, reportId: Int): Int

    fun deleteAllReportsOnExam(examId: Int): Int

    fun deleteReportOnExam(examId: Int, reportId: Int): Int

    fun deleteAllReportsOnWorkAssignment(workAssignmentId: Int): Int

    fun deleteReportOnWorkAssignment(workAssignmentId: Int, reportId: Int): Int

}