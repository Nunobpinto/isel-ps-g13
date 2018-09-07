package isel.leic.ps.eduWikiAPI.configuration.security

import isel.leic.ps.eduWikiAPI.configuration.persistence.CaptureTenantFilter.Companion.NO_TENANT_FOUND
import isel.leic.ps.eduWikiAPI.configuration.persistence.CaptureTenantFilter.Companion.NO_TENANT_PROVIDED
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.UserDAO
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.repository.interfaces.TenantDAO
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    lateinit var userDAO: UserDAO
    @Autowired
    lateinit var tenantDAO: TenantDAO
    @Autowired
    lateinit var reputationDAO: ReputationDAO

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        // Check if there's a problem with provided tenant
        if(TenantContext.getTenantSchema() == NO_TENANT_FOUND)
            throw UsernameNotFoundException("The provided tenant does not exist")
        if(TenantContext.getTenantSchema() == NO_TENANT_PROVIDED)
            throw UsernameNotFoundException("A tenant must be provided")
        // Retrieve user details
        val user = userDAO.getUser(username).orElseThrow {
            UsernameNotFoundException("User $username does not exist at specified tenant")
        }
        return toUserDetails(user)
    }

    private fun toUserDetails(user: User): UserDetails {
        val reputation = reputationDAO.getUserReputationDetails(user.username)
                .orElseThrow { UsernameNotFoundException("Could not find a valid role for user ${user.username}") }
        val isConfirmed =
                if(reputation.role != ReputationRole.ROLE_DEV.name)
                    tenantDAO.getRegisteredUserByUsername(user.username).orElseThrow { UsernameNotFoundException("User does not exist at specified tenant") }.confirmed
                else
                    true
        return org.springframework.security.core.userdetails
                .User
                .withUsername(user.username)
                .disabled(!isConfirmed)
                .accountLocked(user.locked)
                .password(user.password)
                .authorities(reputation.role)
                .build()
    }
}