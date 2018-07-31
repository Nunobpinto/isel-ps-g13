package isel.leic.ps.eduWikiAPI.exceptions

data class UnconfirmedException (
        val msg: String,
        val action: String
) : RuntimeException(msg)