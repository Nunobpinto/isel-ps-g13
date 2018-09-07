package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole
import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole.*
import isel.leic.ps.eduWikiAPI.domain.mappers.tenantRequestDetailsToPendingTenantCreator
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.TenantDAO
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
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createMasterTables.sql", "classpath:insertMasterTables.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropMasterTables.sql"]))
)
@Transactional
class TenantDAOImplTest {

    @Autowired
    lateinit var tenantDAO: TenantDAO
    @Autowired
    lateinit var userDAO: UserDAO
    @Autowired
    lateinit var reputationDAO: ReputationDAO
    @Autowired
    lateinit var organizationDAO: OrganizationDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("master")
    }

    @Test
    fun findActiveTenantById() {
        val tenant = tenantDAO.findActiveTenantById("4cd93a0f-5b5c-4902-ae0a-181c780fedb1").get()
        assertEquals("4cd93a0f-5b5c-4902-ae0a-181c780fedb1", tenant.uuid)
        assertEquals(Timestamp.valueOf("2018-07-14 13:30:00.0"), tenant.createdAt)
        assertEquals("nuno", tenant.createdBy)
        assertEquals("@isel.pt", tenant.emailPattern)
        assertEquals("isel", tenant.schemaName)
    }

    @Test
    fun getAllActiveTenants() {
        val tenants = tenantDAO.getAllActiveTenants()
        assertEquals(3, tenants.size)
    }

    @Test
    fun createPendingTenant() {
        val uuid = "9c509b5e-2451-408b-a245-fa0476577132"
        val pending = tenantDAO.createPendingTenant(
                PendingTenantDetails(
                        tenantUuid = UUID.fromString(uuid),
                        fullName = "instituto superior tecnico",
                        shortName = "ist",
                        address = "alameda",
                        contact = "213098765",
                        website = "www.ist.pt",
                        emailPattern = "@alunos.ist.pt",
                        orgSummary = "faculdade de engenharia em lisboa"
                )
        )
        assertEquals("alameda", pending.address)
        assertEquals("instituto superior tecnico", pending.fullName)
        assertEquals("ist", pending.shortName)
        assertEquals(UUID.fromString(uuid), pending.tenantUuid)
        assertEquals("213098765", pending.contact)
        assertEquals("www.ist.pt", pending.website)
        assertEquals("@alunos.ist.pt", pending.emailPattern)
        assertEquals("faculdade de engenharia em lisboa", pending.orgSummary)
    }

    @Test
    fun bulkCreatePendingTenantCreators() {
        val uuid = "9c509b5e-2451-408b-a245-fa0476570132"
        val tenant = tenantDAO.bulkCreatePendingTenantCreators(
                listOf(
                        PendingTenantCreator(
                                username = "bob",
                                email = "bob@alunos.fct.com",
                                pendingTenantUuid = UUID.fromString(uuid),
                                givenName = "Bob",
                                familyName = "Sinclair",
                                principal = true
                        ),
                        PendingTenantCreator(
                                username = "alice",
                                email = "alice@alunos.fct.com",
                                pendingTenantUuid = UUID.fromString(uuid),
                                givenName = "Alice",
                                familyName = "Silva",
                                principal = false
                        ),
                        PendingTenantCreator(
                                username = "gui",
                                email = "gui@alunos.fct.com",
                                pendingTenantUuid = UUID.fromString(uuid),
                                givenName = "Gui",
                                familyName = "Alves",
                                principal = false
                        )
                )
        )
        assertEquals(3, tenant.size)
    }

    @Test
    fun getRegisteredUserByUsername() {
        val user = tenantDAO.getRegisteredUserByUsername("jg").get()
        assertEquals("jg", user.username)
        assertEquals("4cd93a0f-5b5c-4902-ae0a-181c780fedb1", user.tenantUuid)
    }

    @Test
    fun findPendingTenantById() {
        val pending = tenantDAO.findPendingTenantById("9c509b5e-2451-408b-a245-fa0476570132").get()
        assertEquals("236946789", pending.contact)
        assertEquals("faculdade do porto", pending.fullName)
        assertEquals("fp", pending.shortName)
        assertEquals("porto", pending.address)
        assertEquals("www.fp.pt", pending.website)
        assertEquals("@fp.pt", pending.emailPattern)
        assertEquals("faculdade situada no porto", pending.orgSummary)
        assertEquals(UUID.fromString("9c509b5e-2451-408b-a245-fa0476570132"), pending.tenantUuid)
    }

    @Test
    fun getAllPendingTenants() {
        val pending = tenantDAO.getAllPendingTenants()
        assertEquals(2, pending.size)
    }

    @Test
    fun findPendingTenantCreatorsByTenantId() {
        val pending = tenantDAO.findPendingTenantCreatorsByTenantId("9c509b5e-2451-408b-a245-fa0476570132")
        assertEquals(2, pending.size)
    }

    @Test
    fun getCurrentTenantDetails() {
        val details = tenantDAO.getCurrentTenantDetails().get()
        assertEquals("1ed95f93-5533-47b8-81d3-369c8c30ff80", details.uuid)
        assertEquals("master", details.schemaName)
        assertEquals(Timestamp.valueOf("2018-07-14 11:30:00.000000"), details.createdAt)
        assertEquals("nuno", details.createdBy)
    }

    @Test
    fun deletePendingTenantById() {
        val rowsAffected = tenantDAO.deletePendingTenantById("9c509b5e-2451-408b-a245-fa0476570132")
        assertEquals(1, rowsAffected)
    }

    @Test
    fun createActiveTenantEntry() {
        val uuid = "ae2502b4-ca6b-4e6d-99b8-fb423b556a07"
        val activeTenant = tenantDAO.createActiveTenantEntry(
                dev = "ricky",
                timestamp = Timestamp.valueOf(LocalDateTime.now()),
                pendingTenant = PendingTenantDetails(
                        tenantUuid = UUID.fromString(uuid),
                        fullName = "instituto superior tecnico",
                        shortName = "ist",
                        address = "alameda",
                        contact = "213098765",
                        website = "www.ist.pt",
                        emailPattern = "@alunos.ist.pt",
                        orgSummary = "faculdade de engenharia em lisboa"
                )
        )
        assertEquals("ist", activeTenant.schemaName)
        assertEquals("ae2502b4-ca6b-4e6d-99b8-fb423b556a07", activeTenant.uuid)
        assertEquals("@alunos.ist.pt", activeTenant.emailPattern)
        assertEquals("ricky", activeTenant.createdBy)
    }

    @Test
    fun createTenantBasedOnPendingTenant() {
        // Create schema
        tenantDAO.createTenantBasedOnPendingTenant("ist")
        // Set ThreadLocal to new schema
        TenantContext.setTenantSchema("ist")
        // Check if no exception is thrown by accessing a table from this schema
        organizationDAO.getOrganization()
    }

    @Test
    fun populateTenant() {
        // Create schema
        tenantDAO.createTenantBasedOnPendingTenant("ist")
        // Set ThreadLocal to new schema
        TenantContext.setTenantSchema("ist")
        // Populate schema
        tenantDAO.populateTenant(
                "ist",
                Organization(
                        fullName = "Instituto Superior Tecnico",
                        shortName = "ist",
                        address = "alameda",
                        contact = "123432",
                        website = "www.ist.pt"
                ),
                listOf(
                        User(
                                username = "nuno",
                                password = "1234",
                                givenName = "Nuno",
                                familyName = "Pinto",
                                email = "nuno@ist.pt"
                        )
                ),
                listOf(
                        Reputation(
                                points = ROLE_ADMIN.maxPoints,
                                role = ROLE_ADMIN.name,
                                username = "nuno"
                        )
                )
        )
        val organization = organizationDAO.getOrganization().get()
        assertEquals('X', organization.organizationId)
        assertEquals(1, organization.version)
        assertEquals(1, organization.logId)
        assertEquals("Instituto Superior Tecnico", organization.fullName)
        assertEquals("ist", organization.shortName)
        assertEquals("alameda", organization.address)
        assertEquals("123432", organization.contact)
        assertEquals("www.ist.pt", organization.website)
        val user = userDAO.getUser("nuno").get()
        assertEquals("nuno", user.username)
        assertEquals("1234", user.password)
        assertEquals("Nuno", user.givenName)
        assertEquals("Pinto", user.familyName)
        assertEquals("nuno@ist.pt", user.email)
        assertEquals(false, user.locked)
        val reputation = reputationDAO.getUserReputationDetails("nuno").get()
        assertEquals(ROLE_ADMIN.maxPoints, reputation.points)
        assertEquals(1, reputation.reputationId)
        assertEquals(ROLE_ADMIN.name, reputation.role)
        assertEquals("nuno", reputation.username)
    }

    @Test
    fun confirmUser() {
        tenantDAO.confirmUser("luis")
        val user = tenantDAO.getRegisteredUserByUsername("luis").get()
        assertTrue(user.confirmed)
        assertEquals(user.tenantUuid, "d39c1e3a-eec7-447a-8c63-52fda106c5e9")
        assertEquals(user.username, "luis")
    }

    @Test
    fun registerUser() {
        val newUser = RegisteredUser(
                username = "nuno",
                tenantUuid = "4cd93a0f-5b5c-4902-ae0a-181c780fedb1",
                confirmed = true
        )
        val registeredUser = tenantDAO.registerUser(newUser)
        assertEquals(newUser.confirmed, registeredUser.confirmed)
        assertEquals(newUser.tenantUuid, registeredUser.tenantUuid)
        assertEquals(newUser.username, registeredUser.username)
    }

    @Test
    fun bulkRegisterUsers() {
        val users = listOf(
                RegisteredUser(
                        username = "nuno",
                        tenantUuid = "4cd93a0f-5b5c-4902-ae0a-181c780fedb1",
                        confirmed = true
                ),
                RegisteredUser(
                        username = "alberto",
                        tenantUuid = "4cd93a0f-5b5c-4902-ae0a-181c780fedb1",
                        confirmed = false
                )
        )
        val registeredUsers = tenantDAO.bulkRegisterUser(users)
        assertEquals(registeredUsers.size, 2)
        assertEquals(users[0].confirmed, registeredUsers[0].confirmed)
        assertEquals(users[0].tenantUuid, registeredUsers[0].tenantUuid)
        assertEquals(users[0].username, registeredUsers[0].username)
        assertEquals(users[1].confirmed, registeredUsers[1].confirmed)
        assertEquals(users[1].tenantUuid, registeredUsers[1].tenantUuid)
        assertEquals(users[1].username, registeredUsers[1].username)
    }

    @Test
    fun deleteRegisteredUser() {
        val isDeleted = tenantDAO.deleteRegisteredUser("luis")
        assertEquals(isDeleted, 1)
    }

    @Test
    fun getActiveTenantByUuid() {
        val tenant = tenantDAO.getActiveTenantByUuid("4cd93a0f-5b5c-4902-ae0a-181c780fedb1").get()
        assertEquals("4cd93a0f-5b5c-4902-ae0a-181c780fedb1", tenant.uuid)
        assertEquals(Timestamp.valueOf("2018-07-14 13:30:00.0"), tenant.createdAt)
        assertEquals("nuno", tenant.createdBy)
        assertEquals("@isel.pt", tenant.emailPattern)
        assertEquals("isel", tenant.schemaName)
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}