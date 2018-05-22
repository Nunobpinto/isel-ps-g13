package isel.leic.ps.eduWikiAPI.repository.mappers

import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class ProgrammeStageRowMapper : RowMapper<ProgrammeStage> {

    override fun map(rs: ResultSet, ctx: StatementContext) =
            ProgrammeStage(
                    programmeId = rs.getInt(ProgrammeDAOImpl.PROG_ID),
                    createdBy = rs.getString(ProgrammeDAOImpl.PROG_CREATED_BY),
                    fullName = rs.getString(ProgrammeDAOImpl.PROG_FULL_NAME),
                    shortName = rs.getString(ProgrammeDAOImpl.PROG_SHORT_NAME),
                    academicDegree = rs.getString(ProgrammeDAOImpl.PROG_ACADEMIC_DEGREE),
                    totalCredits = rs.getInt(ProgrammeDAOImpl.PROG_TOTAL_CREDITS),
                    duration = rs.getInt(ProgrammeDAOImpl.PROG_DURATION),
                    timestamp = rs.getTimestamp(ProgrammeDAOImpl.PROG_TIMESTAMP)
            )
}