package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.ReputationMatcher
import isel.leic.ps.eduWikiAPI.domain.model.Reputation
import isel.leic.ps.eduWikiAPI.domain.model.ReputationRole
import isel.leic.ps.eduWikiAPI.domain.model.User
import java.util.*

interface ReputationDAO {

    fun getReputationRoleOfUser(username: String): Optional<ReputationRole>

    fun getAllReputationRoles(): List<ReputationRole>

    fun getReputationMatchers(): List<ReputationMatcher>

    fun saveNewUser(reputation: Reputation): Reputation

    fun getRoleByHierarchyLevel(level: Int): Optional<ReputationRole>

    fun changeRole(username: String, reputationPoints: Int, role: String): Int
}