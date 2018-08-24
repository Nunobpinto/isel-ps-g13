package isel.leic.ps.eduWikiAPI.utils

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.ForbiddenException
import java.util.regex.Pattern

fun resolveVote(voter: String, owner: String, pastActions: List<ActionLog>) {
    if(voter == owner)
        throw ForbiddenException("You can't vote on your own resources", "Try voting on a resource that's not created by you")
    if(pastActions.any { it.actionType == ActionType.VOTE_DOWN || it.actionType == ActionType.VOTE_UP })
        throw ForbiddenException("Can't vote more than once on same resource", "Try voting on something else")
}

fun resolveApproval(admin: String, owner: String) {
    if(admin == owner) throw ForbiddenException("You can't approve your own stages/reports", "Since you're and admin, you can now create and update resources directly")
}

fun isEmailValid(email: String?): Boolean {
    return email != null && Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(email).matches()
}