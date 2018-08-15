package isel.leic.ps.eduWikiAPI.configuration.security.authorization

import org.springframework.security.core.GrantedAuthority

enum class ReputationRole(
        val hierarchyLevel: Int,
        val maxPoints: Int,
        val minPoints: Int
) : GrantedAuthority {
    ROLE_ADMIN(2, 100, 51) {
        override fun getAuthority(): String = name
    },
    ROLE_BEGINNER(1, 50, 1) {
        override fun getAuthority(): String = name
    },
    ROLE_UNCONFIRMED(-1, -1, -1) {
        override fun getAuthority(): String = name
    }
}