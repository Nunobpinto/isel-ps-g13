package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import org.junit.After
import org.junit.Test

import org.junit.Assert.*

class TermDAOImplTest {

    @Test
    fun getAllTerms() {
    }

    @Test
    fun getTerm() {
    }

    @After
    fun cleanup() {
        TenantContext.resetTenantSchema()
    }

}