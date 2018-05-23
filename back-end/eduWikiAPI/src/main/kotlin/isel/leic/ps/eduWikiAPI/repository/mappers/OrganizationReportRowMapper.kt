package isel.leic.ps.eduWikiAPI.repository.mappers

import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class OrganizationReportRowMapper : RowMapper<OrganizationReport> {

    override fun map(rs: ResultSet, ctx: StatementContext?) =
            OrganizationReport(
                    reportId = rs.getInt(OrganizationDAOImpl.ORG_REPORT_ID),
                    id = rs.getInt(OrganizationDAOImpl.ORG_ID),
                    fullName = rs.getString(OrganizationDAOImpl.ORG_FULL_NAME),
                    shortName = rs.getString(OrganizationDAOImpl.ORG_SHORT_NAME),
                    address = rs.getString(OrganizationDAOImpl.ORG_ADDRESS),
                    contact = rs.getString(OrganizationDAOImpl.ORG_CONTACT),
                    reportedBy = rs.getString(OrganizationDAOImpl.ORG_REPORTED_BY),
                    votes = rs.getInt(OrganizationDAOImpl.ORG_VOTE),
                    timestamp = rs.getTimestamp(OrganizationDAOImpl.ORG_TIMESTAMP)
            )
}