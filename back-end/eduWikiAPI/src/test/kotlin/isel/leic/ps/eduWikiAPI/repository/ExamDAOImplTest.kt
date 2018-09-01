package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.enums.ExamType
import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import org.junit.Test
import junit.framework.TestCase.*

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createMainTablesDb.sql", "classpath:insertMainTables.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropMainTablesDb.sql"]))
)
@Transactional
class ExamDAOImplTest {

    @Autowired
    lateinit var examDAO: ExamDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getAllExamsFromSpecificTermOfCourse() {
        val exams = examDAO.getAllExamsFromSpecificTermOfCourse(1, 1)
        assertEquals(2, exams.size)
    }

    @Test
    fun getSpecificExamFromSpecificTermOfCourse() {
        val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(1, 1, 5).get()
        assertEquals(1, exam.courseId)
        assertEquals("jg", exam.createdBy)
        assertEquals(LocalDate.parse("2012-07-14"), exam.dueDate)
        assertEquals(5, exam.examId)
        assertEquals("A.2.11", exam.location)
        assertEquals(1, exam.logId)
        assertEquals("2ª", exam.phase)
        assertEquals(1, exam.termId)
        assertEquals("TEST", exam.type.name)
        assertEquals(2, exam.version)
        assertEquals(0, exam.votes)
    }

    @Test
    fun createExamOnCourseInTerm() {
        val exam = examDAO.createExamOnCourseInTerm(
                courseId = 1,
                termId = 1,
                exam = Exam(
                        createdBy = "jg",
                        dueDate = LocalDate.parse("2017-01-18"),
                        type = ExamType.EXAM,
                        phase = "Epoca Especial",
                        location = "A.2.17"
                )
        )
        assertEquals("jg", exam.createdBy)
        assertEquals(LocalDate.parse("2017-01-18"), exam.dueDate)
        assertEquals(ExamType.EXAM, exam.type)
        assertEquals("Epoca Especial", exam.phase)
        assertEquals("A.2.17", exam.location)
        assertEquals(5, exam.logId)
        assertEquals(0, exam.votes)
        assertEquals(1, exam.version)
        assertEquals(9, exam.examId)
    }

    @Test
    fun updateExam() {
        val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(1, 1, 5).get()
        assertEquals(2, exam.version)
        val updatedExam = examDAO.updateExam(
                examId = 5,
                exam = Exam(
                        examId = exam.examId,
                        createdBy = "mike",
                        dueDate = LocalDate.parse("2016-07-22"),
                        type = ExamType.TEST,
                        phase = "Global",
                        location = "E.1.10",
                        logId = exam.logId,
                        version = exam.version.inc()
                )
        )
        assertEquals(3, updatedExam.version)
        assertEquals("mike", updatedExam.createdBy)
        assertEquals(LocalDate.parse("2016-07-22"), updatedExam.dueDate)
        assertEquals("TEST", updatedExam.type.name)
        assertEquals("Global", updatedExam.phase)
        assertEquals("E.1.10", updatedExam.location)
        assertEquals(1, updatedExam.logId)
        assertEquals(0, updatedExam.votes)
        assertEquals(5, updatedExam.examId)
    }

    @Test
    fun updateVotesOnExam() {
        val oldExam = examDAO.getSpecificExamFromSpecificTermOfCourse(1, 1, 5).get()
        assertEquals(0, oldExam.votes)
        val rowsAffected = examDAO.updateVotesOnExam(5, 64)
        assertEquals(1, rowsAffected)
        val updatedExam = examDAO.getSpecificExamFromSpecificTermOfCourse(1, 1, 5).get()
        assertEquals(64, updatedExam.votes)
    }

    @Test
    fun deleteSpecificExamOfCourseInTerm() {
        val rowsAffected = examDAO.deleteSpecificExamOfCourseInTerm(1, 1, 6)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getStageEntriesFromExamOnSpecificTermOfCourse() {
        val stageEntries = examDAO.getStageEntriesFromExamOnSpecificTermOfCourse(1, 1)
        assertEquals(2, stageEntries.size)
    }

    @Test
    fun getStageEntryFromExamOnSpecificTermOfCourse() {
        val stageEntry = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(1, 1, 1).get()
        assertEquals(1, stageEntry.courseId)
        assertEquals(1, stageEntry.termId)
        assertEquals("jg", stageEntry.createdBy)
        assertEquals(LocalDate.parse("2015-06-30"), stageEntry.dueDate)
        assertEquals("F.0.1", stageEntry.location)
        assertEquals(1, stageEntry.logId)
        assertEquals("1ª", stageEntry.phase)
        assertEquals(1, stageEntry.stageId)
        assertEquals(1, stageEntry.termId)
        assertEquals("EXAM", stageEntry.type.name)
        assertEquals(24, stageEntry.votes)
    }

    @Test
    fun createStagingExamOnCourseInTerm() {
        val stagingExam = examDAO.createStagingExamOnCourseInTerm(
                1,
                1,
                ExamStage(
                        courseId = 1,
                        termId = 1,
                        dueDate = LocalDate.parse("2014-06-14"),
                        type = ExamType.TEST,
                        phase = "Global",
                        location = "G.0.24",
                        createdBy = "andreia"
                )
        )
        assertEquals("TEST", stagingExam.type.name)
        assertEquals(5, stagingExam.stageId)
        assertEquals("Global", stagingExam.phase)
        assertEquals(3, stagingExam.logId)
        assertEquals("G.0.24", stagingExam.location)
        assertEquals(LocalDate.parse("2014-06-14"), stagingExam.dueDate)
        assertEquals("andreia", stagingExam.createdBy)
        assertEquals(0, stagingExam.votes)
    }

    @Test
    fun updateVotesOnStagedExam() {
        val oldStagedExam = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(1, 1, 2).get()
        assertEquals(7, oldStagedExam.votes)
        val rowsAffected = examDAO.updateVotesOnStagedExam(2, 12)
        assertEquals(1, rowsAffected)
        val updatedExam = examDAO.getStageEntryFromExamOnSpecificTermOfCourse(1, 1, 2).get()
        assertEquals(12, updatedExam.votes)
    }

    @Test
    fun deleteSpecificStagedExamOfCourseInTerm() {
        val rowsAffected = examDAO.deleteSpecificStagedExamOfCourseInTerm(1, 1, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllVersionsOfSpecificExam() {
        val versions = examDAO.getAllVersionsOfSpecificExam(1, 1, 5)
        assertEquals(2, versions.size)
    }

    @Test
    fun getVersionOfSpecificExam() {
        val examVersion = examDAO.getVersionOfSpecificExam(1, 1, 5, 2).get()
        assertEquals("jg", examVersion.createdBy)
        assertEquals(LocalDate.parse("2012-07-14"), examVersion.dueDate)
        assertEquals(5, examVersion.examId)
        assertEquals("A.2.11", examVersion.location)
        assertEquals("2ª", examVersion.phase)
        assertEquals("TEST", examVersion.type.name)
        assertEquals(2, examVersion.version)
    }

    @Test
    fun createExamVersion() {
        val examVersion = examDAO.createExamVersion(
                ExamVersion(
                        version = 3,
                        examId = 5,
                        dueDate = LocalDate.parse("2012-08-17"),
                        type = ExamType.EXAM,
                        phase = "1ª",
                        location = "C.1.21",
                        createdBy = "maria"
                )
        )
        assertEquals("maria", examVersion.createdBy)
        assertEquals(LocalDate.parse("2012-08-17"), examVersion.dueDate)
        assertEquals(5, examVersion.examId)
        assertEquals("C.1.21", examVersion.location)
        assertEquals("1ª", examVersion.phase)
        assertEquals("EXAM", examVersion.type.name)
        assertEquals(3, examVersion.version)
    }

    @Test
    fun getAllReportsOnExamOnSpecificTermOfCourse() {
        val reports = examDAO.getAllReportsOnExamOnSpecificTermOfCourse(1, 1, 5)
        assertEquals(2, reports.size)
    }

    @Test
    fun getSpecificReportOnExamOnSpecificTermOfCourse() {
        val report = examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(1, 1, 5, 2).get()
        assertEquals(LocalDate.parse("2015-07-09"), report.dueDate)
        assertEquals(5, report.examId)
        assertEquals("E.2.20", report.location)
        assertEquals(2, report.logId)
        assertEquals("1ª", report.phase)
        assertEquals(2, report.reportId)
        assertEquals("rita", report.reportedBy)
        assertEquals(1, report.termId)
        assertEquals(1, report.courseId)
        assertEquals("TEST", report.type!!.name)
        assertEquals(81, report.votes)
    }

    @Test
    fun reportExam() {
        val report = examDAO.reportExam(
                ExamReport(
                        examId = 5,
                        courseId = 1,
                        termId = 1,
                        dueDate = LocalDate.parse("2015-07-10"),
                        type = ExamType.EXAM,
                        phase = "1",
                        location = "E.2.20",
                        reportedBy = "filipa",
                        votes = 73
                )
        )
        assertEquals(LocalDate.parse("2015-07-10"), report.dueDate)
        assertEquals(5, report.examId)
        assertEquals("E.2.20", report.location)
        assertEquals(3, report.logId)
        assertEquals("1", report.phase)
        assertEquals(3, report.reportId)
        assertEquals("filipa", report.reportedBy)
        assertEquals("EXAM", report.type!!.name)
        assertEquals(73, report.votes)
    }

    @Test
    fun updateVotesOnReportedExam() {
        val oldReportedExam = examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(1, 1, 5, 1).get()
        assertEquals(14, oldReportedExam.votes)
        val rowsAffected = examDAO.updateVotesOnReportedExam(1, 10)
        assertEquals(1, rowsAffected)
        val updatedExam = examDAO.getSpecificReportOnExamOnSpecificTermOfCourse(1, 1, 5, 1).get()
        assertEquals(10, updatedExam.votes)
    }

    @Test
    fun deleteReportOnExam() {
        val rowsAffected = examDAO.deleteReportOnExam(1, 1, 5, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getExamByLogId() {
        val exam = examDAO.getExamByLogId(1).get()
        assertEquals(1, exam.courseId)
        assertEquals("jg", exam.createdBy)
        assertEquals(LocalDate.parse("2012-07-14"), exam.dueDate)
        assertEquals(5, exam.examId)
        assertEquals("A.2.11", exam.location)
        assertEquals(1, exam.logId)
        assertEquals("2ª", exam.phase)
        assertEquals(1, exam.termId)
        assertEquals("TEST", exam.type.name)
        assertEquals(2, exam.version)
        assertEquals(0, exam.votes)
    }

    @Test
    fun getExamStageByLogId() {
        val stage = examDAO.getExamStageByLogId(1).get()
        assertEquals(1, stage.courseId)
        assertEquals(1, stage.termId)
        assertEquals("jg", stage.createdBy)
        assertEquals(LocalDate.parse("2015-06-30"), stage.dueDate)
        assertEquals("F.0.1", stage.location)
        assertEquals(1, stage.logId)
        assertEquals("1ª", stage.phase)
        assertEquals(1, stage.stageId)
        assertEquals(1, stage.termId)
        assertEquals("EXAM", stage.type.name)
        assertEquals(24, stage.votes)
    }

    @Test
    fun getExamReportByLogId() {
        val report = examDAO.getExamReportByLogId(1).get()
        assertEquals(LocalDate.parse("2015-07-11"), report.dueDate)
        assertEquals(5, report.examId)
        assertEquals("E.2.20", report.location)
        assertEquals(1, report.logId)
        assertEquals("1ª", report.phase)
        assertEquals(1, report.reportId)
        assertEquals("sara", report.reportedBy)
        assertEquals(1, report.termId)
        assertEquals(1, report.courseId)
        assertEquals("TEST", report.type!!.name)
        assertEquals(14, report.votes)
    }

    @Test
    fun getAllExamsFromSpecificCourse() {
        val exams = examDAO.getAllExamsFromSpecificCourse(1)
        assertEquals(2, exams.size)
    }

    @Test
    fun getAllStagedExamOnSpecificCourse() {
        val stagedExams = examDAO.getAllStagedExamOnSpecificCourse(1)
        assertEquals(2, stagedExams.size)
    }

}