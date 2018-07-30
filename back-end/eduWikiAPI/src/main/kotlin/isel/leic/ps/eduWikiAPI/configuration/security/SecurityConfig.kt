package isel.leic.ps.eduWikiAPI.configuration.security

import isel.leic.ps.eduWikiAPI.configuration.security.authorization.AuthorizationConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@EnableWebSecurity
@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    companion object {
        const val REALM: String = "EduWikiRealm"
    }

    @Autowired
    lateinit var userDetailsServiceImpl: UserDetailsServiceImpl
    @Autowired
    lateinit var daoAuthenticationProvider: DaoAuthenticationProvider
    @Autowired
    lateinit var authorizationConfig: AuthorizationConfig

    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .httpBasic().realmName(REALM)
                .and().cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        authorizationConfig.configureRequestAuthorizations(http)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(daoAuthenticationProvider)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
                .antMatchers("/swagger-ui.html")
                .antMatchers("/swagger-resources")
                .antMatchers("/webjars/**")
                .antMatchers("/users")
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val auth = DaoAuthenticationProvider()
        auth.isHideUserNotFoundExceptions = false
        auth.setUserDetailsService(userDetailsServiceImpl)
        auth.setPasswordEncoder(BCryptPasswordEncoder())
        return auth
    }
}