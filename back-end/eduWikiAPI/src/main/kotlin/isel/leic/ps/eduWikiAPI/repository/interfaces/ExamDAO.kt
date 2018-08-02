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

    fun createExamOnCourseInTerm(courseId: Int, termId: Int, exam: Exam): Exam

    fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam>

    fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Optional<Exam>

    fun getVotesOnExam(examId: Int): Int

    fun updateVotesOnExam(examId: Int, votes: Int): Int

    /**
     * Stage entities queries
     */

    fun getExamSpecificStageEntry(stageId: Int): Optional<ExamStage>

    fun getAllStagedExams(): List<ExamStage>

    fun createStagingExamOnCourseInTerm(courseId: Int, termId: Int, examStage: ExamStage): ExamStage

    fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage>

    fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<ExamStage>

    fun deleteStagedExam(stageId: Int): Int

    fun deleteAllStagedExamsOfCourseInTerm(courseId: Int, termId: Int): Int

    fun getVotesOnStagedExam(stageId: Int): Int

    fun updateVotesOnStagedExam(stageId: Int, votes: Int): Int


    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificExam(examId: Int): List<ExamVersion>

    fun getVersionOfSpecificExam(examId: Int, version: Int): Optional<ExamVersion>

    fun getVersionExam(examId: Int, version: Int): Optional<ExamVersion>

    fun getAllVersionExams(): List<ExamVersion>

    fun deleteVersionOfExam(examId: Int, version: Int): Int

    fun deleteAllVersionOfExam(examId: Int): Int

    fun createExamVersion(examVersion: ExamVersion): ExamVersion

    /**
     * Report entity queries
     */

    fun reportExam(examReport: ExamReport): ExamReport

    fun deleteReportOnExam(examId: Int, reportId: Int): Int

    fun deleteAllReportsOnExam(examId: Int): Int

    fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport>

    fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): Optional<ExamReport>

    fun getSpecificReportOfExam(examId: Int, reportId: Int): Optional<ExamReport>

    fun getVotesOnReportedExam(reportId: Int): Int

    fun updateVotesOnReportedExam(reportId: Int, votes: Int): Int

}


