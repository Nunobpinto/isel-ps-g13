package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.repository.interfaces.TenantDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.locator.ClasspathSqlLocator
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.*
import org.jdbi.v3.stringtemplate4.StringTemplateEngine
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Timestamp
import java.util.*


@Repository
class TenantDAOImpl : TenantDAO {

    companion object {
        // MASTER SCHEMA NAME
        const val MASTER_SCHEMA = "master"
        // TABLE NAMES
        const val TENANTS_TABLE = "tenants"
        const val PENDING_TENANTS_TABLE = "pending_tenants"
        const val PENDING_TENANTS_CREATOR_TABLE = "pending_tenant_creators"
        const val REGISTERED_USERS_TABLE = "registered_users"
        // TENANTS FIELDS
        const val TENANTS_UUID = "uuid"
        const val TENANTS_SCHEMA_NAME = "schema_name"
        const val TENANTS_EMAIL_PATTERN = "email_pattern"
        const val TENANTS_CREATED_AT = "created_at"
        const val TENANTS_CREATED_BY = "created_by"
        // PENDING_TENANTS FIELDS
        const val PENDING_TENANTS_UUID = "uuid"
        const val PENDING_TENANTS_FULL_NAME = "tenant_full_name"
        const val PENDING_TENANTS_SHORT_NAME = "tenant_short_name"
        const val PENDING_TENANTS_ADDRESS = "tenant_address"
        const val PENDING_TENANTS_CONTACT = "tenant_contact"
        const val PENDING_TENANTS_WEBSITE = "tenant_website"
        const val PENDING_TENANTS_EMAIL_PATTERN = "tenant_email_pattern"
        const val PENDING_TENANTS_TIMESTAMP = "tenant_timestamp"
        const val PENDING_TENANTS_ORG_SUMMARY = "tenant_organization_summary"
        // PENDINT_TENANTS_CREATOR FIELDS
        const val PENDING_TENANTS_CREATOR_EMAIL = "user_email"
        const val PENDING_TENANTS_CREATOR_USERNAME = "user_username"
        const val PENDING_TENANTS_CREATOR_TENANT_UUID = "pending_tenant_uuid"
        const val PENDING_TENANTS_CREATOR_GIVEN_NAME = "user_given_name"
        const val PENDING_TENANTS_CREATOR_FAMILY_NAME = "user_family_name"
        const val PENDING_TENANTS_CREATOR_IS_PRINCIPAL = "principal_user"
        // REGISTERED_USERS FIELDS
        const val REGISTERED_USER_USERNAME = "user_username"
        const val REGISTERED_TENANT_UUID = "tenant_uuid"
    }

    @Autowired
    lateinit var jdbi: Jdbi

    override fun findActiveTenantById(tenantUuid: String): Optional<TenantDetails> =
            jdbi.open().attach(TenantDAOJdbi::class.java).findActiveTenantById(tenantUuid)

    override fun getAllActiveTenants(): List<TenantDetails> =
            jdbi.open().attach(TenantDAOJdbi::class.java).getAllActiveTenants()

    override fun createPendingTenant(pendingTenantDetails: PendingTenantDetails): PendingTenantDetails =
            jdbi.open().attach(TenantDAOJdbi::class.java).createPendingTenant(pendingTenantDetails)

    override fun bulkCreatePendingTenantCreators(tenantCreators: List<PendingTenantCreator>): List<PendingTenantCreator> =
            jdbi.open().attach(TenantDAOJdbi::class.java).bulkCreatePendingTenantCreators(tenantCreators)

    override fun getRegisteredUserByUsername(username: String): Optional<RegisteredUser> =
            jdbi.open().attach(TenantDAOJdbi::class.java).getRegisteredUserByUsername(username)

    override fun findPendingTenantById(tenantUuid: String): Optional<PendingTenantDetails> =
            jdbi.open().attach(TenantDAOJdbi::class.java).findPendingTenantById(tenantUuid)

    override fun getAllPendingTenants(): List<PendingTenantDetails> =
            jdbi.open().attach(TenantDAOJdbi::class.java).getAllPendingTenants()

    override fun findPendingTenantCreatorsByTenantId(tenantUuid: String): List<PendingTenantCreator> =
            jdbi.open().attach(TenantDAOJdbi::class.java).findPendingTenantCreatorsByTenantId(tenantUuid)

    override fun getCurrentTenantDetails(): Optional<TenantDetails> =
            jdbi.open().attach(TenantDAOJdbi::class.java).getCurrentTenantDetails()

    override fun deletePendingTenantById(tenantUuid: String): Int =
            jdbi.open().attach(TenantDAOJdbi::class.java).deletePendingTenantById(tenantUuid)

    override fun createActiveTenantEntry(dev: String, timestamp: Timestamp, pendingTenant: PendingTenantDetails): TenantDetails =
            jdbi.open().attach(TenantDAOJdbi::class.java).createActiveTenantEntry(dev, timestamp, pendingTenant)

    override fun createTenantBasedOnPendingTenant(schema: String) {
        jdbi.open().createScript(ClasspathSqlLocator.getResourceOnClasspath("scripts/eduwiki_create_tenant.sql"))
                .setTemplateEngine(StringTemplateEngine())
                .define("schema", schema)
                .execute()
    }

    override fun populateTenant(schema: String, organization: Organization, users: List<User>, reputations: List<Reputation>) {
        jdbi.open().createScript(ClasspathSqlLocator.getResourceOnClasspath("scripts/eduwiki_populate_tables.sql"))
                .setTemplateEngine(StringTemplateEngine())
                .define("schema", schema)
                .define("org", organization)
                .define("users", users)
                .define("reputations", reputations)
                .execute()
    }

    interface TenantDAOJdbi : TenantDAO {

        @SqlQuery("SELECT * FROM $MASTER_SCHEMA.$TENANTS_TABLE WHERE $TENANTS_UUID = :tenantUuid")
        override fun findActiveTenantById(tenantUuid: String): Optional<TenantDetails>

        @SqlQuery("SELECT * FROM $MASTER_SCHEMA.$TENANTS_TABLE")
        override fun getAllActiveTenants(): List<TenantDetails>

        @SqlUpdate(
                "INSERT INTO $MASTER_SCHEMA.$PENDING_TENANTS_TABLE(" +
                        "$PENDING_TENANTS_UUID, " +
                        "$PENDING_TENANTS_FULL_NAME, " +
                        "$PENDING_TENANTS_SHORT_NAME, " +
                        "$PENDING_TENANTS_ADDRESS, " +
                        "$PENDING_TENANTS_CONTACT, " +
                        "$PENDING_TENANTS_WEBSITE, " +
                        "$PENDING_TENANTS_EMAIL_PATTERN, " +
                        "$PENDING_TENANTS_ORG_SUMMARY, " +
                        "$PENDING_TENANTS_TIMESTAMP) " +
                        "VALUES (" +
                        ":pendingTenantDetails.tenantUuid, :pendingTenantDetails.fullName, :pendingTenantDetails.shortName, " +
                        ":pendingTenantDetails.address, :pendingTenantDetails.contact, :pendingTenantDetails.website, " +
                        ":pendingTenantDetails.emailPattern, :pendingTenantDetails.orgSummary, :pendingTenantDetails.timestamp)"
        )
        @GetGeneratedKeys
        override fun createPendingTenant(pendingTenantDetails: PendingTenantDetails): PendingTenantDetails

        @SqlBatch(
                "INSERT INTO $MASTER_SCHEMA.$PENDING_TENANTS_CREATOR_TABLE (" +
                        "$PENDING_TENANTS_CREATOR_USERNAME, " +
                        "$PENDING_TENANTS_CREATOR_EMAIL, " +
                        "$PENDING_TENANTS_CREATOR_TENANT_UUID, " +
                        "$PENDING_TENANTS_CREATOR_GIVEN_NAME, " +
                        "$PENDING_TENANTS_CREATOR_IS_PRINCIPAL, " +
                        "$PENDING_TENANTS_CREATOR_FAMILY_NAME )" +
                        "VALUES (" +
                        ":tenantCreator.username, :tenantCreator.email, :tenantCreator.pendingTenantUuid, " +
                        ":tenantCreator.givenName, :tenantCreator.principal, :tenantCreator.familyName)"
        )
        @GetGeneratedKeys
        override fun bulkCreatePendingTenantCreators(@BindBean("tenantCreator") tenantCreators: List<PendingTenantCreator>): List<PendingTenantCreator>

        @SqlQuery("SELECT * FROM $MASTER_SCHEMA.$REGISTERED_USERS_TABLE WHERE $REGISTERED_USER_USERNAME = :username")
        override fun getRegisteredUserByUsername(username: String): Optional<RegisteredUser>

        @SqlQuery("SELECT * FROM $MASTER_SCHEMA.$PENDING_TENANTS_TABLE WHERE $PENDING_TENANTS_UUID = :tenantUuid")
        override fun findPendingTenantById(tenantUuid: String): Optional<PendingTenantDetails>

        @SqlQuery("SELECT * FROM $MASTER_SCHEMA.$PENDING_TENANTS_TABLE")
        override fun getAllPendingTenants(): List<PendingTenantDetails>

        @SqlQuery("SELECT * FROM $MASTER_SCHEMA.$PENDING_TENANTS_CREATOR_TABLE WHERE $PENDING_TENANTS_CREATOR_TENANT_UUID = :tenantUuid")
        override fun findPendingTenantCreatorsByTenantId(tenantUuid: String): List<PendingTenantCreator>

        override fun createTenantBasedOnPendingTenant(schema: String) = Unit

        override fun populateTenant(schema: String, organization: Organization, users: List<User>, reputations: List<Reputation>) = Unit

        @SqlQuery("SELECT * FROM $MASTER_SCHEMA.$TENANTS_TABLE WHERE $TENANTS_SCHEMA_NAME = ':schema'")
        override fun getCurrentTenantDetails(): Optional<TenantDetails>

        @SqlUpdate("DELETE FROM $MASTER_SCHEMA.$PENDING_TENANTS_TABLE WHERE $PENDING_TENANTS_UUID = :tenantUuid")
        override fun deletePendingTenantById(tenantUuid: String): Int

        @SqlUpdate(
                "INSERT INTO $MASTER_SCHEMA.$TENANTS_TABLE (" +
                        "$TENANTS_CREATED_BY, " +
                        "$TENANTS_CREATED_AT, " +
                        "$TENANTS_EMAIL_PATTERN, " +
                        "$TENANTS_SCHEMA_NAME, " +
                        "$TENANTS_UUID)" +
                        "VALUES (:dev, :timestamp, " +
                        ":pendingTenant.emailPattern, :pendingTenant.shortName, :pendingTenant.tenantUuid)"
        )
        @GetGeneratedKeys
        override fun createActiveTenantEntry(dev: String, timestamp: Timestamp, pendingTenant: PendingTenantDetails): TenantDetails

    }
}