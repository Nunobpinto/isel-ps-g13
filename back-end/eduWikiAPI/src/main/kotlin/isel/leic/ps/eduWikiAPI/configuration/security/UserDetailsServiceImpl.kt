package isel.leic.ps.eduWikiAPI.configuration.security

import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.repository.interfaces.ReputationDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.UserDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    lateinit var userDAO: UserDAO
    @Autowired
    lateinit var reputationDAO: ReputationDAO

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails =
            toUserDetails(
                    userDAO.getUser(username)
                            .orElseThrow { UsernameNotFoundException("User $username does not exist") }
            )

    private fun toUserDetails(user: User): UserDetails = org.springframework.security.core.userdetails
            .User
            .withUsername(user.username)
            .disabled(!user.confirmed)
            .password(BCryptPasswordEncoder().encode(user.password)) //TODO: WRONG!! PASSWORD MUST BE ENCRYPTED ALREADY!!!
            .authorities(reputationDAO.getReputationRoleOfUser(user.username)
                    .orElseThrow { UsernameNotFoundException("Could not find a valid role for user ${user.username}") }
            )
            .build()
}