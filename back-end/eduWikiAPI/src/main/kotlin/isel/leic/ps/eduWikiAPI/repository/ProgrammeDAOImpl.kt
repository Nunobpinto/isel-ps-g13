package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProgrammeDAOImpl : ProgrammeDAO {

    companion object {
        //TABLE NAMES
        const val PROGRAMME_TABLE = "programme"
        const val PROGRAMME_VERSION_TABLE = "programme_version"
        const val PROGRAMME_REPORT_TABLE = "programme_report"
        const val PROGRAMME_STAGE_TABLE = "programme_stage"
        // PROGRAMME FIELDS
        const val PROGRAMME_ID = "programme_id"
        const val PROGRAMME_VERSION = "programme_version"
        const val PROGRAMME_FULL_NAME = "programme_full_name"
        const val PROGRAMME_SHORT_NAME = "programme_short_name"
        const val PROGRAMME_ACADEMIC_DEGREE = "programme_academic_degree"
        const val PROGRAMME_TOTAL_CREDITS = "programme_total_credits"
        const val PROGRAMME_DURATION = "programme_duration"
        const val PROGRAMME_VOTES = "votes"
        const val PROGRAMME_TIMESTAMP = "time_stamp"
        const val PROGRAMME_CREATED_BY = "created_by"
        const val PROGRAMME_LOG_ID = "log_id"
        // PROGRAMME REPORT FIELDS
        const val PROGRAMME_REPORT_ID = "programme_report_id"
        const val PROGRAMME_REPORT_PROGRAMME_ID = "programme_id"
        const val PROGRAMME_REPORTED_BY = "reported_by"
        const val PROGRAMME_REPORT_FULL_NAME = "programme_full_name"
        const val PROGRAMME_REPORT_SHORT_NAME = "programme_short_name"
        const val PROGRAMME_REPORT_ACADEMIC_DEGREE = "programme_academic_degree"
        const val PROGRAMME_REPORT_TOTAL_CREDITS = "programme_total_credits"
        const val PROGRAMME_REPORT_DURATION = "programme_duration"
        const val PROGRAMME_REPORT_VOTES = "votes"
        const val PROGRAMME_REPORT_TIMESTAMP = "time_stamp"
        const val PROGRAMME_REPORT_LOG_ID = "log_id"
        // PROGRAMME STAGE FIELDS
        const val PROGRAMME_STAGE_ID = "programme_stage_id"
        const val PROGRAMME_STAGE_CREATED_BY = "created_by"
        const val PROGRAMME_STAGE_FULL_NAME = "programme_full_name"
        const val PROGRAMME_STAGE_SHORT_NAME = "programme_short_name"
        const val PROGRAMME_STAGE_ACADEMIC_DEGREE = "programme_academic_degree"
        const val PROGRAMME_STAGE_TOTAL_CREDITS = "programme_total_credits"
        const val PROGRAMME_STAGE_DURATION = "programme_duration"
        const val PROGRAMME_STAGE_VOTES = "votes"
        const val PROGRAMME_STAGE_TIMESTAMP = "time_stamp"
        const val PROGRAMME_STAGE_LOG_ID = "log_id"
        // PROGRAMME VERSION FIELDS
        const val PROGRAMME_VERSION_PROGRAMME_ID = "programme_id"
        const val PROGRAMME_VERSION_ID = "programme_version"
        const val PROGRAMME_VERSION_FULL_NAME = "programme_full_name"
        const val PROGRAMME_VERSION_SHORT_NAME = "programme_short_name"
        const val PROGRAMME_VERSION_ACADEMIC_DEGREE = "programme_academic_degree"
        const val PROGRAMME_VERSION_TOTAL_CREDITS = "programme_total_credits"
        const val PROGRAMME_VERSION_DURATION = "programme_duration"
        const val PROGRAMME_VERSION_VOTES = "votes"
        const val PROGRAMME_VERSION_TIMESTAMP = "time_stamp"
        const val PROGRAMME_VERSION_CREATED_BY = "created_by"

    }

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAllProgrammes(): List<Programme> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getAllProgrammes()

    override fun getSpecificProgramme(programmeId: Int): Optional<Programme> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getSpecificProgramme(programmeId)

    override fun createProgramme(programme: Programme): Programme =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).createProgramme(programme)

    override fun updateProgramme(programmeId: Int, programme: Programme): Programme =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).updateProgramme(programmeId, programme)

    override fun updateVotesOnProgramme(programmeId: Int, votes: Int): Int =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).updateVotesOnProgramme(programmeId, votes)

    override fun deleteSpecificProgramme(programmeId: Int): Int =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).deleteSpecificProgramme(programmeId)

    override fun getSpecificStageEntryOfProgramme(stageId: Int): Optional<ProgrammeStage> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getSpecificStageEntryOfProgramme(stageId)

    override fun getAllProgrammeStageEntries(): List<ProgrammeStage> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getAllProgrammeStageEntries()

    override fun createStagingProgramme(programmeStage: ProgrammeStage): ProgrammeStage =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).createStagingProgramme(programmeStage)

    override fun updateVotesOnStagedProgramme(stageId: Int, votes: Int): Int =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).updateVotesOnStagedProgramme(stageId, votes)

    override fun deleteSpecificStagedProgramme(stageId: Int): Int =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).deleteSpecificStagedProgramme(stageId)

    override fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getAllVersionsOfProgramme(programmeId)

    override fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): Optional<ProgrammeVersion> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getSpecificVersionOfProgramme(programmeId, version)

    override fun createProgrammeVersion(programmeVersion: ProgrammeVersion): ProgrammeVersion =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).createProgrammeVersion(programmeVersion)

    override fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int): Int =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).deleteSpecificProgrammeVersion(programmeId, version)

    override fun getAllReportsOfSpecificProgramme(programmeId: Int): List<ProgrammeReport> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getAllReportsOfSpecificProgramme(programmeId)

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): Optional<ProgrammeReport> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getSpecificReportOfProgramme(programmeId, reportId)

    override fun reportProgramme(programmeId: Int, programmeReport: ProgrammeReport): ProgrammeReport =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).reportProgramme(programmeId, programmeReport)

    override fun updateVotesOnReportedProgramme(programmeId: Int, reportId: Int, votes: Int): Int =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).updateVotesOnReportedProgramme(programmeId, reportId, votes)

    override fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int): Int =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).deleteSpecificReportOnProgramme(programmeId, reportId)

    override fun getProgrammeByLogId(logId: Int): Optional<Programme> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getProgrammeByLogId(logId)

    override fun getProgrammeReportByLogId(logId: Int): Optional<ProgrammeReport> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getProgrammeReportByLogId(logId)

    override fun getProgrammeStageByLogId(logId: Int): Optional<ProgrammeStage> =
            jdbi.open().attach(ProgrammeDAOJdbi::class.java).getProgrammeStageByLogId(logId)

    interface ProgrammeDAOJdbi : ProgrammeDAO {
        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_TABLE")
        override fun getAllProgrammes(): List<Programme>

        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_TABLE WHERE $PROGRAMME_ID = :programmeId")
        override fun getSpecificProgramme(programmeId: Int): Optional<Programme>

        @SqlUpdate("DELETE FROM :schema.$PROGRAMME_TABLE WHERE $PROGRAMME_ID = :programmeId")
        override fun deleteSpecificProgramme(programmeId: Int): Int

        @SqlUpdate(
                "UPDATE :schema.$PROGRAMME_TABLE SET " +
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
                "INSERT INTO :schema.$PROGRAMME_TABLE ( " +
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

        @SqlUpdate("UPDATE :schema.$PROGRAMME_TABLE SET $PROGRAMME_VOTES = :votes WHERE $PROGRAMME_ID = :programmeId")
        override fun updateVotesOnProgramme(programmeId: Int, votes: Int): Int

        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_STAGE_TABLE WHERE $PROGRAMME_STAGE_ID = :stageId")
        override fun getSpecificStageEntryOfProgramme(stageId: Int): Optional<ProgrammeStage>

        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_STAGE_TABLE")
        override fun getAllProgrammeStageEntries(): List<ProgrammeStage>

        @SqlUpdate(
                "INSERT INTO :schema.$PROGRAMME_STAGE_TABLE (" +
                        "$PROGRAMME_STAGE_FULL_NAME, " +
                        "$PROGRAMME_STAGE_SHORT_NAME, " +
                        "$PROGRAMME_STAGE_ACADEMIC_DEGREE, " +
                        "$PROGRAMME_STAGE_TOTAL_CREDITS, " +
                        "$PROGRAMME_STAGE_DURATION, " +
                        "$PROGRAMME_STAGE_VOTES, " +
                        "$PROGRAMME_STAGE_CREATED_BY, " +
                        "$PROGRAMME_STAGE_TIMESTAMP) " +
                        "VALUES(:programmeStage.fullName, :programmeStage.shortName, :programmeStage.academicDegree, :programmeStage.totalCredits, " +
                        ":programmeStage.duration, :programmeStage.votes, :programmeStage.createdBy, :programmeStage.timestamp)"
        )
        @GetGeneratedKeys
        override fun createStagingProgramme(programmeStage: ProgrammeStage): ProgrammeStage

        @SqlUpdate("UPDATE :schema.$PROGRAMME_STAGE_TABLE SET $PROGRAMME_STAGE_VOTES = :votes WHERE $PROGRAMME_STAGE_ID = :stageId")
        override fun updateVotesOnStagedProgramme(stageId: Int, votes: Int): Int

        @SqlUpdate("DELETE FROM :schema.$PROGRAMME_STAGE_TABLE WHERE $PROGRAMME_STAGE_ID = :stageId")
        override fun deleteSpecificStagedProgramme(stageId: Int): Int

        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_VERSION_TABLE WHERE $PROGRAMME_VERSION_PROGRAMME_ID = :programmeId")
        override fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion>

        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_VERSION_TABLE WHERE $PROGRAMME_VERSION_PROGRAMME_ID = :programmeId AND $PROGRAMME_VERSION_ID = :version")
        override fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): Optional<ProgrammeVersion>

        @SqlUpdate("DELETE FROM :schema.$PROGRAMME_VERSION_TABLE WHERE $PROGRAMME_VERSION_PROGRAMME_ID = :programmeId AND $PROGRAMME_VERSION_ID = :version")
        override fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int): Int

        @SqlUpdate(
                "INSERT INTO :schema.$PROGRAMME_REPORT_TABLE (" +
                        "$PROGRAMME_REPORT_PROGRAMME_ID, " +
                        "$PROGRAMME_REPORT_FULL_NAME, " +
                        "$PROGRAMME_REPORT_SHORT_NAME, " +
                        "$PROGRAMME_REPORT_ACADEMIC_DEGREE, " +
                        "$PROGRAMME_REPORT_TOTAL_CREDITS, " +
                        "$PROGRAMME_REPORT_DURATION, " +
                        "$PROGRAMME_REPORTED_BY, " +
                        "$PROGRAMME_REPORT_VOTES, " +
                        "$PROGRAMME_REPORT_TIMESTAMP) " +
                        "VALUES(:programmeId, :programmeReport.fullName, :programmeReport.shortName, :programmeReport.academicDegree, " +
                        ":programmeReport.totalCredits, :programmeReport.duration, :programmeReport.reportedBy, :programmeReport.votes, :programmeReport.timestamp)"
        )
        @GetGeneratedKeys
        override fun reportProgramme(programmeId: Int, programmeReport: ProgrammeReport): ProgrammeReport

        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_REPORT_TABLE WHERE $PROGRAMME_REPORT_PROGRAMME_ID = :programmeId")
        override fun getAllReportsOfSpecificProgramme(programmeId: Int): List<ProgrammeReport>

        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_REPORT_TABLE WHERE $PROGRAMME_REPORT_PROGRAMME_ID = :programmeId AND $PROGRAMME_REPORT_ID = :reportId")
        override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): Optional<ProgrammeReport>


        @SqlUpdate("DELETE FROM :schema.$PROGRAMME_REPORT_TABLE WHERE $PROGRAMME_REPORT_PROGRAMME_ID = :programmeId AND $PROGRAMME_REPORT_ID = :reportId")
        override fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int): Int

        @SqlUpdate("UPDATE :schema.$PROGRAMME_REPORT_TABLE SET $PROGRAMME_REPORT_VOTES = :votes WHERE $PROGRAMME_REPORT_ID = :reportId AND $PROGRAMME_REPORT_PROGRAMME_ID = :programmeId")
        override fun updateVotesOnReportedProgramme(programmeId: Int, reportId: Int, votes: Int): Int

        @SqlUpdate(
                "INSERT INTO :schema.$PROGRAMME_VERSION_TABLE ( " +
                        "$PROGRAMME_VERSION_PROGRAMME_ID, " +
                        "$PROGRAMME_VERSION_ID, " +
                        "$PROGRAMME_VERSION_FULL_NAME, " +
                        "$PROGRAMME_VERSION_SHORT_NAME, " +
                        "$PROGRAMME_VERSION_ACADEMIC_DEGREE, " +
                        "$PROGRAMME_VERSION_TOTAL_CREDITS, " +
                        "$PROGRAMME_VERSION_DURATION, " +
                        "$PROGRAMME_VERSION_CREATED_BY, " +
                        "$PROGRAMME_VERSION_TIMESTAMP " +
                        ") " +
                        "VALUES (:programmeVersion.programmeId, :programmeVersion.version, :programmeVersion.fullName, " +
                        ":programmeVersion.shortName, :programmeVersion.academicDegree, :programmeVersion.totalCredits, " +
                        ":programmeVersion.duration, :programmeVersion.createdBy, :programmeVersion.timestamp)"
        )
        @GetGeneratedKeys
        override fun createProgrammeVersion(programmeVersion: ProgrammeVersion): ProgrammeVersion

        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_TABLE WHERE $PROGRAMME_LOG_ID = :logId")
        override fun getProgrammeByLogId(logId: Int): Optional<Programme>

        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_REPORT_TABLE WHERE $PROGRAMME_REPORT_LOG_ID = :logId")
        override fun getProgrammeReportByLogId(logId: Int): Optional<ProgrammeReport>

        @SqlQuery("SELECT * FROM :schema.$PROGRAMME_STAGE_TABLE WHERE $PROGRAMME_STAGE_LOG_ID = :logId")
        override fun getProgrammeStageByLogId(logId: Int): Optional<ProgrammeStage>

    }

}