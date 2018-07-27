package isel.leic.ps.eduWikiAPI.configuration.security.authorization

import isel.leic.ps.eduWikiAPI.repository.ReputationDAOJdbi
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.access.expression.SecurityExpressionHandler
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.FilterInvocation

@Component
class AuthorizationConfig {

    @Autowired
    lateinit var jdbi: Jdbi

    fun configureRequestAuthorizations(http: HttpSecurity) {
        // Define role hierarchy
        http.authorizeRequests().expressionHandler(webExpressionHandler())
        // Match roles to URIs
        jdbi.useExtension<ReputationDAOJdbi, Exception>(ReputationDAOJdbi::class.java) {
            it.getReputationMatchers().forEach { http.authorizeRequests().antMatchers(it.UriMatch).hasRole(it.reputationId.removePrefix("ROLE_")) }
        }
    }

    private fun webExpressionHandler(): SecurityExpressionHandler<FilterInvocation> {
        val defaultWebSecurityExpressionHandler = DefaultWebSecurityExpressionHandler()
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy())
        return defaultWebSecurityExpressionHandler
    }

    @Bean
    fun roleHierarchy(): RoleHierarchyImpl {
        val roleHierarchy = RoleHierarchyImpl()

        var roleHierarchyString = ""
        jdbi.useExtension<ReputationDAOJdbi, Exception>(ReputationDAOJdbi::class.java) {
            val roles = it.getAllReputationRoles()
            roles.forEachIndexed {index, reputationRole ->
                roleHierarchyString += reputationRole.reputationRoleId
                if( index < roles.size - 1 ) roleHierarchyString += " > "
            }
        }

        roleHierarchy.setHierarchy(roleHierarchyString)
        return roleHierarchy
    }

}