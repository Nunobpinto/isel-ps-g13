package isel.leic.ps.eduWikiAPI.mailSender

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.MailException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service


@Service
class EmailServiceImpl : EmailService {

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

    override fun sendSimpleMessageUsingTemplate(to: String,
                                                subject: String,
                                                template: SimpleMailMessage,
                                                vararg templateArgs: String) {
        val text = String.format(template.text!!, *templateArgs)
        sendSimpleMessage(to, subject, text)
    }


}