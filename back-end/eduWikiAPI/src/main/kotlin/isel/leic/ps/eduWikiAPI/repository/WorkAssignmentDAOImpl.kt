package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import org.jdbi.v3.core.Jdbi
import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class WorkAssignmentDAOImpl : WorkAssignmentDAO {

    companion object {
        //TABLE NAMES
        const val WRK_ASS_TABLE = "work_assignment"
        const val WRK_ASS_VERSION_TABLE = "work_assignment_version"
        const val WRK_ASS_REPORT_TABLE = "work_assignment_report"
        const val WRK_ASS_STAGE_TABLE = "work_assignment_stage"
        // FIELDS
        const val CRS_MISC_UNIT_ID = "id"
        const val WRK_ASS_VERSION = "work_assignment_version"
        const val WRK_ASS_SHEET = "sheet"
        const val WRK_ASS_SUPPLEMENT = "supplement"
        const val WRK_ASS_DUE_DATE = "due_date"
        const val WRK_ASS_INDIVIDUAL = "individual"
        const val WRK_ASS_LATE_DELIVERY = "late_delivery"
        const val WRK_ASS_MULTIPLE_DELIVERIES = "multiple_deliveries"
        const val WRK_ASS_REQUIRES_REPORT = "requires_report"
        const val WRK_ASS_VOTE = "votes"
        const val WRK_ASS_TIMESTAMP = "time_stamp"
        const val WRK_ASS_REPORT_ID = "report_id"
        const val WRK_ASS_CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var dsl: DSLContext
    @Autowired
    lateinit var dbi: Jdbi

    override fun getWorkAssignment(courseMiscUnitId: Int): WorkAssignment = dbi.withHandle<WorkAssignment, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(WRK_ASS_VERSION),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_VOTE)
                )
                .from(table(WRK_ASS_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        ).mapTo(WorkAssignment::class.java).findOnly()
    }

    override fun getAllWorkAssignment(): List<WorkAssignment> = dbi.withHandle<List<WorkAssignment>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(WRK_ASS_VERSION),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_VOTE)
                )
                .from(table(WRK_ASS_TABLE))
                .sql
        ).mapTo(WorkAssignment::class.java).list()
    }


    override fun deleteWorkAssignment(courseMiscUnitId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(WRK_ASS_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        )
    }

    override fun deleteAllWorkAssignments(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(WRK_ASS_TABLE))
                .sql
        )
    }

    override fun updateWorkAssignment(workAssignment: WorkAssignment): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createWorkAssignment(workAssignment: WorkAssignment) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(WRK_ASS_TABLE),
                        field(CRS_MISC_UNIT_ID),
                        field(WRK_ASS_VERSION),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_VOTE)
                )
                .values(
                        workAssignment.id,
                        workAssignment.version,
                        workAssignment.createdBy,
                        workAssignment.sheet,
                        workAssignment.supplement,
                        workAssignment.dueDate,
                        workAssignment.individual,
                        workAssignment.lateDelivery,
                        workAssignment.multipleDeliveries,
                        workAssignment.requiresReport,
                        workAssignment.votes
                ).sql
        )
    }

    override fun voteOnWorkAssignment(courseMiscUnitId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field(WRK_ASS_VOTE))
                .from(table(WRK_ASS_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(WRK_ASS_TABLE))
                .set(field(WRK_ASS_VOTE), if (voteType == -1) votes.dec() else votes.inc())
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        )
    }

    override fun getWorkAssignmentStage(courseMiscUnitStageId: Int): WorkAssignmentStage = dbi.withHandle<WorkAssignmentStage, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTE),
                        field(WRK_ASS_TIMESTAMP)
                )
                .from(table(WRK_ASS_STAGE_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        ).mapTo(WorkAssignmentStage::class.java).findOnly()
    }

    override fun getAllWorkAssignmentStages(): List<WorkAssignmentStage> = dbi.withHandle<List<WorkAssignmentStage>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTE),
                        field(WRK_ASS_TIMESTAMP)
                )
                .from(table(WRK_ASS_STAGE_TABLE))
                .sql
        ).mapTo(WorkAssignmentStage::class.java).toList()
    }

    override fun deleteWorkAssignmentStage(courseMiscUnitStageId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(WRK_ASS_STAGE_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        )
    }

    override fun deleteAllWorkAssignmentStages(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(WRK_ASS_STAGE_TABLE))
                .sql
        )
    }

    override fun createWorkAssignmentStage(workAssignmentStage: WorkAssignmentStage) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(WRK_ASS_STAGE_TABLE),
                        field(CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTE),
                        field(WRK_ASS_TIMESTAMP)
                )
                .values(
                        workAssignmentStage.workAssignmentId,
                        workAssignmentStage.sheet,
                        workAssignmentStage.supplement,
                        workAssignmentStage.dueDate,
                        workAssignmentStage.individual,
                        workAssignmentStage.lateDelivery,
                        workAssignmentStage.multipleDeliveries,
                        workAssignmentStage.requiresReport,
                        workAssignmentStage.createdBy,
                        workAssignmentStage.votes,
                        workAssignmentStage.timestamp
                ).sql
        )
    }

    override fun voteOnWorkAssignmentStage(courseMiscUnitStageId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field(WRK_ASS_VOTE))
                .from(table(WRK_ASS_STAGE_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(WRK_ASS_STAGE_TABLE))
                .set(field(WRK_ASS_VOTE), if (voteType == -1) votes.dec() else votes.inc())
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        )
    }

    override fun getVersionWorkAssignment(versionWorkAssignmentId: Int, version: Int): WorkAssignmentVersion = dbi.withHandle<WorkAssignmentVersion, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTE),
                        field(WRK_ASS_TIMESTAMP),
                        field(WRK_ASS_VERSION)
                )
                .from(table(WRK_ASS_VERSION_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(versionWorkAssignmentId).and(field(WRK_ASS_VERSION).eq(version)))
                .sql
        ).mapTo(WorkAssignmentVersion::class.java).first()
    }

    override fun getAllVersionWorkAssignments(): List<WorkAssignmentVersion> = dbi.withHandle<List<WorkAssignmentVersion>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTE),
                        field(WRK_ASS_TIMESTAMP),
                        field(WRK_ASS_VERSION)
                )
                .from(table(WRK_ASS_VERSION_TABLE))
                .sql
        ).mapTo(WorkAssignmentVersion::class.java).list()
    }

    override fun deleteVersionWorkAssignment(versionWorkAssignmentId: Int, version: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(WRK_ASS_VERSION_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(versionWorkAssignmentId).and(field(WRK_ASS_VERSION).eq(version)))
                .sql
        )
    }

    override fun deleteAllVersionWorkAssignments(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(WRK_ASS_VERSION_TABLE))
                .sql
        )
    }

    override fun createVersionWorkAssignment(workAssignmentVersion: WorkAssignmentVersion) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(WRK_ASS_VERSION_TABLE),
                        field(CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_TIMESTAMP),
                        field(WRK_ASS_VERSION)
                )
                .values(
                        workAssignmentVersion.courseMiscUnitId,
                        workAssignmentVersion.sheet,
                        workAssignmentVersion.supplement,
                        workAssignmentVersion.dueDate,
                        workAssignmentVersion.individual,
                        workAssignmentVersion.lateDelivery,
                        workAssignmentVersion.multipleDeliveries,
                        workAssignmentVersion.requiresReport,
                        workAssignmentVersion.createdBy,
                        workAssignmentVersion.timestamp,
                        workAssignmentVersion.version
                ).sql
        )
    }

    override fun reportWorkAssignment(workAssignmentReport: WorkAssignmentReport) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(WRK_ASS_REPORT_TABLE),
                        field(WRK_ASS_REPORT_ID),
                        field(CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTE)
                )
                .values(
                        workAssignmentReport.reportId,
                        workAssignmentReport.courseMiscUnitId,
                        workAssignmentReport.sheet,
                        workAssignmentReport.supplement,
                        workAssignmentReport.dueDate,
                        workAssignmentReport.individual,
                        workAssignmentReport.lateDelivery,
                        workAssignmentReport.multipleDeliveries,
                        workAssignmentReport.requiresReport,
                        workAssignmentReport.createdBy,
                        workAssignmentReport.votes
                ).sql
        )
    }

    override fun deleteReportOnWorkAssignment(reportId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(WRK_ASS_REPORT_TABLE))
                .where(field(WRK_ASS_REPORT_ID).eq(reportId))
                .sql
        )
    }

    override fun deleteAllReportsOnWorkAssignment(courseMiscUnitId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(WRK_ASS_REPORT_TABLE))
                .where(field(CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        )
    }

    override fun deleteAllReports(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl.
                delete(table(WRK_ASS_REPORT_TABLE))
                .sql
        )
    }

}