package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ProgrammeDAOImpl : ProgrammeDAO {

    companion object {
        //TABLE NAMES
        const val PROGRAMME_TABLE = "programme"
//        const val COURSE_PROGRAMME_TABLE = "course_programme"
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
        const val PROGRAMME_REPORT_ID = "report_id"
        const val PROGRAMME_CREATED_BY = "created_by"
        const val PROGRAMME_REPORTED_BY = "reported_by"

    }

    @Autowired
    lateinit var dbi: Jdbi

    override fun getSpecificProgramme(programmeId: Int): Programme = dbi.withHandle<Programme, Exception> {
        val select = "select * from $PROGRAMME_TABLE where $PROGRAMME_ID = :programmeId"
        it.createQuery(select).bind("programmeId", programmeId).mapTo(Programme::class.java).findOnly()
    }

    override fun getAllProgrammes(): List<Programme> = dbi.withHandle<List<Programme>, Exception> {
        val select = "select * from $PROGRAMME_TABLE"
        it.createQuery(select).mapTo(Programme::class.java).list()
    }

    override fun deleteSpecificProgramme(programmeId: Int) = dbi.withHandle<Int, Exception> {
        val delete = "delete from $PROGRAMME_TABLE where $PROGRAMME_ID = :programmeId"
        it.createUpdate(delete)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun deleteAllProgrammes() = dbi.useHandle<Exception> {
        it.createUpdate("delete from $PROGRAMME_TABLE").execute()
    }

    override fun updateProgramme(programmeId: Int, programme: Programme) = dbi.withHandle<Int, Exception> {
        val update = "update $PROGRAMME_TABLE SET " +
                "$PROGRAMME_VERSION = :version, $PROGRAMME_CREATED_BY = :reportedBy, " +
                "$PROGRAMME_FULL_NAME = :fullName, $PROGRAMME_SHORT_NAME = :shortName, " +
                "$PROGRAMME_ACADEMIC_DEGREE = :academicDegree, $PROGRAMME_TOTAL_CREDITS = :totalCredits, " +
                "$PROGRAMME_DURATION = :duration, $PROGRAMME_VOTES = :votes, $PROGRAMME_TIMESTAMP = :timestamp " +
                "where $PROGRAMME_ID = :programmeId"

        it.createUpdate(update)
                .bind("version", programme.version)
                .bind("reportedBy", programme.createdBy)
                .bind("fullName", programme.fullName)
                .bind("shortName", programme.shortName)
                .bind("academicDegree", programme.academicDegree)
                .bind("totalCredits", programme.totalCredits)
                .bind("duration", programme.duration)
                .bind("votes", programme.votes)
                .bind("programmeId", programmeId)
                .bind("timestamp", programme.timestamp)
                .execute()
    }

    override fun createProgramme(programme: Programme): Int = dbi.withHandle<Int, Exception> {
        val insert = "insert into $PROGRAMME_TABLE " +
                "($PROGRAMME_VERSION, $PROGRAMME_FULL_NAME, " +
                "$PROGRAMME_SHORT_NAME, $PROGRAMME_ACADEMIC_DEGREE, $PROGRAMME_TOTAL_CREDITS, " +
                "$PROGRAMME_DURATION, $PROGRAMME_VOTES, $PROGRAMME_CREATED_BY, $PROGRAMME_TIMESTAMP) " +
                "values(:version, :fullName, :shortName, :academicDegree, :totalCredits, :duration, :votes, :credits, :timestamp)"

        it.createUpdate(insert)
                .bind("version", programme.version)
                .bind("fullName", programme.fullName)
                .bind("shortName", programme.shortName)
                .bind("academicDegree", programme.academicDegree)
                .bind("totalCredits", programme.totalCredits)
                .bind("duration", programme.duration)
                .bind("votes", programme.votes)
                .bind("credits", programme.createdBy)
                .bind("timestamp", programme.timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(Int::class.java)
                .findOnly()
    }

    override fun voteOnProgramme(programmeId: Int, vote: Vote) = dbi.useTransaction<Exception> {
        val voteQuery = "select $PROGRAMME_VOTES from $PROGRAMME_TABLE where $PROGRAMME_ID = :programmeId"
        var votes = it.createQuery(voteQuery)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $PROGRAMME_TABLE set $PROGRAMME_VOTES = :votes where $PROGRAMME_ID = :programmeId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun getSpecificProgrammeStage(programmeId: Int): ProgrammeStage = dbi.withHandle<ProgrammeStage, Exception> {
        val select = "select * from $PROGRAMME_STAGE_TABLE where $PROGRAMME_ID = :programmeId"
        it.createQuery(select).bind("programmeId", programmeId).mapTo(ProgrammeStage::class.java).findOnly()
    }

    override fun getAllProgrammeStages(): List<ProgrammeStage> = dbi.withHandle<List<ProgrammeStage>, Exception> {
        val select = "select * from $PROGRAMME_STAGE_TABLE"
        it.createQuery(select).mapTo(ProgrammeStage::class.java).toList()
    }

    override fun deleteAllStagedProgrammes() = dbi.useHandle<Exception> {
        it.createUpdate("delete from $PROGRAMME_STAGE_TABLE").execute()
    }

    override fun createStagingProgramme(programmeStage: ProgrammeStage) = dbi.withHandle<Int,Exception> {
        val insert = "insert into $PROGRAMME_STAGE_TABLE " +
                "($PROGRAMME_FULL_NAME, " +
                "$PROGRAMME_SHORT_NAME, $PROGRAMME_ACADEMIC_DEGREE, $PROGRAMME_TOTAL_CREDITS, " +
                "$PROGRAMME_DURATION, $PROGRAMME_VOTES, $PROGRAMME_CREATED_BY, $PROGRAMME_TIMESTAMP) " +
                "values(:fullName, :shortName, :academicDegree, :totalCredits, :duration, :votes, :credits, :timestamp)"
        it.createUpdate(insert)
                .bind("fullName", programmeStage.fullName)
                .bind("shortName", programmeStage.shortName)
                .bind("academicDegree", programmeStage.academicDegree)
                .bind("totalCredits", programmeStage.totalCredits)
                .bind("duration", programmeStage.duration)
                .bind("votes", programmeStage.votes)
                .bind("credits", programmeStage.createdBy)
                .bind("timestamp", programmeStage.timestamp)
                .execute()
    }

    override fun voteOnStagedProgramme(programmeStageId: Int, vote: Vote) = dbi.useTransaction<Exception> {
        val voteQuery = "select $PROGRAMME_VOTES from $PROGRAMME_STAGE_TABLE where $PROGRAMME_ID = :programmeId"
        var votes = it.createQuery(voteQuery)
                .bind("programmeId", programmeStageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote.equals(Vote.Down)) --votes else ++votes
        val updateQuery = "update $PROGRAMME_STAGE_TABLE set $PROGRAMME_VOTES = :votes where $PROGRAMME_ID = :programmeId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("programmeId", programmeStageId)
                .execute()
    }

    override fun updateStagedProgramme(programmeId: Int, programme: ProgrammeStage) = dbi.useHandle<Exception> {
        val update = "update $PROGRAMME_STAGE_TABLE SET" +
                "$PROGRAMME_FULL_NAME = :fullName, $PROGRAMME_SHORT_NAME = :shortName, " +
                "$PROGRAMME_ACADEMIC_DEGREE = :academicDegree, $PROGRAMME_TOTAL_CREDITS = :totalCredits, " +
                "$PROGRAMME_DURATION = :duration, $PROGRAMME_CREATED_BY = :reportedBy, $PROGRAMME_VOTES = :votes, " +
                "$PROGRAMME_TIMESTAMP = :timestamp) " +
                "where $PROGRAMME_ID = :programmeId"

        it.createUpdate(update)
                .bind("fullName", programme.fullName)
                .bind("shortName", programme.shortName)
                .bind("academicDegree", programme.academicDegree)
                .bind("totalCredits", programme.totalCredits)
                .bind("duration", programme.duration)
                .bind("reportedBy", programme.createdBy)
                .bind("votes", programme.votes)
                .bind("timestamp", programme.timestamp)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun deleteStagedProgramme(stageId: Int) = dbi.withHandle<Int,Exception> {
        val delete = "delete from $PROGRAMME_STAGE_TABLE where $PROGRAMME_ID = :programmeId"
        it.createUpdate(delete)
                .bind("programmeId", stageId)
                .execute()
    }

    override fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion> = dbi.withHandle<List<ProgrammeVersion>, Exception> {
        val query = "select * from $PROGRAMME_VERSION_TABLE where $PROGRAMME_ID = :programmeId"
        it.createQuery(query).bind("programmeId", programmeId).mapTo(ProgrammeVersion::class.java).list()
    }

    override fun getSpecificVersionOfProgramme(programmeId: Int, versionId: Int): ProgrammeVersion = dbi.withHandle<ProgrammeVersion, Exception> {
        val query = "select * from $PROGRAMME_VERSION_TABLE where $PROGRAMME_ID = :programmeId and $PROGRAMME_VERSION = :versionId"
        it.createQuery(query)
                .bind("programmeId", programmeId)
                .bind("versionId", versionId)
                .mapTo(ProgrammeVersion::class.java).findOnly()
    }

    override fun deleteVersionProgramme(versionProgrammeId: Int, version: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $PROGRAMME_VERSION_TABLE where $PROGRAMME_ID = :courseId and $PROGRAMME_VERSION = :version"
        it.createUpdate(delete)
                .bind("courseId", versionProgrammeId)
                .bind("version", version)
                .execute()
    }

    override fun deleteAllVersionsOfProgramme(programmeId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $PROGRAMME_VERSION_TABLE where $PROGRAMME_ID = :courseId"
        it.createUpdate(delete)
                .bind("courseId", programmeId)
                .execute()
    }

    override fun reportProgramme(programmeId: Int, programmeReport: ProgrammeReport) = dbi.withHandle<Int, Exception> {
        val insert = "insert into $PROGRAMME_REPORT_TABLE " +
                "($PROGRAMME_ID, $PROGRAMME_FULL_NAME, " +
                "$PROGRAMME_SHORT_NAME, $PROGRAMME_ACADEMIC_DEGREE, $PROGRAMME_TOTAL_CREDITS, " +
                "$PROGRAMME_DURATION, $PROGRAMME_REPORTED_BY, $PROGRAMME_TIMESTAMP) " +
                "values(:programmeId, :fullName, :shortName, :academicDegree, " +
                ":totalCredits, :duration, :reportedBy, :timestamp)"
        it.createUpdate(insert)
                .bind("programmeId", programmeId)
                .bind("fullName", programmeReport.programmeFullName)
                .bind("shortName", programmeReport.programmeShortName)
                .bind("academicDegree", programmeReport.programmeAcademicDegree)
                .bind("totalCredits", programmeReport.programmeTotalCredits)
                .bind("duration", programmeReport.programmeDuration)
                .bind("reportedBy", programmeReport.reportedBy)
                .bind("timestamp", programmeReport.timestamp)
                .execute()
    }

    override fun getAllReportsOfProgramme(programmeId: Int): List<ProgrammeReport> = dbi.withHandle<List<ProgrammeReport>, Exception> {
        val select = "select * from $PROGRAMME_REPORT_TABLE where $PROGRAMME_ID = :programmeId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .mapTo(ProgrammeReport::class.java).list()
    }

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReport = dbi.withHandle<ProgrammeReport, Exception> {
        val select = "select * from $PROGRAMME_REPORT_TABLE where $PROGRAMME_ID = :programmeId and $PROGRAMME_REPORT_ID = :reportId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .bind("reportId", reportId)
                .mapTo(ProgrammeReport::class.java).findOnly()
    }

    override fun deleteAllReportsOnProgramme(programmeId: Int) = dbi.useHandle<Exception> {
        val delete = "delete from $PROGRAMME_REPORT_TABLE where $PROGRAMME_ID = :programmeId"
        it.createUpdate(delete)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun deleteReportOnProgramme(programmeId: Int, reportId: Int) = dbi.withHandle<Int,Exception> {
        val delete = "delete from $PROGRAMME_REPORT_TABLE where $PROGRAMME_ID = :programmeId and $PROGRAMME_REPORT_ID = :reportId"
        it.createUpdate(delete)
                .bind("programmeId", programmeId)
                .bind("reportId", reportId)
                .execute()
    }

    override fun voteOnReportedProgramme(reportId: Int, vote: Vote) = dbi.useTransaction<Exception> {
        val voteQuery = "select $PROGRAMME_VOTES from $PROGRAMME_REPORT_TABLE where $PROGRAMME_REPORT_ID = :reportId"
        var votes = it.createQuery(voteQuery)
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $PROGRAMME_REPORT_TABLE set $PROGRAMME_VOTES = :votes where $PROGRAMME_REPORT_ID = :reportId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun addToProgrammeVersion(programme: Programme) = dbi.withHandle<Int,Exception> {
        val insert = "insert into $PROGRAMME_VERSION_TABLE " +
                "($PROGRAMME_ID, $PROGRAMME_VERSION, $PROGRAMME_FULL_NAME, $PROGRAMME_SHORT_NAME, " +
                "$PROGRAMME_ACADEMIC_DEGREE, $PROGRAMME_TOTAL_CREDITS," +
                "$PROGRAMME_DURATION, $PROGRAMME_CREATED_BY, $PROGRAMME_TIMESTAMP)" +
                "values (:programmeId, :programmeVersion, :programmeFullName, :programmeShortName," +
                ":programmeAcademicDegree, :programmeTotalCredits, :programmeDuration, :programmeCreatedBy, :timestamp)"
        it.createUpdate(insert)
                .bind("programmeId", programme.programmeId)
                .bind("programmeVersion", programme.version)
                .bind("programmeFullName", programme.fullName)
                .bind("programmeShortName", programme.shortName)
                .bind("programmeAcademicDegree", programme.academicDegree)
                .bind("programmeTotalCredits", programme.totalCredits)
                .bind("programmeDuration", programme.duration)
                .bind("programmeCreatedBy", programme.createdBy)
                .bind("timestamp", programme.timestamp)
                .execute()
    }

    override fun updateCourseProgramme(programmeId: Int, courseId: Int, updatedCourseProgramme: Course) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}