package isel.leic.ps.eduWikiAPI.domain.outputModel.version

import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_ID
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_CONTACT
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_ADDRESS
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOJdbi.Companion.ORGANIZATION_VERSION
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime

data class OrganizationVersionOutputModel (
        val version: Int = 1,
        val organizationId: Int = 0,
        val createdBy: String = "",
        val fullName: String = "",
        val shortName: String = "",
        val address: String = "",
        val contact: String = "",
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)