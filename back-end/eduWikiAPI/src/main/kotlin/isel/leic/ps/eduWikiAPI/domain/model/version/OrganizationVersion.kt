package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_CONTACT
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_ADDRESS
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_VERSION
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORGANIZATION_WEBSITE
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class OrganizationVersion (
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
        @ColumnName(ORGANIZATION_WEBSITE)
        val website: String = "",
        @ColumnName(ORGANIZATION_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)