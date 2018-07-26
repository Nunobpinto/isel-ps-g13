package isel.leic.ps.eduWikiAPI.exceptionHandlers

import isel.leic.ps.eduWikiAPI.domain.outputModel.error.ErrorOutputModel
import isel.leic.ps.eduWikiAPI.exceptions.*
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(NoSuchElementException::class)])
    fun handleNoSuchElementException(
            ex: NoSuchElementException,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Not Found",
                        detail = ex.message ?: "Could not find the resource you wanted",
                        status = 404,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(value = [(OwnAccessDeniedException::class)])
    fun handleAccessDeniedException(
            ex: OwnAccessDeniedException,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Forbidden",
                        detail = ex.msg,
                        status = 403,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(value = [(NotFoundException::class)])
    fun handleNotFoundException(
            ex: NotFoundException,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Not Found",
                        detail = "Could not find the resource you wanted",
                        status = 404,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(BadRequestException::class)])
    fun handleBadRequestException(
            ex: BadRequestException,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Invalid Syntax",
                        detail = ex.msg,
                        status = 400,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(UnauthenticatedException::class)])
    fun handleUnauthenticatedException(
            ex: UnauthenticatedException,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Not Authenticated",
                        detail = "Authentication Required",
                        status = 401,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(value = [(ConflictException::class)])
    fun handleConflictException(
            ex: ConflictException,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Conflict",
                        detail = ex.msg,
                        status = 409,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.CONFLICT)
    }

    @ExceptionHandler(value = [(IllegalArgumentException::class)])
    fun handleIllegalArgumentException(
            ex: Exception,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Invalid Syntax",
                        detail = "Error in request parameters",
                        status = 400,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [(EmptyResultDataAccessException::class)])
    fun handleEmptyDataException(
            ex: Exception,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Not Found",
                        detail = "Could not find the resource you wanted",
                        status = 404,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.NOT_FOUND)
    }

}