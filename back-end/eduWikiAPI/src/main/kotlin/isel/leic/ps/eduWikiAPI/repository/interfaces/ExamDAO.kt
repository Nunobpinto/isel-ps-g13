package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import java.util.*

interface ExamDAO {

    /**
     * Main entities queries
     */

    fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam>

    fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Optional<Exam>

    fun createExamOnCourseInTerm(courseId: Int, termId: Int, exam: Exam): Exam

    fun updateExam(examId: Int, exam: Exam): Exam

    fun updateVotesOnExam(examId: Int, votes: Int): Int

    fun deleteSpecificExamOfCourseInTerm(termId: Int, courseId: Int, examId: Int): Int

    /**
     * Stage entities queries
     */

    fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage>

    fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<ExamStage>

    fun createStagingExamOnCourseInTerm(courseId: Int, termId: Int, examStage: ExamStage): ExamStage

    fun updateVotesOnStagedExam(stageId: Int, votes: Int): Int

    fun deleteSpecificStagedExamOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int


    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificExam(termId: Int, courseId: Int, examId: Int): List<ExamVersion>

    fun getVersionOfSpecificExam(termId: Int, courseId: Int, examId: Int, version: Int): Optional<ExamVersion>

    fun createExamVersion(examVersion: ExamVersion): ExamVersion

    /**
     * Report entity queries
     */

    fun getAllReportsOnExamOnSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): List<ExamReport>

    fun getSpecificReportOnExamOnSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int, reportId: Int): Optional<ExamReport>

    fun reportExam(examReport: ExamReport): ExamReport

    fun updateVotesOnReportedExam(reportId: Int, votes: Int): Int

    fun deleteReportOnExam(courseId: Int, termId: Int, examId: Int, reportId: Int): Int

    fun getExamByLogId(logId: Int): Optional<Exam>

    fun getExamStageByLogId(logId: Int): Optional<ExamStage>

    fun getExamReportByLogId(logId: Int): Optional<ExamReport>

    fun getAllExamsFromSpecificCourse(courseId: Int): List<Exam>

    fun getAllStagedExamOnSpecificCourse(courseId: Int): List<ExamStage>

}


