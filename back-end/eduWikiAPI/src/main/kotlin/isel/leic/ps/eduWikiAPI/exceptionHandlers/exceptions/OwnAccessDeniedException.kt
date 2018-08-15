package isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions

data class OwnAccessDeniedException (
        val msg: String,
        val action: String
) : RuntimeException(msg)