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

    override fun getWorkAssignment(course_misc_unitId: Int): WorkAssignment = dbi.withHandle<WorkAssignment, Exception> {
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
                .where(field(CRS_MISC_UNIT_ID).eq(course_misc_unitId))
                .sql
        ).mapTo(WorkAssignment::class.java).findOnly()
    }

    override fun getAllWorkAssignment(): List<WorkAssignment> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteWorkAssignment(workAssignmentId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllWorkAssignments(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateWorkAssignment(workAssignment: WorkAssignment, user: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createWorkAssignment(workAssignment: WorkAssignment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnWorkAssignment(workAssignmentId: Int, voteType: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWorkAssignmentStage(workAssignmentId: Int): WorkAssignmentStage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllWorkAssignmentStages(): List<WorkAssignmentStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteWorkAssignmentStage(workAssignmentId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllWorkAssignmentStages(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createWorkAssignmentStage(workAssignmentStage: WorkAssignmentStage) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnWorkAssignmentStage(workAssignmentId: Int, voteType: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getVersionWorkAssignment(versionWorkAssignmentId: Int, version: Int): WorkAssignmentVersion {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllVersionWorkAssignments(): List<WorkAssignmentVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteVersionWorkAssignment(versionWorkAssignmentId: Int, version: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllVersionWorkAssignments(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createVersionWorkAssignment(workAssignmentVersion: WorkAssignmentVersion) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reportWorkAssignment(workAssignmentReport: WorkAssignmentReport) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteReportOnWorkAssignment(reportId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllReportsOnWorkAssignment(workAssignmentId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllReports(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}