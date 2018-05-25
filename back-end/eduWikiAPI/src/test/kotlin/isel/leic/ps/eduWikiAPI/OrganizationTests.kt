package isel.leic.ps.eduWikiAPI

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import junit.framework.TestCase.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.sql.Timestamp
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(EduWikiApiApplication::class), (H2Config::class)])
class OrganizationTests {

    @Autowired
    lateinit var organizationDAO: OrganizationDAO

    @Test
    fun testGetOrganization() {
        val org = organizationDAO.getSpecificOrganization(1)
        assertEquals("ISEL", org.shortName)
        assertEquals("Instituto Superior de Engenharia de Lisboa", org.fullName)
        assertEquals("ze", org.createdBy)
    }

    @Test
    fun testInsertOrganization() {
        val newOrg = Organization(
                createdBy = "nuno",
                fullName = "Instituto Superior Técnico",
                shortName = "IST",
                address = "Alameda D.Afonso Henriques",
                contact = "222222"
        )
        val rows = organizationDAO.createOrganization(newOrg)
        assertEquals(1, rows)
        val deleteRows = organizationDAO.deleteOrganization(2)
        assertEquals(1, deleteRows)
    }

    @Test
    fun testApprovedReport() {
        val report = OrganizationReport(
                id = 1,
                contact = "+351218317000",
                timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        val rows = organizationDAO.reportOrganization(organizationReport = report)
        assertEquals(1, rows)
        val organization = organizationDAO.getSpecificOrganization(1)
        val versionRows = organizationDAO.addToOrganizationVersion(organization)
        assertEquals(1, versionRows)
        val updatedOrganization = Organization(
                id = organization.id,
                contact = report.contact!!,
                address = organization.address,
                fullName = organization.fullName,
                shortName = organization.shortName,
                version = organization.version + 1
        )
        val updateRow = organizationDAO.updateOrganization(updatedOrganization)
        assertEquals(1, updateRow)
        val newOrg = organizationDAO.getSpecificOrganization(1)
        assertEquals(2, newOrg.version)
        val deleteReportRows = organizationDAO.deleteReportOnOrganization(1)
        assertEquals(1, deleteReportRows)
        val deleteVersonRows = organizationDAO.deleteVersion(1, 1)
        assertEquals(1, deleteVersonRows)
    }
}