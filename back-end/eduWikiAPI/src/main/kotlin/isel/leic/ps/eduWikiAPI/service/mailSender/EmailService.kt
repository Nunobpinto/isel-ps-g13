package isel.leic.ps.eduWikiAPI.service.mailSender

import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.domain.model.PendingTenantCreator
import isel.leic.ps.eduWikiAPI.domain.model.PendingTenantDetails
import org.springframework.mail.SimpleMailMessage



interface EmailService {
    fun sendSimpleMessage(to: String,
                          subject: String,
                          text: String)

    fun sendSimpleMessageUsingTemplate(to: String,
                                       subject: String,
                                       template: SimpleMailMessage,
                                       vararg templateArgs: String)

    fun sendTenantRegistrationEmail(to: PendingTenantCreator)

    fun sendTenantRegistrationEmailToDev(developer: User, pendingTenant: PendingTenantDetails, creators: List<PendingTenantCreator>)
}