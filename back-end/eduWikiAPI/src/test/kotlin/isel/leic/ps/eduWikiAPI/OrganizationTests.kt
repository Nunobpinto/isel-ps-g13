package isel.leic.ps.eduWikiAPI

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import junit.framework.TestCase.*
import org.jdbi.v3.core.Handle
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.junit4.SpringRunner
import java.sql.Timestamp
import java.time.LocalDateTime


@SqlGroup(
        (Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:createDB.sql","classpath:inserts.sql"])),
        (Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = ["classpath:dropDB.sql"]))
)
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(EduWikiApiApplication::class), (H2Config::class)])
class OrganizationTests {

    @Autowired
    lateinit var organizationDAO: OrganizationDAO

    @Autowired
    lateinit var handle: Handle

    @Test
    fun testGetOrganization() {
        handle.begin()
        val org = organizationDAO.getSpecificOrganization(1).get()
        assertEquals("ISEL", org.shortName)
        assertEquals("Instituto Superior de Engenharia de Lisboa", org.fullName)
        assertEquals("ze", org.createdBy)
        handle.commit()
    }

    @Test
    fun testGetAllOrganizations(){
        handle.begin()
        val orgs = organizationDAO.getAllOrganizations()
        assertEquals(1,orgs.size)
        handle.commit()
    }

    @Test
    fun testDeleteOrganization() {
        handle.begin()
        assertEquals(organizationDAO.deleteOrganization(1), 1)
        handle.commit()
    }

    @Test
    fun testDeleteAllOrganizations() {
        handle.begin()
        assertEquals(organizationDAO.deleteAllOrganizations(), 1)
        handle.commit()
    }

    @Test
    fun testUpdateOrganization() {
        handle.begin()
        val time = Timestamp.valueOf(LocalDateTime.now())
        val org = Organization(
                organizationId = 1,
                version = 2,
                fullName = "Instituto Superior de Engenharia de Lisboa",
                shortName = "ISEL",
                address = "Rua Emídio Navarro 1",
                contact = "218 317 000",
                createdBy = "joao",
                timestamp = time
        )
        assertEquals(organizationDAO.updateOrganization(org), 1)


        val currOrg = organizationDAO.getSpecificOrganization(1).get()
        assertEquals(currOrg.version, 2)
        assertEquals(currOrg.address, "Rua Emídio Navarro 1")
        assertEquals(currOrg.createdBy, "joao")
        assertEquals(currOrg.timestamp, time)
        handle.commit()
    }

    @Test
    fun testInsertOrganization() {
        handle.begin()
        val newOrg = Organization(
                createdBy = "nuno",
                fullName = "Instituto Superior Técnico",
                shortName = "IST",
                address = "Alameda D.Afonso Henriques",
                contact = "222222"
        )
        val dbOrg = organizationDAO.createOrganization(newOrg).get()
        //TODO: verificar se o ReturnGeneratedKeys() não esta a funcionar por estar-se no H2
        assertEquals(dbOrg.organizationId, 2)
        val deleteRows = organizationDAO.deleteOrganization(dbOrg.organizationId)
        assertEquals(deleteRows, 1)
        handle.commit()
    }

    @Test
    fun testVoteOnOrganization() {
        handle.begin()
        var updatedRows = organizationDAO.voteOnOrganization(1, Vote.Up)
        assertEquals(updatedRows, 1)
        var org = organizationDAO.getSpecificOrganization(1).get()
        assertEquals(org.votes, 1)

        updatedRows = organizationDAO.voteOnOrganization(1, Vote.Down)
        assertEquals(updatedRows, 1)
        org = organizationDAO.getSpecificOrganization(1).get()
        assertEquals(org.votes, 0)
        handle.commit()
    }

    @Test
    fun testApprovedReport() {
        handle.begin()
        val report = OrganizationReport(
                organizationId = 1,
                contact = "+351218317000",
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        val orgReport = organizationDAO.reportOrganization(report).get()
        assertEquals(orgReport.reportId, 1)
        handle.commit()
        /*val organization = organizationDAO.getSpecificOrganization(1).get()
        val versionRows = organizationDAO.addToOrganizationVersion(organization)
        assertEquals(1, versionRows)
        val updatedOrganization = Organization(
                courseId = organization.courseId,
                contact = report.contact!!,
                address = organization.address,
                fullName = organization.fullName,
                shortName = organization.shortName,
                version = organization.version + 1
        )
        val updateRow = organizationDAO.updateOrganization(updatedOrganization)
        assertEquals(1, updateRow)
        val newOrg = organizationDAO.getSpecificOrganization(1).get()
        assertEquals(2, newOrg.version)
        val deleteReportRows = organizationDAO.deleteReportOnOrganization(1)
        assertEquals(1, deleteReportRows)
        val deleteVersonRows = organizationDAO.deleteSpecificVersionOfOrganization(1, 1)
        assertEquals(1, deleteVersonRows)*/
    }
}