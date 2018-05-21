package isel.leic.ps.eduWikiAPI.repository.mappers

import isel.leic.ps.eduWikiAPI.domain.model.Organization
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class OrganizationRowMapper : RowMapper<Organization> {

    override fun map(rs: ResultSet, ctx: StatementContext) =
            Organization(
                    rs.getInt(OrganizationDAOImpl.ORG_ID),
                    rs.getInt(OrganizationDAOImpl.ORG_VERSION),
                    rs.getString(OrganizationDAOImpl.ORG_CREATED_BY),
                    rs.getString(OrganizationDAOImpl.ORG_FULL_NAME),
                    rs.getString(OrganizationDAOImpl.ORG_SHORT_NAME),
                    rs.getString(OrganizationDAOImpl.ORG_ADDRESS),
                    rs.getString(OrganizationDAOImpl.ORG_CONTACT)
            )
}