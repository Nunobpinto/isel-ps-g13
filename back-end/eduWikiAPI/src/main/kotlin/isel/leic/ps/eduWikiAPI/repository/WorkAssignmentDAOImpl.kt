package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.CourseMiscUnit
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
        const val WRK_ASS_VERSION = "work_assignment_version"
        const val WRK_ASS_SHEET = "sheet"
        const val WRK_ASS_SUPPLEMENT = "supplement"
        const val WRK_ASS_DUE_DATE = "due_date"
        const val WRK_ASS_INDIVIDUAL = "individual"
        const val WRK_ASS_LATE_DELIVERY = "late_delivery"
        const val WRK_ASS_MULTIPLE_DELIVERIES = "multiple_deliveries"
        const val WRK_ASS_REQUIRES_REPORT = "requires_report"
        const val WRK_ASS_VOTES = "votes"
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
                        field(CourseDAOImpl.CRS_MISC_UNIT_ID),
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
                        field(WRK_ASS_VOTES)
                )
                .from(table(WRK_ASS_TABLE))
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        ).mapTo(WorkAssignment::class.java).findOnly()
    }

    override fun getAllWorkAssignment(): List<WorkAssignment> = dbi.withHandle<List<WorkAssignment>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CourseDAOImpl.CRS_MISC_UNIT_ID),
                        field(WRK_ASS_VERSION),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_VOTES)
                )
                .from(table(WRK_ASS_TABLE))
                .sql
        ).mapTo(WorkAssignment::class.java).list()
    }


    override fun deleteWorkAssignment(courseMiscUnitId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(WRK_ASS_TABLE))
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
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

    override fun createWorkAssignmentOnCourseInTerm(workAssignment: WorkAssignment) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(WRK_ASS_TABLE),
                        field(CourseDAOImpl.CRS_MISC_UNIT_ID),
                        field(WRK_ASS_VERSION),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_VOTES)
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
                .select(field(WRK_ASS_VOTES))
                .from(table(WRK_ASS_TABLE))
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(WRK_ASS_TABLE))
                .set(field(WRK_ASS_VOTES), if (voteType == -1) votes.dec() else votes.inc())
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        )
    }

    override fun getWorkAssignmentStage(courseMiscUnitStageId: Int): WorkAssignmentStage = dbi.withHandle<WorkAssignmentStage, Exception> {
        it.createQuery(dsl
                .select(
                        field(CourseDAOImpl.CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTES),
                        field(WRK_ASS_TIMESTAMP)
                )
                .from(table(WRK_ASS_STAGE_TABLE))
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        ).mapTo(WorkAssignmentStage::class.java).findOnly()
    }

    override fun getAllWorkAssignmentStages(): List<WorkAssignmentStage> = dbi.withHandle<List<WorkAssignmentStage>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CourseDAOImpl.CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTES),
                        field(WRK_ASS_TIMESTAMP)
                )
                .from(table(WRK_ASS_STAGE_TABLE))
                .sql
        ).mapTo(WorkAssignmentStage::class.java).toList()
    }

    override fun deleteWorkAssignmentStage(courseMiscUnitStageId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(WRK_ASS_STAGE_TABLE))
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
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
                        field(CourseDAOImpl.CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTES),
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
                .select(field(WRK_ASS_VOTES))
                .from(table(WRK_ASS_STAGE_TABLE))
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(WRK_ASS_STAGE_TABLE))
                .set(field(WRK_ASS_VOTES), if (voteType == -1) votes.dec() else votes.inc())
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(courseMiscUnitStageId))
                .sql
        )
    }

    override fun getVersionWorkAssignment(versionWorkAssignmentId: Int, version: Int): WorkAssignmentVersion = dbi.withHandle<WorkAssignmentVersion, Exception> {
        it.createQuery(dsl
                .select(
                        field(CourseDAOImpl.CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTES),
                        field(WRK_ASS_TIMESTAMP),
                        field(WRK_ASS_VERSION)
                )
                .from(table(WRK_ASS_VERSION_TABLE))
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(versionWorkAssignmentId).and(field(WRK_ASS_VERSION).eq(version)))
                .sql
        ).mapTo(WorkAssignmentVersion::class.java).first()
    }

    override fun getAllVersionWorkAssignments(): List<WorkAssignmentVersion> = dbi.withHandle<List<WorkAssignmentVersion>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CourseDAOImpl.CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTES),
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
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(versionWorkAssignmentId).and(field(WRK_ASS_VERSION).eq(version)))
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
                        field(CourseDAOImpl.CRS_MISC_UNIT_ID),
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
                        field(CourseDAOImpl.CRS_MISC_UNIT_ID),
                        field(WRK_ASS_SHEET),
                        field(WRK_ASS_SUPPLEMENT),
                        field(WRK_ASS_DUE_DATE),
                        field(WRK_ASS_INDIVIDUAL),
                        field(WRK_ASS_LATE_DELIVERY),
                        field(WRK_ASS_MULTIPLE_DELIVERIES),
                        field(WRK_ASS_REQUIRES_REPORT),
                        field(WRK_ASS_CREATED_BY),
                        field(WRK_ASS_VOTES)
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
                        workAssignmentReport.reportedBy,
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
                .where(field(CourseDAOImpl.CRS_MISC_UNIT_ID).eq(courseMiscUnitId))
                .sql
        )
    }

    override fun deleteAllReports(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl.
                delete(table(WRK_ASS_REPORT_TABLE))
                .sql
        )
    }
    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment> = dbi.withHandle<List<WorkAssignment>, Exception> {
        val select = "select * from $WRK_ASS_TABLE as W" +
                "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                "on W.${CourseDAOImpl.COURSE_MISC_UNIT_ID} = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID}" +
                "where C.${CourseDAOImpl.COURSE_ID} = :courseId and C.${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(WorkAssignment::class.java)
                .list()
    }

    override fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int): WorkAssignment = dbi.withHandle<WorkAssignment, Exception>{
        val select = "select * from $WRK_ASS_TABLE" +
                "where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :workItemId"
        it.createQuery(select)
                .bind("courseId", workAssignmentId)
                .mapTo(WorkAssignment::class.java)
                .findOnly()
    }

    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage> = dbi.withHandle<List<WorkAssignmentStage>, Exception> {
        val select = "select * from $WRK_ASS_STAGE_TABLE as W" +
                "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                "on W.${CourseDAOImpl.COURSE_MISC_UNIT_ID} = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID}" +
                "where C.${CourseDAOImpl.COURSE_ID} = :courseId and C.${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(WorkAssignmentStage::class.java)
                .list()
    }

    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStage = dbi.withHandle<WorkAssignmentStage, Exception> {
        val select = "select * from $.WRK_ASS_STAGE_TABLE as W" +
                "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                "on W.${CourseDAOImpl.COURSE_MISC_UNIT_ID} = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID}" +
                "where C.${CourseDAOImpl.COURSE_ID} = :courseId " +
                "and C.${TermDAOImpl.TERM_ID} = :termId " +
                "and ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :stageId"

        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("stageId", stageId)
                .mapTo(WorkAssignmentStage::class.java)
                .findOnly()
    }

    override fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport> = dbi.withHandle<List<WorkAssignmentReport>, Exception>{
        val select = "select * from $WRK_ASS_REPORT_TABLE" +
                "where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :workAssignmentId"
        it.createQuery(select)
                .bind("examId", workAssignmentId)
                .mapTo(WorkAssignmentReport::class.java)
                .list()
    }

    override fun getSpecificReportFromWorkAssignmentOnSpecificTermOfCourse(reportId: Int): WorkAssignmentReport = dbi.withHandle<WorkAssignmentReport, Exception>(){
        val select = "select * from $WRK_ASS_REPORT_TABLE" +
                "where ${CourseDAOImpl.COURSE_REPORT_ID} = :reportId"
        it.createQuery(select)
                .bind("reportId", reportId)
                .mapTo(WorkAssignmentReport::class.java)
                .findOnly()
    }

    override fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, workAssignment: WorkAssignment): Int = dbi.inTransaction<Int, Exception>{
        val insertInCourseMiscUnit = "insert into ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE}" +
                "(${CourseDAOImpl.COURSE_MISC_TYPE}, ${CourseDAOImpl.COURSE_ID}" +
                "${TermDAOImpl.TERM_ID}, ${CourseDAOImpl.COURSE_TIMESTAMP}" +
                "values(:miscType, :courseId, :termId, :timestamp)"

        val courseMiscUnit = it.createUpdate(insertInCourseMiscUnit)
                .bind("miscType", "Work Assignment")
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("timestamp", workAssignment.timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnit::class.java)
                .findOnly()

        val insertInWorkAssignment = "insert into $WRK_ASS_TABLE" +
                "(${CourseDAOImpl.COURSE_MISC_UNIT_ID}, $WRK_ASS_VERSION" +
                "$WRK_ASS_CREATED_BY, $WRK_ASS_SHEET, $WRK_ASS_SUPPLEMENT, $WRK_ASS_DUE_DATE, " +
                "$WRK_ASS_INDIVIDUAL, $WRK_ASS_LATE_DELIVERY, $WRK_ASS_MULTIPLE_DELIVERIES" +
                "$WRK_ASS_REQUIRES_REPORT, $WRK_ASS_VOTES, $WRK_ASS_TIMESTAMP" +
                "values(:courseMiscUnitId, :version, :createdBy, :sheet," +
                ":supplement, :dueDate, :individual, :lateDelivery, :multipleDeliveries, :requiresReport, :votes, :timestamp)"
        it.createUpdate(insertInWorkAssignment)
                .bind("courseMiscUnitId", courseMiscUnit.id)
                .bind("version", workAssignment.version)
                .bind("createdBy", workAssignment.createdBy)
                .bind("sheet", workAssignment.sheet)
                .bind("supplement", workAssignment.supplement)
                .bind("dueDate", workAssignment.dueDate)
                .bind("individual", workAssignment.individual)
                .bind("lateDelivey", workAssignment.lateDelivery)
                .bind("multipleDeliveries", workAssignment.multipleDeliveries)
                .bind("requiresReport", workAssignment.requiresReport)
                .bind("votes", workAssignment.votes)
                .bind("timestamp", workAssignment.timestamp)
                .execute()


    }


}