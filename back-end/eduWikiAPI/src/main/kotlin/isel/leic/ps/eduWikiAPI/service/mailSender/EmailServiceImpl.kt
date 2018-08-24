package isel.leic.ps.eduWikiAPI.service.mailSender

import isel.leic.ps.eduWikiAPI.domain.model.PendingTenantCreator
import isel.leic.ps.eduWikiAPI.domain.model.PendingTenantDetails
import isel.leic.ps.eduWikiAPI.domain.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.MailException
import org.springframework.stereotype.Service


@Service
class EmailServiceImpl : EmailService {

    companion object {
        const val TENANT_REGISTRATION_SUBJECT = "Your EduWiki tenant request was received"
    }

    @Autowired
    lateinit var emailSender: JavaMailSender

    override fun sendSimpleMessage(to: String, subject: String, text: String) {
        try {
            val message = SimpleMailMessage()
            message.setTo(to)
            message.setSubject(subject)
            message.setText(text)
            emailSender.send(message)
        } catch (exception: MailException) {
            exception.printStackTrace()
        }

    }

    override fun sendSimpleMessageUsingTemplate(to: String, subject: String, template: SimpleMailMessage, vararg templateArgs: String) {
        val text = String.format(template.text!!, *templateArgs)
        sendSimpleMessage(to, subject, text)
    }

    override fun sendTenantRegistrationEmail(to: PendingTenantCreator) {
        val message = SimpleMailMessage()
        val mailText = "Thank you ${to.givenName} ${to.familyName} for requesting a tenant from EduWiki! \n" +
                "The EduWiki team is now evaluating your request. If we have any further questions we'll contact " +
                "${if(to.principal) "you" else "the user chosen by all of you"}. "
        message.setTo(to.organizationEmail)
        message.setSubject(TENANT_REGISTRATION_SUBJECT)
        message.setText(mailText)
        emailSender.send(message)
    }

    override fun sendTenantRegistrationEmailToDev(developer: User, pendingTenant: PendingTenantDetails, creators: List<PendingTenantCreator>) {
        val message = SimpleMailMessage()
        message.setTo(developer.personalEmail)
        message.setSubject("test")
        message.setText("test")
        emailSender.send(message)    }

}