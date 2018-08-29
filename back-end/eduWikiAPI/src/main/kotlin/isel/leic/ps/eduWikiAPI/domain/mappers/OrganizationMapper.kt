package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.ActionLog
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.OrganizationCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.OrganizationOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.OrganizationReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.OrganizationVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.UserActionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.OrganizationReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.OrganizationVersionOutputModel

fun toOrganizationReport(input: OrganizationReportInputModel, principal: String) = OrganizationReport(
        fullName = input.fullName,
        shortName = input.shortName,
        address = input.address,
        contact = input.contact,
        website = input.website,
        reportedBy = principal
)

fun toOrganizationVersion(username: String, organization: Organization) = OrganizationVersion(
        version = organization.version,
        fullName = organization.fullName,
        shortName = organization.shortName,
        contact = organization.contact,
        address = organization.address,
        website = organization.website,
        timestamp = organization.timestamp,
        createdBy = username
)

fun toOrganizationOutputModel(organization: Organization) = OrganizationOutputModel(
        version = organization.version,
        contact = organization.contact,
        address = organization.address,
        shortName = organization.shortName,
        fullName = organization.fullName,
        website = organization.website,
        timestamp = organization.timestamp
)

fun toOrganizationReportOutputModel(organizationReport: OrganizationReport) = OrganizationReportOutputModel(
        fullName = organizationReport.fullName,
        shortName = organizationReport.shortName,
        address = organizationReport.address,
        contact = organizationReport.contact,
        website = organizationReport.website,
        reportedBy = organizationReport.reportedBy,
        reportId = organizationReport.reportId,
        votes = organizationReport.votes
)

fun toOrganizationVersionOutputModel(organizationVersion: OrganizationVersion) = OrganizationVersionOutputModel(
        version = organizationVersion.version,
        createdBy = organizationVersion.createdBy,
        fullName = organizationVersion.fullName,
        shortName = organizationVersion.shortName,
        address = organizationVersion.address,
        contact = organizationVersion.contact,
        website = organizationVersion.website,
        timestamp = organizationVersion.timestamp
)

fun toOrganizationCollectionOutputModel(organizationList: List<OrganizationOutputModel>) = OrganizationCollectionOutputModel(
        organizationList = organizationList
)

fun toOrganizationReportCollectionOutputModel(organizationReportList: List<OrganizationReportOutputModel>) = OrganizationReportCollectionOutputModel(
        organizationReportList = organizationReportList
)

fun toOrganizationVersionCollectionOutputModel(organizationVersionList: List<OrganizationVersionOutputModel>) = OrganizationVersionCollectionOutputModel(
        organizationVersionList = organizationVersionList
)

fun Organization.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "organization",
        timestamp = actionLog.timestamp
)

fun OrganizationReport.toUserActionOutputModel(actionLog: ActionLog) = UserActionOutputModel(
        action_type = actionLog.actionType.name,
        action_user = actionLog.user,
        entity_type = actionLog.entity,
        entity_link = "organization/reports/$reportId",
        timestamp = actionLog.timestamp
)