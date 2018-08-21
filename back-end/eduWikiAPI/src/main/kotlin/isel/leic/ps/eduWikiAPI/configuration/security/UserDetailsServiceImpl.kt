package isel.leic.ps.eduWikiAPI.configuration.security

import isel.leic.ps.eduWikiAPI.configuration.persistence.CaptureTenantFilter.Companion.NO_TENANT_FOUND
import isel.leic.ps.eduWikiAPI.configuration.persistence.CaptureTenantFilter.Companion.NO_TENANT_PROVIDED
import isel.leic.ps.eduWikiAPI.configuration.persistence.TenantContext
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.UserDAO
import isel.leic.ps.eduWikiAPI.domain.model.User
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
    lateinit var reputationDAO: ReputationDAO
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        // Check if there's a problem with provided tenant
        if(TenantContext.getTenantSchema() == NO_TENANT_FOUND)
            throw UsernameNotFoundException("The provided tenant does not exist")
        if(TenantContext.getTenantSchema() == NO_TENANT_PROVIDED)
            throw UsernameNotFoundException("A tenant must be provided")
        // Retrieve user details
        return toUserDetails(userDAO.getUser(username).orElseThrow {
            UsernameNotFoundException("User $username does not exist at specified tenant")
        })
    }

    private fun toUserDetails(user: User): UserDetails = org.springframework.security.core.userdetails
            .User
            .withUsername(user.username)
            .disabled(!user.confirmed)
            .password(passwordEncoder.encode(user.password)) //TODO Nao te esqueças de tirar esta merda
            .authorities(reputationDAO.getReputationRoleOfUser(user.username).orElseThrow { UsernameNotFoundException("Could not find a valid role for user ${user.username}") })
            .build()
}