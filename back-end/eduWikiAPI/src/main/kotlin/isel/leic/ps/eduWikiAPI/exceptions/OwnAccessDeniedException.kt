package isel.leic.ps.eduWikiAPI.exceptions

data class OwnAccessDeniedException (
        val msg: String,
        val action: String
) : RuntimeException(msg)