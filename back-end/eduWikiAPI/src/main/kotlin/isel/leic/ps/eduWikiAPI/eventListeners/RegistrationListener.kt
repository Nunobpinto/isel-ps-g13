package isel.leic.ps.eduWikiAPI.eventListeners

import isel.leic.ps.eduWikiAPI.domain.model.ValidationToken
import isel.leic.ps.eduWikiAPI.mailSender.EmailService
import isel.leic.ps.eduWikiAPI.repository.TokenDAOImpl
import isel.leic.ps.eduWikiAPI.eventListeners.events.OnRegistrationEvent
import isel.leic.ps.eduWikiAPI.repository.interfaces.TokenDAO
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener
import java.util.*

@Component
class RegistrationListener {

    @Autowired
    lateinit var tokenDAO: TokenDAO
    @Autowired
    lateinit var emailService: EmailService

    @Transactional
    @TransactionalEventListener
    fun handleRegistrationEvent(event: OnRegistrationEvent) {
        val user = event.user
        val token = ValidationToken(token = UUID.randomUUID())
        tokenDAO.saveToken(token)
        val message =
                "Please follow this link to confirm your account " +
                        "" + "http://localhost:8080/users/" + user.username + "/confirm/" + token.token
        emailService.sendSimpleMessage(
                to = user.organizationEmail,
                subject = "Verify your Eduwiki account",
                text = message
        )
    }
}