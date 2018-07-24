package isel.leic.ps.eduWikiAPI

import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import junit.framework.TestCase.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringRunner
import java.sql.Timestamp
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(EduWikiApiApplication::class), (H2Config::class)])

@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createDB.sql", "classpath:inserts.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropDB.sql"]))
)
class CourseTests {

    @Autowired
    lateinit var courseDAO: CourseDAO

    @Test
    fun testGetCourse() {
        val course = courseDAO.getSpecificCourse(1).get()
        assertEquals("ze", course.createdBy)
        assertEquals("PI", course.shortName)
        assertEquals("Programação na Internet", course.fullName)
    }

    @Test
    fun testGetAllCourses() {
        val courses = courseDAO.getAllCourses()
        assertEquals(3, courses.size)
    }

    @Test
    fun testGetCoursesOfProgramme() {
        val courses = courseDAO.getCoursesOnSpecificProgramme(1)
        assertEquals(2, courses.size)
    }

    @Test
    fun testGetCourseOfProgramme() {
        val course = courseDAO.getSpecificCourseOfProgramme(1, 1).get()
        assertEquals(6, course.credits)
        assertEquals(false, course.optional)
    }

    @Test
    fun testGetTermsOfCourse() {
        val terms = courseDAO.getTermsOfCourse(1)
        assertEquals(2, terms.size)
        assertEquals("1718v", terms[0].shortName)
        assertEquals("1718i", terms[1].shortName)
    }

    @Test
    fun testCreateCourseAndAddToProgramme() {
        val course = Course(
                organizationId = 1,
                createdBy = "ze",
                fullName = "Laboratório de Software",
                shortName = "LS"
        )
        val insertRows = courseDAO.createCourse(course)
        assertEquals(1, insertRows)
        val courseProgramme = Course(
                courseId = 4,
                programmeId = 1,
                lecturedTerm = "quarto",
                optional = false,
                credits = 6,
                createdBy = "nuno",
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        val associateRows = courseDAO.addCourseToProgramme(1, courseProgramme)
        assertEquals(1, associateRows)
    }

    @Test
    fun testReportCourseOnProgramme() {
        val report = CourseProgrammeReport(
                courseId = 1,
                programmeId = 1,
                reportedBy = "joao",
                lecturedTerm = "todos"
        )
        val reportedRows = courseDAO.reportCourseOnProgramme(1, 1, report)
        assertEquals(1, reportedRows)
    }

    @Test
    fun testDeleteCourseWithCascade() {
        val deletedRows = courseDAO.deleteSpecificCourse(1)
        assertEquals(1, deletedRows)
        val course = courseDAO.getSpecificCourse(1)
        assertFalse(course.isPresent)
    }

    @Test
    fun testVoteOnCourse(){
        courseDAO.voteOnCourse(1,Vote.Up)
        val course = courseDAO.getSpecificCourse(1)
        assertEquals(1,course.get().votes)
    }

    @Test
    fun testVoteOnCourseInProgramme(){
        courseDAO.voteOnCourseOfProgramme(1,Vote.Up,1)
        val course = courseDAO.getSpecificCourseOfProgramme(1,1).get()
        assertEquals(1,course.votes)
    }
}