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
    fun testGetCourse(){
        val course =courseDAO.getSpecificCourse(1)
        assertEquals("ze",course.createdBy)
        assertEquals("PI",course.shortName)
        assertEquals("Programação na Internet",course.fullName)
    }

    @Test
    fun testGetAllCourses(){
        val courses = courseDAO.getAllCourses()
        assertEquals(3,courses.size)
    }

    @Test
    fun testGetCoursesOfProgramme(){
        val courses = courseDAO.getCoursesOnSpecificProgramme(1)
        assertEquals(2,courses.size)
    }

    @Test
    fun testGetCourseOfProgramme(){
        val course = courseDAO.getSpecificCourseOfProgramme(1,1)
        assertEquals(6,course.credits)
        assertEquals(false,course.optional)
    }

    @Test
    fun testCreateCourseAndAddToProgramme(){}

    @Test
    fun testReportCourseOnProgramme(){}

    @Test
    fun testComplexDeleteOnCourse(){

    }
}