package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.domain.model.UserCourseClass
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.UserDAO
import org.junit.After
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

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createMainTablesDb.sql", "classpath:insertMainTables.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropMainTablesDb.sql"]))
)
@Transactional
class UserDAOImplTest {

    @Autowired
    lateinit var userDAO: UserDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getUser() {
        val user = userDAO.getUser("jg").get()
        assertEquals("jg", user.username)
        assertEquals("1234", user.password)
        assertEquals("João", user.givenName)
        assertEquals("Gameiro", user.familyName)
        assertEquals(true, user.confirmed)
        assertEquals("jg@isel.pt", user.email)
        assertEquals(false, user.locked)
    }

    @Test
    fun createUser() {
        val user = userDAO.createUser(
                User(
                        username = "bob",
                        password = "abcd",
                        givenName = "bob",
                        familyName = "simons",
                        confirmed = true,
                        email = "bob@alunos.isel.pt",
                        locked = false
                )
        )
        assertEquals("bob", user.username)
        assertEquals("abcd", user.password)
        assertEquals("bob", user.givenName)
        assertEquals("simons", user.familyName)
        assertEquals(true, user.confirmed)
        assertEquals("bob@alunos.isel.pt", user.email)
        assertEquals(false, user.locked)
    }

    @Test
    fun confirmUser() {
        val user = userDAO.createUser(
                User(
                        username = "bob",
                        password = "abcd",
                        givenName = "bob",
                        familyName = "simons",
                        confirmed = false,
                        email = "bob@alunos.isel.pt",
                        locked = false
                )
        )
        assertEquals(false, user.confirmed)
        val confirmedUser = userDAO.confirmUser(user.username)
        assertEquals(true, confirmedUser.confirmed)
    }

    @Test
    fun getCoursesOfUser() {
        val courses = userDAO.getCoursesOfUser("ze")
        assertEquals(2, courses.size)
    }

    @Test
    fun getClassesOfUser() {
        val classes = userDAO.getClassesOfUser("ze")
        assertEquals(2, classes.size)
    }

    @Test
    fun getProgrammeOfUser() {
        val programme = userDAO.getProgrammeOfUser("ze").get()
        assertEquals(2, programme.programmeId)
        assertEquals(1, programme.version)
        assertEquals(2, programme.logId)
        assertEquals(0, programme.votes)
        assertEquals("ze", programme.createdBy)
        assertEquals("LEIM", programme.shortName)
        assertEquals("Licenciatura em Engenharia Informática e Multimédia", programme.fullName)
        assertEquals("Licenciatura", programme.academicDegree)
        assertEquals(180, programme.totalCredits)
        assertEquals(6, programme.duration)
    }

    @Test
    fun addProgrammeToUser() {
        val userProgramme = userDAO.addProgrammeToUser("jg", 1)
        assertEquals(1, userProgramme.programmeId)
        assertEquals("jg", userProgramme.username)
    }

    @Test
    fun addCourseToUser() {
        val userCourse = userDAO.addCourseToUser(
                UserCourseClass(
                        "jg",
                        1
                )
        )
        assertEquals(1, userCourse.courseId)
        assertEquals("jg", userCourse.username)
    }

    @Test
    fun addClassToUser() {
        val userClass = userDAO.addClassToUser(
                UserCourseClass(
                        "ze",
                        2,
                        1
                )
        )
        assertEquals(1, userClass.courseClassId)
        assertEquals(2, userClass.courseId)
        assertEquals("ze", userClass.username)
    }

    @Test
    fun deleteAllCoursesOfUser() {
        val rowsAffected = userDAO.deleteAllCoursesOfUser("ze")
        assertEquals(2, rowsAffected)
    }

    @Test
    fun deleteProgramme() {
        val rowsAffected = userDAO.deleteProgramme("ze")
        assertEquals(1, rowsAffected)
    }

    @Test
    fun deleteSpecificCourseOfUser() {
        val rowsAffected = userDAO.deleteSpecificCourseOfUser("ze", 2)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun reportUser() {
        val report = userDAO.reportUser(
                UserReport(
                        username = "bruno",
                        reportedBy = "ze",
                        reason = "bad behavior"
                )
        )
        assertEquals(1, report.logId)
        assertEquals("bruno", report.username)
        assertEquals("ze", report.reportedBy)
        assertEquals("bad behavior", report.reason)
        assertEquals(1, report.reportId)

    }

    @Test
    fun updateUser() {
        val oldUser = userDAO.getUser("jg").get()
        assertEquals("Gameiro", oldUser.familyName)
        assertEquals("João", oldUser.givenName)
        assertEquals("jg", oldUser.username)
        val updatedUser = userDAO.updateUser(
                User(
                        username = "jg",
                        familyName = "gonçalves",
                        givenName = "jonas"
                )
        )
        assertEquals("gonçalves", updatedUser.familyName)
        assertEquals("jonas", updatedUser.givenName)
        assertEquals("jg", updatedUser.username)
    }

    @Test
    fun deleteUser() {
        val rowsAffected = userDAO.deleteUser("jg")
        assertEquals(1, rowsAffected)
    }

    @Test
    fun deleteSpecificReportOfUser() {
        val rowsAffected = userDAO.deleteSpecificReportOfUser("ze", 1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getSpecificReportOfUser() {
        val userReport = userDAO.getSpecificReportOfUser("ze", 1).get()
        assertEquals(1, userReport.reportId)
        assertEquals("ze", userReport.username)
        assertEquals("bad infos", userReport.reason)
        assertEquals("bruno", userReport.reportedBy)
        assertEquals(1, userReport.logId)

    }

    @Test
    fun getAllReportsOfUser() {
        val reports = userDAO.getAllReportsOfUser("ze")
        assertEquals(2, reports.size)
    }

    @Test
    fun deleteAllClassesOfUser() {
        val rowsAffected = userDAO.deleteAllClassesOfUser("ze")
        assertEquals(2, rowsAffected)
    }

    @Test
    fun deleteSpecificClassOfUser() {
        val rowsAffected = userDAO.deleteSpecificClassOfUser("ze", 3)
        assertEquals(2, rowsAffected)
    }

    @Test
    fun getDevs() {

    }

    @Test
    fun getUsersByRole() {

    }

    @Test
    fun lockUser() {
    }

    @Test
    fun deleteAllReportsOnUser() {
    }

    @Test
    fun getUserByEmail() {
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}