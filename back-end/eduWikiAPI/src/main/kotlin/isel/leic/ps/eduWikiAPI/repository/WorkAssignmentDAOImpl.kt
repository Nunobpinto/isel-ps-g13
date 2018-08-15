package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.enums.CourseMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.WorkAssignment
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.WorkAssignmentVersion
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_STAGE_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOImpl.Companion.COURSE_MISC_UNIT_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.WorkAssignmentDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Repository
class WorkAssignmentDAOImpl : WorkAssignmentDAO {

    companion object {
        //TABLE NAMES
        const val WORK_ASSIGNMENT_TABLE = "work_assignment"
        const val WORK_ASSIGNMENT_VERSION_TABLE = "work_assignment_version"
        const val WORK_ASSIGNMENT_REPORT_TABLE = "work_assignment_report"
        const val WORK_ASSIGNMENT_STAGE_TABLE = "work_assignment_stage"

        // WORK ASSIGNMENT FIELDS
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
        const val WORK_ASSIGNMENT_CREATED_BY = "created_by"
        const val WORK_ASSIGNMENT_LOG_ID = "log_id"

        // WORK ASSIGNMENT STAGE FIELDS
        const val WORK_ASSIGNMENT_STAGE_ID = "work_assignment_stage_id"
        const val WORK_ASSIGNMENT_STAGE_CREATED_BY = "created_by"
        const val WORK_ASSIGNMENT_STAGE_SHEET_ID = "sheet_id"
        const val WORK_ASSIGNMENT_STAGE_SUPPLEMENT = "supplement_id"
        const val WORK_ASSIGNMENT_STAGE_DUE_DATE = "due_date"
        const val WORK_ASSIGNMENT_STAGE_INDIVIDUAL = "individual"
        const val WORK_ASSIGNMENT_STAGE_LATE_DELIVERY = "late_delivery"
        const val WORK_ASSIGNMENT_STAGE_MULTIPLE_DELIVERIES = "multipleDeliveries"
        const val WORK_ASSIGNMENT_STAGE_REQUIRES_REPORT = "requires_report"
        const val WORK_ASSIGNMENT_STAGE_VOTES = "votes"
        const val WORK_ASSIGNMENT_STAGE_TIMESTAMP = "time_stamp"
        const val WORK_ASSIGNMENT_STAGE_LOG_ID = "log_id"


        // WORK ASSIGNMENT REPORT FIELDS
        const val WORK_ASSIGNMENT_REPORT_ID = "work_assignment_report_id"
        const val WORK_ASSIGNMENT_REPORT_WORK_ASSIGN_ID = "work_assignment_id"
        const val WORK_ASSIGNMENT_REPORTED_BY = "reported_by"
        const val WORK_ASSIGNMENT_REPORT_SHEET_ID = "sheet_id"
        const val WORK_ASSIGNMENT_REPORT_SUPPLEMENT = "supplement_id"
        const val WORK_ASSIGNMENT_REPORT_DUE_DATE = "due_date"
        const val WORK_ASSIGNMENT_REPORT_INDIVIDUAL = "individual"
        const val WORK_ASSIGNMENT_REPORT_LATE_DELIVERY = "late_delivery"
        const val WORK_ASSIGNMENT_REPORT_MULTIPLE_DELIVERIES = "multipleDeliveries"
        const val WORK_ASSIGNMENT_REPORT_REQUIRES_REPORT = "requires_report"
        const val WORK_ASSIGNMENT_REPORT_VOTES = "votes"
        const val WORK_ASSIGNMENT_REPORT_TIMESTAMP = "time_stamp"
        const val WORK_ASSIGNMENT_REPORT_LOG_ID = "log_id"

        // WORK ASSIGNMENT VERSION FIELDS
        const val WORK_ASSIGNMENT_VERSION_WORK_ASSIGN_ID = "work_assignment_id"
        const val WORK_ASSIGNMENT_VERSION_ID = "work_assignment_version"
        const val WORK_ASSIGNMENT_VERSION_CREATED_BY = "created_by"
        const val WORK_ASSIGNMENT_VERSION_SHEET_ID = "sheet_id"
        const val WORK_ASSIGNMENT_VERSION_SUPPLEMENT = "supplement_id"
        const val WORK_ASSIGNMENT_VERSION_DUE_DATE = "due_date"
        const val WORK_ASSIGNMENT_VERSION_INDIVIDUAL = "individual"
        const val WORK_ASSIGNMENT_VERSION_LATE_DELIVERY = "late_delivery"
        const val WORK_ASSIGNMENT_VERSION_MULTIPLE_DELIVERIES = "multipleDeliveries"
        const val WORK_ASSIGNMENT_VERSION_REQUIRES_REPORT = "requires_report"
        const val WORK_ASSIGNMENT_VERSION_TIMESTAMP = "time_stamp"
    }

    @Qualifier("MainJdbi")
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getSpecificWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, workAssignmentId: Int): Optional<WorkAssignment> =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).getSpecificWorkAssignmentOfCourseInTerm(courseId, termId, workAssignmentId)

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment> =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).getAllWorkAssignmentsFromSpecificTermOfCourse(courseId, termId)

    override fun createWorkAssignmentOnCourseInTerm(courseId: Int, termId: Int, workAssignment: WorkAssignment): WorkAssignment =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).createWorkAssignmentOnCourseInTerm(courseId, termId, workAssignment)

    override fun updateWorkAssignment(workAssignmentId: Int, workAssignment: WorkAssignment): WorkAssignment =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).updateWorkAssignment(workAssignmentId, workAssignment)

    override fun updateVotesOnWorkAssignment(workAssignmentId: Int, votes: Int): Int =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).updateVotesOnWorkAssignment(workAssignmentId, votes)

    override fun deleteSpecificWorkAssignment(courseId: Int, termId: Int, workAssignmentId: Int): Int =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).deleteSpecificWorkAssignment(courseId, termId, workAssignmentId)

    override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage> =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId)

    override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<WorkAssignmentStage> =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId, termId, stageId)

    override fun createStagingWorkAssingment(courseId: Int, termId: Int, workAssignmentStage: WorkAssignmentStage): WorkAssignmentStage =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).createStagingWorkAssingment(courseId, termId, workAssignmentStage)

    override fun updateStagedWorkAssignmentVotes(stageId: Int, votes: Int): Int =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).updateStagedWorkAssignmentVotes(stageId, votes)

    override fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId, termId, stageId)

    override fun getAllVersionsOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int): List<WorkAssignmentVersion> =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).getAllVersionsOfSpecificWorkAssignment(termId, courseId, workAssignmentId)

    override fun getVersionOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, version: Int): Optional<WorkAssignmentVersion> =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).getVersionOfSpecificWorkAssignment(termId, courseId, workAssignmentId, version)

    override fun createWorkAssignmentVersion(workAssignmentVersion: WorkAssignmentVersion): WorkAssignmentVersion =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).createWorkAssignmentVersion(workAssignmentVersion)

    override fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport> =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId, termId, workAssignmentId)

    override fun getSpecificReportOfWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int): Optional<WorkAssignmentReport> =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).getSpecificReportOfWorkAssignment(termId, courseId, workAssignmentId, reportId)

    override fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, workAssignmentReport: WorkAssignmentReport): WorkAssignmentReport =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).addReportToWorkAssignmentOnCourseInTerm(workAssignmentId, workAssignmentReport)

    override fun updateVotesOnReportedWorkAssignment(reportId: Int, votes: Int): Int =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).updateVotesOnReportedWorkAssignment(reportId, votes)

    override fun deleteReportOnWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int): Int =
            jdbi.open().attach(WorkAssignmentDAOJdbi::class.java).deleteReportOnWorkAssignment(termId, courseId, workAssignmentId, reportId)

    interface WorkAssignmentDAOJdbi : WorkAssignmentDAO {
        @CreateSqlObject
        fun createCourseDAO(): CourseDAOImpl.CourseDAOJdbi

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

        override fun deleteSpecificWorkAssignment(courseId: Int, termId: Int, workAssignmentId: Int): Int =
                createCourseDAO().deleteSpecificCourseMiscUnitEntry(courseId, termId, workAssignmentId)

        @SqlUpdate(
                "UPDATE $WORK_ASSIGNMENT_TABLE SET " +
                        "$WORK_ASSIGNMENT_VERSION = :workAssignment.version, " +
                        "$WORK_ASSIGNMENT_CREATED_BY = :workAssignment.createdBy, " +
                        "$WORK_ASSIGNMENT_SHEET_ID = :workAssignment.sheetId, " +
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

        @SqlUpdate(
                "UPDATE $WORK_ASSIGNMENT_TABLE SET $WORK_ASSIGNMENT_VOTES = :votes" +
                        " WHERE $WORK_ASSIGNMENT_ID = :workAssignmentId"
        )
        override fun updateVotesOnWorkAssignment(workAssignmentId: Int, votes: Int): Int

        override fun deleteSpecificStagedWorkAssignmentOfCourseInTerm(courseId: Int, termId: Int, stageId: Int): Int =
                createCourseDAO().deleteSpecificStagedCourseMiscUnitEntry(courseId, termId, stageId)

        @SqlUpdate(
                "DELETE FROM $WORK_ASSIGNMENT_REPORT_TABLE AS W " +
                        "USING $COURSE_MISC_UNIT_TABLE AS C " +
                        "WHERE W.$WORK_ASSIGNMENT_REPORT_WORK_ASSIGN_ID = C.$COURSE_MISC_UNIT_ID " +
                        "C.$COURSE_MISC_UNIT_COURSE_ID = :courseId AND " +
                        "C.$COURSE_MISC_UNIT_TERM_ID = :termId AND " +
                        "C.$COURSE_MISC_UNIT_ID = :workAssignmentId AND " +
                        "AND W.$WORK_ASSIGNMENT_REPORT_ID = :reportId"
        )
        override fun deleteReportOnWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int): Int

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
                        "W.$WORK_ASSIGNMENT_STAGE_CREATED_BY, " +
                        "W.$WORK_ASSIGNMENT_STAGE_SHEET_ID, " +
                        "W.$WORK_ASSIGNMENT_STAGE_SUPPLEMENT, " +
                        "W.$WORK_ASSIGNMENT_STAGE_DUE_DATE, " +
                        "W.$WORK_ASSIGNMENT_STAGE_INDIVIDUAL, " +
                        "W.$WORK_ASSIGNMENT_STAGE_LATE_DELIVERY, " +
                        "W.$WORK_ASSIGNMENT_STAGE_MULTIPLE_DELIVERIES, " +
                        "W.$WORK_ASSIGNMENT_STAGE_REQUIRES_REPORT, " +
                        "W.$WORK_ASSIGNMENT_STAGE_VOTES," +
                        "W.$WORK_ASSIGNMENT_STAGE_TIMESTAMP " +
                        "FROM $WORK_ASSIGNMENT_STAGE_TABLE AS W " +
                        "INNER JOIN $COURSE_MISC_UNIT_STAGE_TABLE AS C " +
                        "ON W.$WORK_ASSIGNMENT_STAGE_ID = C.$COURSE_MISC_UNIT_STAGE_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_STAGE_COURSE_ID = :courseId " +
                        "AND C.$COURSE_MISC_UNIT_STAGE_TERM_ID = :termId"
        )
        override fun getStageEntriesFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage>

        @SqlQuery(
                "SELECT W.$WORK_ASSIGNMENT_STAGE_ID, " +
                        "W.$WORK_ASSIGNMENT_STAGE_SHEET_ID," +
                        "W.$WORK_ASSIGNMENT_STAGE_SUPPLEMENT, " +
                        "W.$WORK_ASSIGNMENT_STAGE_DUE_DATE, " +
                        "W.$WORK_ASSIGNMENT_STAGE_INDIVIDUAL, " +
                        "W.$WORK_ASSIGNMENT_STAGE_LATE_DELIVERY, " +
                        "W.$WORK_ASSIGNMENT_STAGE_MULTIPLE_DELIVERIES, " +
                        "W.$WORK_ASSIGNMENT_STAGE_REQUIRES_REPORT, " +
                        "W.$WORK_ASSIGNMENT_STAGE_CREATED_BY, " +
                        "W.$WORK_ASSIGNMENT_STAGE_VOTES, " +
                        "W.$WORK_ASSIGNMENT_STAGE_TIMESTAMP " +
                        "FROM $WORK_ASSIGNMENT_STAGE_TABLE AS W " +
                        "INNER JOIN $COURSE_MISC_UNIT_STAGE_TABLE AS C " +
                        "ON W.$WORK_ASSIGNMENT_STAGE_ID = C.$COURSE_MISC_UNIT_STAGE_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_STAGE_COURSE_ID = :courseId " +
                        "AND C.$COURSE_MISC_UNIT_STAGE_TERM_ID = :termId " +
                        "AND C.$COURSE_MISC_UNIT_STAGE_ID = :stageId"
        )
        override fun getStageEntryFromWorkAssignmentOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): Optional<WorkAssignmentStage>

        @SqlQuery(
                "SELECT W.$WORK_ASSIGNMENT_REPORT_ID, " +
                        "W.$WORK_ASSIGNMENT_REPORT_WORK_ASSIGN_ID, " +
                        "W.$WORK_ASSIGNMENT_REPORT_SHEET_ID, " +
                        "W.$WORK_ASSIGNMENT_REPORT_SUPPLEMENT, " +
                        "W.$WORK_ASSIGNMENT_REPORT_DUE_DATE, " +
                        "W.$WORK_ASSIGNMENT_REPORT_INDIVIDUAL, " +
                        "W.$WORK_ASSIGNMENT_REPORT_LATE_DELIVERY, " +
                        "W.$WORK_ASSIGNMENT_REPORT_MULTIPLE_DELIVERIES, " +
                        "W.$WORK_ASSIGNMENT_REPORT_REQUIRES_REPORT, " +
                        "W.$WORK_ASSIGNMENT_REPORTED_BY, " +
                        "W.$WORK_ASSIGNMENT_REPORT_VOTES, " +
                        "W.$WORK_ASSIGNMENT_REPORT_TIMESTAMP " +
                        "FROM $WORK_ASSIGNMENT_REPORT_TABLE AS W" +
                        "INNER JOIN $COURSE_MISC_UNIT_TABLE AS C " +
                        "ON W.$WORK_ASSIGNMENT_REPORT_WORK_ASSIGN_ID = C.$COURSE_MISC_UNIT_ID " +
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
                        ":workAssignment.sheetId, :workAssignment.supplementId, :workAssignment.dueDate," +
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
            val courseMiscUnit = courseDAO.createCourseMiscUnit(courseId, termId, CourseMiscUnitType.WORK_ASSIGNMENT)
            return createWorkAssignment(courseMiscUnit.courseMiscUnitId, workAssignment)
        }

        @SqlUpdate(
                "INSERT INTO $WORK_ASSIGNMENT_REPORT_TABLE (" +
                        "$WORK_ASSIGNMENT_REPORT_WORK_ASSIGN_ID, " +
                        "$WORK_ASSIGNMENT_REPORT_SHEET_ID, " +
                        "$WORK_ASSIGNMENT_REPORT_SUPPLEMENT, " +
                        "$WORK_ASSIGNMENT_REPORT_DUE_DATE, " +
                        "$WORK_ASSIGNMENT_REPORT_INDIVIDUAL, " +
                        "$WORK_ASSIGNMENT_REPORT_LATE_DELIVERY, " +
                        "$WORK_ASSIGNMENT_REPORT_MULTIPLE_DELIVERIES, " +
                        "$WORK_ASSIGNMENT_REPORT_REQUIRES_REPORT, " +
                        "$WORK_ASSIGNMENT_REPORTED_BY, " +
                        "$WORK_ASSIGNMENT_REPORT_VOTES, " +
                        "$WORK_ASSIGNMENT_REPORT_TIMESTAMP) " +
                        "VALUES(:workAssignmentId, :workAssignmentReport.sheetId, :workAssignmentReport.supplementId, " +
                        ":workAssignmentReport.dueDate, :workAssignmentReport.individual, :workAssignmentReport.lateDelivery, " +
                        ":workAssignmentReport.multipleDeliveries, :workAssignmentReport.requiresReport, " +
                        ":workAssignmentReport.reportedBy, :workAssignmentReport.votes, :workAssignmentReport.timestamp)"
        )
        @GetGeneratedKeys
        override fun addReportToWorkAssignmentOnCourseInTerm(workAssignmentId: Int, workAssignmentReport: WorkAssignmentReport): WorkAssignmentReport

        @SqlUpdate(
                "UPDATE $WORK_ASSIGNMENT_REPORT_TABLE SET $WORK_ASSIGNMENT_REPORT_VOTES = :votes " +
                        "WHERE $WORK_ASSIGNMENT_REPORT_ID = :reportId"
        )
        override fun updateVotesOnReportedWorkAssignment(reportId: Int, votes: Int): Int

        @SqlQuery(
                "SELECT W.$WORK_ASSIGNMENT_REPORT_ID, " +
                        "W.$WORK_ASSIGNMENT_REPORT_WORK_ASSIGN_ID, " +
                        "W.$WORK_ASSIGNMENT_REPORT_SHEET_ID, " +
                        "W.$WORK_ASSIGNMENT_REPORT_SUPPLEMENT, " +
                        "W.$WORK_ASSIGNMENT_REPORT_DUE_DATE, " +
                        "W.$WORK_ASSIGNMENT_REPORT_INDIVIDUAL, " +
                        "W.$WORK_ASSIGNMENT_REPORT_LATE_DELIVERY, " +
                        "W.$WORK_ASSIGNMENT_REPORT_MULTIPLE_DELIVERIES, " +
                        "W.$WORK_ASSIGNMENT_REPORT_REQUIRES_REPORT, " +
                        "W.$WORK_ASSIGNMENT_REPORTED_BY, " +
                        "W.$WORK_ASSIGNMENT_REPORT_VOTES, " +
                        "W.$WORK_ASSIGNMENT_REPORT_TIMESTAMP " +
                        "FROM $WORK_ASSIGNMENT_REPORT_TABLE AS W" +
                        "INNER JOIN $COURSE_MISC_UNIT_TABLE AS C " +
                        "ON W.$WORK_ASSIGNMENT_REPORT_WORK_ASSIGN_ID = C.$COURSE_MISC_UNIT_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_ID = :workAssignmentId" +
                        "AND C.$COURSE_MISC_UNIT_TERM_ID = :termId " +
                        "AND C.$COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                        "AND W.$WORK_ASSIGNMENT_REPORT_ID = :reportId"
        )
        override fun getSpecificReportOfWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, reportId: Int): Optional<WorkAssignmentReport>

        @SqlUpdate(
                "INSERT INTO $WORK_ASSIGNMENT_VERSION_TABLE ( " +
                        "$WORK_ASSIGNMENT_VERSION_WORK_ASSIGN_ID," +
                        "$WORK_ASSIGNMENT_VERSION, " +
                        "$WORK_ASSIGNMENT_VERSION_SHEET_ID, " +
                        "$WORK_ASSIGNMENT_VERSION_SUPPLEMENT, " +
                        "$WORK_ASSIGNMENT_VERSION_DUE_DATE, " +
                        "$WORK_ASSIGNMENT_VERSION_INDIVIDUAL, " +
                        "$WORK_ASSIGNMENT_VERSION_LATE_DELIVERY, " +
                        "$WORK_ASSIGNMENT_VERSION_MULTIPLE_DELIVERIES, " +
                        "$WORK_ASSIGNMENT_VERSION_REQUIRES_REPORT, " +
                        "$WORK_ASSIGNMENT_VERSION_CREATED_BY, " +
                        "$WORK_ASSIGNMENT_VERSION_TIMESTAMP " +
                        ") " +
                        "VALUES (:workAssignmentVersion.workAssignmentId, :workAssignmentVersion.version, " +
                        ":workAssignmentVersion.sheetId, :workAssignmentVersion.supplementId, " +
                        ":workAssignmentVersion.dueDate, :workAssignmentVersion.individual, " +
                        ":workAssignmentVersion.lateDelivery, :workAssignmentVersion.multipleDeliveries, " +
                        ":workAssignmentVersion.requiresReport, :workAssignmentVersion.createdBy, :workAssignmentVersion.timestamp)"
        )
        @GetGeneratedKeys
        override fun createWorkAssignmentVersion(workAssignmentVersion: WorkAssignmentVersion): WorkAssignmentVersion

        @SqlUpdate(
                "INSERT INTO $WORK_ASSIGNMENT_STAGE_TABLE (" +
                        "$WORK_ASSIGNMENT_STAGE_ID, " +
                        "$WORK_ASSIGNMENT_STAGE_SHEET_ID, " +
                        "$WORK_ASSIGNMENT_STAGE_SUPPLEMENT, " +
                        "$WORK_ASSIGNMENT_STAGE_DUE_DATE, " +
                        "$WORK_ASSIGNMENT_STAGE_INDIVIDUAL, " +
                        "$WORK_ASSIGNMENT_STAGE_LATE_DELIVERY, " +
                        "$WORK_ASSIGNMENT_STAGE_MULTIPLE_DELIVERIES, " +
                        "$WORK_ASSIGNMENT_STAGE_REQUIRES_REPORT, " +
                        "$WORK_ASSIGNMENT_STAGE_CREATED_BY, " +
                        "$WORK_ASSIGNMENT_STAGE_VOTES, " +
                        "$WORK_ASSIGNMENT_STAGE_TIMESTAMP) " +
                        "VALUES(:stageId, :workAssignmentStage.sheetId, :workAssignmentStage.supplementId, :workAssignmentStage.dueDate," +
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
            val courseMiscUnitStage = courseDAO.createStagingCourseMiscUnit(courseId, termId, CourseMiscUnitType.WORK_ASSIGNMENT)
            return createStagingWorkAssignment(workAssignmentStage, courseMiscUnitStage.stageId)
        }

        @SqlUpdate(
                "UPDATE $WORK_ASSIGNMENT_STAGE_TABLE SET $WORK_ASSIGNMENT_STAGE_VOTES = :votes " +
                        "WHERE $WORK_ASSIGNMENT_STAGE_ID = :stageId"
        )
        override fun updateStagedWorkAssignmentVotes(stageId: Int, votes: Int): Int

        @SqlQuery(
                "SELECT * " +
                        "W.$WORK_ASSIGNMENT_VERSION_WORK_ASSIGN_ID," +
                        "W.$WORK_ASSIGNMENT_VERSION, " +
                        "W.$WORK_ASSIGNMENT_VERSION_SHEET_ID, " +
                        "W.$WORK_ASSIGNMENT_VERSION_SUPPLEMENT, " +
                        "W.$WORK_ASSIGNMENT_VERSION_DUE_DATE, " +
                        "W.$WORK_ASSIGNMENT_VERSION_INDIVIDUAL, " +
                        "W.$WORK_ASSIGNMENT_VERSION_LATE_DELIVERY, " +
                        "W.$WORK_ASSIGNMENT_VERSION_MULTIPLE_DELIVERIES, " +
                        "W.$WORK_ASSIGNMENT_VERSION_REQUIRES_REPORT, " +
                        "W.$WORK_ASSIGNMENT_VERSION_CREATED_BY, " +
                        "W.$WORK_ASSIGNMENT_VERSION_TIMESTAMP " +
                        "FROM $WORK_ASSIGNMENT_VERSION_TABLE AS W " +
                        "INNER JOIN $COURSE_MISC_UNIT_TABLE as C ON W.$WORK_ASSIGNMENT_VERSION_WORK_ASSIGN_ID = C.$COURSE_MISC_UNIT_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId AND C.$COURSE_MISC_UNIT_TERM_ID = :termId " +
                        "C.$COURSE_MISC_UNIT_ID = :workAssignment"
        )
        override fun getAllVersionsOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int): List<WorkAssignmentVersion>

        @SqlQuery(
                "SELECT * " +
                        "W.$WORK_ASSIGNMENT_VERSION_WORK_ASSIGN_ID," +
                        "W.$WORK_ASSIGNMENT_VERSION, " +
                        "W.$WORK_ASSIGNMENT_VERSION_SHEET_ID, " +
                        "W.$WORK_ASSIGNMENT_VERSION_SUPPLEMENT, " +
                        "W.$WORK_ASSIGNMENT_VERSION_DUE_DATE, " +
                        "W.$WORK_ASSIGNMENT_VERSION_INDIVIDUAL, " +
                        "W.$WORK_ASSIGNMENT_VERSION_LATE_DELIVERY, " +
                        "W.$WORK_ASSIGNMENT_VERSION_MULTIPLE_DELIVERIES, " +
                        "W.$WORK_ASSIGNMENT_VERSION_REQUIRES_REPORT, " +
                        "W.$WORK_ASSIGNMENT_VERSION_CREATED_BY, " +
                        "W.$WORK_ASSIGNMENT_VERSION_TIMESTAMP " +
                        "FROM $WORK_ASSIGNMENT_VERSION_TABLE AS W " +
                        "INNER JOIN $COURSE_MISC_UNIT_TABLE as C ON W.$WORK_ASSIGNMENT_VERSION_WORK_ASSIGN_ID = C.$COURSE_MISC_UNIT_ID " +
                        "WHERE C.$COURSE_MISC_UNIT_COURSE_ID = :courseId AND C.$COURSE_MISC_UNIT_TERM_ID = :termId " +
                        "AND C.$COURSE_MISC_UNIT_ID = :workAssignment AND W.$WORK_ASSIGNMENT_VERSION_ID = :version"
        )
        override fun getVersionOfSpecificWorkAssignment(termId: Int, courseId: Int, workAssignmentId: Int, version: Int): Optional<WorkAssignmentVersion>

    }

}