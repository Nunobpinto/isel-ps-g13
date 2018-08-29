package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_CREATOR_FAMILY_NAME
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_CREATOR_GIVEN_NAME
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_CREATOR_IS_PRINCIPAL
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_CREATOR_EMAIL
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_CREATOR_TENANT_UUID
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.PENDING_TENANTS_CREATOR_USERNAME
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.util.*

data class PendingTenantCreator(
        @ColumnName(PENDING_TENANTS_CREATOR_USERNAME)
        val username: String = "",
        @ColumnName(PENDING_TENANTS_CREATOR_EMAIL)
        val email: String = "",
        @ColumnName(PENDING_TENANTS_CREATOR_TENANT_UUID)
        val pendingTenantUuid: UUID = UUID.randomUUID(),
        @ColumnName(PENDING_TENANTS_CREATOR_GIVEN_NAME)
        val givenName: String = "",
        @ColumnName(PENDING_TENANTS_CREATOR_FAMILY_NAME)
        val familyName: String = "",
        @ColumnName(PENDING_TENANTS_CREATOR_IS_PRINCIPAL)
        val principal: Boolean = true
)
