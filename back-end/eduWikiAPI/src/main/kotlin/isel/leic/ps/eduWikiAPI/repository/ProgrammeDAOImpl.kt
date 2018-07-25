package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    lateinit var handle: Handle

    override fun getAllProgrammes(): List<Programme> =
            handle.createQuery("select * from $PROGRAMME_TABLE")
                    .mapTo(Programme::class.java)
                    .list()

    override fun getSpecificProgramme(programmeId: Int): Optional<Programme> =
            handle.createQuery(
                    "select * from $PROGRAMME_TABLE " +
                            "where $PROGRAMME_ID = :programmeId"
            )
                    .bind("programmeId", programmeId)
                    .mapTo(Programme::class.java)
                    .findFirst()

    override fun deleteAllProgrammes() =
            handle.createUpdate("delete from $PROGRAMME_TABLE")
                    .execute()

    override fun deleteSpecificProgramme(programmeId: Int): Int =
            handle.createUpdate(
                    "delete from $PROGRAMME_TABLE " +
                            "where $PROGRAMME_ID = :programmeId"
            )
                    .bind("programmeId", programmeId)
                    .execute()

    override fun updateProgramme(programmeId: Int, programme: Programme): Optional<Programme> =
            handle.createUpdate("update $PROGRAMME_TABLE SET " +
                    "$PROGRAMME_VERSION = :version, " +
                    "$PROGRAMME_CREATED_BY = :createdBy, " +
                    "$PROGRAMME_FULL_NAME = :fullName, " +
                    "$PROGRAMME_SHORT_NAME = :shortName, " +
                    "$PROGRAMME_ACADEMIC_DEGREE = :academicDegree, " +
                    "$PROGRAMME_TOTAL_CREDITS = :totalCredits, " +
                    "$PROGRAMME_DURATION = :duration, " +
                    "$PROGRAMME_VOTES = :votes, " +
                    "$PROGRAMME_TIMESTAMP = :timestamp " +
                    "where $PROGRAMME_ID = :programmeId"
            )
                    .bind("version", programme.version)
                    .bind("createdBy", programme.createdBy)
                    .bind("fullName", programme.fullName)
                    .bind("shortName", programme.shortName)
                    .bind("academicDegree", programme.academicDegree)
                    .bind("totalCredits", programme.totalCredits)
                    .bind("duration", programme.duration)
                    .bind("votes", programme.votes)
                    .bind("programmeId", programmeId)
                    .bind("timestamp", programme.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Programme::class.java)
                    .findFirst()

    override fun createProgramme(programme: Programme): Optional<Programme> =
            handle.createUpdate(
                    "insert into $PROGRAMME_TABLE (  " +
                            "$PROGRAMME_VERSION, " +
                            "$PROGRAMME_FULL_NAME, " +
                            "$PROGRAMME_SHORT_NAME, " +
                            "$PROGRAMME_ACADEMIC_DEGREE, " +
                            "$PROGRAMME_TOTAL_CREDITS, " +
                            "$PROGRAMME_DURATION, " +
                            "$PROGRAMME_VOTES, " +
                            "$PROGRAMME_CREATED_BY, " +
                            "$PROGRAMME_TIMESTAMP " +
                            ") " +
                            "values(:version, :fullName, :shortName, :academicDegree, " +
                            ":totalCredits, :duration, :votes, :createdBy, :timestamp)"
            )
                    .bind("version", programme.version)
                    .bind("fullName", programme.fullName)
                    .bind("shortName", programme.shortName)
                    .bind("academicDegree", programme.academicDegree)
                    .bind("totalCredits", programme.totalCredits)
                    .bind("duration", programme.duration)
                    .bind("votes", programme.votes)
                    .bind("createdBy", programme.createdBy)
                    .bind("timestamp", programme.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Programme::class.java)
                    .findFirst()

    override fun voteOnProgramme(programmeId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $PROGRAMME_VOTES from $PROGRAMME_TABLE " +
                        "where $PROGRAMME_ID = :programmeId"
        )
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java)
                .findOnly()

        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate(
                "update $PROGRAMME_TABLE set $PROGRAMME_VOTES = :votes " +
                        "where $PROGRAMME_ID = :programmeId"
        )
                .bind("votes", votes)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun getSpecificStageEntryOfProgramme(stageId: Int): Optional<ProgrammeStage> =
            handle.createQuery(
                    "select * from $PROGRAMME_STAGE_TABLE " +
                            "where $PROGRAMME_STAGE_ID = :stageId"
            )
                    .bind("stageId", stageId)
                    .mapTo(ProgrammeStage::class.java)
                    .findFirst()

    override fun getAllProgrammeStageEntries(): List<ProgrammeStage> =
            handle.createQuery("select * from $PROGRAMME_STAGE_TABLE")
                    .mapTo(ProgrammeStage::class.java)
                    .list()

    override fun deleteAllStagedProgrammes(): Int =
            handle.createUpdate("delete from $PROGRAMME_STAGE_TABLE")
                    .execute()

    override fun createStagingProgramme(programmeStage: ProgrammeStage): Optional<ProgrammeStage> =
            handle.createUpdate(
                    "insert into $PROGRAMME_STAGE_TABLE (" +
                            "$PROGRAMME_FULL_NAME, " +
                            "$PROGRAMME_SHORT_NAME, " +
                            "$PROGRAMME_ACADEMIC_DEGREE, " +
                            "$PROGRAMME_TOTAL_CREDITS, " +
                            "$PROGRAMME_DURATION, " +
                            "$PROGRAMME_VOTES, " +
                            "$PROGRAMME_CREATED_BY, " +
                            "$PROGRAMME_TIMESTAMP " +
                            ") " +
                            "values(:fullName, :shortName, :academicDegree, :totalCredits, " +
                            ":duration, :votes, :createdBy, :timestamp)"
            )
                    .bind("fullName", programmeStage.fullName)
                    .bind("shortName", programmeStage.shortName)
                    .bind("academicDegree", programmeStage.academicDegree)
                    .bind("totalCredits", programmeStage.totalCredits)
                    .bind("duration", programmeStage.duration)
                    .bind("votes", programmeStage.votes)
                    .bind("createdBy", programmeStage.createdBy)
                    .bind("timestamp", programmeStage.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ProgrammeStage::class.java)
                    .findFirst()

    override fun voteOnStagedProgramme(stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $PROGRAMME_VOTES from $PROGRAMME_STAGE_TABLE " +
                        "where $PROGRAMME_STAGE_ID = :stageId"
        )
                .bind("stageId", stageId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $PROGRAMME_STAGE_TABLE set $PROGRAMME_VOTES = :votes " +
                        "where $PROGRAMME_STAGE_ID = :stageId"
        )
                .bind("votes", votes)
                .bind("stageId", stageId)
                .execute()
    }

    override fun deleteSpecificStagedProgramme(stageId: Int) =
            handle.createUpdate(
                    "delete from $PROGRAMME_STAGE_TABLE " +
                            "where $PROGRAMME_STAGE_ID = :stageId"
            )
                    .bind("stageId", stageId)
                    .execute()

    override fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion> =
            handle.createQuery(
                    "select * from $PROGRAMME_VERSION_TABLE " +
                            "where $PROGRAMME_ID = :programmeId"
            )
                    .bind("programmeId", programmeId)
                    .mapTo(ProgrammeVersion::class.java)
                    .list()

    override fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): Optional<ProgrammeVersion> =
            handle.createQuery(
                    "select * from $PROGRAMME_VERSION_TABLE " +
                            "where $PROGRAMME_ID = :programmeId " +
                            "and $PROGRAMME_VERSION = :version"
            )
                    .bind("programmeId", programmeId)
                    .bind("version", version)
                    .mapTo(ProgrammeVersion::class.java)
                    .findFirst()

    override fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int): Int =
            handle.createUpdate(
                    "delete * from $PROGRAMME_VERSION_TABLE" +
                            "where $PROGRAMME_ID = :programmeId " +
                            "and $PROGRAMME_VERSION = :version"
            )
                    .bind("programmeId", programmeId)
                    .bind("version", version)
                    .execute()


    override fun deleteAllProgrammeVersions(programmeId: Int): Int =
            handle.createUpdate(
                    "delete from $PROGRAMME_VERSION_TABLE " +
                            "where $PROGRAMME_ID = :programmeId"
            )
                    .bind("programmeId", programmeId)
                    .execute()

    override fun reportProgramme(programmeId: Int, programmeReport: ProgrammeReport): Optional<ProgrammeReport> =
            handle.createUpdate(
                    "insert into $PROGRAMME_REPORT_TABLE (" +
                            "$PROGRAMME_ID, " +
                            "$PROGRAMME_FULL_NAME, " +
                            "$PROGRAMME_SHORT_NAME, " +
                            "$PROGRAMME_ACADEMIC_DEGREE, " +
                            "$PROGRAMME_TOTAL_CREDITS, " +
                            "$PROGRAMME_DURATION, " +
                            "$PROGRAMME_REPORTED_BY, " +
                            "$PROGRAMME_VOTES, " +
                            "$PROGRAMME_TIMESTAMP " +
                            ") " +
                            "values(:programmeId, :fullName, :shortName, :academicDegree, " +
                            ":totalCredits, :duration, :reportedBy, :votes, :timestamp)"
            )
                    .bind("programmeId", programmeId)
                    .bind("fullName", programmeReport.fullName)
                    .bind("shortName", programmeReport.shortName)
                    .bind("academicDegree", programmeReport.academicDegree)
                    .bind("totalCredits", programmeReport.totalCredits)
                    .bind("duration", programmeReport.duration)
                    .bind("reportedBy", programmeReport.reportedBy)
                    .bind("votes", programmeReport.votes)
                    .bind("timestamp", programmeReport.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ProgrammeReport::class.java)
                    .findFirst()

    override fun getAllReportsOfSpecificProgramme(programmeId: Int): List<ProgrammeReport> =
            handle.createQuery(
                    "select * from $PROGRAMME_REPORT_TABLE " +
                            "where $PROGRAMME_ID = :programmeId"
            )
                    .bind("programmeId", programmeId)
                    .mapTo(ProgrammeReport::class.java)
                    .list()

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): Optional<ProgrammeReport> =
            handle.createQuery(
                    "select * from $PROGRAMME_REPORT_TABLE " +
                            "where $PROGRAMME_ID = :programmeId " +
                            "and $PROGRAMME_REPORT_ID = :reportId"
            )
                    .bind("programmeId", programmeId)
                    .bind("reportId", reportId)
                    .mapTo(ProgrammeReport::class.java)
                    .findFirst()

    override fun deleteAllReportsOnProgramme(programmeId: Int): Int =
            handle.createUpdate(
                    "delete from $PROGRAMME_REPORT_TABLE " +
                            "where $PROGRAMME_ID = :programmeId"
            )
                    .bind("programmeId", programmeId)
                    .execute()


    override fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int): Int =
            handle.createUpdate(
                    "delete from $PROGRAMME_REPORT_TABLE " +
                            "where $PROGRAMME_ID = :programmeId " +
                            "and $PROGRAMME_REPORT_ID = :reportId"
            )
                    .bind("programmeId", programmeId)
                    .bind("reportId", reportId)
                    .execute()

    override fun voteOnReportedProgramme(programmeId: Int, reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $PROGRAMME_VOTES from $PROGRAMME_REPORT_TABLE " +
                        "where $PROGRAMME_REPORT_ID = :reportId " +
                        "and $PROGRAMME_ID = :programmeId"
        )
                .bind("reportId", reportId)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $PROGRAMME_REPORT_TABLE set $PROGRAMME_VOTES = :votes " +
                        "where $PROGRAMME_REPORT_ID = :reportId " +
                        "and $PROGRAMME_ID = :programmeId"
        )
                .bind("votes", votes)
                .bind("reportId", reportId)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun createProgrammeVersion(programme: Programme): Optional<ProgrammeVersion> =
        handle.createUpdate(
                "insert into $PROGRAMME_VERSION_TABLE (" +
                        "$PROGRAMME_ID, " +
                        "$PROGRAMME_VERSION, " +
                        "$PROGRAMME_FULL_NAME, " +
                        "$PROGRAMME_SHORT_NAME, " +
                        "$PROGRAMME_ACADEMIC_DEGREE, " +
                        "$PROGRAMME_TOTAL_CREDITS," +
                        "$PROGRAMME_DURATION, " +
                        "$PROGRAMME_CREATED_BY, " +
                        "$PROGRAMME_TIMESTAMP " +
                        ")" +
                        "values (:programmeId, :version, :fullName, :shortName," +
                        ":academicDegree, :totalCredits, :duration, :createdBy, :timestamp)"
        )
                .bind("programmeId", programme.programmeId)
                .bind("version", programme.version)
                .bind("fullName", programme.fullName)
                .bind("shortName", programme.shortName)
                .bind("academicDegree", programme.academicDegree)
                .bind("totalCredits", programme.totalCredits)
                .bind("duration", programme.duration)
                .bind("createdBy", programme.createdBy)
                .bind("timestamp", programme.timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(ProgrammeVersion::class.java)
                .findFirst()

}