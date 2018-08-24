package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserActionOutputModel

data class UserActionCollectionOutputModel(
        val username: String = "",
        val actions: List<UserActionOutputModel> = emptyList()
)