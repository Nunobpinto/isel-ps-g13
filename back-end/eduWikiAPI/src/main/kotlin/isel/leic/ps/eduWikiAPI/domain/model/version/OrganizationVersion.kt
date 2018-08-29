package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION_CONTACT
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION_ADDRESS
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION_VERSION_ID
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION_WEBSITE
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class OrganizationVersion (
        @ColumnName(ORGANIZATION_VERSION_VERSION_ID)
        val version: Int = 1,
        @ColumnName(ORGANIZATION_VERSION_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(ORGANIZATION_VERSION_FULL_NAME)
        val fullName: String = "",
        @ColumnName(ORGANIZATION_VERSION_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(ORGANIZATION_VERSION_ADDRESS)
        val address: String = "",
        @ColumnName(ORGANIZATION_VERSION_CONTACT)
        val contact: String = "",
        @ColumnName(ORGANIZATION_VERSION_WEBSITE)
        val website: String = "",
        @ColumnName(ORGANIZATION_VERSION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)