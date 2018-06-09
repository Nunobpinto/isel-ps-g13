package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.CourseMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_TYPE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class WorkAssignmentDAOImpl : WorkAssignmentDAO {

    companion object {
        //TABLE NAMES
        const val WRK_ASS_TABLE = "work_assignment"
        const val WRK_ASS_VERSION_TABLE = "work_assignment_version"
        const val WRK_ASS_REPORT_TABLE = "work_assignment_report"
        const val WRK_ASS_STAGE_TABLE = "work_assignment_stage"
        // FIELDS
        const val WRK_ASS_ID = "id"
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
        const val WRK_ASS_REPORTED_BY = "reported_by"
        const val WRK_ASS_CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var handle: Handle

    override fun getSpecificWorkAssignment(courseMiscUnitId: Int, courseId: Int, termId: Int) =
            handle.createQuery(
                    "select C.$COURSE_MISC_UNIT_ID, C.$TERM_ID, " +
                            "W.$WRK_ASS_VERSION, W.$WRK_ASS_VOTES, W.$WRK_ASS_CREATED_BY, " +
                            "W.$WRK_ASS_SHEET, W.$WRK_ASS_SUPPLEMENT, " +
                            "W.$WRK_ASS_DUE_DATE, W.$WRK_ASS_INDIVIDUAL, " +
                            "W.$WRK_ASS_LATE_DELIVERY, W.$WRK_ASS_MULTIPLE_DELIVERIES, " +
                            "W.$WRK_ASS_REQUIRES_REPORT, W.$WRK_ASS_TIMESTAMP " +
                            "from $WRK_ASS_TABLE as W " +
                            "inner join $COURSE_MISC_UNIT_TABLE as C " +
                            "on W.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID " +
                            "where C.$COURSE_ID = :courseId " +
                            "and C.$TERM_ID = :termId " +
                            "and W.$WRK_ASS_ID = :workItemId"
            )
                    .bind("workItemId", courseMiscUnitId)
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(WorkAssignment::class.java)
                    .findFirst()

    override fun getAllWorkAssignment() =
            handle.createQuery("select * from $WRK_ASS_TABLE")
                    .mapTo(WorkAssignment::class.java)
                    .list()

    override fun deleteSpecificWorkAssignment(courseMiscUnitId: Int) =
            handle.createUpdate("delete from $COURSE_MISC_UNIT_TABLE where $COURSE_MISC_UNIT_ID = :id")
                    .bind("id", courseMiscUnitId)
                    .execute()

    override fun updateWorkAssignment(workAssignmentId: Int, workAssignment: WorkAssignment) =
            handle.createUpdate(
                    "update $WRK_ASS_TABLE SET " +
                            "$WRK_ASS_VERSION = :version, $WRK_ASS_CREATED_BY = :createdBy, " +
                            "$WRK_ASS_SHEET = :sheet, $WRK_ASS_SUPPLEMENT = :supplement, $WRK_ASS_DUE_DATE = :dueDate, " +
                            "$WRK_ASS_INDIVIDUAL = :individual, $WRK_ASS_LATE_DELIVERY = :lateDelivery, " +
                            "$WRK_ASS_MULTIPLE_DELIVERIES = :multipleDeliveries, " +
                            "$WRK_ASS_REQUIRES_REPORT = :requiresReport, $WRK_ASS_VOTES = :votes, $WRK_ASS_TIMESTAMP = :timestamp " +
                            "where $COURSE_MISC_UNIT_ID = :workAssignmentId"
            )
                    .bind("version", workAssignment.version)
                    .bind("createdBy", workAssignment.createdBy)
                    .bind("sheet", workAssignment.sheet)
                    .bind("supplement", workAssignment.supplement)
                    .bind("dueDate", workAssignment.dueDate)
                    .bind("individual", workAssignment.individual)
                    .bind("lateDelivery", workAssignment.lateDelivery)
                    .bind("multipleDeliveries", workAssignment.multipleDeliveries)
                    .bind("requiresReport", workAssignment.requiresReport)
                    .bind("votes", workAssignment.votes)
                    .bind("timestamp", workAssignment.timestamp)
                    .bind("workAssignmentId", workAssignmentId)
                    .execute()

    override fun voteOnWorkAssignment(courseMiscUnitId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $WRK_ASS_VOTES from $WRK_ASS_TABLE where $WRK_ASS_ID = :workAssignmentId")
                .bind("workAssignmentId", courseMiscUnitId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate("update $WRK_ASS_TABLE set $WRK_ASS_VOTES = :votes where $WRK_ASS_ID = :workAssignmentId")
                .bind("votes", votes)
                .bind("workAssignmentId", courseMiscUnitId)
                .execute()
    }

    override fun getWorkAssignmentSpecificStageEntry(stageId: Int) =
            handle.createQuery(
                    "select * from $WRK_ASS_STAGE_TABLE " +
                            "where $COURSE_MISC_UNIT_ID = :workAssignmentId"
            )
                    .bind("workAssignmentId", stageId)
                    .mapTo(WorkAssignmentStage::class.java)
                    .findFirst()

    override fun getAllWorkAssignmentStages() =
            handle.createQuery("select * from $WRK_ASS_STAGE_TABLE")
                    .mapTo(WorkAssignmentStage::class.java)
                    .list()

    override fun deleteSpecificStagedWorkAssignment(stageId: Int) =
            handle.createUpdate(
                    "delete from $COURSE_MISC_UNIT_STAGE_TABLE " +
                            "where $COURSE_MISC_UNIT_ID = :workAssignmentId"
            )
                    .bind("workAssignmentId", stageId)
                    .execute()

    override fun getVersionOfSpecificWorkAssignment(versionWorkAssignmentId: Int, version: Int, courseId: Int, termId: Int) =
            handle.createQuery(
                    "select * from $WRK_ASS_VERSION_TABLE " +
                            "where $COURSE_MISC_UNIT_ID = :workAssignmentId and " +
                            "$WRK_ASS_VERSION = :version and " +
                            ":workAssignmentId in (SELECT $COURSE_MISC_UNIT_ID " +
                                "FROM $COURSE_MISC_UNIT_TABLE " +
                                "WHERE $COURSE_ID = :courseId and $TERM_ID = :termId)"
            )
                    .bind("workAssignmentId", versionWorkAssignmentId)
                    .bind("version", version)
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(WorkAssignmentVersion::class.java)
                    .findFirst()


    override fun deleteVersionWorkAssignment(versionWorkAssignmentId: Int, version: Int) =
            handle.createUpdate("delete from $WRK_ASS_VERSION_TABLE where $WRK_ASS_ID = :versionWorkAssignmentId and $WRK_ASS_VERSION = :version")
                    .bind("versionWorkAssignmentId", versionWorkAssignmentId)
                    .bind("version", version)
                    .execute()

    override fun deleteReportOnWorkAssignment(reportId: Int) =
            handle.createUpdate("delete from $WRK_ASS_REPORT_TABLE where $WRK_ASS_REPORT_ID = :reportId")
                    .bind("reportId", reportId)
                    .execute()

    override fun deleteAllReportsOnWorkAssignment(courseMiscUnitId: Int) =
            handle.createUpdate("delete from $WRK_ASS_REPORT_TABLE where $COURSE_MISC_UNIT_ID = :workAssignmentId")
                    .bind("workAssignmentId", courseMiscUnitId)
                    .execute()

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int) =
            handle.createQuery(
                    "select C.$COURSE_MISC_UNIT_ID, C.$TERM_ID, " +
                            "W.$WRK_ASS_VERSION, W.$WRK_ASS_VOTES, W.$WRK_ASS_CREATED_BY, " +
                            "W.$WRK_ASS_SHEET, W.$WRK_ASS_SUPPLEMENT, " +
                            "W.$WRK_ASS_DUE_DATE, W.$WRK_ASS_INDIVIDUAL, " +
                            "W.$WRK_ASS_LATE_DELIVERY, W.$WRK_ASS_MULTIPLE_DELIVERIES, " +
                            "W.$WRK_ASS_REQUIRES_REPORT, W.$WRK_ASS_TIMESTAMP " +
                            "from $WRK_ASS_TABLE as W " +
                            "inner join $COURSE_MISC_UNIT_TABLE as C " +
                            "on W.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID " +
                            "where C.$COURSE_ID = :courseId and C.$TERM_ID = :termId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(WorkAssignment::class.java)
                    .list()

    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int) =
            handle.createQuery(
                    "select C.$COURSE_MISC_UNIT_ID, " +
                            "W.$WRK_ASS_VOTES, W.$WRK_ASS_CREATED_BY, " +
                            "W.$WRK_ASS_SHEET, W.$WRK_ASS_SUPPLEMENT, " +
                            "W.$WRK_ASS_DUE_DATE, W.$WRK_ASS_INDIVIDUAL, " +
                            "W.$WRK_ASS_LATE_DELIVERY, W.$WRK_ASS_MULTIPLE_DELIVERIES, " +
                            "W.$WRK_ASS_REQUIRES_REPORT, W.$WRK_ASS_TIMESTAMP " +
                            "from $WRK_ASS_STAGE_TABLE as W " +
                            "inner join $COURSE_MISC_UNIT_STAGE_TABLE as C " +
                            "on W.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID " +
                            "where C.$COURSE_ID = :courseId and C.$TERM_ID = :termId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(WorkAssignmentStage::class.java)
                    .list()

    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int) =
            handle.createQuery(
                    "select C.$COURSE_MISC_UNIT_ID, " +
                            "W.$WRK_ASS_VOTES, W.$WRK_ASS_CREATED_BY, " +
                            "W.$WRK_ASS_SHEET, W.$WRK_ASS_SUPPLEMENT, " +
                            "W.$WRK_ASS_DUE_DATE, W.$WRK_ASS_INDIVIDUAL, " +
                            "W.$WRK_ASS_LATE_DELIVERY, W.$WRK_ASS_MULTIPLE_DELIVERIES, " +
                            "W.$WRK_ASS_REQUIRES_REPORT, W.$WRK_ASS_TIMESTAMP " +
                            "from $WRK_ASS_STAGE_TABLE as W " +
                            "inner join $COURSE_MISC_UNIT_STAGE_TABLE as C " +
                            "on W.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID " +
                            "where C.$COURSE_ID = :courseId " +
                            "and C.$TERM_ID = :termId " +
                            "and W.$COURSE_MISC_UNIT_ID = :stageId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .bind("stageId", stageId)
                    .mapTo(WorkAssignmentStage::class.java)
                    .findFirst()

    override fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int) =
            handle.createQuery(
                    "select * from $WRK_ASS_REPORT_TABLE " +
                            "where $COURSE_MISC_UNIT_ID = :workAssignmentId"
            )
                    .bind("workAssignmentId", workAssignmentId)
                    .mapTo(WorkAssignmentReport::class.java)
                    .list()

    override fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, workAssignment: WorkAssignment): Optional<WorkAssignment> {
        val courseMiscUnit = handle.createUpdate(
                "insert into $COURSE_MISC_UNIT_TABLE " +
                        "($COURSE_MISC_TYPE, $COURSE_ID, $TERM_ID)" +
                        "values(:miscType::course_misc_unit_type, :courseId, :termId)"
        )
                .bind("miscType", "Work Assignment")
                .bind("courseId", courseId)
                .bind("termId", termId)
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnit::class.java)
                .findOnly()

        val res = handle.createUpdate(
                "insert into $WRK_ASS_TABLE " +
                        "($COURSE_MISC_UNIT_ID, " +
                        "$WRK_ASS_CREATED_BY, $WRK_ASS_SHEET, $WRK_ASS_SUPPLEMENT, $WRK_ASS_DUE_DATE, " +
                        "$WRK_ASS_INDIVIDUAL, $WRK_ASS_LATE_DELIVERY, $WRK_ASS_MULTIPLE_DELIVERIES, " +
                        "$WRK_ASS_REQUIRES_REPORT, $WRK_ASS_VOTES, $WRK_ASS_TIMESTAMP) " +
                        "values(:courseMiscUnitId, :createdBy, :sheet," +
                        ":supplement, :dueDate, :individual, :lateDelivery, :multipleDeliveries, :requiresReport, :votes, :timestamp)"
        )
                .bind("courseMiscUnitId", courseMiscUnit.id)
                .bind("createdBy", workAssignment.createdBy)
                .bind("sheet", workAssignment.sheet)
                .bind("supplement", workAssignment.supplement)
                .bind("dueDate", workAssignment.dueDate)
                .bind("individual", workAssignment.individual)
                .bind("lateDelivery", workAssignment.lateDelivery)
                .bind("multipleDeliveries", workAssignment.multipleDeliveries)
                .bind("requiresReport", workAssignment.requiresReport)
                .bind("votes", workAssignment.votes)
                .bind("timestamp", workAssignment.timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(WorkAssignment::class.java)
                .findFirst()
        return res
    }

    override fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, workAssignmentReport: WorkAssignmentReport) =
            handle.createUpdate(
                    "insert into $WRK_ASS_REPORT_TABLE " +
                            "($COURSE_MISC_UNIT_ID, $WRK_ASS_SHEET, $WRK_ASS_SUPPLEMENT, " +
                            "$WRK_ASS_DUE_DATE, $WRK_ASS_INDIVIDUAL, $WRK_ASS_LATE_DELIVERY, $WRK_ASS_MULTIPLE_DELIVERIES, " +
                            "$WRK_ASS_REQUIRES_REPORT, $WRK_ASS_REPORTED_BY, $WRK_ASS_VOTES, $WRK_ASS_TIMESTAMP) " +
                            "values(:workAssignmentId, :sheet, :supplement, :dueDate, :individual, :lateDelivery, " +
                            ":multipleDeliveries, :requiresReport, :reportedBy, :votes, :timestamp)"
            )
                    .bind("workAssignmentId", workAssignmentId)
                    .bind("sheet", workAssignmentReport.sheet)
                    .bind("supplement", workAssignmentReport.supplement)
                    .bind("dueDate", workAssignmentReport.dueDate)
                    .bind("individual", workAssignmentReport.individual)
                    .bind("lateDelivery", workAssignmentReport.lateDelivery)
                    .bind("multipleDeliveries", workAssignmentReport.multipleDeliveries)
                    .bind("requiresReport", workAssignmentReport.requiresReport)
                    .bind("reportedBy", workAssignmentReport.reportedBy)
                    .bind("votes", workAssignmentReport.votes)
                    .bind("timestamp", workAssignmentReport.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(WorkAssignmentReport::class.java)
                    .findFirst()

    override fun voteOnReportToWorkAssignmentOnCourseInTerm(reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $WRK_ASS_VOTES from $WRK_ASS_REPORT_TABLE where $WRK_ASS_REPORT_ID = :reportId")
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate("update $WRK_ASS_REPORT_TABLE set $WRK_ASS_VOTES = :votes where $WRK_ASS_REPORT_ID = :reportId")
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getSpecificReportOfWorkAssignment(workAssignmentId: Int, reportId: Int) =
            handle.createQuery(
                    "select * from $WRK_ASS_REPORT_TABLE " +
                            "where $WRK_ASS_REPORT_ID = :reportId and $COURSE_MISC_UNIT_ID = :workAssignmentId"
            )
                    .bind("reportId", reportId)
                    .bind("workAssignmentId", workAssignmentId)
                    .mapTo(WorkAssignmentReport::class.java)
                    .findFirst()

    override fun createWorkAssignmentVersion(workAssignmentVersion: WorkAssignmentVersion) =
            handle.createUpdate(
                    "insert into $WRK_ASS_VERSION_TABLE " +
                            "($COURSE_MISC_UNIT_ID, $WRK_ASS_VERSION, $WRK_ASS_SHEET, " +
                            "$WRK_ASS_SUPPLEMENT, $WRK_ASS_DUE_DATE, $WRK_ASS_INDIVIDUAL, $WRK_ASS_LATE_DELIVERY, " +
                            "$WRK_ASS_MULTIPLE_DELIVERIES, $WRK_ASS_REQUIRES_REPORT, $WRK_ASS_CREATED_BY, $WRK_ASS_TIMESTAMP) " +
                            "values (:workAssignmentId, :version, :sheet, :supplement, :dueDate, " +
                            ":individual, :lateDelivery, :multipleDeliveries, " +
                            ":requiresReport, :createdBy, :timestamp)"
            )
                    .bind("workAssignmentId", workAssignmentVersion.courseMiscUnitId)
                    .bind("version", workAssignmentVersion.version)
                    .bind("sheet", workAssignmentVersion.sheet)
                    .bind("supplement", workAssignmentVersion.supplement)
                    .bind("dueDate", workAssignmentVersion.dueDate)
                    .bind("individual", workAssignmentVersion.individual)
                    .bind("lateDelivery", workAssignmentVersion.lateDelivery)
                    .bind("multipleDeliveries", workAssignmentVersion.multipleDeliveries)
                    .bind("requiresReport", workAssignmentVersion.requiresReport)
                    .bind("createdBy", workAssignmentVersion.createdBy)
                    .bind("timestamp", workAssignmentVersion.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(WorkAssignmentVersion::class.java)
                    .findFirst()

    override fun createStagingWorkAssingment(courseId: Int, termId: Int, stage: WorkAssignmentStage): Optional<WorkAssignmentStage> {
        val courseMiscUnitStage = handle.createUpdate(
                "insert into $COURSE_MISC_UNIT_STAGE_TABLE " +
                        "($COURSE_ID, $TERM_ID, $COURSE_MISC_TYPE) " +
                        "values(:courseId, :termId, :miscType::course_misc_unit_type)"
        )
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("miscType", "Work Assignment")
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnitStage::class.java)
                .findOnly()

        val workAssignmentStage = handle.createUpdate(
                "insert into $WRK_ASS_STAGE_TABLE " +
                        "($COURSE_MISC_UNIT_ID, $WRK_ASS_SHEET, $WRK_ASS_SUPPLEMENT, $WRK_ASS_DUE_DATE, " +
                        "$WRK_ASS_INDIVIDUAL, $WRK_ASS_LATE_DELIVERY, $WRK_ASS_MULTIPLE_DELIVERIES, $WRK_ASS_REQUIRES_REPORT, " +
                        "$WRK_ASS_CREATED_BY, $WRK_ASS_VOTES, $WRK_ASS_TIMESTAMP) " +
                        "values(:courseMiscUnitId, :sheet, :supplement, :dueDate, :individual, :lateDelivery, " +
                        ":multipleDeliveries, :requiresReport, :createdBy, :votes, :timestamp)"
        )
                .bind("courseMiscUnitId", courseMiscUnitStage.id)
                .bind("sheet", stage.sheet)
                .bind("supplement", stage.supplement)
                .bind("dueDate", stage.dueDate)
                .bind("individual", stage.individual)
                .bind("lateDelivery", stage.lateDelivery)
                .bind("multipleDeliveries", stage.multipleDeliveries)
                .bind("requiresReport", stage.requiresReport)
                .bind("createdBy", stage.createdBy)
                .bind("votes", stage.votes)
                .bind("timestamp", stage.timestamp)
                .executeAndReturnGeneratedKeys()
                .mapTo(WorkAssignmentStage::class.java)
                .findFirst()
        return workAssignmentStage
    }

    override fun voteOnStagedWorkAssignment(stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $WRK_ASS_VOTES from $WRK_ASS_STAGE_TABLE where $COURSE_MISC_UNIT_ID = :workAssignmentId")
                .bind("workAssignmentId", stageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate("update $WRK_ASS_STAGE_TABLE set $WRK_ASS_VOTES = :votes where $COURSE_MISC_UNIT_ID = :workAssignmentId")
                .bind("votes", votes)
                .bind("workAssignmentId", stageId)
                .execute()
    }

    override fun deleteAllWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int) =
            handle.createUpdate(
                    "delete from $COURSE_MISC_UNIT_TABLE " +
                            "where $COURSE_ID = :courseId " +
                            "and $TERM_ID = :termId " +
                            "and $COURSE_MISC_TYPE = Work Assignment"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .execute()

    override fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int) =
            handle.createUpdate(
                    "delete from $COURSE_MISC_UNIT_STAGE_TABLE " +
                            "where $COURSE_ID = :courseId " +
                            "and $TERM_ID = :termId " +
                            "and $COURSE_MISC_TYPE  = Work Assignment"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .execute()

    override fun deleteAllVersionOfWorkAssignments(versionWorkAssignmentId: Int) =
            handle.createUpdate(
                    "delete from $WRK_ASS_VERSION_TABLE where $WRK_ASS_ID = :versionWorkAssignmentId"
            )
                    .bind("versionWorkAssignmentId", versionWorkAssignmentId)
                    .execute()

    override fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int) =
            handle.createQuery(
                    "select * from $WRK_ASS_VERSION_TABLE where $COURSE_MISC_UNIT_ID = :workAssignmentId"
            )
                    .bind("workAssignmentId", workAssignmentId)
                    .mapTo(WorkAssignmentVersion::class.java)
                    .list()
}