package isel.leic.ps.eduWikiAPI.utils

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.ForbiddenException

fun resolveVote(voter: String, owner: String, pastActions: List<ActionLog>) {
    if(voter == owner)
        throw ForbiddenException("You can't vote on your own resources", "Try voting on a resource that's not created by you")
    if(pastActions.any { it.actionType == ActionType.VOTE_DOWN || it.actionType == ActionType.VOTE_UP })
        throw ForbiddenException("Can't vote more than once on same resource", "Try voting on something else")
}