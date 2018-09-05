package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import org.junit.Test

import junit.framework.TestCase.*
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createMainTablesDb.sql", "classpath:insertMainTables.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropMainTablesDb.sql"]))
)
@Transactional
class ProgrammeDAOImplTest {

    @Autowired
    lateinit var programmeDAO: ProgrammeDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getAllProgrammes() {
        val programmes = programmeDAO.getAllProgrammes()
        assertEquals(3, programmes.size)
    }

    @Test
    fun getSpecificProgramme() {
        val programme = programmeDAO.getSpecificProgramme(1).get()
        assertEquals(1, programme.programmeId)
        assertEquals(1, programme.version)
        assertEquals(1, programme.logId)
        assertEquals("ze", programme.createdBy)
        assertEquals("Licenciatura em Engenharia Informática e Computadores", programme.fullName)
        assertEquals("LEIC", programme.shortName)
        assertEquals("Licenciatura", programme.academicDegree)
        assertEquals(180, programme.totalCredits)
        assertEquals(6, programme.duration)
        assertEquals(0, programme.votes)
    }

    @Test
    fun createProgramme() {
        val programme = programmeDAO.createProgramme(
                Programme(
                        createdBy = "jg",
                        fullName = "Mestrado Em Engenharia e Gestão Industrial",
                        shortName = "MEGI",
                        academicDegree = "Mestrado",
                        totalCredits = 120,
                        duration = 4,
                        votes = 94
                )
        )
        assertEquals(4, programme.programmeId)
        assertEquals("jg", programme.createdBy)
        assertEquals("Mestrado Em Engenharia e Gestão Industrial", programme.fullName)
        assertEquals("MEGI", programme.shortName)
        assertEquals("Mestrado", programme.academicDegree)
        assertEquals(120, programme.totalCredits)
        assertEquals(4, programme.duration)
        assertEquals(94, programme.votes)
        assertEquals(1, programme.version)
        assertEquals(4, programme.logId)
    }

    @Test
    fun updateProgramme() {
        val oldProgramme = programmeDAO.getSpecificProgramme(1).get()
        assertEquals(1, oldProgramme.version)
        val programme = programmeDAO.updateProgramme(
                1,
                Programme(
                        programmeId = 1,
                        version = oldProgramme.version.inc(),
                        votes = 22,
                        createdBy = "jg",
                        fullName = "Licenciatura em Engenharia Quimica",
                        shortName = "LEQ",
                        academicDegree = oldProgramme.academicDegree,
                        totalCredits = oldProgramme.totalCredits,
                        duration = oldProgramme.duration
                )
        )
        assertEquals(1, programme.programmeId)
        assertEquals("jg", programme.createdBy)
        assertEquals("Licenciatura em Engenharia Quimica", programme.fullName)
        assertEquals("LEQ", programme.shortName)
        assertEquals("Licenciatura", programme.academicDegree)
        assertEquals(180, programme.totalCredits)
        assertEquals(6, programme.duration)
        assertEquals(22, programme.votes)
        assertEquals(2, programme.version)
        assertEquals(1, programme.logId)
    }

    @Test
    fun updateVotesOnProgramme() {
        val oldProgramme = programmeDAO.getSpecificProgramme(1).get()
        assertEquals(0, oldProgramme.votes)
        val rowsAffected = programmeDAO.updateVotesOnProgramme(1, 11)
        assertEquals(1, rowsAffected)
        val updatedProgramme = programmeDAO.getSpecificProgramme(1).get()
        assertEquals(11, updatedProgramme.votes)
    }

    @Test
    fun deleteSpecificProgramme() {
        val rowsAffected = programmeDAO.deleteSpecificProgramme(3)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getSpecificStageEntryOfProgramme() {
        val stagedProgramme = programmeDAO.getSpecificStageEntryOfProgramme(1).get()
        assertEquals(1, stagedProgramme.stageId)
        assertEquals(1, stagedProgramme.logId)
        assertEquals("Licenciatura em Engenharia Eletrotecnica", stagedProgramme.fullName)
        assertEquals("LEE", stagedProgramme.shortName)
        assertEquals("Licenciatura", stagedProgramme.academicDegree)
        assertEquals(180, stagedProgramme.totalCredits)
        assertEquals(6, stagedProgramme.duration)
        assertEquals("nuno", stagedProgramme.createdBy)
        assertEquals(0, stagedProgramme.votes)
    }

    @Test
    fun getAllProgrammeStageEntries() {
        val stagedProgrammes = programmeDAO.getAllProgrammeStageEntries()
        assertEquals(2, stagedProgrammes.size)
    }

    @Test
    fun createStagingProgramme() {
        val stagingProgramme = programmeDAO.createStagingProgramme(
                ProgrammeStage(
                        votes = 3,
                        createdBy = "bruno",
                        fullName = "Licenciatura em Engenharia Ambiental",
                        shortName = "LEA",
                        academicDegree = "Licenciatura",
                        totalCredits = 136,
                        duration = 5
                )
        )
        assertEquals(3, stagingProgramme.votes)
        assertEquals("bruno", stagingProgramme.createdBy)
        assertEquals("Licenciatura em Engenharia Ambiental", stagingProgramme.fullName)
        assertEquals("LEA", stagingProgramme.shortName)
        assertEquals("Licenciatura", stagingProgramme.academicDegree)
        assertEquals(136, stagingProgramme.totalCredits)
        assertEquals(5, stagingProgramme.duration)
        assertEquals(3, stagingProgramme.stageId)
        assertEquals(3, stagingProgramme.logId)
    }

    @Test
    fun updateVotesOnStagedProgramme() {
        val oldStagedProgramme = programmeDAO.getSpecificStageEntryOfProgramme(1).get()
        assertEquals(0, oldStagedProgramme.votes)
        val rowsAffected = programmeDAO.updateVotesOnStagedProgramme(1, 64)
        assertEquals(1, rowsAffected)
        val updatedStagedProgramme = programmeDAO.getSpecificStageEntryOfProgramme(1).get()
        assertEquals(64, updatedStagedProgramme.votes)
    }

    @Test
    fun deleteSpecificStagedProgramme() {
        val rowsAffected = programmeDAO.deleteSpecificStagedProgramme(1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllVersionsOfProgramme() {
        val versions = programmeDAO.getAllVersionsOfProgramme(3)
        assertEquals(2, versions.size)
    }

    @Test
    fun getSpecificVersionOfProgramme() {
        val programmeVersion = programmeDAO.getSpecificVersionOfProgramme(3, 2).get()
        assertEquals(3, programmeVersion.programmeId)
        assertEquals("Licenciatura em Engenharia Civil", programmeVersion.fullName)
        assertEquals("LEC", programmeVersion.shortName)
        assertEquals("Licenciatura", programmeVersion.academicDegree)
        assertEquals(2, programmeVersion.version)
        assertEquals(180, programmeVersion.totalCredits)
        assertEquals(6, programmeVersion.duration)
        assertEquals("jg", programmeVersion.createdBy)
    }

    @Test
    fun createProgrammeVersion() {
        val programmeVersion = programmeDAO.createProgrammeVersion(
                ProgrammeVersion(
                        version = 3,
                        programmeId = 3,
                        fullName = "Mestrado em Quimica",
                        shortName = "MC",
                        academicDegree = "Mestrado",
                        totalCredits = 100,
                        duration = 4,
                        createdBy = "bruno"
                )
        )
        assertEquals(3, programmeVersion.programmeId)
        assertEquals(3, programmeVersion.version)
        assertEquals("Mestrado em Quimica", programmeVersion.fullName)
        assertEquals("MC", programmeVersion.shortName)
        assertEquals("Mestrado", programmeVersion.academicDegree)
        assertEquals(100, programmeVersion.totalCredits)
        assertEquals(4, programmeVersion.duration)
        assertEquals("bruno", programmeVersion.createdBy)
    }

    @Test
    fun deleteSpecificProgrammeVersion() {
        val rowsAffected = programmeDAO.deleteSpecificProgrammeVersion(3, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllReportsOfSpecificProgramme() {
        val reports = programmeDAO.getAllReportsOfSpecificProgramme(1)
        assertEquals(2, reports.size)
    }

    @Test
    fun getSpecificReportOfProgramme() {
        val programmeReport = programmeDAO.getSpecificReportOfProgramme(1, 1).get()
        assertEquals(1, programmeReport.programmeId)
        assertEquals(1, programmeReport.reportId)
        assertEquals(1, programmeReport.logId)
        assertEquals("Licenciatura em Engenharia Quimica", programmeReport.fullName)
        assertEquals("LEQ", programmeReport.shortName)
        assertEquals("Licenciatura", programmeReport.academicDegree)
        assertEquals(180, programmeReport.totalCredits)
        assertEquals(6, programmeReport.duration)
        assertEquals("nuno", programmeReport.reportedBy)
        assertEquals(0, programmeReport.votes)
    }

    @Test
    fun reportProgramme() {
        val reportProgramme = programmeDAO.reportProgramme(
                1,
                ProgrammeReport(
                        programmeId = 1,
                        fullName = "Mestrado em Engenharia Informatica",
                        shortName = "MEI",
                        academicDegree = "Mestrado",
                        reportedBy = "ze",
                        votes = 2
                )
        )
        assertEquals(1, reportProgramme.programmeId)
        assertEquals(3, reportProgramme.reportId)
        assertEquals(3, reportProgramme.logId)
        assertEquals("Mestrado em Engenharia Informatica", reportProgramme.fullName)
        assertEquals("MEI", reportProgramme.shortName)
        assertEquals("Mestrado", reportProgramme.academicDegree)
        assertEquals("ze", reportProgramme.reportedBy)
        assertEquals(2, reportProgramme.votes)
    }

    @Test
    fun updateVotesOnReportedProgramme() {
        val oldReportedProgramme = programmeDAO.getSpecificReportOfProgramme(1, 1).get()
        assertEquals(0, oldReportedProgramme.votes)
        val rowsAffected = programmeDAO.updateVotesOnReportedProgramme(1, 1, 98)
        assertEquals(1, rowsAffected)
        val updatedReportedProgramme = programmeDAO.getSpecificReportOfProgramme(1,1).get()
        assertEquals(98, updatedReportedProgramme.votes)
    }

    @Test
    fun deleteSpecificReportOnProgramme() {
        val rowsAffected = programmeDAO.deleteSpecificReportOnProgramme(1, 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getProgrammeByLogId() {
        val programme = programmeDAO.getProgrammeByLogId(1).get()
        assertEquals(1, programme.programmeId)
        assertEquals(1, programme.version)
        assertEquals(1, programme.logId)
        assertEquals("ze", programme.createdBy)
        assertEquals("Licenciatura em Engenharia Informática e Computadores", programme.fullName)
        assertEquals("LEIC", programme.shortName)
        assertEquals("Licenciatura", programme.academicDegree)
        assertEquals(180, programme.totalCredits)
        assertEquals(6, programme.duration)
        assertEquals(0, programme.votes)
    }

    @Test
    fun getProgrammeReportByLogId() {
        val programmeReport = programmeDAO.getProgrammeReportByLogId(1).get()
        assertEquals(1, programmeReport.programmeId)
        assertEquals(1, programmeReport.reportId)
        assertEquals(1, programmeReport.logId)
        assertEquals("Licenciatura em Engenharia Quimica", programmeReport.fullName)
        assertEquals("LEQ", programmeReport.shortName)
        assertEquals("Licenciatura", programmeReport.academicDegree)
        assertEquals(180, programmeReport.totalCredits)
        assertEquals(6, programmeReport.duration)
        assertEquals("nuno", programmeReport.reportedBy)
        assertEquals(0, programmeReport.votes)
    }

    @Test
    fun getProgrammeStageByLogId() {
        val programmeStage = programmeDAO.getProgrammeStageByLogId(1).get()
        assertEquals(1, programmeStage.stageId)
        assertEquals(1, programmeStage.logId)
        assertEquals("Licenciatura em Engenharia Eletrotecnica", programmeStage.fullName)
        assertEquals("LEE", programmeStage.shortName)
        assertEquals("Licenciatura", programmeStage.academicDegree)
        assertEquals(180, programmeStage.totalCredits)
        assertEquals(6, programmeStage.duration)
        assertEquals("nuno", programmeStage.createdBy)
        assertEquals(0, programmeStage.votes)
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}