package isel.leic.ps.eduWikiAPI.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
data class BadRequestException(
        val msg: String,
        val action: String
) : RuntimeException(msg)