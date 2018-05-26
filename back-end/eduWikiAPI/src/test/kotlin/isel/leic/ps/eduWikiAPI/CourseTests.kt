package isel.leic.ps.eduWikiAPI

import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import junit.framework.TestCase.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringRunner
import java.sql.Timestamp
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@SpringBootTest(classes= [(EduWikiApiApplication::class), (H2Config::class)])

@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createDB.sql","classpath:inserts.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropDB.sql"]))
)
class CourseTests {

    @Autowired
    lateinit var courseDAO: CourseDAO

    @Test
    fun testGetCourse(){}

    @Test
    fun testGetCourseOfProgramme(){}

    @Test
    fun testCreateCourseAndAddToProgramme(){}

    @Test
    fun testReportCourseOnProgramme(){}
}