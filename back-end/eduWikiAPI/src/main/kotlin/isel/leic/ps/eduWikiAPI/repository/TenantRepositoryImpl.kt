package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.TenantDetails
import isel.leic.ps.eduWikiAPI.repository.interfaces.TenantRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import java.util.*

@Repository
class TenantRepositoryImpl : TenantRepository {

    companion object {
        // MASTER SCHEMA NAME
        const val MASTER_SCHEMA = "master"
        // TABLE NAMES
        const val TENANTS_TABLE = "tenants"
        // FIELDS
        const val TENANTS_UUID = "uuid"
        const val TENANTS_SCHEMA_NAME = "schema_name"
        const val TENANTS_EMAIL_PATTERN = "email_pattern"
        const val TENANTS_CREATED_AT = "created_at"
    }

    @Qualifier("MainJdbi")
    @Autowired
    lateinit var jdbi: Jdbi

    override fun findById(tenantUuid: String): Optional<TenantDetails> =
            jdbi.open().attach(TenantRepositoryJdbi::class.java).findById(tenantUuid)

    interface TenantRepositoryJdbi : TenantRepository {

        @SqlQuery("SELECT * FROM $MASTER_SCHEMA.$TENANTS_TABLE WHERE $TENANTS_UUID = :tenantUuid")
        override fun findById(tenantUuid: String): Optional<TenantDetails>

    }
}