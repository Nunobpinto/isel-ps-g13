package isel.leic.ps.eduWikiAPI.repository.mappers

import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class TermRowMapper : RowMapper<Term> {

    override fun map(rs: ResultSet, ctx: StatementContext) =
            Term(
                    id = rs.getInt(TermDAOImpl.TERM_ID),
                    shortName = rs.getString(TermDAOImpl.TERM_SHORT_NAME),
                    year = rs.getString(TermDAOImpl.TERM_YEAR),
                    type = rs.getString(TermDAOImpl.TERM_TYPE)
            )
}