package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_ID
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_VOTES
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_CONTACT
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_ADDRESS
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_LOG_ID
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class Organization(
        @ColumnName(ORGANIZATION_ID)
        val organizationId: Int = -1,
        @ColumnName(ORGANIZATION_VERSION)
        val version: Int = 1,
        @ColumnName(ORGANIZATION_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(ORGANIZATION_FULL_NAME)
        val fullName: String = "",
        @ColumnName(ORGANIZATION_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(ORGANIZATION_ADDRESS)
        val address: String = "",
        @ColumnName(ORGANIZATION_CONTACT)
        val contact: String = "",
        @ColumnName(ORGANIZATION_VOTES)
        val votes: Int = 0,
        @ColumnName(ORGANIZATION_LOG_ID)
        val logId: Int = 0,
        @ColumnName(ORGANIZATION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)