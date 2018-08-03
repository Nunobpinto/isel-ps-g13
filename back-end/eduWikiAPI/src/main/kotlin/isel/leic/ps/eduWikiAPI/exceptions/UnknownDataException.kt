package isel.leic.ps.eduWikiAPI.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
class UnknownDataException(val msg: String, val action: String): Exception(msg)