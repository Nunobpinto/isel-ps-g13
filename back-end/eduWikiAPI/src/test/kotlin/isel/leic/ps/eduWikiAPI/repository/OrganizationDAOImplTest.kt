package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.EduWikiApiApplication
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.OrganizationDAO
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
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
class OrganizationDAOImplTest {

    @Autowired
    lateinit var organizationDAO: OrganizationDAO

    @Before
    fun init() {
        TenantContext.setTenantSchema("isel")
    }

    @Test
    fun getOrganization() {
        val organization = organizationDAO.getOrganization().get()
        assertEquals('X', organization.organizationId)
        assertEquals(1, organization.version)
        assertEquals(1, organization.logId)
        assertEquals("Instituto Superior de Engenharia de Lisboa", organization.fullName)
        assertEquals("ISEL", organization.shortName)
        assertEquals("Rua Emídio Navarro", organization.address)
        assertEquals("218 317 000", organization.contact)
        assertEquals("https://www.isel.pt/", organization.website)
    }

    @Test
    fun updateOrganization() {
        val updatedOrg = organizationDAO.updateOrganization(
                Organization(
                        version = 2,
                        fullName = "Other full name",
                        shortName = "OFN",
                        address = "somewhere",
                        contact = "918081464",
                        website = "www.example.com"
                )
        )
        assertEquals(2, updatedOrg.version)
        assertEquals("Other full name", updatedOrg.fullName)
        assertEquals("OFN", updatedOrg.shortName)
        assertEquals("somewhere", updatedOrg.address)
        assertEquals("918081464", updatedOrg.contact)
        assertEquals("www.example.com", updatedOrg.website)
    }

    @Test
    fun getAllVersionsOfOrganization() {
        val orgVersions = organizationDAO.getAllVersionsOfOrganization()
        assertEquals(1, orgVersions.size)
    }

    @Test
    fun getSpecificVersionOfOrganization() {
        val orgVersion = organizationDAO.getSpecificVersionOfOrganization(1).get()
        assertEquals(1, orgVersion.version)
        assertEquals("Instituto Superior de Engenharia de Lisboa", orgVersion.fullName)
        assertEquals("ISEL", orgVersion.shortName)
        assertEquals("Rua Emídio Navarro", orgVersion.address)
        assertEquals("218 317 000", orgVersion.contact)
        assertEquals("https://www.isel.pt/", orgVersion.website)
        assertEquals("ze", orgVersion.createdBy)
    }

    @Test
    fun createOrganizationVersion() {
        val organizationVersion = organizationDAO.createOrganizationVersion(
                OrganizationVersion(
                        version = 3,
                        createdBy = "rui",
                        fullName = "Other full name",
                        shortName = "OFN",
                        address = "somewhere",
                        contact = "918081464",
                        website = "www.example.com"
                )
        )
        assertEquals(3, organizationVersion.version)
        assertEquals("Other full name", organizationVersion.fullName)
        assertEquals("OFN", organizationVersion.shortName)
        assertEquals("somewhere", organizationVersion.address)
        assertEquals("918081464", organizationVersion.contact)
        assertEquals("www.example.com", organizationVersion.website)
    }

    @Test
    fun reportOrganization() {
        val orgReport = organizationDAO.reportOrganization(
                OrganizationReport(
                        fullName = "New Full Name",
                        shortName = "NFN",
                        reportedBy = "Nuno"
                )
        )
        assertEquals(2, orgReport.reportId)
        assertEquals("New Full Name", orgReport.fullName)
        assertEquals("NFN", orgReport.shortName)
        assertEquals("Nuno", orgReport.reportedBy)
        assertNull(orgReport.address)
        assertNull(orgReport.contact)
        assertNull(orgReport.website)
    }

    @Test
    fun deleteSpecificReportOnOrganization() {
        val rowsAffected = organizationDAO.deleteSpecificReportOnOrganization(1)
        assertEquals(1, rowsAffected)
    }

    @Test
    fun deleteAllReportsOnOrganization() {
        val rowsAffected = organizationDAO.deleteAllReportsOnOrganization()
        assertEquals(1, rowsAffected)
    }

    @Test
    fun getAllReportsOnOrganization() {
        val reports = organizationDAO.getAllReportsOnOrganization()
        assertEquals(1, reports.size)
    }

    @Test
    fun getSpecificReportOnOrganization() {
        val orgReport = organizationDAO.getSpecificReportOnOrganization(1).get()
        assertEquals(1, orgReport.reportId)
        assertEquals("Instituo Superior de Engenharia de Torres Novas", orgReport.fullName)
        assertEquals("ISETN", orgReport.shortName)
        assertEquals("Nuno", orgReport.reportedBy)
        assertEquals(2, orgReport.votes)
        assertNull(orgReport.address)
        assertNull(orgReport.contact)
        assertNull(orgReport.website)
    }

    @Test
    fun updateVotesOnOrganizationReport() {
        val oldOrgReport = organizationDAO.getSpecificReportOnOrganization(1).get()
        assertEquals(2, oldOrgReport.votes)
        val rowsAffected = organizationDAO.updateVotesOnOrganizationReport(1, 1)
        assertEquals(1, rowsAffected)
        val updatedOrgReport = organizationDAO.getSpecificReportOnOrganization(1).get()
        assertEquals(1, updatedOrgReport.votes)
    }

    @Test
    fun getOrganizationReportByLogId() {
        val orgReport = organizationDAO.getOrganizationReportByLogId(1).get()
        assertEquals(1, orgReport.logId)
        assertEquals("Instituo Superior de Engenharia de Torres Novas", orgReport.fullName)
        assertEquals("ISETN", orgReport.shortName)
        assertNull(orgReport.address)
        assertNull(orgReport.contact)
        assertNull(orgReport.website)
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}