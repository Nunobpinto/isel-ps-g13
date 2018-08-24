package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.TenantRequestDetails
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.Reputation
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.PendingTenantDetailsCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.TenantDetailsCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.PendingTenantDetailsOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TenantDetailsOutputModel
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.BadRequestException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.interfaces.TenantDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.UserDAO
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.TenantService
import isel.leic.ps.eduWikiAPI.service.mailSender.EmailService
import isel.leic.ps.eduWikiAPI.utils.isEmailValid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

@Transactional
@Service
class TenantServiceImpl : TenantService {

    companion object {
        const val REQUIRED_REQUEST_COUNT = 2
    }

    @Autowired
    lateinit var tenantDAO: TenantDAO
    @Autowired
    lateinit var userDAO: UserDAO
    @Autowired
    lateinit var emailService: EmailService

    override fun findTenantById(tenantId: String): TenantDetailsOutputModel =
            toTenantDetailsOutputModel(tenantDAO.findActiveTenatById(tenantId).orElseThrow { NotFoundException("Tenant not found", "Try a valid tenant id") })

    override fun getAllActiveTenants(): TenantDetailsCollectionOutputModel =
            toTenantDetailsCollectionOutputModel(tenantDAO.getAllActiveTenants())

    override fun findPendingTenantById(tenantUuid: String): PendingTenantDetailsOutputModel =
            toPendingTenantDetailsOutputModel(
                    tenantDAO.findPendingTenantById(tenantUuid).orElseThrow { NotFoundException("Pending tenant not found", "Try a valid tenant id") },
                    tenantDAO.findPendingTenantCreatorsByTenantId(tenantUuid)
            )

    override fun getAllPendingTenants(): PendingTenantDetailsCollectionOutputModel =
            toPendingTenantDetailsCollectionOutputModel(
                    tenantDAO.getAllPendingTenants().map {
                        toPendingTenantDetailsOutputModel(it, tenantDAO.findPendingTenantCreatorsByTenantId(it.tenantUuid.toString()))
                    }
            )

    override fun createPendingTenant(requestDetails: TenantRequestDetails): PendingTenantDetailsOutputModel {
        // Check if usernames are already picked
        requestDetails.requesters.forEach {
            tenantDAO.getRegisteredUserByUsername(it.username).ifPresent {
                throw BadRequestException("Username already exists", "The username ${it.username} already exists, pick another one")
            }
        }
        // Check if emails are on par with provided email pattern
        requestDetails.requesters.forEach {
            if(! isEmailValid(it.organizationEmail) || ! it.organizationEmail.contains(requestDetails.emailPattern))
                throw BadRequestException("Bad email", "${it.givenName} ${it.familyName}'s email is not valid. Either it does not match the email pattern provided or it's not a valid email")
        }
        // Check if there's any repeated emails
        if(requestDetails.requesters.map { it.organizationEmail }.toSet().size != requestDetails.requesters.size)
            throw BadRequestException("Repeated emails", "Some users seem to have the same email, make sure each one is unique")
        // Check if number of requesters is in fact the required count
        if(requestDetails.requesters.size != REQUIRED_REQUEST_COUNT)
            throw BadRequestException("Not enough requesters", "The required number of requesters to make an EduWiki request is $REQUIRED_REQUEST_COUNT")

        // Register request in database
        val pendingTenant = tenantDAO.createPendingTenant(tenantRequestDetailsToPendingTenantDetails(requestDetails))
        val creators = tenantDAO.bulkCreatePendingTenantCreators(requestDetails.requesters.map { tenantRequestDetailsToPendingTenantCreator(it, pendingTenant.tenantUuid) })

        // Notify requesters
        creators.forEach { emailService.sendTenantRegistrationEmail(it) }
        // Notify developers
        userDAO.getDevs().forEach { emailService.sendTenantRegistrationEmailToDev(it, pendingTenant, creators) }
        return toPendingTenantDetailsOutputModel(pendingTenant, creators)
    }

    override fun realizePendingTenant(tenantUuid: String, principal: Principal) {
        // Get tenant details to create
        val pendingTenant = tenantDAO.findPendingTenantById(tenantUuid)
                .orElseThrow { NotFoundException("Pending tenant not found", "Please provide a valid tenant id") }
        val tenantCreators = tenantDAO.findPendingTenantCreatorsByTenantId(tenantUuid)

        // Create schema
        tenantDAO.createTenantBasedOnPendingTenant(pendingTenant.shortName)
        // Fill schema
        val organization = Organization(
                fullName = pendingTenant.fullName,
                shortName = pendingTenant.shortName,
                address = pendingTenant.address,
                website = pendingTenant.website
        )
        val usersAndRep = tenantCreators.map {
            User(
                    username = it.username,
                    password = "1234", //todo gen random password
                    givenName = it.givenName,
                    familyName = it.familyName,
                    organizationEmail = it.organizationEmail,
                    confirmed = true
            ) to Reputation(
                    username = it.username,
                    points = ROLE_ADMIN.maxPoints,
                    role = ROLE_ADMIN.name
            )
        }
        tenantDAO.populateTenant(pendingTenant.shortName, organization, usersAndRep)
    }

    override fun rejectPendingTenant(tenantUuid: String, principal: Principal) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}