package isel.leic.ps.eduWikiAPI.configuration.security

import com.fasterxml.jackson.databind.ObjectMapper
import isel.leic.ps.eduWikiAPI.domain.outputModel.error.ErrorOutputModel
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomBasicAuthenticationEntryPoint : BasicAuthenticationEntryPoint() {
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        var content = ""
        response.setHeader("Content-Type", MediaType.APPLICATION_PROBLEM_JSON_UTF8.toString())
        val mapper = ObjectMapper()
        when (authException) {
            is DisabledException -> {
                val error = ErrorOutputModel(
                        title = "Unconfirmed Account",
                        detail = "Please confirm your organization email",
                        status = 401,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                )
                content = mapper.writeValueAsString(error)
                response.status = 401
            }
            is BadCredentialsException -> {
                val error = ErrorOutputModel(
                        title = "Invalid credentials",
                        detail = "Please verify your credentials again",
                        status = 400,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                )
                content = mapper.writeValueAsString(error)
                response.status = 400
            }
            is InsufficientAuthenticationException -> {
                response.addHeader("WWW-Authenticate", "Basic realm=\"$realmName\"")
                val error = ErrorOutputModel(
                        title = "Insufficient Authentication",
                        detail = "Please authenticate (see header Authorization",
                        status = 401,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                )
                content = mapper.writeValueAsString(error)
                response.status = 401
            }
            is UsernameNotFoundException -> {
                val error = ErrorOutputModel(
                        title = "No user",
                        detail = authException.message!!,
                        status = 401,
                        type = "https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
                )
                content = mapper.writeValueAsString(error)
                response.status = 401
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