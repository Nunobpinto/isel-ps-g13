package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import java.util.*

interface ProgrammeDAOJdbi : ProgrammeDAO {

    companion object {
        //TABLE NAMES
        const val PROGRAMME_TABLE = "programme"
        const val PROGRAMME_VERSION_TABLE = "programme_version"
        const val PROGRAMME_REPORT_TABLE = "programme_report"
        const val PROGRAMME_STAGE_TABLE = "programme_stage"
        // FIELDS
        const val PROGRAMME_STAGE_ID = "programme_stage_id"
        const val PROGRAMME_ID = "programme_id"
        const val PROGRAMME_VERSION = "programme_version"
        const val PROGRAMME_FULL_NAME = "programme_full_name"
        const val PROGRAMME_SHORT_NAME = "programme_short_name"
        const val PROGRAMME_ACADEMIC_DEGREE = "programme_academic_degree"
        const val PROGRAMME_TOTAL_CREDITS = "programme_total_credits"
        const val PROGRAMME_DURATION = "programme_duration"
        const val PROGRAMME_VOTES = "votes"
        const val PROGRAMME_TIMESTAMP = "time_stamp"
        const val PROGRAMME_REPORT_ID = "programme_report_id"
        const val PROGRAMME_CREATED_BY = "created_by"
        const val PROGRAMME_REPORTED_BY = "reported_by"
    }

    @SqlQuery("SELECT * FROM $PROGRAMME_TABLE")
    override fun getAllProgrammes(): List<Programme>

    @SqlQuery("SELECT * FROM $PROGRAMME_TABLE WHERE $PROGRAMME_ID = :programmeId")
    override fun getSpecificProgramme(programmeId: Int): Optional<Programme>

    @SqlUpdate("DELETE FROM $PROGRAMME_TABLE")
    override fun deleteAllProgrammes(): Int

    @SqlUpdate("DELETE FROM $PROGRAMME_TABLE WHERE $PROGRAMME_ID = :programmeId")
    override fun deleteSpecificProgramme(programmeId: Int): Int

    @SqlUpdate(
            "UPDATE $PROGRAMME_TABLE SET " +
                    "$PROGRAMME_VERSION = :programme.version, " +
                    "$PROGRAMME_CREATED_BY = :programme.createdBy, " +
                    "$PROGRAMME_FULL_NAME = :programme.fullName, " +
                    "$PROGRAMME_SHORT_NAME = :programme.shortName, " +
                    "$PROGRAMME_ACADEMIC_DEGREE = :programme.academicDegree, " +
                    "$PROGRAMME_TOTAL_CREDITS = :programme.totalCredits, " +
                    "$PROGRAMME_DURATION = :programme.duration, " +
                    "$PROGRAMME_VOTES = :programme.votes, " +
                    "$PROGRAMME_TIMESTAMP = :programme.timestamp " +
                    "WHERE $PROGRAMME_ID = :programmeId"
    )
    @GetGeneratedKeys
    override fun updateProgramme(programmeId: Int, programme: Programme): Programme

    @SqlUpdate(
            "INSERT INTO $PROGRAMME_TABLE ( " +
                    "$PROGRAMME_VERSION, " +
                    "$PROGRAMME_FULL_NAME, " +
                    "$PROGRAMME_SHORT_NAME, " +
                    "$PROGRAMME_ACADEMIC_DEGREE, " +
                    "$PROGRAMME_TOTAL_CREDITS, " +
                    "$PROGRAMME_DURATION, " +
                    "$PROGRAMME_VOTES, " +
                    "$PROGRAMME_CREATED_BY, " +
                    "$PROGRAMME_TIMESTAMP) " +
                    "VALUES(:programme.version, :programme.fullName, :programme.shortName, :programme.academicDegree, " +
                    ":programme.totalCredits, :programme.duration, :programme.votes, :programme.createdBy, :programme.timestamp)"
    )
    @GetGeneratedKeys
    override fun createProgramme(programme: Programme): Programme

    @SqlQuery("SELECT $PROGRAMME_VOTES FROM $PROGRAMME_TABLE WHERE $PROGRAMME_ID = :programmeId")
    override fun getVotesOnProgramme(programmeId: Int): Int

    @SqlUpdate("UPDATE $PROGRAMME_TABLE SET $PROGRAMME_VOTES = :votes WHERE $PROGRAMME_ID = :programmeId")
    override fun updateVotesOnProgramme(programmeId: Int, votes: Int): Int

    @SqlQuery("SELECT * FROM $PROGRAMME_STAGE_TABLE WHERE $PROGRAMME_STAGE_ID = :stageId")
    override fun getSpecificStageEntryOfProgramme(stageId: Int): Optional<ProgrammeStage>

    @SqlQuery("SELECT * FROM $PROGRAMME_STAGE_TABLE")
    override fun getAllProgrammeStageEntries(): List<ProgrammeStage>

    @SqlUpdate("DELETE FROM $PROGRAMME_STAGE_TABLE")
    override fun deleteAllStagedProgrammes(): Int

    @SqlUpdate(
            "INSERT INTO $PROGRAMME_STAGE_TABLE (" +
                    "$PROGRAMME_FULL_NAME, " +
                    "$PROGRAMME_SHORT_NAME, " +
                    "$PROGRAMME_ACADEMIC_DEGREE, " +
                    "$PROGRAMME_TOTAL_CREDITS, " +
                    "$PROGRAMME_DURATION, " +
                    "$PROGRAMME_VOTES, " +
                    "$PROGRAMME_CREATED_BY, " +
                    "$PROGRAMME_TIMESTAMP) " +
                    "VALUES(:programmeStage.fullName, :programmeStage.shortName, :programmeStage.academicDegree, :programmeStage.totalCredits, " +
                    ":programmeStage.duration, :programmeStage.votes, :programmeStage.createdBy, :programmeStage.timestamp)"
    )
    @GetGeneratedKeys
    override fun createStagingProgramme(programmeStage: ProgrammeStage): ProgrammeStage

    @SqlQuery(
            "SELECT $PROGRAMME_VOTES FROM $PROGRAMME_STAGE_TABLE " +
            "WHERE $PROGRAMME_STAGE_ID = :stageId"
    )
    override fun getVotesOnStagedProgramme(stageId: Int): Int

    @SqlUpdate("UPDATE $PROGRAMME_STAGE_TABLE SET $PROGRAMME_VOTES = :votes WHERE $PROGRAMME_STAGE_ID = :stageId")
    override fun updateVotesOnStagedProgramme(stageId: Int, votes: Int): Int

    @SqlUpdate("DELETE FROM $PROGRAMME_STAGE_TABLE WHERE $PROGRAMME_STAGE_ID = :stageId")
    override fun deleteSpecificStagedProgramme(stageId: Int): Int

    @SqlQuery("SELECT * FROM $PROGRAMME_VERSION_TABLE WHERE $PROGRAMME_ID = :programmeId")
    override fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion>

    @SqlQuery("SELECT * FROM $PROGRAMME_VERSION_TABLE WHERE $PROGRAMME_ID = :programmeId AND $PROGRAMME_VERSION = :version")
    override fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): Optional<ProgrammeVersion>

    @SqlUpdate("DELETE FROM $PROGRAMME_VERSION_TABLE WHERE $PROGRAMME_ID = :programmeId AND $PROGRAMME_VERSION = :version")
    override fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int): Int

    @SqlUpdate("DELETE FROM $PROGRAMME_VERSION_TABLE WHERE $PROGRAMME_ID = :programmeId")
    override fun deleteAllProgrammeVersions(programmeId: Int): Int

    @SqlUpdate(
            "INSERT INTO $PROGRAMME_REPORT_TABLE (" +
                    "$PROGRAMME_ID, " +
                    "$PROGRAMME_FULL_NAME, " +
                    "$PROGRAMME_SHORT_NAME, " +
                    "$PROGRAMME_ACADEMIC_DEGREE, " +
                    "$PROGRAMME_TOTAL_CREDITS, " +
                    "$PROGRAMME_DURATION, " +
                    "$PROGRAMME_REPORTED_BY, " +
                    "$PROGRAMME_VOTES, " +
                    "$PROGRAMME_TIMESTAMP) " +
                    "VALUES(:programmeId, :programmeReport.fullName, :programmeReport.shortName, :programmeReport.academicDegree, " +
                    ":programmeReport.totalCredits, :programmeReport.duration, :programmeReport.reportedBy, :programmeReport.votes, :programmeReport.timestamp)"
    )
    @GetGeneratedKeys
    override fun reportProgramme(programmeId: Int, programmeReport: ProgrammeReport): ProgrammeReport

    @SqlQuery("SELECT * FROM $PROGRAMME_REPORT_TABLE WHERE $PROGRAMME_ID = :programmeId")
    override fun getAllReportsOfSpecificProgramme(programmeId: Int): List<ProgrammeReport>

    @SqlQuery("SELECT * FROM $PROGRAMME_REPORT_TABLE WHERE $PROGRAMME_ID = :programmeId AND $PROGRAMME_REPORT_ID = :reportId")
    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): Optional<ProgrammeReport>

    @SqlUpdate("DELETE FROM $PROGRAMME_REPORT_TABLE WHERE $PROGRAMME_ID = :programmeId")
    override fun deleteAllReportsOnProgramme(programmeId: Int): Int

    @SqlUpdate("DELETE FROM $PROGRAMME_REPORT_TABLE WHERE $PROGRAMME_ID = :programmeId AND $PROGRAMME_REPORT_ID = :reportId")
    override fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int): Int

    @SqlQuery("SELECT $PROGRAMME_VOTES FROM $PROGRAMME_REPORT_TABLE WHERE $PROGRAMME_REPORT_ID = :reportId AND $PROGRAMME_ID = :programmeId")
    override fun getVotesOnReportedProgramme(reportId: Int, programmeId: Int): Int

    @SqlUpdate("UPDATE $PROGRAMME_REPORT_TABLE SET $PROGRAMME_VOTES = :votes WHERE $PROGRAMME_REPORT_ID = :reportId AND $PROGRAMME_ID = :programmeId")
    override fun updateVotesOnReportedProgramme(programmeId: Int, reportId: Int, votes: Int): Int

    @SqlUpdate(
            "INSERT INTO $PROGRAMME_VERSION_TABLE ( " +
                    "$PROGRAMME_ID, " +
                    "$PROGRAMME_VERSION, " +
                    "$PROGRAMME_FULL_NAME, " +
                    "$PROGRAMME_SHORT_NAME, " +
                    "$PROGRAMME_ACADEMIC_DEGREE, " +
                    "$PROGRAMME_TOTAL_CREDITS, " +
                    "$PROGRAMME_DURATION, " +
                    "$PROGRAMME_CREATED_BY, " +
                    "$PROGRAMME_TIMESTAMP " +
                    ") " +
                    "VALUES (:programmeVersion.programmeId, :programmeVersion.version, :programmeVersion.fullName, " +
                    ":programmeVersion.shortName, :programmeVersion.academicDegree, :programmeVersion.totalCredits, " +
                    ":programmeVersion.duration, :programmeVersion.createdBy, :programmeVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createProgrammeVersion(programmeVersion: ProgrammeVersion): ProgrammeVersion
}