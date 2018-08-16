package isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value =  HttpStatus.FORBIDDEN)
data class ForbiddenException (
        val title: String,
        val detail: String
) : RuntimeException(title)