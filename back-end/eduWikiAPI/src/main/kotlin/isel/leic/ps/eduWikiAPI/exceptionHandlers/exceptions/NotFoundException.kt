package isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value =  HttpStatus.NOT_FOUND)
data class NotFoundException(
        val msg: String,
        val action: String
) : RuntimeException(msg)