package isel.leic.ps.eduWikiAPI.configuration.security.authorization

import org.springframework.security.core.GrantedAuthority

/**
 * The roles of the system
 */
enum class ReputationRole(
        val hierarchyLevel: Int,
        val maxPoints: Int,
        val minPoints: Int,
        val userRole: Boolean
) : GrantedAuthority {
    ROLE_ADMIN(2, 100, 51, true) {
        override fun getAuthority(): String = name
    },
    ROLE_BEGINNER(1, 50, 1, true) {
        override fun getAuthority(): String = name
    },
    ROLE_DEV(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, false) {
        override fun getAuthority(): String = name
    }
}