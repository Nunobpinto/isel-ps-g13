package isel.leic.ps.eduWikiAPI.repository.mappers

import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class ProgrammeRowMapper : RowMapper<Programme> {

    override fun map(rs: ResultSet, ctx: StatementContext) =
            Programme(
                    id = rs.getInt(ProgrammeDAOImpl.PROG_ID),
                    version = rs.getInt(ProgrammeDAOImpl.PROG_VERSION),
                    votes = rs.getInt(ProgrammeDAOImpl.PROG_VOTE),
                    createdBy = rs.getString(ProgrammeDAOImpl.CREATED_BY),
                    fullName = rs.getString(ProgrammeDAOImpl.PROG_FULL_NAME),
                    shortName = rs.getString(ProgrammeDAOImpl.PROG_SHORT_NAME),
                    academicDegree = rs.getString(ProgrammeDAOImpl.PROG_ACADEMIC_DEGREE),
                    totalCredits = rs.getInt(ProgrammeDAOImpl.PROG_TOTAL_CREDITS),
                    duration = rs.getInt(ProgrammeDAOImpl.PROG_DURATION)
            )
}