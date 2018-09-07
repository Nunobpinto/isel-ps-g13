package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.*
import java.sql.Timestamp
import java.util.*

interface TenantDAO {

    fun findActiveTenantById(tenantUuid: String): Optional<TenantDetails>

    fun getAllActiveTenants(): List<TenantDetails>

    fun createPendingTenant(pendingTenantDetails: PendingTenantDetails): PendingTenantDetails

    fun bulkCreatePendingTenantCreators(tenantCreators: List<PendingTenantCreator>): List<PendingTenantCreator>

    fun findPendingTenantById(tenantUuid: String): Optional<PendingTenantDetails>

    fun findPendingTenantCreatorsByTenantId(tenantUuid: String): List<PendingTenantCreator>

    fun getAllPendingTenants(): List<PendingTenantDetails>

    fun getRegisteredUserByUsername(username: String): Optional<RegisteredUser>

    fun confirmUser(username: String): RegisteredUser

    fun createTenantBasedOnPendingTenant(schema: String)

    fun populateTenant(schema: String, organization: Organization, users: List<User>, reputations: List<Reputation>)

    fun getCurrentTenantDetails(): Optional<TenantDetails>

    fun deletePendingTenantById(tenantUuid: String): Int

    fun createActiveTenantEntry(dev: String, timestamp: Timestamp, pendingTenant: PendingTenantDetails): TenantDetails

    fun registerUser(registeredUser: RegisteredUser): RegisteredUser

    fun bulkRegisterUser(registeredUsers: List<RegisteredUser>): List<RegisteredUser>

    fun deleteRegisteredUser(username: String): Int

    fun getActiveTenantByUuid(tenantUuid: String): Optional<TenantDetails>

}