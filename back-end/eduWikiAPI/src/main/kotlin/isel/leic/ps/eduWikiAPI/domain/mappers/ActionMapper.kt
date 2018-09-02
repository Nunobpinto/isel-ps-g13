package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserActionOutputModel

fun ActionLog.toUserActionOutputModel() = UserActionOutputModel (
        action_type = actionType.name,
        action_user = user,
        entity_type = entity,
        entity_link = "",
        timestamp = timestamp
)