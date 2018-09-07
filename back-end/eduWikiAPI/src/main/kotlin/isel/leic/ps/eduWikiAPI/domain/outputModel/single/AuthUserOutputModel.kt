package isel.leic.ps.eduWikiAPI.domain.outputModel.single

data class AuthUserOutputModel (
        val username: String = "",
        val givenName: String = "",
        val familyName: String = "",
        val email: String = "",
        val reputation: ReputationOutputModel
)