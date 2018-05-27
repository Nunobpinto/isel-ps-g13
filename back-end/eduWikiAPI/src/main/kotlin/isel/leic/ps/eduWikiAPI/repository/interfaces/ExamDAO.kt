package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion

interface ExamDAO {

    /**
     * Main entities queries
     */

    fun getSpecificExam(courseMiscUnitId: Int): Exam

    fun getAllExams(): List<Exam>

    fun deleteExam(courseMiscUnitId: Int): Int

    fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun updateExam(examId: Int, exam: Exam): Int

    fun createExam(courseId: Int, termId: Int, exam: Exam): Int

    fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam>

    fun voteOnExam(courseMiscUnitId: Int, voteType: Int)

    fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Exam

    fun createExamOnCourseInTerm(courseId: Int, termId: Int, exam: Exam): Int

    /**
     * Stage entities queries
     */

    fun getExamSpecificStageEntry(stageId: Int): ExamStage

    fun getAllExamStages(): List<ExamStage>

    fun createStagingExam(courseId: Int, termId: Int, examStage: ExamStage): Int

    fun voteOnStagedExam(stageId: Int, inputVote: VoteInputModel): Int

    fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage>

    fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStage

    fun deleteStagedExam(stageId: Int): Int

    fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificExam(examId: Int): List<ExamVersion>

    fun getVersionOfSpecificExam(examId: Int, versionId: Int): ExamVersion

    fun getVersionExam(versionExamId: Int, version: Int): ExamVersion

    fun getAllVersionExams(): List<ExamVersion>

    fun deleteVersionExam(versionExamId: Int, version: Int): Int

    fun deleteAllVersionExams(versionExamId: Int): Int

    fun createVersionExam(examVersion: ExamVersion)

    fun addToExamVersion(exam: Exam): Int

    /**
     * Report entity queries
     */

    fun reportExam(examReport: ExamReport)

    fun deleteReportOnExam(examId: Int, reportId: Int): Int

    fun deleteAllReportsOnExam(courseMiscUnitId: Int): Int

    fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport>

    fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): ExamReport

    fun addReportToExamOnCourseInTerm(examId: Int, examReport: ExamReport): Int

    fun voteOnReportToExamOnCourseInTerm(reportId: Int, inputVote: VoteInputModel): Int

    fun getSpecificReportOfExam(examId: Int, reportId: Int): ExamReport

}


