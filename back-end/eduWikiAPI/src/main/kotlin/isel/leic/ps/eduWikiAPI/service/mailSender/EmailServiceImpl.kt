package isel.leic.ps.eduWikiAPI.service.mailSender

import freemarker.template.Configuration
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.UserReport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.MimeMessageHelper
import java.nio.charset.StandardCharsets

@Service
class EmailServiceImpl : EmailService {

    @Autowired
    lateinit var emailSender: JavaMailSender
    @Autowired
    lateinit var freemarkerConfig: Configuration

    override fun sendConfirmAccountEmail(user: User, token: ValidationToken) {
        val message = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name())

        val html = FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfig.getTemplate("eduwiki-email-template.ftl"),
                mapOf(
                        "header" to "Thank you for registering!",
                        "content" to "Please follow this link to confirm your account:  <br /> " +
                                "http://localhost:8080/users/${user.username}/confirm/${token.token} <br /> " +
                                "If you didn't create an account on this system, simply ignore this email."
                )
        )
        helper.setTo(user.email)
        helper.setText(html, true)
        helper.setSubject("Confirm your account")

        emailSender.send(message)
    }

    override fun sendTenantRegistrationEmail(to: PendingTenantCreator) {
        val message = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name())

        val html = FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfig.getTemplate("eduwiki-email-template.ftl"),
                mapOf(
                        "header" to "Thank you ${to.givenName} ${to.familyName}, username ${to.username}, for requesting a tenant from EduWiki!",
                        "content" to "The EduWiki team is now evaluating your request. If we have any further questions we'll contact ${if(to.principal) "you" else "the user chosen by all of you"}. <br /> " +
                                "We'll just ask a few questions to make sure you're serious about using the system and give you some extra info about it. We'll speak again soon! <br /> " +
                                "Thank you, the EduWiki Team"
                )
        )
        helper.setTo(to.email)
        helper.setText(html, true)
        helper.setSubject("Your EduWiki tenant request was received")

        emailSender.send(message)
    }

    override fun sendTenantRegistrationEmailToDev(developer: User, pendingTenant: PendingTenantDetails, creators: List<PendingTenantCreator>) {
        val message = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name())

        val usersText = StringBuilder()
        for(user in creators) {
            usersText.append("${if(user.principal) "Principal" else "User"} - <br /> ")
            usersText.append("<b>Given Name</b>: ${user.givenName} <br /> ")
            usersText.append("<b>Family Name</b>: ${user.givenName} <br /> ")
            usersText.append("<b>Username</b>: ${user.givenName} <br /> ")
            usersText.append("<b>Email</b>: ${user.email} <br /> ")
        }

        val html = FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfig.getTemplate("eduwiki-email-template.ftl"),
                mapOf(
                        "header" to "Tenant requested!",
                        "content" to "A tenant was requested, the organization's details are:  <br /> " +
                                "<b>Name</b>: ${pendingTenant.fullName} <br /> " +
                                "<b>Acronym</b>: ${pendingTenant.shortName} <br /> " +
                                "<b>Address</b>: ${pendingTenant.address} <br /> " +
                                "<b>Contact</b>: ${pendingTenant.contact} <br /> " +
                                "<b>Email pattern</b>: ${pendingTenant.emailPattern} <br /> " +
                                "<b>Website</b>: ${pendingTenant.website} <br /> " +
                                "<b>Summary</b>: ${pendingTenant.orgSummary} <br /> " +
                                "The users that made the request are: <br /> " +
                                usersText.toString()
                )
        )
        helper.setTo(developer.email)
        helper.setText(html, true)
        helper.setSubject("A tenant was requested")

        emailSender.send(message)
    }

    override fun sendUserReportedEmailToAdmin(report: UserReport, admin: User) {
        val message = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name())

        val html = FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfig.getTemplate("eduwiki-email-template.ftl"),
                mapOf(
                        "header" to "A user has been reported",
                        "content" to "The user ${report.username} has been reported by ${report.reportedBy}  <br /> " +
                                "The reason behind the report was: <br /> " +
                                "--BEGIN REPORT REASON--<br /> " +
                                "${report.reason}<br /> " +
                                "--END REPORT REASON--<br /> " +
                                "Evaluate this case with the other admins and take a decision."
                )
        )
        helper.setTo(admin.email)
        helper.setText(html, true)
        helper.setSubject("User on your tenant was reported")

        emailSender.send(message)
    }

    override fun sendUserBannedEmail(reportedUser: User, report: UserReport) {
        val message = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name())

        val html = FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfig.getTemplate("eduwiki-email-template.ftl"),
                mapOf(
                        "header" to "You have been banned from this system!",
                        "content" to "We're sorry to inform you but the admins of your tenant have decided to ban you.<br /> " +
                                "The report, created at ${report.timestamp}, that led to this decision was: <br /> " +
                                "--BEGIN REPORT REASON--<br /> " +
                                "${report.reason}<br /> " +
                                "--END REPORT REASON--<br /> "
                )
        )
        helper.setTo(reportedUser.email)
        helper.setText(html, true)
        helper.setSubject("You have been banned")

        emailSender.send(message)
    }

    override fun sendTenantRejectedEmail(creator: PendingTenantCreator, pendingTenant: PendingTenantDetails) {
        val message = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name())

        val html = FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfig.getTemplate("eduwiki-email-template.ftl"),
                mapOf(
                        "header" to "Your request was rejected",
                        "content" to "We're sorry to inform you that after careful consideration, we decided that your request had to be rejected. <br /> " +
                                "Either your request was fake (if that's the case shame on you), or we decided that your request was not acceptable for some reason. <br /> " +
                                "We're sorry, we hope to see you again, <br /> " +
                                "The EduWiki Team"
                )
        )
        helper.setTo(creator.email)
        helper.setText(html, true)
        helper.setSubject("Your tenant request was rejected :(")

        emailSender.send(message)
    }

    override fun sendTenantApprovedEmail(user: User, organization: Organization, tenantUuid: String) {
        val message = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name())

        val html = FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfig.getTemplate("eduwiki-email-template.ftl"),
                mapOf(
                        "header" to "Your request was accepted! Hurray!",
                        "content" to "We're glad to inform you that your request was accepted! You can start using EduWiki right away! <br /> " +
                                "Your username and password are: <br /> " +
                                "${user.username} <br /> " +
                                "${user.password} <br /> " +
                                "Besides using our web and mobile applications, you can also create your own client applications or use our API directly to interact with EduWiki! <br /> " +
                                "Your tenant identifier is: $tenantUuid <br /> " +
                                "We hope you enjoy our system, <br /> " +
                                "The EduWiki Team"
                )
        )
        helper.setTo(user.email)
        helper.setText(html, true)
        helper.setSubject("Your tenant request was accepted!")

        emailSender.send(message)
    }

}