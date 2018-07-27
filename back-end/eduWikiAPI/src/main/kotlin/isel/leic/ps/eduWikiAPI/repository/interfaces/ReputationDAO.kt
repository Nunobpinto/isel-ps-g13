package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.ReputationMatcher
import isel.leic.ps.eduWikiAPI.domain.model.ReputationRole
import java.util.*

interface ReputationDAO {

    fun getReputationRoleOfUser(username: String): Optional<ReputationRole>

    fun getAllReputationRoles(): List<ReputationRole>

    fun getReputationMatchers(): List<ReputationMatcher>
}