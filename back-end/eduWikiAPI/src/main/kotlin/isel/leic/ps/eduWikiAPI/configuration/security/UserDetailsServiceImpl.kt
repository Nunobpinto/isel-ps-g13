package isel.leic.ps.eduWikiAPI.configuration.security

import isel.leic.ps.eduWikiAPI.domain.model.ReputationRole
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.repository.ReputationDAOJdbi
import isel.leic.ps.eduWikiAPI.repository.UserDAOJdbi
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun loadUserByUsername(username: String): UserDetails =
        toUserDetails(
                jdbi.withExtension<Optional<User>, UserDAOJdbi, Exception>(UserDAOJdbi::class.java){ it.getUser(username) }
                        .orElseThrow { UsernameNotFoundException("User $username does not exist") }
        )

    private fun toUserDetails(user: User): UserDetails =
            org.springframework.security.core.userdetails
                    .User
                    .withUsername(user.username)
                    .password(BCryptPasswordEncoder().encode(user.password)) //TODO: WRONG!! PASSWORD MUST BE ENCRYPTED ALREADY!!!
                    .authorities(jdbi.withExtension<Optional<ReputationRole>, ReputationDAOJdbi, Exception>(ReputationDAOJdbi::class.java){ it.getReputationRoleOfUser(user.username) }
                            .orElseThrow { UsernameNotFoundException("Could not find a valid role for user ${user.username}") }
                    )
                    .build()
}