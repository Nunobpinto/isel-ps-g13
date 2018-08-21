package isel.leic.ps.eduWikiAPI.domain.model

import isel.leic.ps.eduWikiAPI.repository.TenantRepositoryImpl.Companion.TENANTS_EMAIL_PATTERN
import isel.leic.ps.eduWikiAPI.repository.TenantRepositoryImpl.Companion.TENANTS_CREATED_AT
import isel.leic.ps.eduWikiAPI.repository.TenantRepositoryImpl.Companion.TENANTS_SCHEMA_NAME
import isel.leic.ps.eduWikiAPI.repository.TenantRepositoryImpl.Companion.TENANTS_UUID
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

data class TenantDetails(
        @ColumnName(TENANTS_UUID)
        val uuid: String = UUID.randomUUID().toString(),
        @ColumnName(TENANTS_SCHEMA_NAME)
        val schemaName: String = "",
        @ColumnName(TENANTS_EMAIL_PATTERN)
        val emailPattern: String = "",
        @ColumnName(TENANTS_CREATED_AT)
        val createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now())
)
