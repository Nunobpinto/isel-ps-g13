package isel.leic.ps.eduWikiAPI.repository.mappers

import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class ProgrammeVersionRowMapper : RowMapper<ProgrammeVersion> {

    override fun map(rs: ResultSet, ctx: StatementContext) =
            ProgrammeVersion(
                    programmeId = rs.getInt(ProgrammeDAOImpl.PROG_ID),
                    version = rs.getInt(ProgrammeDAOImpl.PROG_VERSION),
                    createdBy = rs.getString(ProgrammeDAOImpl.PROG_CREATED_BY),
                    fullName = rs.getString(ProgrammeDAOImpl.PROG_FULL_NAME),
                    shortName = rs.getString(ProgrammeDAOImpl.PROG_SHORT_NAME),
                    academicDegree = rs.getString(ProgrammeDAOImpl.PROG_ACADEMIC_DEGREE),
                    totalCredits = rs.getInt(ProgrammeDAOImpl.PROG_TOTAL_CREDITS),
                    duration = rs.getInt(ProgrammeDAOImpl.PROG_DURATION),
                    timestamp = rs.getTimestamp(ProgrammeDAOImpl.PROG_TIMESTAMP)
            )
}