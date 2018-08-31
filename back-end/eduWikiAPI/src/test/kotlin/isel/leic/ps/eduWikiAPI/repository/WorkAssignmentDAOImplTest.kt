package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import junit.framework.TestCase.*
import org.junit.Test

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
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createDB.sql", "classpath:inserts.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropDB.sql"]))
)
@Transactional
class WorkAssignmentDAOImplTest {

    @Autowired
    lateinit var workAssignmentDAO: WorkAssignmentDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getAllWorkAssignmentsFromSpecificTermOfCourse() {
        val exams = workAssignmentDAO.getAllWorkAssignmentsFromSpecificTermOfCourse(1, 1)
        assertEquals(2, exams.size)
    }

    @Test
    fun getSpecificWorkAssignmentOfCourseInTerm() {
        val workAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(1, 1, 2).get()
        assertEquals(1, workAssignment.courseId)
        assertEquals("ze", workAssignment.createdBy)
        assertEquals(LocalDate.parse("2016-03-24"), workAssignment.dueDate)
        assertEquals(2, workAssignment.workAssignmentId)
        assertEquals(2, workAssignment.logId)
        assertEquals("2º", workAssignment.phase)
        assertEquals(1, workAssignment.termId)
        assertEquals(2, workAssignment.version)
        assertEquals(false, workAssignment.individual)
        assertEquals(true, workAssignment.lateDelivery)
        assertEquals(true, workAssignment.multipleDeliveries)
        assertEquals(false, workAssignment.requiresReport)
        assertEquals(0, workAssignment.votes)
    }

    @Test
    fun createWorkAssignmentOnCourseInTerm() {
        val workAssignment = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(
                courseId = 1,
                termId = 1,
                workAssignment = WorkAssignment(
                        individual = true,
                        lateDelivery = false,
                        multipleDeliveries = true,
                        requiresReport = false,
                        createdBy = "ines",
                        dueDate = LocalDate.parse("2017-09-07"),
                        phase = "Epoca Especial",
                        votes = 36
                )
        )
        assertEquals("ines", workAssignment.createdBy)
        assertEquals(LocalDate.parse("2017-09-07"), workAssignment.dueDate)
        assertEquals("Epoca Especial", workAssignment.phase)
        assertEquals(5, workAssignment.logId)
        assertEquals(36, workAssignment.votes)
        assertEquals(1, workAssignment.version)
        assertEquals(9, workAssignment.workAssignmentId)
        assertEquals(true, workAssignment.individual)
        assertEquals(false, workAssignment.lateDelivery)
        assertEquals(true, workAssignment.multipleDeliveries)
        assertEquals(false, workAssignment.requiresReport)
    }

    @Test
    fun updateWorkAssignment() {
        val workAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(1, 1, 1).get()
        assertEquals(2, workAssignment.version)
        val updatedworkAssignment = workAssignmentDAO.updateWorkAssignment(
                workAssignmentId = 1,
                workAssignment = WorkAssignment(
                        courseId = workAssignment.courseId,
                        termId = workAssignment.termId,
                        workAssignmentId = workAssignment.workAssignmentId,
                        createdBy = "carolina",
                        dueDate = LocalDate.parse("2017-05-12"),
                        votes = 78,
                        individual = false,
                        lateDelivery = true,
                        multipleDeliveries = true,
                        requiresReport = false,
                        phase = "2º",
                        logId = workAssignment.logId,
                        version = workAssignment.version.inc()
                )
        )
        assertEquals(3, updatedworkAssignment.version)
        assertEquals("carolina", updatedworkAssignment.createdBy)
        assertEquals(LocalDate.parse("2017-05-12"), updatedworkAssignment.dueDate)
        assertEquals("2º", updatedworkAssignment.phase)
        assertEquals(false, updatedworkAssignment.individual)
        assertEquals(true, updatedworkAssignment.lateDelivery)
        assertEquals(true, updatedworkAssignment.multipleDeliveries)
        assertEquals(false, updatedworkAssignment.requiresReport)
        assertEquals(1, updatedworkAssignment.logId)
        assertEquals(78, updatedworkAssignment.votes)
        assertEquals(1, updatedworkAssignment.workAssignmentId)
    }

    @Test
    fun updateVotesOnWorkAssignment() {
        val oldExam = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(1, 1, 1).get()
        assertEquals(0, oldExam.votes)
        val rowsAffected = workAssignmentDAO.updateVotesOnWorkAssignment(1, 174)
        assertEquals(1, rowsAffected)
        val updatedWorkAssignment = workAssignmentDAO.getSpecificWorkAssignmentOfCourseInTerm(1, 1, 1).get()
        assertEquals(174, updatedWorkAssignment.votes)
    }

    @Test
    fun deleteSpecificWorkAssignment() {
        val rowsAffected = workAssignmentDAO.deleteSpecificWorkAssignment(1, 1, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse() {
        val stageEntries = workAssignmentDAO.getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(1, 1)
        assertEquals(2, stageEntries.size)
    }

    @Test
    fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse() {
        val stageEntry = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(1, 1, 1).get()
        assertEquals(1, stageEntry.courseId)
        assertEquals(1, stageEntry.termId)
        assertEquals("carlos", stageEntry.createdBy)
        assertEquals(LocalDate.parse("2014-12-10"), stageEntry.dueDate)
        assertEquals(false, stageEntry.individual)
        assertEquals(1, stageEntry.logId)
        assertEquals("2º", stageEntry.phase)
        assertEquals(1, stageEntry.stageId)
        assertEquals(1, stageEntry.termId)
        assertEquals(true, stageEntry.lateDelivery)
        assertEquals(true, stageEntry.multipleDeliveries)
        assertEquals(false, stageEntry.requiresReport)
        assertEquals(0, stageEntry.votes)
    }

    @Test
    fun createStagingWorkAssingment() {
        val stageEntry = workAssignmentDAO.createStagingWorkAssingment(
                1,
                1,
                WorkAssignmentStage(
                        dueDate = LocalDate.parse("2010-11-14"),
                        individual = true,
                        lateDelivery = true,
                        multipleDeliveries = true,
                        requiresReport = true,
                        phase = "Epoca Especial",
                        createdBy = "daniela",
                        votes = 7
                )
        )
        assertEquals("daniela", stageEntry.createdBy)
        assertEquals(LocalDate.parse("2010-11-14"), stageEntry.dueDate)
        assertEquals(true, stageEntry.individual)
        assertEquals(3, stageEntry.logId)
        assertEquals("Epoca Especial", stageEntry.phase)
        assertEquals(5, stageEntry.stageId)
        assertEquals(true, stageEntry.lateDelivery)
        assertEquals(true, stageEntry.multipleDeliveries)
        assertEquals(true, stageEntry.requiresReport)
        assertEquals(7, stageEntry.votes)
    }

    @Test
    fun updateStagedWorkAssignmentVotes() {
        val oldStagedWorkAssignment = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(1, 1, 2).get()
        assertEquals(0, oldStagedWorkAssignment.votes)
        val rowsAffected = workAssignmentDAO.updateStagedWorkAssignmentVotes(2, 28)
        assertEquals(1, rowsAffected)
        val updatedExam = workAssignmentDAO.getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(1, 1, 2).get()
        assertEquals(28, updatedExam.votes)
    }

    @Test
    fun deleteSpecificStagedWorkAssignmentOfCourseInTerm() {
        val rowsAffected = workAssignmentDAO.deleteSpecificStagedWorkAssignmentOfCourseInTerm(1, 1, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllVersionsOfSpecificWorkAssignment() {
        val versions = workAssignmentDAO.getAllVersionsOfSpecificWorkAssignment(1, 1, 1)
        assertEquals(2, versions.size)
    }

    @Test
    fun getVersionOfSpecificWorkAssignment() {
        val workAssignmentVersion = workAssignmentDAO.getVersionOfSpecificWorkAssignment(1, 1, 1, 1).get()
        assertEquals("ze", workAssignmentVersion.createdBy)
        assertEquals(LocalDate.parse("2015-04-25"), workAssignmentVersion.dueDate)
        assertEquals("1º", workAssignmentVersion.phase)
        assertEquals(1, workAssignmentVersion.version)
        assertEquals(false, workAssignmentVersion.individual)
        assertEquals(true, workAssignmentVersion.lateDelivery)
        assertEquals(true, workAssignmentVersion.multipleDeliveries)
        assertEquals(false, workAssignmentVersion.requiresReport)
        assertEquals(1, workAssignmentVersion.workAssignmentId)
    }

    @Test
    fun createWorkAssignmentVersion() {
        val workAssignmentVersion = workAssignmentDAO.createWorkAssignmentVersion(
                WorkAssignmentVersion(
                        individual = false,
                        requiresReport = true,
                        lateDelivery = false,
                        multipleDeliveries = true,
                        version = 3,
                        workAssignmentId = 1,
                        dueDate = LocalDate.parse("2015-01-10"),
                        phase = "1ª",
                        createdBy = "ana"
                )
        )
        assertEquals("ana", workAssignmentVersion.createdBy)
        assertEquals(LocalDate.parse("2015-01-10"), workAssignmentVersion.dueDate)
        assertEquals(1, workAssignmentVersion.workAssignmentId)
        assertEquals("1ª", workAssignmentVersion.phase)
        assertEquals(false, workAssignmentVersion.lateDelivery)
        assertEquals(true, workAssignmentVersion.multipleDeliveries)
        assertEquals(true, workAssignmentVersion.requiresReport)
        assertEquals(false, workAssignmentVersion.individual)
        assertEquals(3, workAssignmentVersion.version)
    }

    @Test
    fun getAllReportsOnWorkUnitOnSpecificTermOfCourse() {
        val reports = workAssignmentDAO.getAllReportsOnWorkUnitOnSpecificTermOfCourse(1, 1, 1)
        assertEquals(2, reports.size)
    }

    @Test
    fun getSpecificReportOfWorkAssignment() {
        val report = workAssignmentDAO.getSpecificReportOfWorkAssignment(1, 1, 1, 1).get()
        assertEquals(LocalDate.parse("2015-04-25"), report.dueDate)
        assertEquals(1, report.workAssignmentId)
        assertEquals(true, report.individual)
        assertEquals(true, report.lateDelivery)
        assertEquals(true, report.multipleDeliveries)
        assertEquals(true, report.requiresReport)
        assertEquals(1, report.logId)
        assertEquals("2º", report.phase)
        assertEquals(1, report.reportId)
        assertEquals("gonçalo", report.reportedBy)
        assertEquals(0, report.votes)
        assertEquals(1, report.courseId)
        assertEquals(1, report.termId)
    }

    @Test
    fun addReportToWorkAssignmentOnCourseInTerm() {
        val report = workAssignmentDAO.addReportToWorkAssignmentOnCourseInTerm(
                workAssignmentId = 1,
                workAssignmentReport = WorkAssignmentReport(
                        dueDate = LocalDate.parse("2015-04-23"),
                        workAssignmentId = 1,
                        individual = false,
                        lateDelivery = false,
                        multipleDeliveries = false,
                        requiresReport = false,
                        phase = "1º",
                        reportId = 3,
                        reportedBy = "jg",
                        votes = 74
                )
        )
        assertEquals(LocalDate.parse("2015-04-23"), report.dueDate)
        assertEquals(1, report.workAssignmentId)
        assertEquals(false, report.individual)
        assertEquals(false, report.lateDelivery)
        assertEquals(false, report.multipleDeliveries)
        assertEquals(false, report.requiresReport)
        assertEquals(3, report.logId)
        assertEquals("1º", report.phase)
        assertEquals(3, report.reportId)
        assertEquals("jg", report.reportedBy)
        assertEquals(74, report.votes)
    }

    @Test
    fun updateVotesOnReportedWorkAssignment() {
        val oldReportedWorkAssignment = workAssignmentDAO.getSpecificReportOfWorkAssignment(1, 1, 1, 1).get()
        assertEquals(0, oldReportedWorkAssignment.votes)
        val rowsAffected = workAssignmentDAO.updateVotesOnReportedWorkAssignment(1, 9)
        assertEquals(1, rowsAffected)
        val updatedWorkAssignment = workAssignmentDAO.getSpecificReportOfWorkAssignment(1, 1, 1, 1).get()
        assertEquals(9, updatedWorkAssignment.votes)
    }

    @Test
    fun deleteReportOnWorkAssignment() {
        val rowsAffected = workAssignmentDAO.deleteReportOnWorkAssignment(1, 1, 1, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getWorkAssignmentByLogId() {
        val workAssignment = workAssignmentDAO.getWorkAssignmentByLogId(1).get()
        assertEquals(1, workAssignment.courseId)
        assertEquals("wilson", workAssignment.createdBy)
        assertEquals(LocalDate.parse("2015-04-28"), workAssignment.dueDate)
        assertEquals(1, workAssignment.logId)
        assertEquals("2º", workAssignment.phase)
        assertEquals(2, workAssignment.version)
        assertEquals(0, workAssignment.votes)
        assertEquals(1, workAssignment.workAssignmentId)
        assertEquals(1, workAssignment.termId)
        assertEquals(0, workAssignment.votes)
        assertEquals(false, workAssignment.individual)
        assertEquals(true, workAssignment.lateDelivery)
        assertEquals(true, workAssignment.multipleDeliveries)
        assertEquals(true, workAssignment.requiresReport)
    }

    @Test
    fun getWorkAssignmentReportByLogId() {
        val report = workAssignmentDAO.getWorkAssignmentReportByLogId(1).get()
        assertEquals(LocalDate.parse("2015-04-25"), report.dueDate)
        assertEquals(1, report.reportId)
        assertEquals(1, report.courseId)
        assertEquals(1, report.termId)
        assertEquals(1, report.logId)
        assertEquals(1, report.workAssignmentId)
        assertEquals("2º", report.phase)
        assertEquals(true, report.individual)
        assertEquals(true, report.lateDelivery)
        assertEquals(true, report.multipleDeliveries)
        assertEquals(true, report.requiresReport)
        assertEquals(0, report.votes)
        assertEquals("gonçalo", report.reportedBy)
    }

    @Test
    fun getWorkAssignmentStageByLogId() {
        val stage = workAssignmentDAO.getWorkAssignmentStageByLogId(1).get()
        assertEquals(1, stage.courseId)
        assertEquals(1, stage.termId)
        assertEquals("carlos", stage.createdBy)
        assertEquals(LocalDate.parse("2014-12-10"), stage.dueDate)
        assertEquals(false, stage.individual)
        assertEquals(true, stage.lateDelivery)
        assertEquals(true, stage.multipleDeliveries)
        assertEquals(false, stage.requiresReport)
        assertEquals(1, stage.logId)
        assertEquals("2º", stage.phase)
        assertEquals(1, stage.stageId)
        assertEquals(0, stage.votes)
    }

    @Test
    fun getAllWorkAssignmentsOfSpecificCourse() {
        val workAssignments = workAssignmentDAO.getAllWorkAssignmentsOfSpecificCourse(1)
        assertEquals(3, workAssignments.size)
    }

    @Test
    fun getAllStagedWorkAssignmentOnSpecificCourse() {
        val stagedWorkAssignments = workAssignmentDAO.getAllStagedWorkAssignmentOnSpecificCourse(1)
        assertEquals(2, stagedWorkAssignments.size)
    }

}