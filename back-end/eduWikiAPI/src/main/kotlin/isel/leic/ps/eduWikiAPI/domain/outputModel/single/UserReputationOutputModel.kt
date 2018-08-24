package isel.leic.ps.eduWikiAPI.domain.outputModel.single

data class UserReputationOutputModel(
        val givenBy: String = "",
        val pointsGiven: Int = 0,
        val action: UserActionOutputModel
)