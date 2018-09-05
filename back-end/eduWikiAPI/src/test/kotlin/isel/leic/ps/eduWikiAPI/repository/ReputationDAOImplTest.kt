package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.model.Reputation
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
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
import java.time.LocalDateTime

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createMainTablesDb.sql", "classpath:insertMainTables.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropMainTablesDb.sql"]))
)
@Transactional
class ReputationDAOImplTest {

    @Autowired
    lateinit var reputationDAO: ReputationDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getReputationRoleOfUser() {
        val role = reputationDAO.getReputationRoleOfUser("bruno").get()
        assertEquals("ROLE_BEGINNER", role)
    }

    @Test
    fun getUserReputationDetails() {
        val details = reputationDAO.getUserReputationDetails("bruno").get()
        assertEquals(5, details.points)
        assertEquals(2, details.reputationId)
        assertEquals("ROLE_BEGINNER", details.role)
        assertEquals("bruno", details.username)
    }

    @Test
    fun createUserReputation() {
        val reputation = reputationDAO.createUserReputation(
                Reputation(
                        points = 70,
                        role = "ROLE_ADMIN",
                        username = "jg"
                )
        )
        assertEquals("jg", reputation.username)
        assertEquals(70, reputation.points)
        assertEquals("ROLE_ADMIN", reputation.role)
        assertEquals(4, reputation.reputationId)
    }

    @Test
    fun updateUserRole() {
        val oldRole = reputationDAO.getReputationRoleOfUser("jg").get()
        assertEquals("ROLE_ADMIN", oldRole)
        val updatedRole = reputationDAO.updateUserRole("jg", 46, "ROLE_BEGINNER")
        assertEquals("ROLE_BEGINNER", updatedRole.role)
        assertEquals("jg", updatedRole.username)
        assertEquals(3, updatedRole.reputationId)
        assertEquals(46, updatedRole.points)
    }

    @Test
    fun updateUserReputation() {
        val oldReputation = reputationDAO.getUserReputationDetails("jg").get()
        assertEquals(55, oldReputation.points)
        val rowsAffected = reputationDAO.updateUserReputation(
                Reputation(
                        reputationId = 3,
                        points = 8,
                        role = "ROLE_BEGINNER",
                        username = "jg"
                )
        )
        assertEquals(1, rowsAffected)
        val updatedReputation = reputationDAO.getUserReputationDetails("jg").get()
        assertEquals(8, updatedReputation.points)
        assertEquals(3, updatedReputation.reputationId)
        assertEquals("ROLE_BEGINNER", updatedReputation.role)
        assertEquals("jg", updatedReputation.username)
    }

    @Test
    fun registerActionLog() {
        val register = reputationDAO.registerActionLog(
                "jg",
                ActionType.CREATE,
                "course",
                3,
                Timestamp.valueOf(LocalDateTime.now())
        )
        assertEquals("jg", register.user)
        assertEquals("course", register.entity)
        assertEquals(3, register.actionId)
        assertEquals("CREATE", register.actionType.name)
        assertEquals(3, register.logId)
    }

    @Test
    fun registerReputationLog() {
        val register = reputationDAO.registerReputationLog(
                user = "jg",
                reputationId = 3,
                pointsGiven = 7,
                givenBy = "bruno",
                actionId = 2
        )
        assertEquals("jg", register.user)
        assertEquals(2, register.repLogId)
        assertEquals(2, register.repActionId)
        assertEquals("bruno", register.givenBy)
        assertEquals(7, register.points)
        assertEquals(3, register.userRepId)
    }

    @Test
    fun getActionLogsByResource() {
        val logs = reputationDAO.getActionLogsByResource("course", 1)
        assertEquals(2, logs.size)
    }

    @Test
    fun getActionLogsByUserAndResource() {
        val logs = reputationDAO.getActionLogsByUserAndResource("bruno", "course", 2)
        assertEquals(1, logs.size)
    }

    @Test
    fun getActionLogsByUser() {
        val logs = reputationDAO.getActionLogsByUser("bruno")
        assertEquals(2, logs.size)
    }

    @Test
    fun getReputationLogsByUser() {
        val logs = reputationDAO.getReputationLogsByUser("ze")
        assertEquals(2, logs.size)
    }

    @Test
    fun getActionLogById() {
        val log = reputationDAO.getActionLogById(1).get()
        assertEquals(2, log.logId)
        assertEquals(1, log.actionId)
        assertEquals("CREATE", log.actionType.name)
        assertEquals("bruno", log.user)
        assertEquals("course", log.entity)
        assertEquals(2, log.logId)
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}