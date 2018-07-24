package isel.leic.ps.eduWikiAPI


import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Test
import org.junit.internal.runners.statements.Fail
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate


@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createDB.sql", "classpath:inserts.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropDB.sql"]))
)
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(EduWikiApiApplication::class), (H2Config::class)])
class ExamTests {

    @Autowired
    lateinit var examDAO: ExamDAO

    @Test
    fun testGetAllExams() {
        val exams = examDAO.getAllExamsFromSpecificTermOfCourse(1, 1)
        assertEquals(1, exams.size)
    }

    @Test
    fun testGetExam() {
        val exam = examDAO.getSpecificExamFromSpecificTermOfCourse(1, 1, 4)
        assertEquals("1ºexame de PI 1718v", exam.get().sheet)
        assertEquals("1ª", exam.get().phase)
        assertEquals("A.2.14", exam.get().location)
    }

    @Test
    fun testAddExam() {
        val exam = Exam(
                createdBy = "bruno",
                dueDate = LocalDate.now(),
                location = "A.2.13",
                sheet = "Exame Época Especial 1718v",
                type = "Exam",
                phase = "E.E"
        )
        val insertedRows = examDAO.createExam(1, 1, exam)
        assertEquals(1, insertedRows)
    }
}