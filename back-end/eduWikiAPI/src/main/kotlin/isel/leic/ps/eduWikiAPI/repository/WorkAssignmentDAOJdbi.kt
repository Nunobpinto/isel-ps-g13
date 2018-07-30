package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.CourseMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TYPE
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import java.util.*

interface WorkAssignmentDAOJdbi : WorkAssignmentDAO {

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

    @SqlQuery(
            "SELECT W.$WORK_ASSIGNMENT_ID," +
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
                    "FROM $WORK_ASSIGNMENT_TABLE AS W " +
                    "INNER JOIN $COURSE_MISC_UNIT_TABLE AS C " +
                    "ON W.$WORK_ASSIGNMENT_ID = C.$COURSE_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId " +
                    "AND W.$WORK_ASSIGNMENT_ID = :courseMiscUnitId"
    )
    override fun getSpecificWorkAssignment(courseMiscUnitId: Int, courseId: Int, termId: Int): Optional<WorkAssignment>

    @SqlQuery("SELECT * FROM $WORK_ASSIGNMENT_TABLE")
    override fun getAllWorkAssignment(): List<WorkAssignment>

    @SqlUpdate("DELETE FROM $COURSE_MISC_UNIT_TABLE WHERE $COURSE_MISC_UNIT_ID = :courseMiscUnitId")
    override fun deleteSpecificWorkAssignment(courseMiscUnitId: Int): Int

    @SqlUpdate(
            "UPDATE $WORK_ASSIGNMENT_TABLE SET " +
                    "$WORK_ASSIGNMENT_VERSION = :workAssignment.version, " +
                    "$WORK_ASSIGNMENT_CREATED_BY = :workAssignment.createdBy, " +
                    "$WORK_ASSIGNMENT_SHEET = :workAssignment.sheet, " +
                    "$WORK_ASSIGNMENT_SUPPLEMENT = :workAssignment.supplement, " +
                    "$WORK_ASSIGNMENT_DUE_DATE = :workAssignment.dueDate, " +
                    "$WORK_ASSIGNMENT_INDIVIDUAL = :workAssignment.individual, " +
                    "$WORK_ASSIGNMENT_LATE_DELIVERY = :workAssignment.lateDelivery, " +
                    "$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES = :workAssignment.multipleDeliveries, " +
                    "$WORK_ASSIGNMENT_REQUIRES_REPORT = :workAssignment.requiresReport, " +
                    "$WORK_ASSIGNMENT_VOTES = :workAssignment.votes, " +
                    "$WORK_ASSIGNMENT_TIMESTAMP = :workAssignment.timestamp " +
                    "WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId"
    )
    override fun updateWorkAssignment(workAssignmentId: Int, workAssignment: WorkAssignment): WorkAssignment

    @SqlQuery("SELECT $WORK_ASSIGNMENT_VOTES FROM $WORK_ASSIGNMENT_TABLE WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId")
    fun getVotesOnWorkAssignment(workAssignmentId: Int): Int

    @SqlQuery("UPDATE $WORK_ASSIGNMENT_TABLE set $WORK_ASSIGNMENT_VOTES = :votes WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId")
    fun updateWorkAssignmentVotes(workAssignmentId: Int, votes: Int): Int

    @Transaction
    override fun voteOnWorkAssignment(courseMiscUnitId: Int, vote: Vote): Int {
        var votes = getVotesOnWorkAssignment(courseMiscUnitId)
        votes = if(vote == Vote.Down) -- votes else ++ votes
        return updateWorkAssignmentVotes(courseMiscUnitId, votes)
    }

    @SqlQuery("SELECT * FROM $WORK_ASSIGNMENT_STAGE_TABLE WHERE $WORK_ASSIGNMENT_STAGE_ID = :stageId")
    override fun getWorkAssignmentSpecificStageEntry(stageId: Int): Optional<WorkAssignmentStage>

    @SqlQuery("SELECT * FROM $WORK_ASSIGNMENT_STAGE_TABLE")
    override fun getAllWorkAssignmentStages(): List<WorkAssignmentStage>

    @SqlUpdate("DELETE FROM $COURSE_MISC_UNIT_STAGE_TABLE WHERE $COURSE_MISC_UNIT_STAGE_ID = :stageId")
    override fun deleteSpecificStagedWorkAssignment(stageId: Int): Int

    @SqlQuery(
            "SELECT * FROM $WORK_ASSIGNMENT_VERSION_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId " +
                    "AND $WORK_ASSIGNMENT_VERSION = :version"
    )
    override fun getVersionOfSpecificWorkAssignment(workAssignmentId: Int, version: Int): Optional<WorkAssignmentVersion>

    @SqlUpdate(
            "DELETE FROM $WORK_ASSIGNMENT_VERSION_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId " +
                    "AND $WORK_ASSIGNMENT_VERSION = :version"
    )
    override fun deleteVersionWorkAssignment(workAssignmentId: Int, version: Int): Int

    @SqlUpdate("DELETE FROM $WORK_ASSIGNMENT_REPORT_TABLE WHERE $WORK_ASSIGNMENT_REPORT_ID = :reportId")
    override fun deleteReportOnWorkAssignment(reportId: Int): Int

    @SqlUpdate("DELETE FROM $WORK_ASSIGNMENT_REPORT_TABLE WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId")
    override fun deleteAllReportsOnWorkAssignment(courseMiscUnitId: Int): Int

    @SqlQuery(
            "SELECT W.$WORK_ASSIGNMENT_ID " +
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
                    "FROM $WORK_ASSIGNMENT_TABLE AS W " +
                    "INNER JOIN $COURSE_MISC_UNIT_TABLE AS C " +
                    "ON W.$WORK_ASSIGNMENT_ID = C.$COURSE_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId"
    )
    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment>

    @SqlQuery(
            "SELECT W.$WORK_ASSIGNMENT_STAGE_ID, " +
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
                    "FROM $WORK_ASSIGNMENT_STAGE_TABLE AS W " +
                    "INNER JOIN $COURSE_MISC_UNIT_STAGE_TABLE AS C " +
                    "ON W.$WORK_ASSIGNMENT_STAGE_ID = C.$COURSE_MISC_UNIT_STAGE_ID " +
                    "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId"
    )
    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage>

    @SqlQuery(
            "SELECT W.$WORK_ASSIGNMENT_STAGE_ID, " +
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
                    "FROM $WORK_ASSIGNMENT_STAGE_TABLE AS W " +
                    "INNER JOIN $COURSE_MISC_UNIT_STAGE_TABLE AS C " +
                    "ON W.$WORK_ASSIGNMENT_STAGE_ID = C.$COURSE_MISC_UNIT_STAGE_ID " +
                    "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId " +
                    "AND C.$COURSE_MISC_UNIT_STAGE_ID = :stageId"
    )
    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<WorkAssignmentStage>

    @SqlQuery(
            "SELECT W.$WORK_ASSIGNMENT_REPORT_ID, " +
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
                    "FROM $WORK_ASSIGNMENT_REPORT_TABLE AS W" +
                    "INNER JOIN $COURSE_MISC_UNIT_TABLE AS C " +
                    "ON W.$WORK_ASSIGNMENT_ID = C.$COURSE_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_MISC_UNIT_ID = :workAssignmentId" +
                    "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId " +
                    "AND C.$COURSE_MISC_UNIT_COURSE_ID = :courseId"
    )
    override fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport>

    @SqlUpdate(
            "INSERT INTO $COURSE_MISC_UNIT_TABLE(" +
                    "$COURSE_MISC_UNIT_TYPE, " +
                    "$COURSE_MISC_UNIT_COURSE_ID, " +
                    "$COURSE_MISC_UNIT_TERM_ID)" +
                    "VALUES(:miscType, :courseId, :termId)"
    )
    @GetGeneratedKeys
    fun createCourseMiscUnit(miscType: String, courseId: Int, termId: Int): CourseMiscUnit //TODO maybe it should be in CourseDAO

    @SqlUpdate(
            "INSERT INTO $WORK_ASSIGNMENT_TABLE (" +
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
                    "$WORK_ASSIGNMENT_TIMESTAMP) " +
                    "VALUES(:courseMiscUnitId, :workAssignment.version, :workAssignment.createdBy, " +
                    ":workAssignment.sheet, :workAssignment.supplement, :workAssignment.dueDate," +
                    ":workAssignment.individual, :workAssignment.lateDelivery, :workAssignment.multipleDeliveries, " +
                    ":workAssignment.requiresReport, :workAssignment.votes, :workAssignment.timestamp)"
    )
    @GetGeneratedKeys
    fun createWorkAssignment(courseMiscUnitId: Int, workAssignment: WorkAssignment): WorkAssignment

    @Transaction
    override fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, workAssignment: WorkAssignment): WorkAssignment {
        val courseMiscUnit = createCourseMiscUnit("Work Assignment", courseId, termId)
        return createWorkAssignment(courseMiscUnit.courseMiscUnitId, workAssignment)
    }

    @SqlUpdate(
            "INSERT INTO $WORK_ASSIGNMENT_REPORT_TABLE (" +
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
                    "$WORK_ASSIGNMENT_TIMESTAMP) " +
                    "VALUES(:workAssignmentId, :workAssignmentReport.sheet, :workAssignmentReport.supplement, " +
                    ":workAssignmentReport.dueDate, :workAssignmentReport.individual, :workAssignmentReport.lateDelivery, " +
                    ":workAssignmentReport.multipleDeliveries, :workAssignmentReport.requiresReport, " +
                    ":workAssignmentReport.reportedBy, :workAssignmentReport.votes, :workAssignmentReport.timestamp)"
    )
    @GetGeneratedKeys
    override fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, workAssignmentReport: WorkAssignmentReport): WorkAssignmentReport

    @SqlQuery("SELECT $WORK_ASSIGNMENT_VOTES FROM $WORK_ASSIGNMENT_REPORT_TABLE WHERE $WORK_ASSIGNMENT_REPORT_ID = :reportId")
    fun getVotesOnReportedWorkAssignment(reportId: Int): Int

    @SqlQuery("UPDATE $WORK_ASSIGNMENT_REPORT_TABLE set $WORK_ASSIGNMENT_VOTES = :votes WHERE $WORK_ASSIGNMENT_REPORT_ID = :reportId")
    fun updateReportedWorkAssignmentVotes(reportId: Int, votes: Int): Int

    @Transaction
    override fun voteOnReportToWorkAssignmentOnCourseInTerm(reportId: Int, vote: Vote): Int {
        var votes = getVotesOnReportedWorkAssignment(reportId)
        votes = if(vote == Vote.Down) -- votes else ++ votes
        return updateReportedWorkAssignmentVotes(reportId, votes)
    }

    @SqlQuery(
            "SELECT * FROM $WORK_ASSIGNMENT_REPORT_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_REPORT_ID = :reportId " +
                    "AND $WORK_ASSIGNMENT_ID = :workAssignmentId"
    )
    override fun getSpecificReportOfWorkAssignment(workAssignmentId: Int, reportId: Int): Optional<WorkAssignmentReport>

    @SqlUpdate(
            "INSERT INTO $WORK_ASSIGNMENT_VERSION_TABLE (" +
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
                    "$WORK_ASSIGNMENT_TIMESTAMP) " +
                    "VALUES (:workAssignmentVersion.workAssignmentId, :workAssignmentVersion.version, :workAssignmentVersion.sheet, :workAssignmentVersion.supplement, :workAssignmentVersion.dueDate, " +
                    ":workAssignmentVersion.individual, :workAssignmentVersion.lateDelivery, :workAssignmentVersion.multipleDeliveries, " +
                    ":workAssignmentVersion.requiresReport, :workAssignmentVersion.createdBy, :workAssignmentVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createWorkAssignmentVersion(workAssignmentVersion: WorkAssignmentVersion): WorkAssignmentVersion

    @SqlUpdate(
            "INSERT INTO $COURSE_MISC_UNIT_STAGE_TABLE (" +
                    "$COURSE_MISC_UNIT_COURSE_ID, " +
                    "$COURSE_MISC_UNIT_TERM_ID, " +
                    "$COURSE_MISC_UNIT_TYPE) " +
                    "VALUES(:courseId, :termId, :miscType)"
    )
    @GetGeneratedKeys
    fun createStageCourseMiscUnit(courseId: Int, termId: Int, miscType: String): CourseMiscUnitStage

    @SqlUpdate(
            "INSERT INTO $WORK_ASSIGNMENT_STAGE_TABLE (" +
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
                    "$WORK_ASSIGNMENT_TIMESTAMP) " +
                    "VALUES(:stageId, :stage.sheet, :stage.supplement, :stage.dueDate, :stage.individual, :stage.lateDelivery, " +
                    ":stage.multipleDeliveries, :stage.requiresReport, :stage.createdBy, :stage.votes, :stage.timestamp)"
    )
    @GetGeneratedKeys
    fun createStagedWorkAssignment(stage: WorkAssignmentStage, stageId: Int): WorkAssignmentStage

    @Transaction
    override fun createStagingWorkAssingment(courseId: Int, termId: Int, stage: WorkAssignmentStage): WorkAssignmentStage {
        val courseMiscUnitStage = createStageCourseMiscUnit(courseId, termId, "Work Assignment")
        return createStagedWorkAssignment(stage, courseMiscUnitStage.stageId)
    }

    @SqlQuery("SELECT $WORK_ASSIGNMENT_VOTES FROM $WORK_ASSIGNMENT_STAGE_TABLE WHERE $WORK_ASSIGNMENT_STAGE_ID = :stageId")
    fun getVotesOnStagedWorkAssignment(stageId: Int): Int

    @SqlQuery("UPDATE $WORK_ASSIGNMENT_STAGE_TABLE set $WORK_ASSIGNMENT_VOTES = :votes WHERE $WORK_ASSIGNMENT_STAGE_ID = :stageId")
    fun updateStagedWorkAssignmentVotes(stageId: Int, votes: Int): Int

    @Transaction
    override fun voteOnStagedWorkAssignment(stageId: Int, vote: Vote): Int {
        var votes = getVotesOnStagedWorkAssignment(stageId)
        votes = if(vote == Vote.Down) -- votes else ++ votes
        return updateStagedWorkAssignmentVotes(stageId, votes)
    }

    @SqlUpdate(
            "DELETE FROM $COURSE_MISC_UNIT_TABLE " +
                    "WHERE $COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND $COURSE_MISC_UNIT_TERM_ID = :termId " +
                    "AND $COURSE_MISC_UNIT_TYPE = :miscType"
    )
    override fun deleteAllWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_MISC_UNIT_STAGE_TABLE " +
                    "WHERE $COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND $COURSE_MISC_UNIT_TERM_ID = :termId " +
                    "AND $COURSE_MISC_UNIT_TYPE  = miscType"
    )
    override fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int

    @SqlUpdate("DELETE FROM $WORK_ASSIGNMENT_VERSION_TABLE WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId")
    override fun deleteAllVersionOfWorkAssignments(workAssignmentId: Int): Int

    @SqlQuery("SELECT * FROM $WORK_ASSIGNMENT_VERSION_TABLE WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId")
    override fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int): List<WorkAssignmentVersion>
}