package isel.leic.ps.eduWikiAPI.configuration.security

import com.fasterxml.jackson.databind.ObjectMapper
import isel.leic.ps.eduWikiAPI.domain.outputModel.error.ErrorOutputModel
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType
import org.springframework.security.authentication.*
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * In case of authentication failure, reports error back to client,
 * in order to commence authentication again
 */
class CustomBasicAuthenticationEntryPoint : BasicAuthenticationEntryPoint() {

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        val content: String
        response.setHeader("Content-Type", MediaType.APPLICATION_PROBLEM_JSON_UTF8.toString())
        val mapper = ObjectMapper()
        when (authException) {
            is DisabledException -> {
                val error = ErrorOutputModel(
                        title = "Unconfirmed Account",
                        detail = "Please confirm your email",
                        status = UNAUTHORIZED.value(),
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                )
                content = mapper.writeValueAsString(error)
                response.status = UNAUTHORIZED.value()
            }
            is BadCredentialsException -> {
                val error = ErrorOutputModel(
                        title = "Invalid credentials",
                        detail = authException.message ?: "Please verify your credentials again",
                        status = BAD_REQUEST.value(),
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                )
                content = mapper.writeValueAsString(error)
                response.status = BAD_REQUEST.value()
            }
            is InsufficientAuthenticationException -> {
                response.addHeader("WWW-Authenticate", "Basic realm=\"$realmName\"")
                val error = ErrorOutputModel(
                        title = "Insufficient Authentication",
                        detail = "Please authenticate (see header Authorization)",
                        status = UNAUTHORIZED.value(),
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                )
                content = mapper.writeValueAsString(error)
                response.status = UNAUTHORIZED.value()
            }
            is UsernameNotFoundException -> {
                val error = ErrorOutputModel(
                        title = "Could not find user",
                        detail = authException.message!!,
                        status = UNAUTHORIZED.value(),
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                )
                content = mapper.writeValueAsString(error)
                response.status = UNAUTHORIZED.value()
            }
            is LockedException -> {
                val error = ErrorOutputModel(
                        title = "The account is banned",
                        detail = authException.message!!,
                        status = UNAUTHORIZED.value(),
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                )
                content = mapper.writeValueAsString(error)
                response.status = UNAUTHORIZED.value()
            }
            else -> {
                response.sendError(INTERNAL_SERVER_ERROR.value(), authException.message)
                return
            }
        }
        response.writer.write(content)
        response.writer.flush()
        response.writer.close()

    }

    override fun afterPropertiesSet() {
        realmName = SecurityConfig.REALM
        super.afterPropertiesSet()
    }
}