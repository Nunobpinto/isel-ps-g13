package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.repository.interfaces.ResourceDAO
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
import java.util.*
import java.util.Random


@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [EduWikiApiApplication::class])
@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createResourceSchemaDb.sql", "classpath:insertResourceTable.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropResourceSchemaDb.sql"]))
)
@Transactional
class ResourceDAOImplTest {

    @Autowired
    lateinit var resourceDAO: ResourceDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("resourcesSchema")
    }

    @Test
    fun storeResource() {
        val byteArrayToStore = ByteArray(1)
        Random().nextBytes(byteArrayToStore)
        val resource = resourceDAO.storeResource(
                uuId = UUID.fromString("19985d4e-ad5b-11e8-98d0-529269fb1459"),
                byteSequence = byteArrayToStore,
                originalFilename = "resourceFileTest.pdf",
                contentType = "application/pdf",
                size = 10
        )
        assertEquals(UUID.fromString("19985d4e-ad5b-11e8-98d0-529269fb1459"), resource.uuId)
        assertEquals(byteArrayToStore.size, resource.byteSequence.size)
        assertEquals("resourceFileTest.pdf", resource.originalFilename)
        assertEquals("application/pdf", resource.contentType)
        assertEquals(10, resource.size)
    }

    @Test
    fun getResource() {
        val resource = resourceDAO.getResource(UUID.fromString("fd5af6b8-132b-4820-9c07-0e7fcb180b56")).get()
        assertEquals(UUID.fromString("fd5af6b8-132b-4820-9c07-0e7fcb180b56"), resource.uuId)
        assertEquals("application/pdf", resource.contentType)
        assertEquals(81576, resource.size)
        assertEquals("t1.pdf", resource.originalFilename)
    }

    @Test
    fun batchDeleteResources() {
        val rowsAffected = resourceDAO.batchDeleteResources(
                listOf(
                        UUID.fromString("fd5af6b8-132b-4820-9c07-0e7fcb180b56"),
                        UUID.fromString("c883de32-551d-460f-b0fc-de702f032fae")
                )
        )
        assertEquals(2, rowsAffected.size)
    }

    @Test
    fun deleteSpecificResource() {
        val rowsAffected = resourceDAO.deleteSpecificResource(UUID.fromString("fd5af6b8-132b-4820-9c07-0e7fcb180b56"))
        assertEquals(1, rowsAffected)
    }
}