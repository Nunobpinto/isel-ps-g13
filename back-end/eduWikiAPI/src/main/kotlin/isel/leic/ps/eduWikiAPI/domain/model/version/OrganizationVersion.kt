package isel.leic.ps.eduWikiAPI.domain.model.version

import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_ID
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_CONTACT
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_ADDRESS
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl.Companion.ORG_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp

data class OrganizationVersion (
        @ColumnName(ORG_ID)
        val organizationId: Int = 0,
        @ColumnName(ORG_VERSION)
        val version: Int = 0,
        @ColumnName(ORG_CREATED_BY)
        val createdBy: String = "",
        @ColumnName(ORG_FULL_NAME)
        val fullName: String = "",
        @ColumnName(ORG_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(ORG_ADDRESS)
        val address: String = "",
        @ColumnName(ORG_CONTACT)
        val contact: String = "",
        @ColumnName(ORG_TIMESTAMP)
        val timestamp: Timestamp = Timestamp(0)
)