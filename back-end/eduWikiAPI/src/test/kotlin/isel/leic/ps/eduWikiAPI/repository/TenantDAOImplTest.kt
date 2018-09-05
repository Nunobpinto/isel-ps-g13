package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.model.PendingTenantCreator
import isel.leic.ps.eduWikiAPI.domain.model.PendingTenantDetails
import isel.leic.ps.eduWikiAPI.repository.interfaces.TenantDAO
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
        assertEquals("@alunos.isel.pt", tenant.emailPattern)
        assertEquals("isel", tenant.schemaName)
    }

    @Test
    fun getAllActiveTenants() {
        val tenants = tenantDAO.getAllActiveTenants()
        assertEquals(3, tenants.size)
    }

    @Test
    fun createPendingTenant() {
        val uuid = "9c509b5e-2451-408b-a245-fa0476570132"
        val pending = tenantDAO.createPendingTenant(
                PendingTenantDetails(
                        tenantUuid = UUID.fromString(uuid),
                        fullName = "instituto superior tecnico",
                        shortName = "ist",
                        address = "alameda",
                        contact = "213098765",
                        website = "www.ist.pt",
                        email_pattern = "@alunos.ist.pt",
                        orgSummary = "faculdade de engenharia em lisboa"
                )
        )
        assertEquals("alameda", pending.address)
        assertEquals("instituto superior tecnico", pending.fullName)
        assertEquals("ist", pending.shortName)
        assertEquals(UUID.fromString(uuid), pending.tenantUuid)
        assertEquals("213098765", pending.contact)
        assertEquals("www.ist.pt", pending.website)
        assertEquals("@alunos.ist.pt", pending.email_pattern)
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
        assertEquals("@alunos.fp.pt", pending.email_pattern)
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
                        email_pattern = "@alunos.ist.pt",
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
    }

    @Test
    fun populateTenant() {
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}