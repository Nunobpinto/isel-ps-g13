package isel.leic.ps.eduWikiAPI.domain.outputModel.single

data class PendingTenantCreatorOutputModel(
        val username: String = "",
        val organizationEmail: String = "",
        val givenName: String = "",
        val familyName: String = "",
        val isPrincipal: Boolean = true
)