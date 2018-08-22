package isel.leic.ps.eduWikiAPI.configuration.security

import isel.leic.ps.eduWikiAPI.configuration.persistence.CaptureTenantFilter
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
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    companion object {
        const val REALM: String = "EduWikiRealm"
    }

    @Autowired
    lateinit var userDetailsServiceImpl: UserDetailsService
    @Autowired
    lateinit var daoAuthenticationProvider: DaoAuthenticationProvider
    @Autowired
    lateinit var authorizationConfig: AuthorizationConfig
    @Autowired
    lateinit var captureTenantFilter: CaptureTenantFilter

    override fun configure(http: HttpSecurity) {
        http
                .addFilterAt(captureTenantFilter, BasicAuthenticationFilter::class.java)
                .csrf().disable()
                .httpBasic().realmName(REALM).authenticationEntryPoint(authenticationEntryPoint())
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
                .antMatchers("/users/{username}/confirm/{token}")
                .antMatchers("/resources/{uuId}")
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val auth = DaoAuthenticationProvider()
        auth.isHideUserNotFoundExceptions = false
        auth.setUserDetailsService(userDetailsServiceImpl)
        auth.setPasswordEncoder(passwordEncoder())
        return auth
    }

    @Bean
    fun authenticationEntryPoint(): CustomBasicAuthenticationEntryPoint = CustomBasicAuthenticationEntryPoint()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}