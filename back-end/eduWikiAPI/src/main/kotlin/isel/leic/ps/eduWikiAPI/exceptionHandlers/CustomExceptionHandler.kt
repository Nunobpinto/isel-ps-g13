package isel.leic.ps.eduWikiAPI.exceptionHandlers

import isel.leic.ps.eduWikiAPI.domain.outputModel.error.ErrorOutputModel
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.*
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Catches exceptions thrown in Service layer and throws Problem+Json errors
 */
@RestControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(NotFoundException::class)])
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = ex.title,
                        detail = ex.detail,
                        status = 404,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(ForbiddenException::class)])
    fun handleAccessDeniedException(
            ex: ForbiddenException,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = ex.title,
                        detail = ex.detail,
                        status = 403,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.FORBIDDEN)
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
                        title = ex.title,
                        detail = ex.detail,
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
                        title = ex.title,
                        detail = ex.detail,
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
                        title = ex.title,
                        detail = ex.detail,
                        status = 409,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.CONFLICT)
    }

    @ExceptionHandler(value = [(UnconfirmedException::class)])
    fun handleUnconfirmedaException(
            ex: UnconfirmedException,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = ex.title,
                        detail = ex.detail,
                        status = 401,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(UnknownDataException::class)])
    fun handleUnknownDataException(
            ex: UnknownDataException,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = ex.title,
                        detail = ex.detail,
                        status = 500,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(value = [(ExceededValidationException::class)])
    fun handleExceededValidationException(
            ex: ExceededValidationException,
            request: WebRequest
    ): ResponseEntity<ErrorOutputModel> {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8
        return ResponseEntity(
                ErrorOutputModel(
                        title = "Exceeded Validation Token Date",
                        detail = "Exceeded Validation Token Date, please register again and confirm your email",
                        status = 401,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                ),
                httpHeaders,
                HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(value = [(NoSuchElementException::class), (EmptyResultDataAccessException::class)])
    fun handleNoSuchElementException(ex: RuntimeException): ResponseEntity<ErrorOutputModel> {
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

    @ExceptionHandler(value = [(IllegalArgumentException::class)])
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorOutputModel> {
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
}