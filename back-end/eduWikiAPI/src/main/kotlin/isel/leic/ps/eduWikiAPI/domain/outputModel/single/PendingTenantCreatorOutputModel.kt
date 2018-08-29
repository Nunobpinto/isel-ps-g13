package isel.leic.ps.eduWikiAPI.domain.outputModel.single

data class PendingTenantCreatorOutputModel(
        val username: String = "",
        val email: String = "",
        val givenName: String = "",
        val familyName: String = "",
        val isPrincipal: Boolean = true
)