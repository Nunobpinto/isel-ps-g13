package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.model.ValidationToken
import isel.leic.ps.eduWikiAPI.repository.interfaces.TokenDAO
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
import java.sql.Timestamp
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createMasterTables.sql", "classpath:insertMasterTables.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropMasterTables.sql"]))
)
@Transactional
class TokenDAOImplTest {

    @Autowired
    lateinit var tokenDAO: TokenDAO

    @Test
    fun saveToken() {
        val uuid = "04805b5d-e089-48e0-b7da-68a6321a17ff"
        val savedToken = tokenDAO.saveToken(
                ValidationToken(
                        id = 1,
                        token = UUID.fromString(uuid),
                        date = Timestamp.valueOf("2018-07-09 14:00:00.000000")
                        )
        )
        assertEquals(2, savedToken.id)
        assertEquals(Timestamp.valueOf("2018-07-09 14:00:00.000000"), savedToken.date)
        assertEquals( UUID.fromString(uuid), savedToken.token)

    }

    @Test
    fun getToken() {
        val token = tokenDAO.getToken(UUID.fromString("04805b5d-e089-48e0-b7da-68a6321a17ff")).get()
        assertEquals(1, token.id)
        assertEquals(Timestamp.valueOf("2018-07-09 14:00:00.000000"), token.date)
        assertEquals( UUID.fromString("04805b5d-e089-48e0-b7da-68a6321a17ff"), token.token)
    }

    @Test
    fun deleteToken() {
        val rowsAffected = tokenDAO.deleteToken(UUID.fromString("04805b5d-e089-48e0-b7da-68a6321a17ff"))
        assertEquals(1, rowsAffected)
    }

}