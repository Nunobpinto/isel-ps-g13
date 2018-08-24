package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserReputationOutputModel

data class UserReputationCollectionOutputModel(
        val reputationChanges: List<UserReputationOutputModel>
)
