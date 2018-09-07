package isel.leic.ps.eduWikiAPI.service.mailSender

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
import org.springframework.mail.SimpleMailMessage

interface EmailService {

    fun sendConfirmAccountEmail(user: User, token: ValidationToken)

    fun sendTenantRegistrationEmail(to: PendingTenantCreator)

    fun sendTenantRegistrationEmailToDev(developer: User, pendingTenant: PendingTenantDetails, creators: List<PendingTenantCreator>)

    fun sendUserReportedEmailToAdmin(report: UserReport, admin: User)

    fun sendUserBannedEmail(reportedUser: User, report: UserReport)

    fun sendTenantRejectedEmail(creator: PendingTenantCreator, pendingTenant: PendingTenantDetails)

    fun sendTenantApprovedEmail(user: User, organization: Organization, tenantUuid: String)
}