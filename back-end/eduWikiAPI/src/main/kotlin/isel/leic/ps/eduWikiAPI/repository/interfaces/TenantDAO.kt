package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.*
import java.util.*

interface TenantDAO {

    fun findActiveTenatById(tenantUuid: String): Optional<TenantDetails>

    fun getAllActiveTenants(): List<TenantDetails>

    fun createPendingTenant(pendingTenantDetails: PendingTenantDetails): PendingTenantDetails

    fun bulkCreatePendingTenantCreators(tenantCreators: List<PendingTenantCreator>): List<PendingTenantCreator>

    fun findPendingTenantById(tenantUuid: String): Optional<PendingTenantDetails>

    fun findPendingTenantCreatorsByTenantId(tenantUuid: String): List<PendingTenantCreator>

    fun getAllPendingTenants(): List<PendingTenantDetails>

    fun getRegisteredUserByUsername(username: String): Optional<RegisteredUser>

    fun createTenantBasedOnPendingTenant(schema: String)

    fun populateTenant(schema: String, organization: Organization, usersAndRep: List<Pair<User, Reputation>>)

}