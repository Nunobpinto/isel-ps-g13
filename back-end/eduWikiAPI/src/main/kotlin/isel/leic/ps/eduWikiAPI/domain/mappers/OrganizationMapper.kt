package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.domain.outputModel.OrganizationOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.OrganizationReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.OrganizationVersionOutputModel

fun toOrganization(organizationInputModel: OrganizationInputModel) = Organization(
        fullName = organizationInputModel.fullName,
        shortName = organizationInputModel.shortName,
        address = organizationInputModel.address,
        createdBy = organizationInputModel.createdBy,
        contact = organizationInputModel.contact
)

fun toOrganizationReport(organizationId: Int, input: OrganizationReportInputModel) = OrganizationReport(
        fullName = input.fullName,
        shortName = input.shortName,
        address = input.address,
        contact = input.contact,
        reportedBy = input.reportedBy,
        organizationId = organizationId
)

fun toOrganizationVersion(organization: Organization) = OrganizationVersion(
        organizationId = organization.organizationId,
        version = organization.version,
        createdBy = organization.createdBy,
        fullName = organization.fullName,
        shortName = organization.shortName,
        contact = organization.contact,
        address = organization.address,
        timestamp = organization.timestamp
)

fun toOrganizationOutputModel(organization: Organization) = OrganizationOutputModel(
        organizationId = organization.organizationId,
        version = organization.version,
        votes = organization.votes,
        contact = organization.contact,
        address = organization.address,
        fullName = organization.fullName,
        timestamp = organization.timestamp,
        username = organization.createdBy,
        shortName = organization.shortName
)

fun toOrganizationReportOutputModel(organizationReport: OrganizationReport) = OrganizationReportOutputModel(
        fullName = organizationReport.fullName,
        shortName = organizationReport.shortName,
        address = organizationReport.address,
        contact = organizationReport.contact,
        reportedBy = organizationReport.reportedBy,
        organizationId = organizationReport.organizationId,
        reportId = organizationReport.reportId,
        votes = organizationReport.votes
)

fun toOrganizationVersionOutputModel(organizationVersion: OrganizationVersion) = OrganizationVersionOutputModel(
        version = organizationVersion.version,
        organizationId = organizationVersion.organizationId,
        createdBy = organizationVersion.createdBy,
        fullName = organizationVersion.fullName,
        shortName = organizationVersion.shortName,
        address = organizationVersion.address,
        contact = organizationVersion.contact,
        timestamp = organizationVersion.timestamp
)