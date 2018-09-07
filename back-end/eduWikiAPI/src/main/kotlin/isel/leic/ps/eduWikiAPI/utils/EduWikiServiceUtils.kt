package isel.leic.ps.eduWikiAPI.utils

import isel.leic.ps.eduWikiAPI.domain.enums.ActionType
import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.ForbiddenException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import java.security.SecureRandom
import java.util.regex.Pattern

/**
 * Throws ForbiddenException if the voter already voted or is voting on it's own resource
 */
fun resolveVote(voter: String, owner: String, pastActions: List<ActionLog>) {
    if(voter == owner)
        throw ForbiddenException("You can't vote on your own resources", "Try voting on a resource that's not created by you")
    if(pastActions.any { it.actionType == ActionType.VOTE_DOWN || it.actionType == ActionType.VOTE_UP })
        throw ForbiddenException("Can't vote more than once on same resource", "Try voting on something else")
}

/**
 * Throws ForbiddenException if admin is approving his own resource
 */
fun resolveApproval(admin: String, owner: String) {
    if(admin == owner) throw ForbiddenException("You can't approve your own stages/reports", "Since you're and admin, you can now create and update resources directly")
}

/**
 * Checks if email has an email format
 */
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

/**
 * Generates random string
 */
fun randomAlphaNumericString(len: Int = 10): String {
    val charPool = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val rnd = SecureRandom()

    val sb = StringBuilder(len)
    for(i in 0 until len)
        sb.append(charPool[rnd.nextInt(charPool.length)])

    return sb.toString()
}