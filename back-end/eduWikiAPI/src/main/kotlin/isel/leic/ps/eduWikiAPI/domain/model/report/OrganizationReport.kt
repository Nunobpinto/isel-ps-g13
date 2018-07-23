package isel.leic.ps.eduWikiAPI.domain.model.report

import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_ID
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VOTES
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_CONTACT
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_ADDRESS
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_REPORTED_BY
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_REPORT_ID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class OrganizationReport (
        @ColumnName(ORGANIZATION_REPORT_ID)
        val reportId: Int = -1,
        @ColumnName(ORGANIZATION_ID)
        val organizationId: Int = 0,
        @ColumnName(ORGANIZATION_FULL_NAME)
        val fullName: String? = null,
        @ColumnName(ORGANIZATION_SHORT_NAME)
        val shortName: String? = null,
        @ColumnName(ORGANIZATION_ADDRESS)
        val address: String? = null,
        @ColumnName(ORGANIZATION_CONTACT)
        val contact: String? = null,
        @ColumnName(ORGANIZATION_REPORTED_BY)
        val reportedBy:String = "",
        @ColumnName(ORGANIZATION_VOTES)
        val votes: Int = 0,
        @ColumnName(ORGANIZATION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)