package isel.leic.ps.eduWikiAPI


import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import junit.framework.TestCase.*
import org.junit.Test
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
class WorkAssignmentTests {

    @Autowired
    lateinit var workAssignmentDAO: WorkAssignmentDAO

    @Test
    fun testGetAlWorkAssignments() {
        val works = workAssignmentDAO.getAllWorkAssignmentsFromSpecificTermOfCourse(1,1)
        assertEquals(1,works.size)
    }

    @Test
    fun testGetWorkAssignment() {
       /* val wrs = workAssignmentDAO.getSpecificWorkAssignment(1,, )
        assertEquals("Apoio", wrs.supplement)
        assertEquals("Exemplo-PI", wrs.sheet)
        assertEquals(false, wrs.individual)*/
    }

    @Test
    fun testAddWorkAssignment() {
        val wrs = WorkAssignment(
                createdBy = "bruno",
                dueDate = LocalDate.now(),
                individual = false,
                sheet = "Fazer 3ºtrabalho",
                supplement = "Nenhum",
                lateDelivery = false,
                multipleDeliveries = true,
                requiresReport = false
        )
        val insertedRows = workAssignmentDAO.createWorkAssignmentOnCourseInTerm(1, 1, wrs)
        assertEquals(1, insertedRows)
    }
}