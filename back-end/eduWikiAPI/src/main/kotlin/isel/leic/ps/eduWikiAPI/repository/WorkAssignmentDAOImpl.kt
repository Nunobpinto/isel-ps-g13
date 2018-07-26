package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.CourseMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TYPE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class WorkAssignmentDAOImpl : WorkAssignmentDAO {

    companion object {
        //TABLE NAMES
        const val WORK_ASSIGNMENT_TABLE = "work_assignment"
        const val WORK_ASSIGNMENT_VERSION_TABLE = "work_assignment_version"
        const val WORK_ASSIGNMENT_REPORT_TABLE = "work_assignment_report"
        const val WORK_ASSIGNMENT_STAGE_TABLE = "work_assignment_stage"
        // FIELDS
        const val WORK_ASSIGNMENT_STAGE_ID = "work_assignment_stage_id"
        const val WORK_ASSIGNMENT_ID = "work_assignment_id"
        const val WORK_ASSIGNMENT_VERSION = "work_assignment_version"
        const val WORK_ASSIGNMENT_SHEET = "sheet"
        const val WORK_ASSIGNMENT_SUPPLEMENT = "supplement"
        const val WORK_ASSIGNMENT_DUE_DATE = "due_date"
        const val WORK_ASSIGNMENT_INDIVIDUAL = "individual"
        const val WORK_ASSIGNMENT_LATE_DELIVERY = "late_delivery"
        const val WORK_ASSIGNMENT_MULTIPLE_DELIVERIES = "multipleDeliveries"
        const val WORK_ASSIGNMENT_REQUIRES_REPORT = "requires_report"
        const val WORK_ASSIGNMENT_VOTES = "votes"
        const val WORK_ASSIGNMENT_TIMESTAMP = "time_stamp"
        const val WORK_ASSIGNMENT_REPORT_ID = "work_assignment_report_id"
        const val WORK_ASSIGNMENT_REPORTED_BY = "reported_by"
        const val WORK_ASSIGNMENT_CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var handle: Handle

    override fun getSpecificWorkAssignment(courseMiscUnitId: Int, courseId: Int, termId: Int) =
            handle.createQuery(
                    "select W.$WORK_ASSIGNMENT_ID," +
                            "W.$WORK_ASSIGNMENT_VERSION," +
                            "W.$WORK_ASSIGNMENT_CREATED_BY, " +
                            "W.$WORK_ASSIGNMENT_VOTES, " +
                            "W.$WORK_ASSIGNMENT_SHEET, " +
                            "W.$WORK_ASSIGNMENT_SUPPLEMENT, " +
                            "W.$WORK_ASSIGNMENT_DUE_DATE," +
                            "W.$WORK_ASSIGNMENT_INDIVIDUAL, " +
                            "W.$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                            "W.$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                            "W.$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                            "W.$WORK_ASSIGNMENT_VOTES, " +
                            "W.$WORK_ASSIGNMENT_TIMESTAMP " +
                            "from $WORK_ASSIGNMENT_TABLE as W " +
                            "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                            "on W.$WORK_ASSIGNMENT_ID = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID} " +
                            "where C.${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId " +
                            "and W.$WORK_ASSIGNMENT_ID = :workItemId"
            )
                    .bind("workItemId", courseMiscUnitId)
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(WorkAssignment::class.java)
                    .findFirst()

    override fun getAllWorkAssignment() =
            handle.createQuery("select * from $WORK_ASSIGNMENT_TABLE")
                    .mapTo(WorkAssignment::class.java)
                    .list()

    override fun deleteSpecificWorkAssignment(courseMiscUnitId: Int) =
            handle.createUpdate(
                    "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE}" +
                            "where ${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :courseId"
            )
                    .bind("courseId", courseMiscUnitId)
                    .execute()

    override fun updateWorkAssignment(workAssignmentId: Int, workAssignment: WorkAssignment): Optional<WorkAssignment> =
            handle.createUpdate(
                    "update $WORK_ASSIGNMENT_TABLE SET " +
                            "$WORK_ASSIGNMENT_VERSION = :version, " +
                            "$WORK_ASSIGNMENT_CREATED_BY = :createdBy, " +
                            "$WORK_ASSIGNMENT_SHEET = :sheet, " +
                            "$WORK_ASSIGNMENT_SUPPLEMENT = :supplement, " +
                            "$WORK_ASSIGNMENT_DUE_DATE = :dueDate, " +
                            "$WORK_ASSIGNMENT_INDIVIDUAL = :individual, " +
                            "$WORK_ASSIGNMENT_LATE_DELIVERY = :lateDelivery, " +
                            "$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES = :multipleDeliveries, " +
                            "$WORK_ASSIGNMENT_REQUIRES_REPORT = :requiresReport, " +
                            "$WORK_ASSIGNMENT_VOTES = :votes, " +
                            "$WORK_ASSIGNMENT_TIMESTAMP = :timestamp " +
                            "where $WORK_ASSIGNMENT_ID = :workAssignmentId"
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
                    .executeAndReturnGeneratedKeys()
                    .mapTo(WorkAssignment::class.java)
                    .findFirst()

    override fun voteOnWorkAssignment(courseMiscUnitId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $WORK_ASSIGNMENT_VOTES from $WORK_ASSIGNMENT_TABLE " +
                        "where $WORK_ASSIGNMENT_ID = :workAssignmentId"
        )
                .bind("workAssignmentId", courseMiscUnitId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $WORK_ASSIGNMENT_TABLE set $WORK_ASSIGNMENT_VOTES = :votes" +
                        "where $WORK_ASSIGNMENT_ID = :workAssignmentId"
        )
                .bind("votes", votes)
                .bind("workAssignmentId", courseMiscUnitId)
                .execute()
    }

    override fun getWorkAssignmentSpecificStageEntry(stageId: Int) =
            handle.createQuery(
                    "select * from $WORK_ASSIGNMENT_STAGE_TABLE " +
                            "where $WORK_ASSIGNMENT_STAGE_ID = :stageId"
            )
                    .bind("stageId", stageId)
                    .mapTo(WorkAssignmentStage::class.java)
                    .findFirst()

    override fun getAllWorkAssignmentStages() =
            handle.createQuery("select * from $WORK_ASSIGNMENT_STAGE_TABLE")
                    .mapTo(WorkAssignmentStage::class.java)
                    .list()

    override fun deleteSpecificStagedWorkAssignment(stageId: Int) =
            handle.createUpdate(
                    "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} " +
                            "where ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_ID} = :stageId"
            )
                    .bind("stageId", stageId)
                    .execute()

    override fun getVersionOfSpecificWorkAssignment(workAssignmentId: Int, version: Int) =
            handle.createQuery(
                    "select * from $WORK_ASSIGNMENT_VERSION_TABLE " +
                            "where $WORK_ASSIGNMENT_ID = :workAssignmentId " +
                            "and $WORK_ASSIGNMENT_VERSION = :version"
            )
                    .bind("workAssignmentId", workAssignmentId)
                    .bind("version", version)
                    .mapTo(WorkAssignmentVersion::class.java)
                    .findFirst()


    override fun deleteVersionWorkAssignment(workAssignmentId: Int, version: Int) =
            handle.createUpdate(
                    "delete from $WORK_ASSIGNMENT_VERSION_TABLE " +
                            "where $WORK_ASSIGNMENT_ID = :workAssignmentId " +
                            "and $WORK_ASSIGNMENT_VERSION = :version"
            )
                    .bind("workAssignmentId", workAssignmentId)
                    .bind("version", version)
                    .execute()

    override fun deleteReportOnWorkAssignment(reportId: Int) =
            handle.createUpdate(
                    "delete from $WORK_ASSIGNMENT_REPORT_TABLE " +
                            "where $WORK_ASSIGNMENT_REPORT_ID = :reportId"
            )
                    .bind("reportId", reportId)
                    .execute()

    override fun deleteAllReportsOnWorkAssignment(courseMiscUnitId: Int) =
            handle.createUpdate(
                    "delete from $WORK_ASSIGNMENT_REPORT_TABLE " +
                            "where $WORK_ASSIGNMENT_ID = :workAssignmentId"
            )
                    .bind("workAssignmentId", courseMiscUnitId)
                    .execute()

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int) =
            handle.createQuery(
                    "select W.$WORK_ASSIGNMENT_ID " +
                            "W.$WORK_ASSIGNMENT_VERSION, " +
                            "W.$WORK_ASSIGNMENT_VOTES," +
                            "W.$WORK_ASSIGNMENT_CREATED_BY, " +
                            "W.$WORK_ASSIGNMENT_SHEET, " +
                            "W.$WORK_ASSIGNMENT_SUPPLEMENT, " +
                            "W.$WORK_ASSIGNMENT_DUE_DATE," +
                            "W.$WORK_ASSIGNMENT_INDIVIDUAL, " +
                            "W.$WORK_ASSIGNMENT_LATE_DELIVERY," +
                            "W.$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                            "W.$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                            "W.$WORK_ASSIGNMENT_TIMESTAMP " +
                            "from $WORK_ASSIGNMENT_TABLE as W " +
                            "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                            "on W.$WORK_ASSIGNMENT_ID = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID}" +
                            "where C.${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(WorkAssignment::class.java)
                    .list()

    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int) =
            handle.createQuery(
                    "select W.$WORK_ASSIGNMENT_STAGE_ID, " +
                            "W.$WORK_ASSIGNMENT_CREATED_BY, " +
                            "W.$WORK_ASSIGNMENT_SHEET, " +
                            "W.$WORK_ASSIGNMENT_SUPPLEMENT, " +
                            "W.$WORK_ASSIGNMENT_DUE_DATE, " +
                            "W.$WORK_ASSIGNMENT_INDIVIDUAL, " +
                            "W.$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                            "W.$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                            "W.$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                            "W.$WORK_ASSIGNMENT_VOTES," +
                            "W.$WORK_ASSIGNMENT_TIMESTAMP " +
                            "from $WORK_ASSIGNMENT_STAGE_TABLE as W " +
                            "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} as C " +
                            "on W.$WORK_ASSIGNMENT_STAGE_ID = C.${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_ID} " +
                            "where C.${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .mapTo(WorkAssignmentStage::class.java)
                    .list()

    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int) =
            handle.createQuery(
                    "select W.$WORK_ASSIGNMENT_STAGE_ID, " +
                            "W.$WORK_ASSIGNMENT_SHEET," +
                            "W.$WORK_ASSIGNMENT_SUPPLEMENT, " +
                            "W.$WORK_ASSIGNMENT_DUE_DATE, " +
                            "W.$WORK_ASSIGNMENT_INDIVIDUAL, " +
                            "W.$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                            "W.$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                            "W.$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                            "W.$WORK_ASSIGNMENT_CREATED_BY, " +
                            "W.$WORK_ASSIGNMENT_VOTES, " +
                            "W.$WORK_ASSIGNMENT_TIMESTAMP " +
                            "from $WORK_ASSIGNMENT_STAGE_TABLE as W " +
                            "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} as C " +
                            "on W.$WORK_ASSIGNMENT_STAGE_ID = C.${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_ID} " +
                            "where C.${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_ID} = :stageId"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .bind("stageId", stageId)
                    .mapTo(WorkAssignmentStage::class.java)
                    .findFirst()

    override fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int) =
            handle.createQuery(
                    "select W.$WORK_ASSIGNMENT_REPORT_ID, " +
                            "W.$WORK_ASSIGNMENT_ID, " +
                            "W.$WORK_ASSIGNMENT_SHEET, " +
                            "W.$WORK_ASSIGNMENT_SUPPLEMENT, " +
                            "W.$WORK_ASSIGNMENT_DUE_DATE, " +
                            "W.$WORK_ASSIGNMENT_INDIVIDUAL, " +
                            "W.$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                            "W.$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                            "W.$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                            "W.$WORK_ASSIGNMENT_REPORTED_BY, " +
                            "W.$WORK_ASSIGNMENT_VOTES, " +
                            "W.$WORK_ASSIGNMENT_TIMESTAMP " +
                            "from $WORK_ASSIGNMENT_REPORT_TABLE as W" +
                            "inner join ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} as C " +
                            "on W.$WORK_ASSIGNMENT_ID = C.${CourseDAOImpl.COURSE_MISC_UNIT_ID} " +
                            "where C.${CourseDAOImpl.COURSE_MISC_UNIT_ID} = :workAssignmentId" +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId " +
                            "and C.${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId"
            )
                    .bind("workAssignmentId", workAssignmentId)
                    .bind("termId", termId)
                    .bind("courseId", courseId)
                    .mapTo(WorkAssignmentReport::class.java)
                    .list()

    override fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, workAssignment: WorkAssignment): Optional<WorkAssignment> {
        val courseMiscUnit = handle.createUpdate(
                "insert into ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE}(" +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_TYPE}, " +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID}, " +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} " +
                        ")" +
                        "values(:miscType, :courseId, :termId)"
        )
                .bind("miscType", "Work Assignment")
                .bind("courseId", courseId)
                .bind("termId", termId)
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnit::class.java)
                .findOnly()

        val res = handle.createUpdate(
                "insert into $WORK_ASSIGNMENT_TABLE (" +
                        "$WORK_ASSIGNMENT_ID, " +
                        "$WORK_ASSIGNMENT_VERSION, " +
                        "$WORK_ASSIGNMENT_CREATED_BY, " +
                        "$WORK_ASSIGNMENT_SHEET, " +
                        "$WORK_ASSIGNMENT_SUPPLEMENT, " +
                        "$WORK_ASSIGNMENT_DUE_DATE, " +
                        "$WORK_ASSIGNMENT_INDIVIDUAL, " +
                        "$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                        "$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                        "$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                        "$WORK_ASSIGNMENT_VOTES, " +
                        "$WORK_ASSIGNMENT_TIMESTAMP " +
                        ") " +
                        "values(:workAssignmentId, :version, :createdBy, :sheet, :supplement, :dueDate," +
                        ":individual, :lateDelivery, :multipleDeliveries, :requiresReport, :votes, :timestamp)"
        )
                .bind("workAssignmentId", courseMiscUnit.courseMiscUnitId)
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
                .executeAndReturnGeneratedKeys()
                .mapTo(WorkAssignment::class.java)
                .findFirst()
        return res
    }

    override fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, workAssignmentReport: WorkAssignmentReport) =
            handle.createUpdate(
                    "insert into $WORK_ASSIGNMENT_REPORT_TABLE (" +
                            "$WORK_ASSIGNMENT_ID, " +
                            "$WORK_ASSIGNMENT_SHEET, " +
                            "$WORK_ASSIGNMENT_SUPPLEMENT, " +
                            "$WORK_ASSIGNMENT_DUE_DATE, " +
                            "$WORK_ASSIGNMENT_INDIVIDUAL, " +
                            "$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                            "$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                            "$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                            "$WORK_ASSIGNMENT_REPORTED_BY, " +
                            "$WORK_ASSIGNMENT_VOTES, " +
                            "$WORK_ASSIGNMENT_TIMESTAMP " +
                            ") " +
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
        var votes = handle.createQuery(
                "select $WORK_ASSIGNMENT_VOTES from $WORK_ASSIGNMENT_REPORT_TABLE " +
                        "where $WORK_ASSIGNMENT_REPORT_ID = :reportId"
        )
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $WORK_ASSIGNMENT_REPORT_TABLE set $WORK_ASSIGNMENT_VOTES = :votes " +
                        "where $WORK_ASSIGNMENT_REPORT_ID = :reportId"
        )
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getSpecificReportOfWorkAssignment(workAssignmentId: Int, reportId: Int) =
            handle.createQuery(
                    "select * from $WORK_ASSIGNMENT_REPORT_TABLE " +
                            "where $WORK_ASSIGNMENT_REPORT_ID = :reportId " +
                            "and $WORK_ASSIGNMENT_ID = :workAssignmentId"
            )
                    .bind("reportId", reportId)
                    .bind("workAssignmentId", workAssignmentId)
                    .mapTo(WorkAssignmentReport::class.java)
                    .findFirst()

    override fun createWorkAssignmentVersion(workAssignmentVersion: WorkAssignmentVersion) =
            handle.createUpdate(
                    "insert into $WORK_ASSIGNMENT_VERSION_TABLE (" +
                            "$WORK_ASSIGNMENT_ID," +
                            "$WORK_ASSIGNMENT_VERSION, " +
                            "$WORK_ASSIGNMENT_SHEET, " +
                            "$WORK_ASSIGNMENT_SUPPLEMENT, " +
                            "$WORK_ASSIGNMENT_DUE_DATE, " +
                            "$WORK_ASSIGNMENT_INDIVIDUAL, " +
                            "$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                            "$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                            "$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                            "$WORK_ASSIGNMENT_CREATED_BY, " +
                            "$WORK_ASSIGNMENT_TIMESTAMP " +
                            ") " +
                            "values (:workAssignmentId, :version, :sheet, :supplement, :dueDate, " +
                            ":individual, :lateDelivery, :multipleDeliveries, " +
                            ":requiresReport, :createdBy, :timestamp)"
            )
                    .bind("workAssignmentId", workAssignmentVersion.workAssignmentId)
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
                "insert into ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} (" +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID}, " +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID}, " +
                        "${CourseDAOImpl.COURSE_MISC_UNIT_TYPE} " +
                        ") " +
                        "values(:courseId, :termId, :miscType)"
        )
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("miscType", "Work Assignment")
                .executeAndReturnGeneratedKeys()
                .mapTo(CourseMiscUnitStage::class.java)
                .findOnly()

        val workAssignmentStage = handle.createUpdate(
                "insert into $WORK_ASSIGNMENT_STAGE_TABLE (" +
                        "$WORK_ASSIGNMENT_STAGE_ID, " +
                        "$WORK_ASSIGNMENT_SHEET, " +
                        "$WORK_ASSIGNMENT_SUPPLEMENT, " +
                        "$WORK_ASSIGNMENT_DUE_DATE, " +
                        "$WORK_ASSIGNMENT_INDIVIDUAL, " +
                        "$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                        "$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                        "$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                        "$WORK_ASSIGNMENT_CREATED_BY, " +
                        "$WORK_ASSIGNMENT_VOTES, " +
                        "$WORK_ASSIGNMENT_TIMESTAMP " +
                        ") " +
                        "values(:courseMiscUnitStageId, :sheet, :supplement, :dueDate, :individual, :lateDelivery, " +
                        ":multipleDeliveries, :requiresReport, :createdBy, :votes, :timestamp)"
        )
                .bind("courseMiscUnitStageId", courseMiscUnitStage.stageId)
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
        var votes = handle.createQuery(
                "select $WORK_ASSIGNMENT_VOTES from $WORK_ASSIGNMENT_STAGE_TABLE " +
                        "where $WORK_ASSIGNMENT_STAGE_ID = :stageId"
        )
                .bind("stageId", stageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $WORK_ASSIGNMENT_STAGE_TABLE set $WORK_ASSIGNMENT_VOTES = :votes " +
                        "where $WORK_ASSIGNMENT_STAGE_ID = :stageId"
        )
                .bind("votes", votes)
                .bind("stageId", stageId)
                .execute()
    }

    override fun deleteAllWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int) =
            handle.createUpdate(
                    "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_TABLE} " +
                            "where ${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and ${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId " +
                            "and ${CourseDAOImpl.COURSE_MISC_UNIT_TYPE} = :miscType"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .bind("miscType", "Work Assignment")
                    .execute()

    override fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int) =
            handle.createUpdate(
                    "delete from ${CourseDAOImpl.COURSE_MISC_UNIT_STAGE_TABLE} " +
                            "where ${CourseDAOImpl.COURSE_MISC_UNIT_COURSE_ID} = :courseId " +
                            "and ${CourseDAOImpl.COURSE_MISC_UNIT_TERM_ID} = :termId " +
                            "and ${CourseDAOImpl.COURSE_MISC_UNIT_TYPE}  = miscType"
            )
                    .bind("courseId", courseId)
                    .bind("termId", termId)
                    .bind("miscType", "Work Assignment")
                    .execute()

    override fun deleteAllVersionOfWorkAssignments(workAssignmentId: Int) =
            handle.createUpdate(
                    "delete from $WORK_ASSIGNMENT_VERSION_TABLE " +
                            "where $WORK_ASSIGNMENT_ID = :workAssignmentId"
            )
                    .bind("workAssignmentId", workAssignmentId)
                    .execute()

    override fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int) =
            handle.createQuery(
                    "select * from $WORK_ASSIGNMENT_VERSION_TABLE " +
                            "where $WORK_ASSIGNMENT_ID = :workAssignmentId"
            )
                    .bind("workAssignmentId", workAssignmentId)
                    .mapTo(WorkAssignmentVersion::class.java)
                    .list()
}