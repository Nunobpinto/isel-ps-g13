package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import org.junit.After
import org.junit.Test

import org.junit.Assert.*

class TokenDAOImplTest {

    @Test
    fun saveToken() {
    }

    @Test
    fun getToken() {
    }

    @Test
    fun deleteToken() {
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}