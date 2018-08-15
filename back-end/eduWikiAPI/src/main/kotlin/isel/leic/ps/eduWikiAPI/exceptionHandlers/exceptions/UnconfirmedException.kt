package isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions

data class UnconfirmedException (
        val msg: String,
        val action: String
) : RuntimeException(msg)