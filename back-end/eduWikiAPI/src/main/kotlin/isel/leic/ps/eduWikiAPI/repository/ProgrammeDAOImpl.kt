package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Programme
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
        const val PROG_VERSION_TABLE = "programme_version"
        const val PROG_REPORT_TABLE = "programme_report"
        const val PROG_STAGE_TABLE = "programme_stage"
        // FIELDS
        const val PROG_ID = "programme_id"
        const val PROG_VERSION = "programme_version"
        const val PROG_FULL_NAME = "programme_full_name"
        const val PROG_SHORT_NAME = "programme_short_name"
        const val PROG_ACADEMIC_DEGREE = "programme_academic_degree"
        const val PROG_TOTAL_CREDITS = "programme_total_credits"
        const val PROG_DURATION = "programme_duration"
        const val PROG_VOTE = "votes"
        const val PROG_TIMESTAMP = "time_stamp"
        const val PROG_REPORT_ID = "report_id"
        const val CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var dsl: DSLContext
    @Autowired
    lateinit var dbi: Jdbi

    override fun getProgramme(programmeId: Int): Programme = dbi.withHandle<Programme, Exception> {
        it.createQuery(dsl
                .select(
                        field(PROG_ID),
                        field(PROG_VERSION),
                        field(PROG_FULL_NAME),
                        field(PROG_SHORT_NAME),
                        field(PROG_ACADEMIC_DEGREE),
                        field(PROG_TOTAL_CREDITS),
                        field(PROG_DURATION),
                        field(CREATED_BY)
                )
                .from(table(PROG_TABLE))
                .where(field(PROG_ID).eq(programmeId))
                .sql
        ).mapTo(Programme::class.java).findOnly()
    }

    override fun getAllProgrammes(): List<Programme> = dbi.withHandle<List<Programme>, Exception> {
        it.createQuery(dsl
                .select(
                        field(PROG_ID),
                        field(PROG_VERSION),
                        field(PROG_FULL_NAME),
                        field(PROG_SHORT_NAME),
                        field(PROG_ACADEMIC_DEGREE),
                        field(PROG_TOTAL_CREDITS),
                        field(PROG_DURATION),
                        field(CREATED_BY)
                )
                .from(table(PROG_TABLE))
                .sql
        ).mapTo(Programme::class.java).list()
    }

    override fun deleteProgramme(programmeId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(PROG_TABLE))
                .where(field(PROG_ID).eq(programmeId))
                .sql
        )
    }

    override fun deleteAllProgrammes(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(PROG_TABLE))
                .sql
        )
    }

    override fun updateProgramme(programme: Programme, user: String): Int = TODO("dynamically update org by filled values in Programme parameter")

    override fun createProgramme(programme: Programme) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(PROG_TABLE),
                        field(PROG_ID),
                        field(PROG_VERSION),
                        field(PROG_FULL_NAME),
                        field(PROG_SHORT_NAME),
                        field(PROG_ACADEMIC_DEGREE),
                        field(PROG_TOTAL_CREDITS),
                        field(PROG_DURATION),
                        field(CREATED_BY)
                )
                .values(
                        programme.id,
                        programme.version,
                        programme.fullName,
                        programme.shortName,
                        programme.academicDegree,
                        programme.totalCredits,
                        programme.duration,
                        programme.createdBy
                ).sql
        )
    }

    override fun voteOnProgramme(programmeId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field(PROG_VOTE))
                .from(table(PROG_TABLE))
                .where(field(PROG_ID).eq(programmeId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(PROG_TABLE))
                .set(field(PROG_VOTE), if (voteType == -1) votes.dec() else votes.inc())
                .where(field(PROG_ID).eq(programmeId))
                .sql
        )
    }

    override fun getProgrammeStage(programmeId: Int): ProgrammeStage = dbi.withHandle<ProgrammeStage, Exception> {
        it.createQuery(dsl
                .select(
                        field(PROG_ID),
                        field(PROG_VERSION),
                        field(PROG_FULL_NAME),
                        field(PROG_SHORT_NAME),
                        field(PROG_ACADEMIC_DEGREE),
                        field(PROG_TOTAL_CREDITS),
                        field(PROG_DURATION),
                        field(CREATED_BY),
                        field(PROG_TIMESTAMP)
                )
                .from(table(PROG_STAGE_TABLE))
                .where(field(PROG_ID).eq(programmeId))
                .sql
        ).mapTo(ProgrammeStage::class.java).findOnly()
    }

    override fun getAllProgrammeStages(): List<ProgrammeStage> = dbi.withHandle<List<ProgrammeStage>, Exception> {
        it.createQuery(dsl
                .select(
                        field(PROG_ID),
                        field(PROG_VERSION),
                        field(PROG_FULL_NAME),
                        field(PROG_SHORT_NAME),
                        field(PROG_ACADEMIC_DEGREE),
                        field(PROG_TOTAL_CREDITS),
                        field(PROG_DURATION),
                        field(CREATED_BY),
                        field(PROG_TIMESTAMP)
                )
                .from(table(PROG_STAGE_TABLE))
                .sql
        ).mapTo(ProgrammeStage::class.java).toList()
    }

    override fun deleteProgrammeStage(programmeId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(PROG_STAGE_TABLE))
                .where(field(PROG_ID).eq(programmeId))
                .sql
        )
    }

    override fun deleteAllProgrammeStages(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(PROG_STAGE_TABLE))
                .sql
        )
    }

    override fun createProgrammeStage(programmeStage: ProgrammeStage) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(PROG_STAGE_TABLE),
                        field(PROG_ID),
                        field(PROG_VERSION),
                        field(PROG_FULL_NAME),
                        field(PROG_SHORT_NAME),
                        field(PROG_ACADEMIC_DEGREE),
                        field(PROG_TOTAL_CREDITS),
                        field(PROG_DURATION),
                        field(PROG_TIMESTAMP),
                        field(CREATED_BY)
                )
                .values(
                        programmeStage.programmeId,
                        programmeStage.version,
                        programmeStage.fullName,
                        programmeStage.shortName,
                        programmeStage.academicDegree,
                        programmeStage.totalCredits,
                        programmeStage.duration,
                        programmeStage.timestamp,
                        programmeStage.createdBy
                ).sql
        )
    }

    override fun voteOnProgrammeStage(programmeId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field(PROG_VOTE))
                .from(table(PROG_STAGE_TABLE))
                .where(field(PROG_ID).eq(programmeId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(PROG_STAGE_TABLE))
                .set(field(PROG_VOTE), if (voteType == -1) votes.dec() else votes.inc())
                .where(field(PROG_ID).eq(programmeId))
                .sql
        )
    }

    override fun getVersionProgramme(versionProgrammeId: Int, version: Int): ProgrammeVersion = dbi.withHandle<ProgrammeVersion, Exception> {
        it.createQuery(dsl
                .select(
                        field(PROG_ID),
                        field(PROG_VERSION),
                        field(PROG_FULL_NAME),
                        field(PROG_SHORT_NAME),
                        field(PROG_ACADEMIC_DEGREE),
                        field(PROG_TOTAL_CREDITS),
                        field(PROG_DURATION),
                        field(CREATED_BY),
                        field(PROG_VOTE),
                        field(PROG_TIMESTAMP)
                )
                .from(table(PROG_VERSION_TABLE))
                .where(field(PROG_ID).eq(versionProgrammeId).and(field(PROG_VERSION).eq(version)))
                .sql
        ).mapTo(ProgrammeVersion::class.java).first()
    }

    override fun getAllVersionProgrammes(): List<ProgrammeVersion> = dbi.withHandle<List<ProgrammeVersion>, Exception> {
        it.createQuery(dsl
                .select(
                        field(PROG_ID),
                        field(PROG_VERSION),
                        field(PROG_FULL_NAME),
                        field(PROG_SHORT_NAME),
                        field(PROG_ACADEMIC_DEGREE),
                        field(PROG_TOTAL_CREDITS),
                        field(PROG_DURATION),
                        field(CREATED_BY),
                        field(PROG_VOTE),
                        field(PROG_TIMESTAMP)
                )
                .from(table(PROG_VERSION_TABLE))
                .sql
        ).mapTo(ProgrammeVersion::class.java).list()
    }

    override fun deleteVersionProgramme(versionProgrammeId: Int, version: Int) = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(PROG_VERSION_TABLE))
                .where(field(PROG_ID).eq(versionProgrammeId).and(field(PROG_VERSION).eq(version)))
                .sql
        )
    }

    override fun deleteAllVersionProgrammes(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(PROG_VERSION_TABLE))
                .sql
        )
    }

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
                        field(CREATED_BY)
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

    override fun reportProgramme(programmeReport: ProgrammeReport) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(PROG_REPORT_TABLE),
                        field(PROG_REPORT_ID),
                        field(PROG_ID),
                        field(PROG_FULL_NAME),
                        field(PROG_SHORT_NAME),
                        field(PROG_ACADEMIC_DEGREE),
                        field(PROG_TOTAL_CREDITS),
                        field(PROG_DURATION),
                        field(CREATED_BY),
                        field(PROG_VOTE)
                )
                .values(
                        programmeReport.reportId,
                        programmeReport.programmeId,
                        programmeReport.programmeFullName,
                        programmeReport.programmeShortName,
                        programmeReport.programmeAcademicDegree,
                        programmeReport.programmeTotalCredits,
                        programmeReport.programmeDuration,
                        programmeReport.createdBy,
                        programmeReport.votes
                ).sql
        )
    }

    override fun deleteAllReportsOnProgramme(programmeId : Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(PROG_REPORT_TABLE))
                .where(field(PROG_ID).eq(programmeId))
                .sql
        )
    }

    override fun deleteReportOnProgramme(reportId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(PROG_REPORT_TABLE))
                .where(field(PROG_REPORT_ID).eq(reportId))
                .sql
        )
    }

    override fun deleteAllReports(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl.
                delete(table(PROG_REPORT_TABLE))
                .sql
        )
    }

}