package isel.leic.ps.eduWikiAPI.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
data class UnauthenticatedException(
        val msg: String,
        val action: String
) : RuntimeException(msg)