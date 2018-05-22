package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Exam
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ExamVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ExamDAO
import org.jdbi.v3.core.Jdbi
import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ExamDAOImpl : ExamDAO {

    companion object {
        //TABLE NAMES
        const val EXM_TABLE = "exam"
        const val EXM_VERSION_TABLE = "exam_version"
        const val EXM_REPORT_TABLE = "exam_report"
        const val EXM_STAGE_TABLE = "exam_stage"
        // FIELDS
        const val CRS_MISC_UNIT_ID = "id"
        const val EXM_VERSION = "exam_version"
        const val EXM_SHEET = "sheet"
        const val EXM_DUE_DATE = "due_date"
        const val EXM_TYPE = "exam_type"
        const val EXM_PHASE = "phase"
        const val EXM_LOCATION = "location"
        const val EXM_VOTE = "votes"
        const val EXM_TIMESTAMP = "time_stamp"
        const val EXM_REPORT_ID = "report_id"
        const val EXM_CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var dsl: DSLContext
    @Autowired
    lateinit var dbi: Jdbi

    override fun getExam(courseMiscUnitId: Int): Exam = dbi.withHandle<Exam, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(EXM_VERSION),
                        field(EXM_CREATED_BY),
                        field(EXM_SHEET),
                        field(EXM_DUE_DATE),
                        field(EXM_TYPE),
                        field(EXM_PHASE),
                        field(EXM_LOCATION),
                        field(EXM_VOTE)
                )
                .from(table(EXM_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        ).mapTo(Exam::class.java).findOnly()
    }

    override fun getAllExams(): List<Exam> = dbi.withHandle<List<Exam>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(EXM_VERSION),
                        field(EXM_CREATED_BY),
                        field(EXM_SHEET),
                        field(EXM_DUE_DATE),
                        field(EXM_TYPE),
                        field(EXM_PHASE),
                        field(EXM_LOCATION),
                        field(EXM_VOTE)
                )
                .from(table(EXM_TABLE))
                .sql
        ).mapTo(Exam::class.java).list()
    }

    override fun deleteExam(courseMiscUnitId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(EXM_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        )
    }

    override fun deleteAllExams(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(EXM_TABLE))
                .sql
        )
    }

    override fun updateExam(exam: Exam): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createExam(exam: Exam) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(EXM_TABLE),
                        field(CRS_MISC_UNIT_ID),
                        field(EXM_VERSION),
                        field(EXM_CREATED_BY),
                        field(EXM_SHEET),
                        field(EXM_DUE_DATE),
                        field(EXM_TYPE),
                        field(EXM_PHASE),
                        field(EXM_LOCATION),
                        field(EXM_VOTE)
                )
                .values(
                        exam.id,
                        exam.version,
                        exam.createdBy,
                        exam.sheet,
                        exam.dueDate,
                        exam.type,
                        exam.phase,
                        exam.location,
                        exam.votes
                ).sql
        )
    }

    override fun voteOnExam(courseMiscUnitId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field(EXM_VOTE))
                .from(table(EXM_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(EXM_TABLE))
                .set(field(EXM_VOTE), if(voteType == -1) votes.dec() else votes.inc())
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        )
    }

    override fun getExamStage(courseMiscUnitStageId: Int): ExamStage = dbi.withHandle<ExamStage, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(EXM_SHEET),
                        field(EXM_DUE_DATE),
                        field(EXM_TYPE),
                        field(EXM_PHASE),
                        field(EXM_LOCATION),
                        field(EXM_CREATED_BY),
                        field(EXM_VOTE),
                        field(EXM_TIMESTAMP)
                )
                .from(table(EXM_STAGE_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        ).mapTo(ExamStage::class.java).findOnly()
    }

    override fun getAllExamStages(): List<ExamStage> = dbi.withHandle<List<ExamStage>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(EXM_SHEET),
                        field(EXM_DUE_DATE),
                        field(EXM_TYPE),
                        field(EXM_PHASE),
                        field(EXM_LOCATION),
                        field(EXM_CREATED_BY),
                        field(EXM_VOTE),
                        field(EXM_TIMESTAMP)
                )
                .from(table(EXM_STAGE_TABLE))
                .sql
        ).mapTo(ExamStage::class.java).toList()
    }

    override fun deleteExamStage(courseMiscUnitStageId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(EXM_STAGE_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        )
    }

    override fun deleteAllExamStages(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(EXM_STAGE_TABLE))
                .sql
        )
    }

    override fun createExamStage(examStage: ExamStage) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(EXM_STAGE_TABLE),
                        field(CRS_MISC_UNIT_ID),
                        field(EXM_SHEET),
                        field(EXM_DUE_DATE),
                        field(EXM_TYPE),
                        field(EXM_PHASE),
                        field(EXM_LOCATION),
                        field(EXM_CREATED_BY),
                        field(EXM_VOTE),
                        field(EXM_TIMESTAMP)
                )
                .values(
                        examStage.exameId,
                        examStage.sheet,
                        examStage.dueDate,
                        examStage.type,
                        examStage.phase,
                        examStage.location,
                        examStage.createdBy,
                        examStage.votes,
                        examStage.timestamp

                ).sql
        )
    }

    override fun voteOnExamStage(courseMiscUnitStageId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field(EXM_VOTE))
                .from(table(EXM_STAGE_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(EXM_STAGE_TABLE))
                .set(field(EXM_VOTE), if (voteType == -1) votes.dec() else votes.inc())
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        )
    }

    override fun getVersionExam(versionExamId: Int, version: Int): ExamVersion = dbi.withHandle<ExamVersion, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(EXM_SHEET),
                        field(EXM_DUE_DATE),
                        field(EXM_TYPE),
                        field(EXM_PHASE),
                        field(EXM_LOCATION),
                        field(EXM_CREATED_BY),
                        field(EXM_VERSION),
                        field(EXM_TIMESTAMP)
                )
                .from(table(EXM_VERSION_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(versionExamId).and(field(EXM_VERSION).eq(version)))
                .sql
        ).mapTo(ExamVersion::class.java).first()
    }

    override fun getAllVersionExams(): List<ExamVersion> = dbi.withHandle<List<ExamVersion>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(EXM_SHEET),
                        field(EXM_DUE_DATE),
                        field(EXM_TYPE),
                        field(EXM_PHASE),
                        field(EXM_LOCATION),
                        field(EXM_CREATED_BY),
                        field(EXM_VERSION),
                        field(EXM_TIMESTAMP)
                )
                .from(table(EXM_VERSION_TABLE))
                .sql
        ).mapTo(ExamVersion::class.java).list()
    }

    override fun deleteVersionExam(versionExamId: Int, version: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(EXM_VERSION_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(versionExamId).and(field(EXM_VERSION).eq(version)))
                .sql
        )
    }
    override fun deleteAllVersionExams(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(EXM_VERSION_TABLE))
                .sql
        )
    }

    override fun createVersionExam(examVersion: ExamVersion) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(EXM_VERSION_TABLE),
                        field(CRS_MISC_UNIT_ID),
                        field(EXM_SHEET),
                        field(EXM_DUE_DATE),
                        field(EXM_TYPE),
                        field(EXM_PHASE),
                        field(EXM_LOCATION),
                        field(EXM_CREATED_BY),
                        field(EXM_VERSION),
                        field(EXM_TIMESTAMP))
                .values(
                        examVersion.courseMiscUnitId,
                        examVersion.sheet,
                        examVersion.dueDate,
                        examVersion.type,
                        examVersion.phase,
                        examVersion.location,
                        examVersion.createdBy,
                        examVersion.version,
                        examVersion.timestamp
                ).sql
        )
    }

    override fun reportExam(examReport: ExamReport) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(EXM_REPORT_TABLE),
                        field(EXM_REPORT_ID),
                        field(CRS_MISC_UNIT_ID),
                        field(EXM_SHEET),
                        field(EXM_DUE_DATE),
                        field(EXM_TYPE),
                        field(EXM_PHASE),
                        field(EXM_LOCATION),
                        field(EXM_CREATED_BY),
                        field(EXM_VOTE))
                .values(
                        examReport.reportId,
                        examReport.courseMiscUnitId,
                        examReport.sheet,
                        examReport.dueDate,
                        examReport.type,
                        examReport.phase,
                        examReport.location,
                        examReport.reportedBy,
                        examReport.votes
                ).sql
        )
    }

    override fun deleteReportOnExam(reportId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(EXM_REPORT_TABLE))
                .where(field(EXM_REPORT_ID).eq(reportId))
                .sql
        )
    }

    override fun deleteAllReportsOnExam(courseMiscUnitId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(EXM_REPORT_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        )
    }

    override fun deleteAllReports(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl.
                delete(table(EXM_REPORT_TABLE))
                .sql
        )
    }

}