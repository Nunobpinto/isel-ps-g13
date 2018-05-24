package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ProgrammeDAO
import org.jdbi.v3.core.Jdbi
import org.jooq.DSLContext
import org.jooq.impl.DSL.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ProgrammeDAOImpl : ProgrammeDAO {

    companion object {
        //TABLE NAMES
        const val PROG_TABLE = "programme"
        const val CRS_PROG_TABLE = "course_programme"
        const val PROG_VERSION_TABLE = "programme_version"
        const val PROG_REPORT_TABLE = "programme_report"
        const val PROG_STAGE_TABLE = "programme_stage"
        // FIELDS
        const val PROG_ID = "programme_id"
        const val CRS_ID = "course_id"
        const val PROG_VERSION = "programme_version"
        const val CRS_PROG_VERSION = "course_programme_version"
        const val CRS_LECT_TERM = "course_lectured_term"
        const val CRS_OPT = "course_optional"
        const val CRS_CRED = "course_credits"
        const val PROG_FULL_NAME = "programme_full_name"
        const val PROG_SHORT_NAME = "programme_short_name"
        const val PROG_ACADEMIC_DEGREE = "programme_academic_degree"
        const val PROG_TOTAL_CREDITS = "programme_total_credits"
        const val PROG_DURATION = "programme_duration"
        const val PROG_VOTE = "votes"
        const val PROG_TIMESTAMP = "time_stamp"
        const val PROG_REPORT_ID = "report_id"
        const val PROG_CREATED_BY = "created_by"
        const val PROG_REPORTED_BY = "reported_by"

    }

    @Autowired
    lateinit var dsl: DSLContext
    @Autowired
    lateinit var dbi: Jdbi

    override fun getSpecificProgramme(programmeId: Int): Programme = dbi.withHandle<Programme, Exception> {
        val select = "select * from $PROG_TABLE where $PROG_ID = :programmeId"
        it.createQuery(select).bind("programmeId", programmeId).mapTo(Programme::class.java).findOnly()
    }

    override fun getAllProgrammes(): List<Programme> = dbi.withHandle<List<Programme>, Exception> {
        val select = "select * from $PROG_TABLE"
        it.createQuery(select).mapTo(Programme::class.java).list()
    }

    override fun deleteSpecificProgramme(programmeId: Int) = dbi.useHandle<Exception> {
        val delete = "delete from $PROG_TABLE where $PROG_ID = :programmeId"
        it.createUpdate(delete)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun deleteAllProgrammes() = dbi.useHandle<Exception> {
        it.createUpdate("delete from $PROG_TABLE").execute()
    }

    override fun updateProgramme(programmeId: Int, programme: Programme) = dbi.useHandle<Exception> {
        val update = "update $PROG_TABLE SET " +
                "$PROG_VERSION = :version, $PROG_CREATED_BY = :createdBy, " +
                "$PROG_FULL_NAME = :fullName, $PROG_SHORT_NAME = :shortName, " +
                "$PROG_ACADEMIC_DEGREE = :academicDegree, $PROG_TOTAL_CREDITS = :totalCredits, " +
                "$PROG_DURATION = :duration, $PROG_VOTE = :votes, $PROG_TIMESTAMP = :timestamp " +
                "where $PROG_ID = :programmeId"

        it.createUpdate(update)
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
                .execute()
    }

    override fun createProgramme(programme: Programme) = dbi.useHandle<Exception> {
        val insert = "insert into $PROG_TABLE " +
                "($PROG_VERSION, $PROG_FULL_NAME, " +
                "$PROG_SHORT_NAME, $PROG_ACADEMIC_DEGREE, $PROG_TOTAL_CREDITS, " +
                "$PROG_DURATION, $PROG_VOTE, $PROG_CREATED_BY, $PROG_TIMESTAMP) " +
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
                .execute()
    }

    override fun addCourseToProgramme(programmeId: Int, course: Course) = dbi.useHandle<Exception> {
        val insert = "insert into $CRS_PROG_TABLE " +
                "($CRS_ID, $PROG_ID, $CRS_LECT_TERM, $CRS_OPT, " +
                "$CRS_CRED, $PROG_VOTE) " +
                "values(:courseId, :programmeId, :term, :optional, :credits)"
        it.createUpdate(insert)
                .bind("courseId", course.id)
                .bind("programmeId", programmeId)
                .bind("term", course.lecturedTerm)
                .bind("optional", course.optional)
                .bind("credits", course.credits)
    }

    override fun voteOnProgramme(programmeId: Int, inputVote: VoteInputModel) = dbi.useTransaction<Exception> {
        val voteQuery = "select votes from $PROG_STAGE_TABLE where $PROG_ID = :programmeId"
        var votes = it.createQuery(voteQuery)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java).findOnly()
        votes = if (inputVote.vote.equals(Vote.Down)) --votes else ++votes
        val updateQuery = "update $PROG_TABLE set $PROG_VOTE = :votes where $PROG_ID = :programmeId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("programmeId", programmeId)
                .execute()

    }

    override fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course> = dbi.withHandle<List<Course>, Exception> {
        val select = "select * from $CRS_PROG_TABLE where $PROG_ID = :programmeId"
        it.createQuery(select).bind("programmeId", programmeId).mapTo(Course::class.java).list()
    }

    override fun getProgrammeStage(programmeId: Int): ProgrammeStage = dbi.withHandle<ProgrammeStage, Exception> {
        val select = "select * from $PROG_STAGE_TABLE where $PROG_ID = :programmeId"
        it.createQuery(select).bind("programmeId", programmeId).mapTo(ProgrammeStage::class.java).findOnly()
    }

    override fun getAllProgrammeStages(): List<ProgrammeStage> = dbi.withHandle<List<ProgrammeStage>, Exception> {
        val select = "select * from $PROG_STAGE_TABLE"
        it.createQuery(select).mapTo(ProgrammeStage::class.java).toList()
    }

    override fun deleteAllStagedProgrammes() = dbi.useHandle<Exception> {
        it.createUpdate("delete from $PROG_STAGE_TABLE").execute()
    }

    override fun createProgrammeStage(programmeStage: ProgrammeStage) = dbi.useHandle<Exception> {
        val insert = "insert into $PROG_STAGE_TABLE " +
                "($PROG_FULL_NAME, " +
                "$PROG_SHORT_NAME, $PROG_ACADEMIC_DEGREE, $PROG_TOTAL_CREDITS, " +
                "$PROG_DURATION, $PROG_VOTE, $PROG_CREATED_BY, $PROG_TIMESTAMP) " +
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

    override fun voteOnStagedProgramme(programmeId: Int, inputVote: VoteInputModel) = dbi.useTransaction<Exception> {
        val voteQuery = "select votes from $PROG_STAGE_TABLE where $PROG_ID = :programmeId"
        var votes = it.createQuery(voteQuery)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java).findOnly()
        votes = if (inputVote.vote.equals(Vote.Down)) --votes else ++votes
        val updateQuery = "update $PROG_STAGE_TABLE set $PROG_VOTE = :votes where $PROG_ID = :programmeId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun updateStagedProgramme(programmeId: Int, programme: ProgrammeStage) = dbi.useHandle<Exception>{
        val update = "update $PROG_STAGE_TABLE SET" +
                "$PROG_FULL_NAME = :fullName, $PROG_SHORT_NAME = :shortName, " +
                "$PROG_ACADEMIC_DEGREE = :academicDegree, $PROG_TOTAL_CREDITS = :totalCredits, " +
                "$PROG_DURATION = :duration, $PROG_CREATED_BY = :createdBy, $PROG_VOTE = :votes, " +
                "$PROG_TIMESTAMP = :timestamp) " +
                "where $PROG_ID = :programmeId"

        it.createUpdate(update)
                .bind("fullName", programme.fullName)
                .bind("shortName", programme.shortName)
                .bind("academicDegree", programme.academicDegree)
                .bind("totalCredits", programme.totalCredits)
                .bind("duration", programme.duration)
                .bind("createdBy", programme.createdBy)
                .bind("votes", programme.votes)
                .bind("timestamp", programme.timestamp)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun deleteStagedProgramme(stageId: Int) = dbi.useHandle<Exception> {
        val delete = "delete from $PROG_STAGE_TABLE where $PROG_ID = :programmeId"
        it.createUpdate(delete)
                .bind("programmeId", stageId)
                .execute()
    }

    override fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion> = dbi.withHandle<List<ProgrammeVersion>, Exception> {
        val query = "select * from $PROG_VERSION_TABLE where $PROG_ID = :programmeId"
        it.createQuery(query).bind("programmeId", programmeId).mapTo(ProgrammeVersion::class.java).list()
    }

    override fun getSpecificVersionOfProgramme(programmeId: Int, versionId: Int): ProgrammeVersion = dbi.withHandle<ProgrammeVersion, Exception> {
        val query = "select * from $PROG_VERSION_TABLE where $PROG_ID = :programmeId and $PROG_VERSION = :versionId"
        it.createQuery(query)
                .bind("programmeId", programmeId)
                .bind("versionId", versionId)
                .mapTo(ProgrammeVersion::class.java).findOnly()
    }

    override fun deleteVersionProgramme(versionProgrammeId: Int, version: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $PROG_VERSION_TABLE where $PROG_ID = :id and $PROG_VERSION = :version"
        it.createUpdate(delete)
                .bind("id", versionProgrammeId)
                .bind("version", version)
                .execute()
    }

    override fun deleteAllVersionsOfProgramme(programmeId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $PROG_VERSION_TABLE where $PROG_ID = :id"
        it.createUpdate(delete)
                .bind("id", programmeId)
                .execute()
    }
/*
    override fun createVersionProgramme(programmeVersion: ProgrammeVersion) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(PROG_VERSION_TABLE),
                        field(PROG_ID),
                        field(PROG_VERSION),
                        field(PROG_FULL_NAME),
                        field(PROG_SHORT_NAME),
                        field(PROG_ACADEMIC_DEGREE),
                        field(PROG_TOTAL_CREDITS),
                        field(PROG_DURATION),
                        field(PROG_TIMESTAMP),
                        field(PROG_CREATED_BY)
                )
                .values(
                        programmeVersion.programmeId,
                        programmeVersion.version,
                        programmeVersion.fullName,
                        programmeVersion.shortName,
                        programmeVersion.academicDegree,
                        programmeVersion.totalCredits,
                        programmeVersion.duration,
                        programmeVersion.timestamp,
                        programmeVersion.createdBy
                ).sql
        )
    }
*/
    override fun reportProgramme(programmeId: Int, programmeReport: ProgrammeReport) = dbi.useHandle<Exception> {
        val insert = "insert into $PROG_REPORT_TABLE " +
                "($PROG_ID, $PROG_FULL_NAME, " +
                "$PROG_SHORT_NAME, $PROG_ACADEMIC_DEGREE, $PROG_TOTAL_CREDITS, " +
                "$PROG_DURATION, $PROG_REPORTED_BY, $PROG_TIMESTAMP) " +
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
        val select = "select * from $PROG_REPORT_TABLE where $PROG_ID = :programmeId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .mapTo(ProgrammeReport::class.java).list()
    }

    override fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReport = dbi.withHandle<ProgrammeReport, Exception> {
        val select = "select * from $PROG_REPORT_TABLE where $PROG_ID = :programmeId and $PROG_REPORT_ID = :reportId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .bind("reportId", reportId)
                .mapTo(ProgrammeReport::class.java).findOnly()
    }

    override fun deleteAllReportsOnProgramme(programmeId: Int) = dbi.useHandle<Exception> {
        val delete = "delete from $PROG_REPORT_TABLE where $PROG_ID = :programmeId"
        it.createUpdate(delete)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun deleteReportOnProgramme(programmeId: Int, reportId: Int) = dbi.useHandle<Exception> {
        val delete = "delete from $PROG_REPORT_TABLE where $PROG_ID = :programmeId and $PROG_REPORT_ID = :reportId"
        it.createUpdate(delete)
                .bind("programmeId", programmeId)
                .bind("reportId", reportId)
                .execute()
    }

    override fun voteOnReportedProgramme(reportId: Int, inputVote: VoteInputModel) = dbi.useTransaction<Exception> {
        val voteQuery = "select votes from $PROG_REPORT_TABLE where $PROG_REPORT_ID = :reportId"
        var votes = it.createQuery(voteQuery)
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (inputVote.vote.equals(Vote.Down)) --votes else ++votes
        val updateQuery = "update $PROG_REPORT_TABLE set votes = :votes where $PROG_REPORT_ID = :reportId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun addToProgrammeVersion(programme: Programme) = dbi.useHandle<Exception> {
        val insert = "insert into $PROG_VERSION_TABLE " +
                "($PROG_ID, $PROG_VERSION, $PROG_FULL_NAME, $PROG_SHORT_NAME, " +
                "$PROG_ACADEMIC_DEGREE, $PROG_TOTAL_CREDITS," +
                "$PROG_DURATION, $PROG_CREATED_BY, $PROG_TIMESTAMP)" +
                "values (:programmeId, :programmeVersion, :programmeFullName, :programmeShortName," +
                ":programmeAcademicDegree, :programmeTotalCredits, :programmeDuration, :programmeCreatedBy, :timestamp)"
        it.createUpdate(insert)
                .bind("programmeId", programme.id)
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

}