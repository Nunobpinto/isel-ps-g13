package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import org.junit.After
import org.junit.Test

import org.junit.Assert.*

class TenantDAOImplTest {

    @Test
    fun findActiveTenatById() {
    }

    @Test
    fun getAllActiveTenants() {
    }

    @Test
    fun createPendingTenant() {
    }

    @Test
    fun bulkCreatePendingTenantCreators() {
    }

    @Test
    fun getRegisteredUserByUsername() {
    }

    @Test
    fun findPendingTenantById() {
    }

    @Test
    fun getAllPendingTenants() {
    }

    @Test
    fun findPendingTenantCreatorsByTenantId() {
    }

    @Test
    fun getCurrentTenantDetails() {
    }

    @Test
    fun getPendingTenantById() {
    }

    @Test
    fun getPendingTenantCreators() {
    }

    @Test
    fun deletePendingTenantById() {
    }

    @Test
    fun createActiveTenantEntry() {
    }

    @Test
    fun createTenantBasedOnPendingTenant() {
    }

    @Test
    fun populateTenant() {
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}