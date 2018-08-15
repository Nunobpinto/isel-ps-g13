package isel.leic.ps.eduWikiAPI.service.mailSender

import org.springframework.mail.SimpleMailMessage



interface EmailService {
    fun sendSimpleMessage(to: String,
                          subject: String,
                          text: String)

    fun sendSimpleMessageUsingTemplate(to: String,
                                       subject: String,
                                       template: SimpleMailMessage,
                                       vararg templateArgs: String)
}