package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion

interface ExamDAO {

    /**
     * Main entities queries
     */

    fun getExam(courseMiscUnitId: Int) : Exam

    fun getAllExams() : List<Exam>

    fun deleteExam(courseMiscUnitId: Int) : Int

    fun deleteAllExams() : Int

    fun updateExam(exam: Exam) : Int

    fun createExam(exam: Exam)

    fun voteOnExam(courseMiscUnitId: Int, voteType: Int)

    /**
     * Stage entities queries
     */

    fun getExamStage(courseMiscUnitStageId: Int) : ExamStage

    fun getAllExamStages() : List<ExamStage>

    fun deleteExamStage(courseMiscUnitStageId: Int) : Int

    fun deleteAllExamStages() : Int

    fun createExamStage(workAssignmentStage: ExamStage)

    fun voteOnExamStage(courseMiscUnitStageId: Int, voteType: Int)

    /**
     * Version entities queries
     */

    fun getVersionExam(versionExamId: Int, version: Int) : ExamVersion

    fun getAllVersionExams() : List<ExamVersion>

    fun deleteVersionExam(versionExamId: Int, version: Int) : Int

    fun deleteAllVersionExams() : Int

    fun createVersionExam(examVersion: ExamVersion)

    /**
     * Report entity queries
     */

    fun reportExam(examReport: ExamReport)

    fun deleteReportOnExam(reportId: Int) : Int

    fun deleteAllReportsOnExam(courseMiscUnitId : Int) : Int

    fun deleteAllReports() : Int

}