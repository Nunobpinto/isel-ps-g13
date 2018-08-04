package isel.leic.ps.eduWikiAPI.service

import isel.leic.ps.eduWikiAPI.domain.model.ValidationToken
import isel.leic.ps.eduWikiAPI.mailSender.EmailService
import isel.leic.ps.eduWikiAPI.repository.TokenDAOJdbi
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.*

@Component
class RegistrationListener {

    @Autowired
    lateinit var jdbi: Jdbi

    @Autowired
    lateinit var emailService: EmailService

    @Async
    @EventListener
    fun handleRegistrationEvent(event: OnRegistrationEvent) {
        val user = event.user
        val token = ValidationToken(token = UUID.randomUUID())
        jdbi.useExtension<TokenDAOJdbi, Exception>(TokenDAOJdbi::class.java) {
            it.saveToken(token)
            val message =
                    "Please follow this link to confirm your account "+
                            "" + "http://localhost:8080/users/" + user.username + "/confirm/" + token.token
            emailService.sendSimpleMessage(
                    to = user.organizationEmail,
                    subject = "Verify your Eduwiki account",
                    text = message
            )
        }
    }
}