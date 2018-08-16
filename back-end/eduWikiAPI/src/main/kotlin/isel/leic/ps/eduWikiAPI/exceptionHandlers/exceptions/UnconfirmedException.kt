package isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions

data class UnconfirmedException (
        val title: String,
        val detail: String
) : RuntimeException(title)