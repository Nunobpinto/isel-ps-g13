package isel.leic.ps.eduWikiAPI.repository.mappers

import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class OrganizationVersionRowMapper : RowMapper<OrganizationVersion> {

    override fun map(rs: ResultSet, ctx: StatementContext) =
            OrganizationVersion(
                    rs.getInt(OrganizationDAOImpl.ORG_ID),
                    rs.getInt(OrganizationDAOImpl.ORG_VERSION),
                    rs.getString(OrganizationDAOImpl.CREATED_BY),
                    rs.getString(OrganizationDAOImpl.ORG_FULL_NAME),
                    rs.getString(OrganizationDAOImpl.ORG_SHORT_NAME),
                    rs.getString(OrganizationDAOImpl.ORG_ADDRESS),
                    rs.getInt(OrganizationDAOImpl.ORG_CONTACT),
                    rs.getTimestamp(OrganizationDAOImpl.ORG_TIMESTAMP)
            )
}