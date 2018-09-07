package isel.leic.ps.eduWikiAPI.service.eduWikiService

import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.TenantRequestDetails
import isel.leic.ps.eduWikiAPI.domain.mappers.*
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.RegisteredUser
import isel.leic.ps.eduWikiAPI.domain.model.Reputation
import isel.leic.ps.eduWikiAPI.domain.model.User
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.PendingTenantDetailsCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.TenantDetailsCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.PendingTenantDetailsOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TenantDetailsOutputModel
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.BadRequestException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.ConflictException
import isel.leic.ps.eduWikiAPI.exceptionHandlers.exceptions.NotFoundException
import isel.leic.ps.eduWikiAPI.repository.interfaces.TenantDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.UserDAO
import isel.leic.ps.eduWikiAPI.service.eduWikiService.interfaces.TenantService
import isel.leic.ps.eduWikiAPI.service.mailSender.EmailService
import isel.leic.ps.eduWikiAPI.utils.randomAlphaNumericString
import isel.leic.ps.eduWikiAPI.utils.isEmailValid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import org.postgresql.util.PSQLException
import org.springframework.security.crypto.password.PasswordEncoder
import java.sql.Timestamp
import java.time.LocalDateTime


@Transactional
@Service
class TenantServiceImpl : TenantService {

    companion object {
        const val REQUIRED_REQUEST_COUNT = 3
    }

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    lateinit var tenantDAO: TenantDAO
    @Autowired
    lateinit var userDAO: UserDAO
    @Autowired
    lateinit var emailService: EmailService

    override fun findTenantById(tenantId: String): TenantDetailsOutputModel =
            toTenantDetailsOutputModel(tenantDAO.findActiveTenantById(tenantId).orElseThrow { NotFoundException("Tenant not found", "Try a valid tenant id") })

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
            if(! isEmailValid(it.email) || ! it.email.contains(requestDetails.emailPattern))
                throw BadRequestException("Bad email", "${it.givenName} ${it.familyName}'s email is not valid. Either it does not match the email pattern provided or it's not a valid email")
        }
        // Check if there's any repeated emails
        if(requestDetails.requesters.map { it.email }.toSet().size != requestDetails.requesters.size)
            throw BadRequestException("Repeated emails", "Some users seem to have the same email, make sure each one is unique")
        // Check if number of requesters is in fact the required count
        if(requestDetails.requesters.size != REQUIRED_REQUEST_COUNT)
            throw BadRequestException("Not enough requesters", "The required number of requesters to make an EduWiki request is $REQUIRED_REQUEST_COUNT")

        // Register request in database
        val pendingTenant = try {
            tenantDAO.createPendingTenant(tenantRequestDetailsToPendingTenantDetails(requestDetails))
        } catch(e: Exception) {
            // If cause was conflict, it means that some fields match those of other tenants
            if(e.cause is PSQLException && (e.cause as PSQLException).sqlState == "23505") {
                // return an HTTP 409
                throw ConflictException("There's a similar tenant already pending approval", "Please check the already pending tenants")
            }
            throw e
        }
        val creators = tenantDAO.bulkCreatePendingTenantCreators(requestDetails.requesters.map { tenantRequestDetailsToPendingTenantCreator(it, pendingTenant.tenantUuid) })

        // Notify requesters
        creators.forEach { emailService.sendTenantRegistrationEmail(it) }
        // Notify developers
        userDAO.getDevs().forEach { emailService.sendTenantRegistrationEmailToDev(it, pendingTenant, creators) }
        return toPendingTenantDetailsOutputModel(pendingTenant, creators)
    }

    override fun realizePendingTenant(tenantUuid: String, principal: Principal): TenantDetailsOutputModel {
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
                contact = pendingTenant.contact,
                website = pendingTenant.website
        )
        val users = tenantCreators.map {
            User(
                    username = it.username,
                    password = randomAlphaNumericString(),
                    givenName = it.givenName,
                    familyName = it.familyName,
                    email = it.email
            )
        }
        val reputations = tenantCreators.map {
            Reputation(
                    username = it.username,
                    points = ROLE_ADMIN.maxPoints,
                    role = ROLE_ADMIN.name
            )
        }
        tenantDAO.populateTenant(
                pendingTenant.shortName,
                organization,
                users.map {
                    User(
                            username = it.username,
                            password = passwordEncoder.encode(it.password),
                            givenName = it.givenName,
                            familyName = it.familyName,
                            email = it.email
                    )
                },
                reputations
        )

        // Delete pending tenant and its creators from pending tables
        tenantDAO.deletePendingTenantById(tenantUuid)
        // Register tenant in tenant details
        val tenantDetails = tenantDAO.createActiveTenantEntry(principal.name, Timestamp.valueOf(LocalDateTime.now()), pendingTenant)
        // Register users in master tenant
        tenantDAO.bulkRegisterUser(tenantCreators.map { RegisteredUser(username = it.username, tenantUuid = pendingTenant.tenantUuid.toString(), confirmed = true) })

        // Notify founders that their tenant was approved
        users.forEach { emailService.sendTenantApprovedEmail(it, organization, tenantUuid) }

        return toTenantDetailsOutputModel(tenantDetails)
    }

    override fun rejectPendingTenant(tenantUuid: String, principal: Principal): PendingTenantDetailsOutputModel {
        val pendingTenant = tenantDAO.findPendingTenantById(tenantUuid)
                .orElseThrow { NotFoundException("No pending tenant found", "Are you sure the specified tenant exists?") }
        val pendingTenantCreators = tenantDAO.findPendingTenantCreatorsByTenantId(tenantUuid)

        // Delete pending tenant and its creators
        tenantDAO.deletePendingTenantById(tenantUuid)

        // Notify creators
        pendingTenantCreators.forEach { emailService.sendTenantRejectedEmail(it, pendingTenant) }
        return toPendingTenantDetailsOutputModel(pendingTenant, pendingTenantCreators)
    }
}