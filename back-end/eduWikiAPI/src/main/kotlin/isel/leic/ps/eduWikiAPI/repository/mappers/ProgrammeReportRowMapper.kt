package isel.leic.ps.eduWikiAPI.repository.mappers

import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.repository.ProgrammeDAOImpl
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

class ProgrammeReportRowMapper : RowMapper<ProgrammeReport> {

    override fun map(rs: ResultSet, ctx: StatementContext) =
            ProgrammeReport(
                    reportId = rs.getInt(ProgrammeDAOImpl.PROG_REPORT_ID),
                    programmeId = rs.getInt(ProgrammeDAOImpl.PROG_VERSION),
                    votes = rs.getInt(ProgrammeDAOImpl.PROG_VOTE),
                    createdBy = rs.getString(ProgrammeDAOImpl.CREATED_BY),
                    programmeFullName = rs.getString(ProgrammeDAOImpl.PROG_FULL_NAME),
                    programmeShortName = rs.getString(ProgrammeDAOImpl.PROG_SHORT_NAME),
                    programmeAcademicDegree = rs.getString(ProgrammeDAOImpl.PROG_ACADEMIC_DEGREE),
                    programmeTotalCredits = rs.getInt(ProgrammeDAOImpl.PROG_TOTAL_CREDITS),
                    programmeDuration = rs.getInt(ProgrammeDAOImpl.PROG_DURATION)
            )
}