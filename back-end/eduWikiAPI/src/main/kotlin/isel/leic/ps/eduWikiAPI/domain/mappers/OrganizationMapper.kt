package isel.leic.ps.eduWikiAPI.domain.mappers

import isel.leic.ps.eduWikiAPI.domain.inputModel.OrganizationInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.OrganizationReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion

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