package isel.leic.ps.eduWikiAPI.repository.mappers

import isel.leic.ps.eduWikiAPI.domain.model.version.OrganizationVersion
import isel.leic.ps.eduWikiAPI.repository.OrganizationDAOImpl
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class OrganizationVersionRowMapper : RowMapper<OrganizationVersion> {

    override fun map(rs: ResultSet, ctx: StatementContext) =
            OrganizationVersion(
                    organizationId = rs.getInt(OrganizationDAOImpl.ORG_ID),
                    version = rs.getInt(OrganizationDAOImpl.ORG_VERSION),
                    createdBy = rs.getString(OrganizationDAOImpl.ORG_CREATED_BY),
                    fullName = rs.getString(OrganizationDAOImpl.ORG_FULL_NAME),
                    shortName = rs.getString(OrganizationDAOImpl.ORG_SHORT_NAME),
                    address = rs.getString(OrganizationDAOImpl.ORG_ADDRESS),
                    contact = rs.getString(OrganizationDAOImpl.ORG_CONTACT),
                    timestamp = rs.getTimestamp(OrganizationDAOImpl.ORG_TIMESTAMP)
            )
}