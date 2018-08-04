package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_MISC_UNIT_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import java.sql.Timestamp
import java.time.LocalDateTime
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
        const val WORK_ASSIGNMENT_SHEET_ID = "sheet_id"
        const val WORK_ASSIGNMENT_SUPPLEMENT = "supplement_id"
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

    @CreateSqlObject
    fun createCourseDAO(): CourseDAOJdbi

    @SqlQuery(
            "SELECT W.$WORK_ASSIGNMENT_ID," +
                    "W.$WORK_ASSIGNMENT_VERSION," +
                    "W.$WORK_ASSIGNMENT_CREATED_BY, " +
                    "W.$WORK_ASSIGNMENT_VOTES, " +
                    "W.$WORK_ASSIGNMENT_SHEET_ID, " +
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
                    "AND W.$WORK_ASSIGNMENT_ID = :workAssignmentId"
    )
    override fun getSpecificWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int): Optional<WorkAssignment>

    @SqlQuery("SELECT * FROM $WORK_ASSIGNMENT_TABLE")
    override fun getAllWorkAssignment(): List<WorkAssignment>

    override fun deleteSpecificWorkAssignment(workAssignmentId: Int): Int =
            createCourseDAO().deleteSpecificCourseMiscUnitEntry(workAssignmentId)

    @SqlUpdate(
            "UPDATE $WORK_ASSIGNMENT_TABLE SET " +
                    "$WORK_ASSIGNMENT_VERSION = :workAssignment.version, " +
                    "$WORK_ASSIGNMENT_CREATED_BY = :workAssignment.createdBy, " +
                    "$WORK_ASSIGNMENT_SHEET_ID = :workAssignment.uuId, " +
                    "$WORK_ASSIGNMENT_SUPPLEMENT = :workAssignment.supplementId, " +
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

    @SqlQuery(
            "SELECT $WORK_ASSIGNMENT_VOTES FROM $WORK_ASSIGNMENT_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId"
    )
    override fun getVotesOnWorkAssignment(workAssignmentId: Int): Int

    @SqlQuery(
            "UPDATE $WORK_ASSIGNMENT_TABLE SET $WORK_ASSIGNMENT_VOTES = :votes" +
                    " WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId"
    )
    override fun updateVotesOnWorkAssignment(workAssignmentId: Int, votes: Int): Int

    @SqlQuery(
            "SELECT * FROM $WORK_ASSIGNMENT_STAGE_TABLE " +
                    " WHERE $WORK_ASSIGNMENT_STAGE_ID = :stageId"
    )
    override fun getWorkAssignmentSpecificStageEntry(stageId: Int): Optional<WorkAssignmentStage>

    @SqlQuery("SELECT * FROM $WORK_ASSIGNMENT_STAGE_TABLE")
    override fun getAllStagedWorkAssignments(): List<WorkAssignmentStage>

    override fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int =
            createCourseDAO().deleteSpecificStagedCourseMiscUnitEntry(courseId, termId, stageId)

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

    @SqlUpdate(
            "DELETE FROM $WORK_ASSIGNMENT_REPORT_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_REPORT_ID = :reportId"
    )
    override fun deleteReportOnWorkAssignment(reportId: Int): Int

    @SqlUpdate(
            "DELETE FROM $WORK_ASSIGNMENT_REPORT_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId"
    )
    override fun deleteAllReportsOnWorkAssignment(courseMiscUnitId: Int): Int

    @SqlQuery(
            "SELECT W.$WORK_ASSIGNMENT_ID " +
                    "W.$WORK_ASSIGNMENT_VERSION, " +
                    "W.$WORK_ASSIGNMENT_VOTES," +
                    "W.$WORK_ASSIGNMENT_CREATED_BY, " +
                    "W.$WORK_ASSIGNMENT_SHEET_ID, " +
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
                    "W.$WORK_ASSIGNMENT_SHEET_ID, " +
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
                    "W.$WORK_ASSIGNMENT_SHEET_ID," +
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
                    "W.$WORK_ASSIGNMENT_SHEET_ID, " +
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
            "INSERT INTO $WORK_ASSIGNMENT_TABLE (" +
                    "$WORK_ASSIGNMENT_ID, " +
                    "$WORK_ASSIGNMENT_VERSION, " +
                    "$WORK_ASSIGNMENT_CREATED_BY, " +
                    "$WORK_ASSIGNMENT_SHEET_ID, " +
                    "$WORK_ASSIGNMENT_SUPPLEMENT, " +
                    "$WORK_ASSIGNMENT_DUE_DATE, " +
                    "$WORK_ASSIGNMENT_INDIVIDUAL, " +
                    "$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                    "$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                    "$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                    "$WORK_ASSIGNMENT_VOTES, " +
                    "$WORK_ASSIGNMENT_TIMESTAMP) " +
                    "VALUES(:courseMiscUnitId, :workAssignment.version, :workAssignment.createdBy, " +
                    ":workAssignment.uuId, :workAssignment.supplementId, :workAssignment.dueDate," +
                    ":workAssignment.individual, :workAssignment.lateDelivery, :workAssignment.multipleDeliveries, " +
                    ":workAssignment.requiresReport, :workAssignment.votes, :workAssignment.timestamp)"
    )
    @GetGeneratedKeys
    fun createWorkAssignment(courseMiscUnitId: Int, workAssignment: WorkAssignment): WorkAssignment

    @Transaction
    override fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, workAssignment: WorkAssignment): WorkAssignment {
        val courseDAO = createCourseDAO()
        if (!courseDAO.getSpecificTermOfCourse(courseId, termId).isPresent) {
            courseDAO.createCourseTerm(courseId, termId, Timestamp.valueOf(LocalDateTime.now()))
        }
        val courseMiscUnit = courseDAO.createCourseMiscUnit(courseId, termId, "WorkAssignment")
        return createWorkAssignment(courseMiscUnit.courseMiscUnitId, workAssignment)
    }

    @SqlUpdate(
            "INSERT INTO $WORK_ASSIGNMENT_REPORT_TABLE (" +
                    "$WORK_ASSIGNMENT_ID, " +
                    "$WORK_ASSIGNMENT_SHEET_ID, " +
                    "$WORK_ASSIGNMENT_SUPPLEMENT, " +
                    "$WORK_ASSIGNMENT_DUE_DATE, " +
                    "$WORK_ASSIGNMENT_INDIVIDUAL, " +
                    "$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                    "$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                    "$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                    "$WORK_ASSIGNMENT_REPORTED_BY, " +
                    "$WORK_ASSIGNMENT_VOTES, " +
                    "$WORK_ASSIGNMENT_TIMESTAMP) " +
                    "VALUES(:workAssignmentId, :workAssignmentReport.uuId, :workAssignmentReport.supplementId, " +
                    ":workAssignmentReport.dueDate, :workAssignmentReport.individual, :workAssignmentReport.lateDelivery, " +
                    ":workAssignmentReport.multipleDeliveries, :workAssignmentReport.requiresReport, " +
                    ":workAssignmentReport.reportedBy, :workAssignmentReport.votes, :workAssignmentReport.timestamp)"
    )
    @GetGeneratedKeys
    override fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, workAssignmentReport: WorkAssignmentReport): WorkAssignmentReport

    @SqlQuery(
            "SELECT $WORK_ASSIGNMENT_VOTES FROM $WORK_ASSIGNMENT_REPORT_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_REPORT_ID = :reportId"
    )
    override fun getVotesOnReportedWorkAssignment(reportId: Int): Int

    @SqlQuery(
            "UPDATE $WORK_ASSIGNMENT_REPORT_TABLE SET $WORK_ASSIGNMENT_VOTES = :votes " +
                    "WHERE $WORK_ASSIGNMENT_REPORT_ID = :reportId"
    )
    override fun updateVotesOnReportedWorkAssignment(reportId: Int, votes: Int): Int

    @SqlQuery(
            "SELECT * FROM $WORK_ASSIGNMENT_REPORT_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_REPORT_ID = :reportId " +
                    "AND $WORK_ASSIGNMENT_ID = :workAssignmentId"
    )
    override fun getSpecificReportOfWorkAssignment(workAssignmentId: Int, reportId: Int): Optional<WorkAssignmentReport>

    @SqlUpdate(
            "INSERT INTO $WORK_ASSIGNMENT_VERSION_TABLE ( " +
                    "$WORK_ASSIGNMENT_ID," +
                    "$WORK_ASSIGNMENT_VERSION, " +
                    "$WORK_ASSIGNMENT_SHEET_ID, " +
                    "$WORK_ASSIGNMENT_SUPPLEMENT, " +
                    "$WORK_ASSIGNMENT_DUE_DATE, " +
                    "$WORK_ASSIGNMENT_INDIVIDUAL, " +
                    "$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                    "$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                    "$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                    "$WORK_ASSIGNMENT_CREATED_BY, " +
                    "$WORK_ASSIGNMENT_TIMESTAMP " +
                    ") " +
                    "VALUES (:workAssignmentVersion.workAssignmentId, :workAssignmentVersion.version, " +
                    ":workAssignmentVersion.uuId, :workAssignmentVersion.supplementId, " +
                    ":workAssignmentVersion.dueDate, :workAssignmentVersion.individual, " +
                    ":workAssignmentVersion.lateDelivery, :workAssignmentVersion.multipleDeliveries, " +
                    ":workAssignmentVersion.requiresReport, :workAssignmentVersion.createdBy, :workAssignmentVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createWorkAssignmentVersion(workAssignmentVersion: WorkAssignmentVersion): WorkAssignmentVersion

    @SqlUpdate(
            "INSERT INTO $WORK_ASSIGNMENT_STAGE_TABLE (" +
                    "$WORK_ASSIGNMENT_STAGE_ID, " +
                    "$WORK_ASSIGNMENT_SHEET_ID, " +
                    "$WORK_ASSIGNMENT_SUPPLEMENT, " +
                    "$WORK_ASSIGNMENT_DUE_DATE, " +
                    "$WORK_ASSIGNMENT_INDIVIDUAL, " +
                    "$WORK_ASSIGNMENT_LATE_DELIVERY, " +
                    "$WORK_ASSIGNMENT_MULTIPLE_DELIVERIES, " +
                    "$WORK_ASSIGNMENT_REQUIRES_REPORT, " +
                    "$WORK_ASSIGNMENT_CREATED_BY, " +
                    "$WORK_ASSIGNMENT_VOTES, " +
                    "$WORK_ASSIGNMENT_TIMESTAMP) " +
                    "VALUES(:stageId, :workAssignmentStage.uuId, :workAssignmentStage.supplementId, :workAssignmentStage.dueDate," +
                    ":workAssignmentStage.individual, :workAssignmentStage.lateDelivery, " +
                    ":workAssignmentStage.multipleDeliveries, :workAssignmentStage.requiresReport, " +
                    ":workAssignmentStage.createdBy, :workAssignmentStage.votes, :workAssignmentStage.timestamp)"
    )
    @GetGeneratedKeys
    fun createStagingWorkAssignment(workAssignmentStage: WorkAssignmentStage, stageId: Int): WorkAssignmentStage

    @Transaction
    override fun createStagingWorkAssingment(courseId: Int, termId: Int, workAssignmentStage: WorkAssignmentStage): WorkAssignmentStage {
        val courseDAO = createCourseDAO()
        if (!courseDAO.getSpecificTermOfCourse(courseId, termId).isPresent) {
            courseDAO.createCourseTerm(courseId, termId, Timestamp.valueOf(LocalDateTime.now()))
        }
        val courseMiscUnitStage = courseDAO.createStagingCourseMiscUnit(courseId, termId, "Work Assignment")
        return createStagingWorkAssignment(workAssignmentStage, courseMiscUnitStage.stageId)
    }

    @SqlQuery(
            "SELECT $WORK_ASSIGNMENT_VOTES FROM $WORK_ASSIGNMENT_STAGE_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_STAGE_ID = :stageId"
    )
    override fun getVotesOnStagedWorkAssignment(stageId: Int): Int

    @SqlQuery(
            "UPDATE $WORK_ASSIGNMENT_STAGE_TABLE SET $WORK_ASSIGNMENT_VOTES = :votes " +
                    "WHERE $WORK_ASSIGNMENT_STAGE_ID = :stageId"
    )
    override fun updateStagedWorkAssignmentVotes(stageId: Int, votes: Int): Int

    override fun deleteAllWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int =
            createCourseDAO().deleteAllCourseMiscUnitsFromTypeOfCourseInTerm(courseId, termId, "Work Assignment")

    override fun deleteAllStagedWorkAssignmentsOfCourseInTerm(courseId: Int, termId: Int): Int =
            createCourseDAO().deleteAllStagedCourseMiscUnitsFromTypeOfCourseInTerm(courseId, termId, "Work Assignment")

    @SqlUpdate(
            "DELETE FROM $WORK_ASSIGNMENT_VERSION_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId"
    )
    override fun deleteAllVersionOfWorkAssignments(workAssignmentId: Int): Int

    @SqlQuery(
            "SELECT * FROM $WORK_ASSIGNMENT_VERSION_TABLE " +
                    "WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId"
    )
    override fun getAllVersionsOfSpecificWorkAssignment(workAssignmentId: Int): List<WorkAssignmentVersion>

}