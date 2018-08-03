package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.ClassMiscUnit
import isel.leic.ps.eduWikiAPI.domain.model.Homework
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.HomeworkReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_TYPE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.HomeworkDAO
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import java.util.*

interface HomeworkDAOJdbi : HomeworkDAO {

    companion object {
        //TABLE NAMES
        const val HOMEWORK_TABLE = "homework"
        const val HOMEWORK_STAGE_TABLE = "homework_stage"
        const val HOMEWORK_VERSION_TABLE = "homework_version"
        const val HOMEWORK_REPORT_TABLE = "homework_report"
        // FIELDS
        const val HOMEWORK_VERSION = "homework_version"
        const val HOMEWORK_ID = "homework_id"
        const val HOMEWORK_STAGE_ID = "class_misc_unit_stage_id"
        const val HOMEWORK_REPORT_ID = "homework_report_id"
        const val HOMEWORK_SHEET = "sheetId"
        const val HOMEWORK_DUE_DATE = "due_date"
        const val HOMEWORK_LATE_DELIVERY = "late_delivery"
        const val HOMEWORK_MULTIPLE_DELIVERIES = "multipleDeliveries"
        const val HOMEWORK_VOTES = "votes"
        const val HOMEWORK_TIMESTAMP = "time_stamp"
        const val HOMEWORK_REPORTED_BY = "reported_by"
        const val HOMEWORK_CREATED_BY = "created_by"
    }

    @CreateSqlObject
    fun createClassDAO(): ClassDAOJdbi

    @SqlQuery(
            "SELECT H.$HOMEWORK_ID, " +
                    "H.$HOMEWORK_VERSION, " +
                    "H.$HOMEWORK_CREATED_BY, " +
                    "H.$HOMEWORK_SHEET, " +
                    "H.$HOMEWORK_DUE_DATE, " +
                    "H.$HOMEWORK_LATE_DELIVERY, " +
                    "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                    "H.$HOMEWORK_VOTES, " +
                    "H.$HOMEWORK_TIMESTAMP " +
                    "FROM $HOMEWORK_TABLE AS H " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON L.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_CLASS_ID = :courseClassId"
    )
    override fun getAllHomeworksFromCourseInClass(courseClassId: Int): List<Homework>

    @SqlQuery(
            "SELECT H.$HOMEWORK_ID, " +
                    "H.$HOMEWORK_VERSION, " +
                    "H.$HOMEWORK_CREATED_BY, " +
                    "H.$HOMEWORK_SHEET, " +
                    "H.$HOMEWORK_DUE_DATE, " +
                    "H.$HOMEWORK_LATE_DELIVERY, " +
                    "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                    "H.$HOMEWORK_VOTES, " +
                    "H.$HOMEWORK_TIMESTAMP " +
                    "FROM $HOMEWORK_TABLE AS H " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON L.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_CLASS_ID = :courseClassId " +
                    "AND H.$HOMEWORK_ID = :homeworkId"
    )
    override fun getSpecificHomeworkFromSpecificCourseInClass(courseClassId: Int, homeworkId: Int): Optional<Homework>

    @SqlUpdate(
            "INSERT INTO $HOMEWORK_TABLE ( " +
                    "$HOMEWORK_ID, " +
                    "$HOMEWORK_VERSION, " +
                    "$HOMEWORK_CREATED_BY, " +
                    "$HOMEWORK_SHEET, " +
                    "$HOMEWORK_DUE_DATE, " +
                    "$HOMEWORK_LATE_DELIVERY, " +
                    "$HOMEWORK_MULTIPLE_DELIVERIES, " +
                    "$HOMEWORK_VOTES, " +
                    "$HOMEWORK_TIMESTAMP " +
                    ") " +
                    "VALUES (:classMiscUnitId, :homework.version, :homework.createdBy, " +
                    ":homework.sheetId, :homework.dueDate, :homework.lateDelivery, " +
                    ":homework.multipleDeliveries, :homework.votes, :homework.timestamp)"
    )
    @GetGeneratedKeys
    fun createHomework(classMiscUnitId: Int, homework: Homework): Homework

    @Transaction
    override fun createHomeworkOnCourseInClass(courseClassId: Int, homework: Homework): Homework {
        val classMiscUnit = createClassDAO().createClassMiscUnit(courseClassId, "Homework")
        return createHomework(classMiscUnit.classMiscUnitId, homework)
    }

    @SqlQuery(
            "SELECT H.$HOMEWORK_VOTES " +
                    "FROM $HOMEWORK_TABLE AS H " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON H.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_CLASS_ID = :courseClassId " +
                    "AND C.$CLASS_MISC_UNIT_TYPE = :miscType " +
                    "AND L.$HOMEWORK_ID = :homeworkId"
    )
    override fun getVotesOnHomework(courseClassId: Int, homeworkId: Int): Int

    @SqlUpdate(
            "UPDATE $HOMEWORK_TABLE AS H SET H.$HOMEWORK_VOTES = :votes " +
                    "FROM $CLASS_MISC_UNIT_TABLE AS C" +
                    "WHERE H.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                    "AND H.$HOMEWORK_ID = :homeworkId"
    )
    override fun updateVotesOnHomework(homeworkId: Int, votes: Int): Int

    override fun deleteAllHomeworksOfCourseInClass(courseClassId: Int): Int =
            createClassDAO().deleteAllClassMiscUnitsFromTypeOfCourseInClass(courseClassId, "Homework")

    override fun deleteSpecificHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int): Int =
            createClassDAO().deleteSpecificClassMiscUnitFromTypeOnCourseInClass(
                    courseClassId,
                    homeworkId,
                    "Homework"
            )

    @SqlQuery(
            "SELECT H.$HOMEWORK_STAGE_ID, " +
                    "H.$HOMEWORK_SHEET, " +
                    "H.$HOMEWORK_DUE_DATE, " +
                    "H.$HOMEWORK_LATE_DELIVERY, " +
                    "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                    "H.$HOMEWORK_TIMESTAMP, " +
                    "H.$HOMEWORK_VOTES, " +
                    "H.$HOMEWORK_CREATED_BY " +
                    "FROM $HOMEWORK_STAGE_TABLE AS H " +
                    "INNER JOIN $CLASS_MISC_UNIT_STAGE_TABLE AS C " +
                    "ON H.$HOMEWORK_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId"
    )
    override fun getAllStagedHomeworksOfCourseInClass(courseClassId: Int): List<HomeworkStage>

    @SqlQuery(
            "SELECT H.$HOMEWORK_STAGE_ID, " +
                    "H.$HOMEWORK_SHEET, " +
                    "H.$HOMEWORK_DUE_DATE, " +
                    "H.$HOMEWORK_LATE_DELIVERY, " +
                    "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                    "H.$HOMEWORK_TIMESTAMP, " +
                    "H.$HOMEWORK_VOTES, " +
                    "H.$HOMEWORK_CREATED_BY " +
                    "FROM $HOMEWORK_STAGE_TABLE AS H " +
                    "INNER JOIN $CLASS_MISC_UNIT_STAGE_TABLE AS C " +
                    "ON H.$HOMEWORK_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND H.$HOMEWORK_STAGE_ID = :stageId"
    )
    override fun getSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Optional<HomeworkStage>

    @SqlQuery(
            "INSERT INTO $HOMEWORK_STAGE_TABLE ( " +
                    "$HOMEWORK_STAGE_ID, " +
                    "$HOMEWORK_SHEET, " +
                    "$HOMEWORK_DUE_DATE, " +
                    "$HOMEWORK_LATE_DELIVERY, " +
                    "$HOMEWORK_MULTIPLE_DELIVERIES, " +
                    "$HOMEWORK_TIMESTAMP, " +
                    "$HOMEWORK_VOTES," +
                    "$HOMEWORK_CREATED_BY " +
                    ") " +
                    "VALUES(:stagedClassMiscUnitId, :homeworkStage.sheetId, :homeworkStage.dueDate, " +
                    ":homeworkStage.lateDelivery, :homeworkStage.multipleDeliveries, :homeworkStage.timestamp, " +
                    ":homeworkStage.votes, :homeworkStage.createdBy)"
    )
    @GetGeneratedKeys
    fun createStagingHomework(stagedClassMiscUnitId: Int, homeworkStage: HomeworkStage): HomeworkStage

    @Transaction
    override fun createStagingHomeworkOnCourseInClass(courseClassId: Int, homeworkStage: HomeworkStage): HomeworkStage {
        val classMiscUnitStage = createClassDAO().createStagingClassMiscUnit(courseClassId, "Homework")
        return createStagingHomework(classMiscUnitStage.courseClassId, homeworkStage)
    }

    override fun deleteSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Int =
            createClassDAO().deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId, stageId, "Homework")

    @SqlUpdate(
            "INSERT INTO $HOMEWORK_VERSION_TABLE ( " +
                    "$HOMEWORK_ID, " +
                    "$HOMEWORK_VERSION, " +
                    "$HOMEWORK_CREATED_BY, " +
                    "$HOMEWORK_SHEET, " +
                    "$HOMEWORK_DUE_DATE, " +
                    "$HOMEWORK_LATE_DELIVERY, " +
                    "$HOMEWORK_MULTIPLE_DELIVERIES, " +
                    "$HOMEWORK_TIMESTAMP " +
                    ") " +
                    "VALUES (:homeworkVersion.homeworkId, :homeworkVersion.version, :homeworkVersion.createdBy, " +
                    ":homeworkVersion.sheetId, :homeworkVersion.dueDate, :homeworkVersion.lateDelivery, " +
                    ":homeworkVersion.multipleDeliveries, :homeworkVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createHomeworkVersion(homeworkVersion: HomeworkVersion): HomeworkVersion

    @SqlQuery(
            "SELECT L.$HOMEWORK_VOTES" +
                    "FROM $HOMEWORK_STAGE_TABLE as L" +
                    "INNER JOIN $CLASS_MISC_UNIT_STAGE_TABLE as C" +
                    "on L.$HOMEWORK_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID" +
                    "WHERE C.$COURSE_CLASS_ID = :courseClassId" +
                    "AND L.$HOMEWORK_STAGE_ID = :stageId"
    )
    override fun getVotesOnStagedHomework(courseClassId: Int, stageId: Int): Int

    @SqlUpdate(
            "UPDATE $HOMEWORK_STAGE_TABLE as L SET L.$HOMEWORK_VOTES = :votes " +
                    "FROM $CLASS_MISC_UNIT_STAGE_TABLE as C" +
                    "WHERE L.$HOMEWORK_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                    "AND L.$HOMEWORK_STAGE_ID = :stageId"
    )
    override fun updateVotesOnStagedHomework(stageId: Int, votes: Int): Int

    @SqlQuery(
            "SELECT H.$HOMEWORK_REPORT_ID, " +
                    "H.$HOMEWORK_ID, " +
                    "H.$HOMEWORK_SHEET, " +
                    "H.$HOMEWORK_DUE_DATE, " +
                    "H.$HOMEWORK_LATE_DELIVERY, " +
                    "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                    "H.$HOMEWORK_REPORTED_BY, " +
                    "H.$HOMEWORK_VOTES, " +
                    "H.$HOMEWORK_TIMESTAMP " +
                    "FROM $HOMEWORK_TABLE AS H " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON H.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND H.$HOMEWORK_ID = :homeworkId"
    )
    override fun getAllReportsOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int): List<HomeworkReport>

    @SqlQuery(
            "SELECT H.$HOMEWORK_REPORT_ID, " +
                    "H.$HOMEWORK_ID, " +
                    "H.$HOMEWORK_SHEET, " +
                    "H.$HOMEWORK_DUE_DATE, " +
                    "H.$HOMEWORK_LATE_DELIVERY, " +
                    "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                    "H.$HOMEWORK_REPORTED_BY, " +
                    "H.$HOMEWORK_VOTES, " +
                    "H.$HOMEWORK_TIMESTAMP " +
                    "FROM $HOMEWORK_TABLE AS H " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON H.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND H.$HOMEWORK_ID = :homeworkId " +
                    "AND H.$HOMEWORK_REPORT_ID = :reportId"
    )
    override fun getSpecificReportOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Optional<HomeworkReport>

    @SqlUpdate(
            "INSERT INTO $HOMEWORK_REPORT_TABLE ( " +
                    "$HOMEWORK_ID, " +
                    "$HOMEWORK_SHEET, " +
                    "$HOMEWORK_DUE_DATE, " +
                    "$HOMEWORK_LATE_DELIVERY, " +
                    "$HOMEWORK_MULTIPLE_DELIVERIES, " +
                    "$HOMEWORK_REPORTED_BY, " +
                    "$HOMEWORK_VOTES, " +
                    "$HOMEWORK_TIMESTAMP " +
                    ") " +
                    "VALUES (:homeworkReport.homeworkId, :homeworkReport.sheetId, :homeworkReport.dueDate, " +
                    ":homeworkReport.lateDelivery, :homeworkReport.multipleDeliveries, " +
                    ":homeworkReport.reportedBy, :homeworkReport.votes, :homeworkReport.timestamp)"
    )
    @GetGeneratedKeys
    override fun createReportOnHomework(homeworkReport: HomeworkReport): HomeworkReport

    @SqlUpdate(
            "UPDATE $HOMEWORK_TABLE SET " +
                    "$HOMEWORK_VERSION = :homework.version, " +
                    "$HOMEWORK_CREATED_BY = :homework.createdBy, " +
                    "$HOMEWORK_SHEET = :homework.sheetId, " +
                    "$HOMEWORK_DUE_DATE = :homework.dueDate, " +
                    "$HOMEWORK_LATE_DELIVERY = :homework.lateDelivery, " +
                    "$HOMEWORK_MULTIPLE_DELIVERIES = :homework.multipleDeliveries, " +
                    "$HOMEWORK_VOTES = :homework.votes " +
                    "$HOMEWORK_TIMESTAMP = :homework.timestamp " +
                    "WHERE $HOMEWORK_ID = :homework.homeworkId"
    )
    @GetGeneratedKeys
    override fun updateHomeWork(homework: Homework): Homework

    @SqlQuery(
            "SELECT $HOMEWORK_VOTES FROM $HOMEWORK_REPORT_TABLE " +
                    "WHERE $HOMEWORK_ID = :homeworkId" +
                    "AND $HOMEWORK_REPORT_ID = :reportId"
    )
    override fun getVotesOnReportedHomework(homeworkId: Int, reportId: Int): Int

    @SqlUpdate(
            "DELETE FROM $HOMEWORK_REPORT_TABLE " +
                    "USING $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $HOMEWORK_ID = $CLASS_MISC_UNIT_ID " +
                    "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $HOMEWORK_ID = :homeworkId"
    )
    override fun updateVotesOnReportedLecture(lectureId: Any, reportId: Int, votes: Int): Int

    @SqlUpdate(
            "DELETE FROM $HOMEWORK_REPORT_TABLE " +
                    "USING $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $HOMEWORK_ID = $CLASS_MISC_UNIT_ID " +
                    "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $HOMEWORK_ID = :homeworkId"
    )
    override fun deleteAllReportsOnHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int): Int

    @SqlUpdate(
            "DELETE FROM $HOMEWORK_REPORT_TABLE " +
                    "USING $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $HOMEWORK_ID = $CLASS_MISC_UNIT_ID " +
                    "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $HOMEWORK_ID = :homeworkId " +
                    "AND $HOMEWORK_REPORT_ID = :reportId"
    )
    override fun deleteSpecificReportOnHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Int

    @SqlQuery(
            "SELECT H.$HOMEWORK_ID, " +
                    "H.$HOMEWORK_VERSION, " +
                    "H.$HOMEWORK_CREATED_BY, " +
                    "H.$HOMEWORK_SHEET, " +
                    "H.$HOMEWORK_DUE_DATE, " +
                    "H.$HOMEWORK_LATE_DELIVERY, " +
                    "H.$HOMEWORK_TIMESTAMP, " +
                    "H.$HOMEWORK_MULTIPLE_DELIVERIES " +
                    "FROM $HOMEWORK_VERSION_TABLE AS H " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON H.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND H.$HOMEWORK_ID = :homeworkId"
    )
    override fun getAllVersionsOfHomeworkOfCourseInclass(courseClassId: Int, homeworkId: Int): List<HomeworkVersion>

    @SqlQuery(
            "SELECT H.$HOMEWORK_ID, " +
                    "H.$HOMEWORK_VERSION, " +
                    "H.$HOMEWORK_CREATED_BY, " +
                    "H.$HOMEWORK_SHEET, " +
                    "H.$HOMEWORK_DUE_DATE, " +
                    "H.$HOMEWORK_LATE_DELIVERY, " +
                    "H.$HOMEWORK_TIMESTAMP, " +
                    "H.$HOMEWORK_MULTIPLE_DELIVERIES " +
                    "FROM $HOMEWORK_VERSION_TABLE AS H " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON H.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND H.$HOMEWORK_ID = :homeworkId " +
                    "AND H.$HOMEWORK_VERSION = :version"
    )
    override fun getSpecificVersionOfHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, version: Int): Optional<HomeworkVersion>

    @SqlUpdate(
            "DELETE FROM $HOMEWORK_VERSION_TABLE " +
                    "USING $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $HOMEWORK_ID = $CLASS_MISC_UNIT_ID " +
                    "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $HOMEWORK_ID = :homeworkId"
    )
    override fun deleteAllVersionsOfHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int): Int

    @SqlUpdate(
            "DELETE FROM $HOMEWORK_VERSION_TABLE " +
                    "USING $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $HOMEWORK_ID = $CLASS_MISC_UNIT_ID " +
                    "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $HOMEWORK_ID = :homeworkId " +
                    "AND $HOMEWORK_VERSION = :version"
    )
    override fun deleteSpecificVersionOfHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, version: Int): Int

    override fun deleteAllStagedHomeworksOfCourseInClass(courseClassId: Int): Int =
            createClassDAO().deleteAllStagedClassMiscUnitsFromTypeOfCourseInClass(courseClassId, "Homework")

}