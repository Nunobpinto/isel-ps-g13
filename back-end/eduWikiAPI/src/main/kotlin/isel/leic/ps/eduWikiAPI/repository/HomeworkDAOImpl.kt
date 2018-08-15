package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.enums.ClassMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.Homework
import isel.leic.ps.eduWikiAPI.domain.model.report.HomeworkReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.HomeworkStage
import isel.leic.ps.eduWikiAPI.domain.model.version.HomeworkVersion
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.HomeworkDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class HomeworkDAOImpl : HomeworkDAO {

    companion object {
        //TABLE NAMES
        const val HOMEWORK_TABLE = "homework"
        const val HOMEWORK_STAGE_TABLE = "homework_stage"
        const val HOMEWORK_VERSION_TABLE = "homework_version"
        const val HOMEWORK_REPORT_TABLE = "homework_report"
        // HOMEWORK FIELDS
        const val HOMEWORK_VERSION = "homework_version"
        const val HOMEWORK_LOG_ID = "log_id"
        const val HOMEWORK_ID = "homework_id"
        const val HOMEWORK_SHEET_ID = "sheet_id"
        const val HOMEWORK_DUE_DATE = "due_date"
        const val HOMEWORK_LATE_DELIVERY = "late_delivery"
        const val HOMEWORK_MULTIPLE_DELIVERIES = "multiple_deliveries"
        const val HOMEWORK_VOTES = "votes"
        const val HOMEWORK_TIMESTAMP = "time_stamp"
        const val HOMEWORK_CREATED_BY = "created_by"
        // HOMEWORK REPORT FIELDS
        const val HOMEWORK_REPORT_ID = "homework_report_id"
        const val HOMEWORK_REPORT_HOMEWORK_ID = "homework_id"
        const val HOMEWORK_REPORTED_BY = "reported_by"
        const val HOMEWORK_REPORT_LOG_ID = "log_id"
        const val HOMEWORK_REPORT_SHEET_ID = "sheet_id"
        const val HOMEWORK_REPORT_DUE_DATE = "due_date"
        const val HOMEWORK_REPORT_LATE_DELIVERY = "late_delivery"
        const val HOMEWORK_REPORT_MULTIPLE_DELIVERIES = "multiple_deliveries"
        const val HOMEWORK_REPORT_VOTES = "votes"
        const val HOMEWORK_REPORT_TIMESTAMP = "time_stamp"
        // HOMEWORK STAGE FIELDS
        const val HOMEWORK_STAGE_ID = "homework_stage_id"
        const val HOMEWORK_STAGE_CREATED_BY = "created_by"
        const val HOMEWORK_STAGE_LOG_ID = "log_id"
        const val HOMEWORK_STAGE_SHEET_ID = "sheet_id"
        const val HOMEWORK_STAGE_DUE_DATE = "due_date"
        const val HOMEWORK_STAGE_LATE_DELIVERY = "late_delivery"
        const val HOMEWORK_STAGE_MULTIPLE_DELIVERIES = "multiple_deliveries"
        const val HOMEWORK_STAGE_VOTES = "votes"
        const val HOMEWORK_STAGE_TIMESTAMP = "time_stamp"
        // HOMEWORK VERSION FIELDS
        const val HOMEWORK_VERSION_ID = "homework_version"
        const val HOMEWORK_VERSION_HOMEWORK_ID = "homework_id"
        const val HOMEWORK_VERSION_SHEET_ID = "sheet_id"
        const val HOMEWORK_VERSION_DUE_DATE = "due_date"
        const val HOMEWORK_VERSION_LATE_DELIVERY = "late_delivery"
        const val HOMEWORK_VERSION_MULTIPLE_DELIVERIES = "multiple_deliveries"
        const val HOMEWORK_VERSION_TIMESTAMP = "time_stamp"
        const val HOMEWORK_VERSION_CREATED_BY = "created_by"
    }

    @Qualifier("MainJdbi")
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAllHomeworksFromCourseInClass(courseClassId: Int): List<Homework> =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).getAllHomeworksFromCourseInClass(courseClassId)

    override fun getSpecificHomeworkFromSpecificCourseInClass(courseClassId: Int, homeworkId: Int): Optional<Homework> =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).getSpecificHomeworkFromSpecificCourseInClass(courseClassId, homeworkId)

    override fun createHomeworkOnCourseInClass(courseClassId: Int, homework: Homework): Homework =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).createHomeworkOnCourseInClass(courseClassId, homework)

    override fun updateHomework(homework: Homework): Homework =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).updateHomework(homework)

    override fun updateVotesOnHomework(homeworkId: Int, votes: Int): Int =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).updateVotesOnHomework(homeworkId, votes)

    override fun deleteSpecificHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int): Int =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).deleteSpecificHomeworkOfCourseInClass(courseClassId, homeworkId)

    override fun getAllReportsOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int): List<HomeworkReport> =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).getAllReportsOfHomeworkFromCourseInClass(courseClassId, homeworkId)

    override fun getSpecificReportOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Optional<HomeworkReport> =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).getSpecificReportOfHomeworkFromCourseInClass(courseClassId, homeworkId, reportId)

    override fun createReportOnHomework(homeworkReport: HomeworkReport): HomeworkReport =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).createReportOnHomework(homeworkReport)

    override fun updateVotesOnReportedHomework(homeworkId: Int, reportId: Int, votes: Int): Int =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).updateVotesOnReportedHomework(homeworkId, reportId, votes)

    override fun deleteSpecificReportOnHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Int =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).deleteSpecificReportOnHomeworkOfCourseInClass(courseClassId, homeworkId, reportId)

    override fun getAllStagedHomeworksOfCourseInClass(courseClassId: Int): List<HomeworkStage> =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).getAllStagedHomeworksOfCourseInClass(courseClassId)

    override fun getSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Optional<HomeworkStage> =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).getSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)

    override fun createStagingHomeworkOnCourseInClass(courseClassId: Int, homeworkStage: HomeworkStage): HomeworkStage =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).createStagingHomeworkOnCourseInClass(courseClassId, homeworkStage)

    override fun updateVotesOnStagedHomework(stageId: Int, votes: Int): Int =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).updateVotesOnStagedHomework(stageId, votes)

    override fun deleteSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Int =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).deleteSpecificStagedHomeworkOfCourseInClass(courseClassId, stageId)

    override fun getAllVersionsOfHomeworkOfCourseInclass(courseClassId: Int, homeworkId: Int): List<HomeworkVersion> =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).getAllVersionsOfHomeworkOfCourseInclass(courseClassId, homeworkId)

    override fun getSpecificVersionOfHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, version: Int): Optional<HomeworkVersion> =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).getSpecificVersionOfHomeworkOfCourseInClass(courseClassId, homeworkId, version)

    override fun createHomeworkVersion(homeworkVersion: HomeworkVersion): HomeworkVersion =
            jdbi.open().attach(HomeworkDAOJdbi::class.java).createHomeworkVersion(homeworkVersion)

    interface HomeworkDAOJdbi : HomeworkDAO {
        @CreateSqlObject
        fun createClassDAO(): ClassDAOImpl.ClassDAOJdbi

        @SqlQuery(
                "SELECT H.$HOMEWORK_ID, " +
                        "H.$HOMEWORK_VERSION, " +
                        "H.$HOMEWORK_CREATED_BY, " +
                        "H.$HOMEWORK_SHEET_ID, " +
                        "H.$HOMEWORK_DUE_DATE, " +
                        "H.$HOMEWORK_LATE_DELIVERY, " +
                        "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                        "H.$HOMEWORK_VOTES, " +
                        "H.$HOMEWORK_TIMESTAMP, " +
                        "H.$HOMEWORK_LOG_ID " +
                        "FROM $HOMEWORK_TABLE AS H " +
                        "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                        "ON H.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                        "WHERE C.$COURSE_CLASS_ID = :courseClassId"
        )
        override fun getAllHomeworksFromCourseInClass(courseClassId: Int): List<Homework>

        @SqlQuery(
                "SELECT H.$HOMEWORK_ID, " +
                        "H.$HOMEWORK_VERSION, " +
                        "H.$HOMEWORK_CREATED_BY, " +
                        "H.$HOMEWORK_SHEET_ID, " +
                        "H.$HOMEWORK_DUE_DATE, " +
                        "H.$HOMEWORK_LATE_DELIVERY, " +
                        "H.$HOMEWORK_MULTIPLE_DELIVERIES, " +
                        "H.$HOMEWORK_VOTES, " +
                        "H.$HOMEWORK_TIMESTAMP, " +
                        "H.$HOMEWORK_LOG_ID " +
                        "FROM $HOMEWORK_TABLE AS H " +
                        "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                        "ON H.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                        "WHERE C.$COURSE_CLASS_ID = :courseClassId " +
                        "AND H.$HOMEWORK_ID = :homeworkId"
        )
        override fun getSpecificHomeworkFromSpecificCourseInClass(courseClassId: Int, homeworkId: Int): Optional<Homework>

        @SqlUpdate(
                "INSERT INTO $HOMEWORK_TABLE ( " +
                        "$HOMEWORK_ID, " +
                        "$HOMEWORK_VERSION, " +
                        "$HOMEWORK_CREATED_BY, " +
                        "$HOMEWORK_SHEET_ID, " +
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
            val classMiscUnit = createClassDAO().createClassMiscUnit(courseClassId, ClassMiscUnitType.HOMEWORK)
            return createHomework(classMiscUnit.classMiscUnitId, homework)
        }

        @SqlUpdate(
                "UPDATE $HOMEWORK_TABLE AS H " +
                        "SET $HOMEWORK_VOTES = :votes " +
                        "FROM $CLASS_MISC_UNIT_TABLE AS C " +
                        "WHERE H.$HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                        "AND H.$HOMEWORK_ID = :homeworkId"
        )
        override fun updateVotesOnHomework(homeworkId: Int, votes: Int): Int

        override fun deleteSpecificHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int): Int =
                createClassDAO().deleteSpecificClassMiscUnitFromTypeOnCourseInClass(
                        courseClassId,
                        homeworkId,
                        ClassMiscUnitType.HOMEWORK
                )

        @SqlQuery(
                "SELECT H.$HOMEWORK_STAGE_ID, " +
                        "H.$HOMEWORK_STAGE_SHEET_ID, " +
                        "H.$HOMEWORK_STAGE_DUE_DATE, " +
                        "H.$HOMEWORK_STAGE_LATE_DELIVERY, " +
                        "H.$HOMEWORK_STAGE_MULTIPLE_DELIVERIES, " +
                        "H.$HOMEWORK_STAGE_TIMESTAMP, " +
                        "H.$HOMEWORK_STAGE_VOTES, " +
                        "H.$HOMEWORK_STAGE_CREATED_BY " +
                        "H.$HOMEWORK_STAGE_LOG_ID " +
                        "FROM $HOMEWORK_STAGE_TABLE AS H " +
                        "INNER JOIN $CLASS_MISC_UNIT_STAGE_TABLE AS C " +
                        "ON H.$HOMEWORK_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId"
        )
        override fun getAllStagedHomeworksOfCourseInClass(courseClassId: Int): List<HomeworkStage>

        @SqlQuery(
                "SELECT H.$HOMEWORK_STAGE_ID, " +
                        "H.$HOMEWORK_STAGE_SHEET_ID, " +
                        "H.$HOMEWORK_STAGE_DUE_DATE, " +
                        "H.$HOMEWORK_STAGE_LATE_DELIVERY, " +
                        "H.$HOMEWORK_STAGE_MULTIPLE_DELIVERIES, " +
                        "H.$HOMEWORK_STAGE_TIMESTAMP, " +
                        "H.$HOMEWORK_STAGE_VOTES, " +
                        "H.$HOMEWORK_STAGE_CREATED_BY " +
                        "H.$HOMEWORK_STAGE_LOG_ID " +
                        "FROM $HOMEWORK_STAGE_TABLE AS H " +
                        "INNER JOIN $CLASS_MISC_UNIT_STAGE_TABLE AS C " +
                        "ON H.$HOMEWORK_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND H.$HOMEWORK_STAGE_ID = :stageId"
        )
        override fun getSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Optional<HomeworkStage>

        @SqlUpdate(
                "INSERT INTO $HOMEWORK_STAGE_TABLE ( " +
                        "$HOMEWORK_STAGE_ID, " +
                        "$HOMEWORK_STAGE_SHEET_ID, " +
                        "$HOMEWORK_STAGE_DUE_DATE, " +
                        "$HOMEWORK_STAGE_LATE_DELIVERY, " +
                        "$HOMEWORK_STAGE_MULTIPLE_DELIVERIES, " +
                        "$HOMEWORK_STAGE_TIMESTAMP, " +
                        "$HOMEWORK_STAGE_VOTES," +
                        "$HOMEWORK_STAGE_CREATED_BY " +
                        ") " +
                        "VALUES(:stagedClassMiscUnitId, :homeworkStage.sheetId, :homeworkStage.dueDate, " +
                        ":homeworkStage.lateDelivery, :homeworkStage.multipleDeliveries, :homeworkStage.timestamp, " +
                        ":homeworkStage.votes, :homeworkStage.createdBy)"
        )
        @GetGeneratedKeys
        fun createStagingHomework(stagedClassMiscUnitId: Int, homeworkStage: HomeworkStage): HomeworkStage

        @Transaction
        override fun createStagingHomeworkOnCourseInClass(courseClassId: Int, homeworkStage: HomeworkStage): HomeworkStage {
            val classMiscUnitStage = createClassDAO().createStagingClassMiscUnit(courseClassId, ClassMiscUnitType.HOMEWORK)
            return createStagingHomework(classMiscUnitStage.courseClassId, homeworkStage)
        }

        override fun deleteSpecificStagedHomeworkOfCourseInClass(courseClassId: Int, stageId: Int): Int =
                createClassDAO().deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId, stageId, ClassMiscUnitType.HOMEWORK)

        @SqlUpdate(
                "INSERT INTO $HOMEWORK_VERSION_TABLE ( " +
                        "$HOMEWORK_VERSION_HOMEWORK_ID, " +
                        "$HOMEWORK_VERSION_ID, " +
                        "$HOMEWORK_VERSION_CREATED_BY, " +
                        "$HOMEWORK_VERSION_SHEET_ID, " +
                        "$HOMEWORK_VERSION_DUE_DATE, " +
                        "$HOMEWORK_VERSION_LATE_DELIVERY, " +
                        "$HOMEWORK_VERSION_MULTIPLE_DELIVERIES, " +
                        "$HOMEWORK_VERSION_TIMESTAMP " +
                        ") " +
                        "VALUES (:homeworkVersion.homeworkId, :homeworkVersion.version, :homeworkVersion.createdBy, " +
                        ":homeworkVersion.sheetId, :homeworkVersion.dueDate, :homeworkVersion.lateDelivery, " +
                        ":homeworkVersion.multipleDeliveries, :homeworkVersion.timestamp)"
        )
        @GetGeneratedKeys
        override fun createHomeworkVersion(homeworkVersion: HomeworkVersion): HomeworkVersion

        @SqlUpdate(
                "UPDATE $HOMEWORK_STAGE_TABLE as H SET H.$HOMEWORK_STAGE_VOTES = :votes " +
                        "FROM $CLASS_MISC_UNIT_STAGE_TABLE as C" +
                        "WHERE H.$HOMEWORK_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                        "AND H.$HOMEWORK_STAGE_ID = :stageId"
        )
        override fun updateVotesOnStagedHomework(stageId: Int, votes: Int): Int

        @SqlQuery(
                "SELECT H.$HOMEWORK_REPORT_ID, " +
                        "H.$HOMEWORK_REPORT_HOMEWORK_ID, " +
                        "H.$HOMEWORK_REPORT_SHEET_ID, " +
                        "H.$HOMEWORK_REPORT_DUE_DATE, " +
                        "H.$HOMEWORK_REPORT_LATE_DELIVERY, " +
                        "H.$HOMEWORK_REPORT_MULTIPLE_DELIVERIES, " +
                        "H.$HOMEWORK_REPORTED_BY, " +
                        "H.$HOMEWORK_REPORT_VOTES, " +
                        "H.$HOMEWORK_REPORT_TIMESTAMP " +
                        "H.$HOMEWORK_REPORT_LOG_ID " +
                        "FROM $HOMEWORK_REPORT_TABLE AS H " +
                        "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                        "ON H.$HOMEWORK_REPORT_HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND H.$HOMEWORK_REPORT_HOMEWORK_ID = :homeworkId"
        )
        override fun getAllReportsOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int): List<HomeworkReport>

        @SqlQuery(
                "SELECT H.$HOMEWORK_REPORT_ID, " +
                        "H.$HOMEWORK_REPORT_HOMEWORK_ID, " +
                        "H.$HOMEWORK_REPORT_SHEET_ID, " +
                        "H.$HOMEWORK_REPORT_DUE_DATE, " +
                        "H.$HOMEWORK_REPORT_LATE_DELIVERY, " +
                        "H.$HOMEWORK_REPORT_MULTIPLE_DELIVERIES, " +
                        "H.$HOMEWORK_REPORTED_BY, " +
                        "H.$HOMEWORK_REPORT_VOTES, " +
                        "H.$HOMEWORK_REPORT_TIMESTAMP " +
                        "H.$HOMEWORK_REPORT_LOG_ID " +
                        "FROM $HOMEWORK_TABLE AS H " +
                        "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                        "ON H.$HOMEWORK_REPORT_HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND H.$HOMEWORK_REPORT_HOMEWORK_ID = :homeworkId " +
                        "AND H.$HOMEWORK_REPORT_ID = :reportId"
        )
        override fun getSpecificReportOfHomeworkFromCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Optional<HomeworkReport>

        @SqlUpdate(
                "INSERT INTO $HOMEWORK_REPORT_TABLE ( " +
                        "$HOMEWORK_REPORT_HOMEWORK_ID, " +
                        "$HOMEWORK_REPORT_SHEET_ID, " +
                        "$HOMEWORK_REPORT_DUE_DATE, " +
                        "$HOMEWORK_REPORT_LATE_DELIVERY, " +
                        "$HOMEWORK_REPORT_MULTIPLE_DELIVERIES, " +
                        "$HOMEWORK_REPORTED_BY, " +
                        "$HOMEWORK_REPORT_VOTES, " +
                        "$HOMEWORK_REPORT_TIMESTAMP " +
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
                        "$HOMEWORK_SHEET_ID = :homework.sheetId, " +
                        "$HOMEWORK_DUE_DATE = :homework.dueDate, " +
                        "$HOMEWORK_LATE_DELIVERY = :homework.lateDelivery, " +
                        "$HOMEWORK_MULTIPLE_DELIVERIES = :homework.multipleDeliveries, " +
                        "$HOMEWORK_VOTES = :homework.votes " +
                        "$HOMEWORK_TIMESTAMP = :homework.timestamp " +
                        "WHERE $HOMEWORK_ID = :homework.homeworkId"
        )
        @GetGeneratedKeys
        override fun updateHomework(homework: Homework): Homework

        @SqlUpdate(
                "DELETE FROM $HOMEWORK_REPORT_TABLE " +
                        "USING $CLASS_MISC_UNIT_TABLE " +
                        "WHERE $HOMEWORK_REPORT_HOMEWORK_ID = $CLASS_MISC_UNIT_ID " +
                        "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :homeworkId " +
                        "AND $HOMEWORK_REPORT_HOMEWORK_ID = :reportId"
        )
        override fun updateVotesOnReportedHomework(homeworkId: Int, reportId: Int, votes: Int): Int

        @SqlUpdate(
                "DELETE FROM $HOMEWORK_REPORT_TABLE " +
                        "USING $CLASS_MISC_UNIT_TABLE " +
                        "WHERE $HOMEWORK_REPORT_HOMEWORK_ID = $CLASS_MISC_UNIT_ID " +
                        "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND $HOMEWORK_REPORT_HOMEWORK_ID = :homeworkId " +
                        "AND $HOMEWORK_REPORT_ID = :reportId"
        )
        override fun deleteSpecificReportOnHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, reportId: Int): Int

        @SqlQuery(
                "SELECT H.$HOMEWORK_VERSION_ID, " +
                        "H.$HOMEWORK_VERSION_HOMEWORK_ID, " +
                        "H.$HOMEWORK_VERSION_CREATED_BY, " +
                        "H.$HOMEWORK_VERSION_SHEET_ID, " +
                        "H.$HOMEWORK_VERSION_DUE_DATE, " +
                        "H.$HOMEWORK_VERSION_LATE_DELIVERY, " +
                        "H.$HOMEWORK_VERSION_TIMESTAMP, " +
                        "H.$HOMEWORK_VERSION_MULTIPLE_DELIVERIES " +
                        "FROM $HOMEWORK_VERSION_TABLE AS H " +
                        "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                        "ON H.$HOMEWORK_VERSION_ID = C.$CLASS_MISC_UNIT_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND H.$HOMEWORK_VERSION_ID = :homeworkId"
        )
        override fun getAllVersionsOfHomeworkOfCourseInclass(courseClassId: Int, homeworkId: Int): List<HomeworkVersion>

        @SqlQuery(
                "SELECT H.$HOMEWORK_VERSION_ID, " +
                        "H.$HOMEWORK_VERSION_HOMEWORK_ID, " +
                        "H.$HOMEWORK_VERSION_CREATED_BY, " +
                        "H.$HOMEWORK_VERSION_SHEET_ID, " +
                        "H.$HOMEWORK_VERSION_DUE_DATE, " +
                        "H.$HOMEWORK_VERSION_LATE_DELIVERY, " +
                        "H.$HOMEWORK_VERSION_TIMESTAMP, " +
                        "H.$HOMEWORK_VERSION_MULTIPLE_DELIVERIES " +
                        "FROM $HOMEWORK_VERSION_TABLE AS H " +
                        "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                        "ON H.$HOMEWORK_VERSION_HOMEWORK_ID = C.$CLASS_MISC_UNIT_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND H.$HOMEWORK_VERSION_HOMEWORK_ID = :homeworkId " +
                        "AND H.$HOMEWORK_VERSION_ID = :version"
        )
        override fun getSpecificVersionOfHomeworkOfCourseInClass(courseClassId: Int, homeworkId: Int, version: Int): Optional<HomeworkVersion>
    }

}