package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.repository.interfaces.TermDAO
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
class TermDAOImplTest {

    @Autowired
    lateinit var termDAO: TermDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getAllTerms() {
        val terms = termDAO.getAllTerms()
        assertEquals(2, terms.size)
    }

    @Test
    fun getTerm() {
        val term = termDAO.getTerm(1).get()
        assertEquals("1718v", term.shortName)
        assertEquals(1, term.termId)
        assertEquals("SUMMER", term.type.name)
        assertEquals(2018, term.year)
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}