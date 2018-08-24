package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_ADDRESS
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_CONTACT
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_EMAIL_PATTERN
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_ORG_SUMMARY
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_UUID
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_WEBSITE
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

data class PendingTenantDetails(
        @ColumnName(PENDING_TENANTS_UUID)
        val tenantUuid: UUID = UUID.randomUUID(),
        @ColumnName(PENDING_TENANTS_FULL_NAME)
        val fullName: String = "",
        @ColumnName(PENDING_TENANTS_SHORT_NAME)
        val shortName: String = "",
        @ColumnName(PENDING_TENANTS_ADDRESS)
        val address: String = "",
        @ColumnName(PENDING_TENANTS_CONTACT)
        val contact: String = "",
        @ColumnName(PENDING_TENANTS_WEBSITE)
        val website: String = "",
        @ColumnName(PENDING_TENANTS_EMAIL_PATTERN)
        val emailPattern: String = "",
        @ColumnName(PENDING_TENANTS_ORG_SUMMARY)
        val orgSummary: String = "",
        @ColumnName(PENDING_TENANTS_TIMESTAMP)
        val timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)