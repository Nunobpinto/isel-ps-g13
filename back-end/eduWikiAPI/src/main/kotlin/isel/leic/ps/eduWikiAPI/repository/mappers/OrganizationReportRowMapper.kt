package isel.leic.ps.eduWikiAPI.repository.mappers

import isel.leic.ps.eduWikiAPI.domain.model.report.OrganizationReport
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class OrganizationReportRowMapper : RowMapper<OrganizationReport> {

    override fun map(rs: ResultSet, ctx: StatementContext?) =
            OrganizationReport(
                    rs.getInt(OrganizationDAOImpl.ORG_REPORT_ID),
                    rs.getInt(OrganizationDAOImpl.ORG_ID),
                    rs.getString(OrganizationDAOImpl.ORG_FULL_NAME),
                    rs.getString(OrganizationDAOImpl.ORG_SHORT_NAME),
                    rs.getString(OrganizationDAOImpl.ORG_ADDRESS),
                    rs.getInt(OrganizationDAOImpl.ORG_CONTACT),
                    rs.getString(OrganizationDAOImpl.CREATED_BY),
                    rs.getInt(OrganizationDAOImpl.ORG_VOTE)
                    )
}