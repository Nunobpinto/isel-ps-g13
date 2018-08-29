package isel.leic.ps.eduWikiAPI.service.mailSender

import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.domain.model.PendingTenantCreator
import isel.leic.ps.eduWikiAPI.domain.model.PendingTenantDetails
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
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

    fun sendUserReportedEmailToAdmin(report: UserReport, admin: User)

    fun sendUserBannedEmail(reportedUser: User, report: UserReport)

    fun sendTenantRejectedEmail(creator: PendingTenantCreator, pendingTenant: PendingTenantDetails)
}