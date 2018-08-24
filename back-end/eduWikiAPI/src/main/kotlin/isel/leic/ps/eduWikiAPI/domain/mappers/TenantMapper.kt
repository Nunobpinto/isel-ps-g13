package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.TenantRequestDetails
import isel.leic.ps.eduWikiAPI.domain.inputModel.TenantRequester
import isel.leic.ps.eduWikiAPI.domain.model.PendingTenantCreator
import isel.leic.ps.eduWikiAPI.domain.model.PendingTenantDetails
import isel.leic.ps.eduWikiAPI.domain.model.TenantDetails
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.PendingTenantDetailsCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.TenantDetailsCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.PendingTenantCreatorOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.PendingTenantDetailsOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.TenantDetailsOutputModel
import java.util.*

fun toTenantDetailsOutputModel(tenant: TenantDetails) = TenantDetailsOutputModel(
        tenantId = tenant.uuid,
        schemaName = tenant.schemaName,
        emailPattern = tenant.emailPattern,
        createdAt = tenant.createdAt
)

fun toTenantDetailsCollectionOutputModel(tenantList: List<TenantDetails>) = TenantDetailsCollectionOutputModel(
        tenantList = tenantList.map { toTenantDetailsOutputModel(it) }
)

fun tenantRequestDetailsToPendingTenantDetails(requestDetails: TenantRequestDetails) = PendingTenantDetails(
        fullName = requestDetails.fullName,
        shortName = requestDetails.shortName,
        address = requestDetails.address,
        contact = requestDetails.contact,
        website = requestDetails.website,
        emailPattern = requestDetails.emailPattern,
        orgSummary = requestDetails.organizationSummary
)

fun tenantRequestDetailsToPendingTenantCreator(requestDetails: TenantRequester, pendingTenantUuid: UUID) = PendingTenantCreator(
        username = requestDetails.username,
        organizationEmail = requestDetails.organizationEmail,
        pendingTenantUuid = pendingTenantUuid,
        givenName = requestDetails.givenName,
        familyName = requestDetails.familyName,
        principal = requestDetails.principal
)

fun toPendingTenantDetailsOutputModel(pendingTenantDetails: PendingTenantDetails, findPendingTenantCreatorsByTenantId: List<PendingTenantCreator>) = PendingTenantDetailsOutputModel(
        tenantUuid = pendingTenantDetails.tenantUuid.toString(),
        fullName = pendingTenantDetails.fullName,
        shortName = pendingTenantDetails.shortName,
        address = pendingTenantDetails.address,
        contact = pendingTenantDetails.contact,
        website = pendingTenantDetails.website,
        emailPattern = pendingTenantDetails.emailPattern,
        orgSummary = pendingTenantDetails.orgSummary,
        timestamp = pendingTenantDetails.timestamp,
        creators = findPendingTenantCreatorsByTenantId.map { toPendingTenantCreatorOutputModel(it) }
)

fun toPendingTenantCreatorOutputModel(pendingCreator: PendingTenantCreator) = PendingTenantCreatorOutputModel(
        username = pendingCreator.username,
        organizationEmail = pendingCreator.organizationEmail,
        givenName = pendingCreator.givenName,
        familyName = pendingCreator.familyName,
        isPrincipal = pendingCreator.principal
)


fun toPendingTenantDetailsCollectionOutputModel(pendingTenantDetails: List<PendingTenantDetailsOutputModel>) = PendingTenantDetailsCollectionOutputModel(
        pendingTenantList = pendingTenantDetails
)