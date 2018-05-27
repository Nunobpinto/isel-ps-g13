package isel.leic.ps.eduWikiAPI

import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringRunner
import java.sql.SQLException
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.HashSet
import javax.sql.DataSource

@RunWith(SpringRunner::class)
@SpringBootTest(classes= [(EduWikiApiApplication::class), (H2Config::class)])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createDB.sql","classpath:inserts.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropDB.sql"]))
)
class ProgrammeTests {

    @Autowired
    lateinit var programmeDAO: ProgrammeDAO

    @Test
    fun testGetProgramme() {
        val prog = programmeDAO.getSpecificProgramme(1)
        assertEquals("LEIC", prog.shortName)
        assertEquals("Licenciatura em Engenharia Informática e Computadores", prog.fullName)
        assertEquals("ze", prog.createdBy)
        assertEquals(180, prog.totalCredits)
    }

    @Test
    fun testApprovedReportOfProgramme(){
        val report = ProgrammeReport(
                programmeId = 1,
                programmeAcademicDegree = "bacherlato",
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        val rows = programmeDAO.reportProgramme(programmeReport = report,programmeId = 1)
        assertEquals(1,rows)
        val programme = programmeDAO.getSpecificProgramme(1)
        val versionRows = programmeDAO.addToProgrammeVersion(programme)
        assertEquals(1,versionRows)
        val updatedProgramme = Programme(
                id=programme.id,
                academicDegree = report.programmeAcademicDegree!!,
                duration = programme.duration,
                fullName = programme.fullName,
                shortName = programme.shortName,
                version = programme.version + 1,
                createdBy = programme.createdBy,
                totalCredits = programme.totalCredits,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        val updateRow = programmeDAO.updateProgramme(1,updatedProgramme)
        assertEquals(1,updateRow)
        val newProg = programmeDAO.getSpecificProgramme(1)
        assertEquals(2,newProg.version)
        val deleteReportRows = programmeDAO.deleteReportOnProgramme(1,1)
        assertEquals(1,deleteReportRows)
        val deleteVersonRows = programmeDAO.deleteVersionProgramme(1,1)
        assertEquals(1,deleteVersonRows)
    }

    @Test
    fun testApprovedStageOfProgramme(){
        val stage = ProgrammeStage(
                fullName = "Licenciatura em Engenharia Mecânica",
                shortName = "LEM",
                academicDegree = "Licenciatura",
                createdBy = "nuno",
                totalCredits = 180,
                duration = 6,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        val rows = programmeDAO.createStagingProgramme(programmeStage = stage)
        assertEquals(1,rows)
        val programme = Programme(
                fullName = "Licenciatura em Engenharia Mecânica",
                shortName = "LEM",
                academicDegree = "Licenciatura",
                createdBy = "nuno",
                totalCredits = 180,
                duration = 6,
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        val deleteRows = programmeDAO.deleteStagedProgramme(1)
        assertEquals(1,deleteRows)
        val createRows =programmeDAO.createProgramme(programme)
        assertEquals(1,createRows)
        val deletedRows = programmeDAO.deleteSpecificProgramme(3)
        assertEquals(1, deletedRows)
    }

    @Test
    fun testGetAllProgrammes() {
        val progs = programmeDAO.getAllProgrammes()
        assertEquals(2,progs.size)
    }


    //TODO = Testing all courses with programme functionality
}