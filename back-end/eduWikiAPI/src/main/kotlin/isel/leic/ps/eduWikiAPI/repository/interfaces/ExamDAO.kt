package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import java.util.*

interface ExamDAO {

    /**
     * Main entities queries
     */

    fun deleteSpecificExamOfCourseInTerm(examId: Int): Int

    fun deleteAllExamsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun updateExam(examId: Int, exam: Exam): Exam

    fun createExam(courseId: Int, termId: Int, exam: Exam): Exam

    fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam>

    fun voteOnExam(examId: Int, vote: Vote) : Int

    fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Optional<Exam>

    /**
     * Stage entities queries
     */

    fun getExamSpecificStageEntry(stageId: Int): Optional<ExamStage>

    fun getAllExamStages(): List<ExamStage>

    fun createStagingExam(courseId: Int, termId: Int, examStage: ExamStage): ExamStage

    fun voteOnStagedExam(stageId: Int, vote: Vote): Int

    fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage>

    fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<ExamStage>

    fun deleteStagedExam(stageId: Int): Int

    fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificExam(examId: Int): List<ExamVersion>

    fun getVersionOfSpecificExam(examId: Int, version: Int): Optional<ExamVersion>

    fun getVersionExam(examId: Int, version: Int): Optional<ExamVersion>

    fun getAllVersionExams(): List<ExamVersion>

    fun deleteVersionOfExam(examId: Int, version: Int): Int

    fun deleteAllVersionOfExam(examId: Int): Int

    fun createVersionExam(examVersion: ExamVersion): ExamVersion

    /**
     * Report entity queries
     */

    fun reportExam(examReport: ExamReport): ExamReport

    fun deleteReportOnExam(examId: Int, reportId: Int): Int

    fun deleteAllReportsOnExam(examId: Int): Int

    fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport>

    fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): Optional<ExamReport>

    fun voteOnReportToExamOnCourseInTerm(reportId: Int, vote: Vote): Int

    fun getSpecificReportOfExam(examId: Int, reportId: Int): Optional<ExamReport>

}


