package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.REGISTERED_USER_CONFIRMED
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.REGISTERED_USER_TENANT_UUID
import isel.leic.ps.eduWikiAPI.repository.TenantDAOImpl.Companion.REGISTERED_USER_USERNAME
import org.jdbi.v3.core.mapper.reflect.ColumnName

data class RegisteredUser(
        @ColumnName(REGISTERED_USER_USERNAME)
        val username: String = "",
        @ColumnName(REGISTERED_USER_TENANT_UUID)
        val tenantUuid: String = "",
        @ColumnName(REGISTERED_USER_CONFIRMED)
        val confirmed: Boolean = false
)
