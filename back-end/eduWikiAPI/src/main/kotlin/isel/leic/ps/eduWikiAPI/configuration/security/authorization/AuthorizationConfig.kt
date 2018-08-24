package isel.leic.ps.eduWikiAPI.configuration.security.authorization

import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole.*
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.access.expression.SecurityExpressionHandler
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.FilterInvocation

@Component
class AuthorizationConfig {

    companion object {
        // Defines which roles include include other roles
        val ROLE_HIERARCHY = "$ROLE_DEV > $ROLE_ADMIN > $ROLE_BEGINNER > $ROLE_UNCONFIRMED"
        // Defines access control for each resource in API
        val REPUTATION_MATCHERS = mapOf(
//                "/**" to ROLE_BEGINNER,
                "/tenants/pending/{tenantUuid}" to ROLE_DEV
        )
    }

    fun configureRequestAuthorizations(http: HttpSecurity) {
        // Define role hierarchy
        http.authorizeRequests().expressionHandler(webExpressionHandler())
        // Match roles to URIs
        REPUTATION_MATCHERS.forEach {
            http.authorizeRequests().antMatchers(it.key).hasRole(it.value.name.removePrefix("ROLE_"))
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
        roleHierarchy.setHierarchy(ROLE_HIERARCHY)
        return roleHierarchy
    }

}