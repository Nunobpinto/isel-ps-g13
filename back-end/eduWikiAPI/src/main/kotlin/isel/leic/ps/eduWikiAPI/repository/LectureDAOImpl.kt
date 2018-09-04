package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.enums.ClassMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.Lecture
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.LectureStage
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_STAGE_COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_TYPE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_STAGE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_STAGE_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.LectureDAO
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
class LectureDAOImpl : LectureDAO {

    companion object {
        //TABLE NAMES
        const val LECTURE_TABLE = "lecture"
        const val LECTURE_REPORT_TABLE = "lecture_report"
        const val LECTURE_STAGE_TABLE = "lecture_stage"
        const val LECTURE_VERSION_TABLE = "lecture_version"
        // LECTURE FIELDS
        const val LECTURE_ID = "lecture_id"
        const val LECTURE_VERSION = "lecture_version"
        const val LECTURE_LOG_ID = "log_id"
        const val LECTURE_WEEK_DAY = "weekday"
        const val LECTURE_BEGINS = "begins"
        const val LECTURE_DURATION = "duration"
        const val LECTURE_LOCATION = "location"
        const val LECTURE_VOTES = "votes"
        const val LECTURE_TIMESTAMP = "time_stamp"
        const val LECTURE_CREATED_BY = "created_by"
        // LECTURE REPORT FIELDS
        const val LECTURE_REPORT_REPORT_ID = "lecture_report_id"
        const val LECTURE_REPORT_LECTURE_ID = "lecture_id"
        const val LECTURE_REPORT_LOG_ID = "log_id"
        const val LECTURE_REPORTED_BY = "reported_by"
        const val LECTURE_REPORT_WEEK_DAY = "weekday"
        const val LECTURE_REPORT_BEGINS = "begins"
        const val LECTURE_REPORT_DURATION = "duration"
        const val LECTURE_REPORT_LOCATION = "location"
        const val LECTURE_REPORT_VOTES = "votes"
        const val LECTURE_REPORT_TIMESTAMP = "time_stamp"
        // LECTURE STAGE FIELDS
        const val LECTURE_STAGE_ID = "lecture_stage_id"
        const val LECTURE_STAGE_LOG_ID = "log_id"
        const val LECTURE_STAGE_WEEK_DAY = "weekday"
        const val LECTURE_STAGE_BEGINS = "begins"
        const val LECTURE_STAGE_DURATION = "duration"
        const val LECTURE_STAGE_LOCATION = "location"
        const val LECTURE_STAGE_VOTES = "votes"
        const val LECTURE_STAGE_TIMESTAMP = "time_stamp"
        const val LECTURE_STAGE_CREATED_BY = "created_by"
        // LECTURE VERSION FIELDS
        const val LECTURE_VERSION_ID = "lecture_version"
        const val LECTURE_VERSION_LECTURE_ID = "lecture_id"
        const val LECTURE_VERSION_WEEK_DAY = "weekday"
        const val LECTURE_VERSION_BEGINS = "begins"
        const val LECTURE_VERSION_DURATION = "duration"
        const val LECTURE_VERSION_LOCATION = "location"
        const val LECTURE_VERSION_TIMESTAMP = "time_stamp"
        const val LECTURE_VERSION_CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAllLecturesFromCourseInClass(courseClassId: Int): List<Lecture> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getAllLecturesFromCourseInClass(courseClassId)

    override fun getSpecificLectureFromCourseInClass(courseClassId: Int, lectureId: Int): Optional<Lecture> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getSpecificLectureFromCourseInClass(courseClassId, lectureId)

    override fun createLectureOnCourseInClass(courseClassId: Int, lecture: Lecture): Lecture =
            jdbi.open().attach(LectureDAOJdbi::class.java).createLectureOnCourseInClass(courseClassId, lecture)

    override fun updateLecture(lecture: Lecture): Lecture =
            jdbi.open().attach(LectureDAOJdbi::class.java).updateLecture(lecture)

    override fun updateVotesOnLecture(lectureId: Int, votes: Int): Int =
            jdbi.open().attach(LectureDAOJdbi::class.java).updateVotesOnLecture(lectureId, votes)

    override fun deleteSpecificLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int =
            jdbi.open().attach(LectureDAOJdbi::class.java).deleteSpecificLectureOfCourseInClass(courseClassId, lectureId)

    override fun getAllStagedLecturesOfCourseInClass(courseClassId: Int): List<LectureStage> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getAllStagedLecturesOfCourseInClass(courseClassId)

    override fun getSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Optional<LectureStage> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getSpecificStagedLectureOfCourseInClass(courseClassId, stageId)

    override fun createStagingLectureOnCourseInClass(courseClassId: Int, lectureStage: LectureStage): LectureStage =
            jdbi.open().attach(LectureDAOJdbi::class.java).createStagingLectureOnCourseInClass(courseClassId, lectureStage)

    override fun updateVotesOnStagedLecture(stageId: Int, votes: Int): Int =
            jdbi.open().attach(LectureDAOJdbi::class.java).updateVotesOnStagedLecture(stageId, votes)

    override fun deleteSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Int =
            jdbi.open().attach(LectureDAOJdbi::class.java).deleteSpecificStagedLectureOfCourseInClass(courseClassId, stageId)

    override fun getAllReportsOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int): List<LectureReport> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getAllReportsOfLectureFromCourseInClass(courseClassId, lectureId)

    override fun getSpecificReportOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Optional<LectureReport> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getSpecificReportOfLectureFromCourseInClass(courseClassId, lectureId, reportId)

    override fun createReportOnLecture(lectureReport: LectureReport): LectureReport =
            jdbi.open().attach(LectureDAOJdbi::class.java).createReportOnLecture(lectureReport)

    override fun updateVotesOnReportedLecture(lectureId: Int, reportId: Int, votes: Int): Int =
            jdbi.open().attach(LectureDAOJdbi::class.java).updateVotesOnReportedLecture(lectureId, reportId, votes)

    override fun deleteSpecificReportOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Int =
            jdbi.open().attach(LectureDAOJdbi::class.java).deleteSpecificReportOnLectureOfCourseInClass(courseClassId, lectureId, reportId)

    override fun getAllVersionsOfLectureOfCourseInclass(courseClassId: Int, lectureId: Int): List<LectureVersion> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getAllVersionsOfLectureOfCourseInclass(courseClassId, lectureId)

    override fun getSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Optional<LectureVersion> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getSpecificVersionOfLectureOfCourseInClass(courseClassId, lectureId, version)

    override fun createLectureVersion(lectureVersion: LectureVersion): LectureVersion =
            jdbi.open().attach(LectureDAOJdbi::class.java).createLectureVersion(lectureVersion)

    override fun getLectureByLogId(logId: Int): Optional<Lecture> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getLectureByLogId(logId)

    override fun getLectureReportByLogId(logId: Int): Optional<LectureReport> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getLectureReportByLogId(logId)

    override fun getLectureStageByLogId(logId: Int): Optional<LectureStage> =
            jdbi.open().attach(LectureDAOJdbi::class.java).getLectureStageByLogId(logId)

    interface LectureDAOJdbi : LectureDAO {
        @CreateSqlObject
        fun createClassDAO(): ClassDAOImpl.ClassDAOJdbi

        @SqlQuery(
                "SELECT L.$LECTURE_ID, " +
                        "L.$LECTURE_VERSION, " +
                        "L.$LECTURE_CREATED_BY, " +
                        "L.$LECTURE_WEEK_DAY, " +
                        "L.$LECTURE_BEGINS, " +
                        "L.$LECTURE_DURATION, " +
                        "L.$LECTURE_LOCATION, " +
                        "L.$LECTURE_VOTES, " +
                        "L.$LECTURE_TIMESTAMP, " +
                        "L.$LECTURE_LOG_ID, " +
                        "CC.$COURSE_CLASS_CLASS_ID, " +
                        "CC.$COURSE_CLASS_COURSE_ID " +
                        "FROM :schema.$LECTURE_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_TABLE AS C " +
                        "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                        "INNER JOIN :schema.$COURSE_CLASS_TABLE as CC " +
                        "ON C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = CC.$COURSE_CLASS_ID " +
                        "WHERE C.$COURSE_CLASS_ID = :courseClassId " +
                        "AND C.$CLASS_MISC_UNIT_TYPE = 'LECTURE'"
        )
        override fun getAllLecturesFromCourseInClass(courseClassId: Int): List<Lecture>

        @SqlQuery(
                "SELECT L.$LECTURE_ID, " +
                        "L.$LECTURE_VERSION, " +
                        "L.$LECTURE_CREATED_BY, " +
                        "L.$LECTURE_WEEK_DAY, " +
                        "L.$LECTURE_BEGINS, " +
                        "L.$LECTURE_DURATION, " +
                        "L.$LECTURE_LOCATION, " +
                        "L.$LECTURE_VOTES, " +
                        "L.$LECTURE_TIMESTAMP, " +
                        "L.$LECTURE_LOG_ID, " +
                        "CC.$COURSE_CLASS_CLASS_ID, " +
                        "CC.$COURSE_CLASS_COURSE_ID " +
                        "FROM :schema.$LECTURE_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_TABLE AS C " +
                        "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                        "INNER JOIN :schema.$COURSE_CLASS_TABLE as CC " +
                        "ON C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = CC.$COURSE_CLASS_ID " +
                        "WHERE C.$COURSE_CLASS_ID = :courseClassId " +
                        "AND L.$LECTURE_ID = :lectureId"
        )
        override fun getSpecificLectureFromCourseInClass(courseClassId: Int, lectureId: Int): Optional<Lecture>

        @SqlUpdate(
                "INSERT INTO :schema.$LECTURE_TABLE (" +
                        "$LECTURE_ID, " +
                        "$LECTURE_VERSION, " +
                        "$LECTURE_CREATED_BY, " +
                        "$LECTURE_WEEK_DAY, " +
                        "$LECTURE_BEGINS, " +
                        "$LECTURE_DURATION, " +
                        "$LECTURE_LOCATION, " +
                        "$LECTURE_VOTES, " +
                        "$LECTURE_TIMESTAMP) " +
                        "VALUES (:classMiscUnitId, :lecture.version, :lecture.createdBy, :lecture.weekDay, " +
                        ":lecture.begins, :lecture.duration, :lecture.location, :lecture.votes, :lecture.timestamp)"
        )
        @GetGeneratedKeys
        fun createLecture(classMiscUnitId: Int, lecture: Lecture): Lecture

        @Transaction
        override fun createLectureOnCourseInClass(courseClassId: Int, lecture: Lecture): Lecture {
            val classMiscUnit = createClassDAO().createClassMiscUnit(courseClassId, ClassMiscUnitType.LECTURE)
            return createLecture(classMiscUnit.classMiscUnitId, lecture)
        }

        @SqlUpdate(
                "UPDATE :schema.$LECTURE_TABLE as L " +
                        "SET $LECTURE_VOTES = :votes " +
                        "FROM :schema.$CLASS_MISC_UNIT_TABLE AS C " +
                        "WHERE L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                        "AND L.$LECTURE_ID = :lectureId"
        )
        override fun updateVotesOnLecture(lectureId: Int, votes: Int): Int

        override fun deleteSpecificLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int =
                createClassDAO().deleteSpecificClassMiscUnitFromTypeOnCourseInClass(courseClassId, lectureId, ClassMiscUnitType.LECTURE)

        @SqlQuery(
                "SELECT L.$LECTURE_REPORT_REPORT_ID, " +
                        "L.$LECTURE_REPORT_LECTURE_ID, " +
                        "L.$LECTURE_REPORT_WEEK_DAY, " +
                        "L.$LECTURE_REPORT_BEGINS, " +
                        "L.$LECTURE_REPORT_DURATION, " +
                        "L.$LECTURE_REPORT_LOCATION, " +
                        "L.$LECTURE_REPORTED_BY, " +
                        "L.$LECTURE_REPORT_VOTES, " +
                        "L.$LECTURE_REPORT_TIMESTAMP, " +
                        "L.$LECTURE_REPORT_LOG_ID, " +
                        "CC.$COURSE_CLASS_CLASS_ID, " +
                        "CC.$COURSE_CLASS_COURSE_ID " +
                        "FROM :schema.$LECTURE_REPORT_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_TABLE AS C " +
                        "ON L.$LECTURE_REPORT_LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                        "INNER JOIN :schema.$COURSE_CLASS_TABLE as CC " +
                        "ON C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = CC.$COURSE_CLASS_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND L.$LECTURE_ID = :lectureId"
        )
        override fun getAllReportsOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int): List<LectureReport>

        @SqlQuery(
                "SELECT L.$LECTURE_REPORT_REPORT_ID, " +
                        "L.$LECTURE_REPORT_LECTURE_ID, " +
                        "L.$LECTURE_REPORT_WEEK_DAY, " +
                        "L.$LECTURE_REPORT_BEGINS, " +
                        "L.$LECTURE_REPORT_DURATION, " +
                        "L.$LECTURE_REPORT_LOCATION, " +
                        "L.$LECTURE_REPORTED_BY, " +
                        "L.$LECTURE_REPORT_VOTES, " +
                        "L.$LECTURE_REPORT_TIMESTAMP, " +
                        "L.$LECTURE_REPORT_LOG_ID, " +
                        "CC.$COURSE_CLASS_CLASS_ID, " +
                        "CC.$COURSE_CLASS_COURSE_ID " +
                        "FROM :schema.$LECTURE_REPORT_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_TABLE AS C " +
                        "ON L.$LECTURE_REPORT_LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                        "INNER JOIN :schema.$COURSE_CLASS_TABLE as CC " +
                        "ON C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = CC.$COURSE_CLASS_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND L.$LECTURE_REPORT_LECTURE_ID = :lectureId " +
                        "AND L.$LECTURE_REPORT_REPORT_ID = :reportId"
        )
        override fun getSpecificReportOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Optional<LectureReport>

        @SqlUpdate(
                "DELETE FROM :schema.$LECTURE_REPORT_TABLE " +
                        "USING :schema.$CLASS_MISC_UNIT_TABLE " +
                        "WHERE $LECTURE_ID = $CLASS_MISC_UNIT_ID " +
                        "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND $LECTURE_ID = :lectureId " +
                        "AND $LECTURE_REPORT_REPORT_ID = :reportId"
        )
        override fun deleteSpecificReportOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Int

        @SqlUpdate(
                "INSERT INTO :schema.$LECTURE_REPORT_TABLE (" +
                        "$LECTURE_REPORT_LECTURE_ID, " +
                        "$LECTURE_REPORT_WEEK_DAY, " +
                        "$LECTURE_REPORT_BEGINS, " +
                        "$LECTURE_REPORT_DURATION, " +
                        "$LECTURE_REPORT_LOCATION, " +
                        "$LECTURE_REPORTED_BY, " +
                        "$LECTURE_REPORT_VOTES, " +
                        "$LECTURE_REPORT_TIMESTAMP " +
                        ") " +
                        "VALUES (:lectureReport.lectureId, :lectureReport.weekDay, :lectureReport.begins, :lectureReport.duration, " +
                        ":lectureReport.location, :lectureReport.reportedBy, :lectureReport.votes, :lectureReport.timestamp)"
        )
        @GetGeneratedKeys
        override fun createReportOnLecture(lectureReport: LectureReport): LectureReport

        @SqlUpdate(
                "UPDATE :schema.$LECTURE_REPORT_TABLE set $LECTURE_VOTES = :votes " +
                        "WHERE $LECTURE_ID = :lectureId " +
                        "AND $LECTURE_REPORT_REPORT_ID = :reportId"
        )
        override fun updateVotesOnReportedLecture(lectureId: Int, reportId: Int, votes: Int): Int

        @SqlUpdate(
                "UPDATE :schema.$LECTURE_TABLE set " +
                        "$LECTURE_VERSION = :lecture.version, " +
                        "$LECTURE_CREATED_BY = :lecture.createdBy, " +
                        "$LECTURE_WEEK_DAY = :lecture.weekDay, " +
                        "$LECTURE_BEGINS = :lecture.begins, " +
                        "$LECTURE_DURATION = :lecture.duration, " +
                        "$LECTURE_LOCATION = :lecture.location, " +
                        "$LECTURE_VOTES = :lecture.votes, " +
                        "$LECTURE_TIMESTAMP = :lecture.timestamp " +
                        "WHERE $LECTURE_ID = :lecture.lectureId"
        )
        @GetGeneratedKeys
        override fun updateLecture(lecture: Lecture): Lecture

        @SqlUpdate(
                "INSERT INTO :schema.$LECTURE_VERSION_TABLE (" +
                        "$LECTURE_ID, " +
                        "$LECTURE_VERSION, " +
                        "$LECTURE_CREATED_BY, " +
                        "$LECTURE_WEEK_DAY, " +
                        "$LECTURE_BEGINS, " +
                        "$LECTURE_LOCATION, " +
                        "$LECTURE_DURATION, " +
                        "$LECTURE_TIMESTAMP " +
                        ")" +
                        "VALUES (:lectureVersion.lectureId, :lectureVersion.version, :lectureVersion.createdBy, :lectureVersion.weekDay, " +
                        ":lectureVersion.begins, :lectureVersion.location, :lectureVersion.duration, :lectureVersion.timestamp)"
        )
        @GetGeneratedKeys
        override fun createLectureVersion(lectureVersion: LectureVersion): LectureVersion

        @SqlQuery(
                "SELECT L.$LECTURE_STAGE_ID, " +
                        "L.$LECTURE_STAGE_WEEK_DAY, " +
                        "L.$LECTURE_STAGE_BEGINS, " +
                        "L.$LECTURE_STAGE_DURATION, " +
                        "L.$LECTURE_STAGE_CREATED_BY, " +
                        "L.$LECTURE_STAGE_TIMESTAMP, " +
                        "L.$LECTURE_STAGE_VOTES, " +
                        "L.$LECTURE_STAGE_LOG_ID, " +
                        "CC.$COURSE_CLASS_CLASS_ID, " +
                        "CC.$COURSE_CLASS_COURSE_ID " +
                        "FROM :schema.$LECTURE_STAGE_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_STAGE_TABLE AS C " +
                        "ON L.$LECTURE_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                        "INNER JOIN :schema.$COURSE_CLASS_TABLE as CC " +
                        "ON C.$CLASS_MISC_UNIT_STAGE_COURSE_CLASS_ID = CC.$COURSE_CLASS_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_STAGE_COURSE_CLASS_ID = :courseClassId"
        )
        override fun getAllStagedLecturesOfCourseInClass(courseClassId: Int): List<LectureStage>

        @SqlQuery(
                "SELECT L.$LECTURE_STAGE_ID, " +
                        "L.$LECTURE_STAGE_WEEK_DAY, " +
                        "L.$LECTURE_STAGE_BEGINS, " +
                        "L.$LECTURE_STAGE_LOCATION, " +
                        "L.$LECTURE_STAGE_DURATION, " +
                        "L.$LECTURE_STAGE_CREATED_BY, " +
                        "L.$LECTURE_STAGE_TIMESTAMP, " +
                        "L.$LECTURE_STAGE_VOTES, " +
                        "L.$LECTURE_STAGE_LOG_ID, " +
                        "CC.$COURSE_CLASS_CLASS_ID, " +
                        "CC.$COURSE_CLASS_COURSE_ID " +
                        "FROM :schema.$LECTURE_STAGE_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_STAGE_TABLE AS C " +
                        "ON L.$LECTURE_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                        "INNER JOIN :schema.$COURSE_CLASS_TABLE as CC " +
                        "ON C.$CLASS_MISC_UNIT_STAGE_COURSE_CLASS_ID = CC.$COURSE_CLASS_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_STAGE_COURSE_CLASS_ID = :courseClassId " +
                        "AND L.$LECTURE_STAGE_ID = :stageId"
        )
        override fun getSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Optional<LectureStage>

        @SqlUpdate(
                "INSERT INTO :schema.$LECTURE_STAGE_TABLE (" +
                        "$LECTURE_STAGE_ID, " +
                        "$LECTURE_STAGE_WEEK_DAY, " +
                        "$LECTURE_STAGE_BEGINS, " +
                        "$LECTURE_STAGE_DURATION, " +
                        "$LECTURE_STAGE_CREATED_BY, " +
                        "$LECTURE_STAGE_TIMESTAMP, " +
                        "$LECTURE_STAGE_LOCATION, " +
                        "$LECTURE_STAGE_VOTES " +
                        ")" +
                        "VALUES(:stagedClassMiscUnitId, :lectureStage.weekDay, :lectureStage.begins, :lectureStage.duration," +
                        ":lectureStage.createdBy, :lectureStage.timestamp, :lectureStage.location, :lectureStage.votes)"
        )
        @GetGeneratedKeys
        fun createStagingLecture(stagedClassMiscUnitId: Int, lectureStage: LectureStage): LectureStage

        @Transaction
        override fun createStagingLectureOnCourseInClass(courseClassId: Int, lectureStage: LectureStage): LectureStage {
            val classMiscUnitStage = createClassDAO().createStagingClassMiscUnit(courseClassId, ClassMiscUnitType.LECTURE)
            return createStagingLecture(classMiscUnitStage.stageId, lectureStage)
        }

        @SqlQuery(
                "SELECT L.$LECTURE_VERSION_ID, " +
                        "L.$LECTURE_VERSION_LECTURE_ID, " +
                        "L.$LECTURE_VERSION_CREATED_BY, " +
                        "L.$LECTURE_VERSION_WEEK_DAY, " +
                        "L.$LECTURE_VERSION_BEGINS, " +
                        "L.$LECTURE_VERSION_DURATION, " +
                        "L.$LECTURE_VERSION_LOCATION, " +
                        "L.$LECTURE_VERSION_TIMESTAMP " +
                        "FROM :schema.$LECTURE_VERSION_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_TABLE AS C " +
                        "ON L.$LECTURE_VERSION_LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND L.$LECTURE_VERSION_LECTURE_ID = :lectureId"
        )
        override fun getAllVersionsOfLectureOfCourseInclass(courseClassId: Int, lectureId: Int): List<LectureVersion>

        @SqlQuery(
                "SELECT L.$LECTURE_VERSION_ID," +
                        "L.$LECTURE_VERSION_LECTURE_ID, " +
                        "L.$LECTURE_VERSION_CREATED_BY, " +
                        "L.$LECTURE_VERSION_WEEK_DAY, " +
                        "L.$LECTURE_VERSION_BEGINS, " +
                        "L.$LECTURE_VERSION_DURATION, " +
                        "L.$LECTURE_VERSION_LOCATION, " +
                        "L.$LECTURE_VERSION_TIMESTAMP " +
                        "FROM :schema.$LECTURE_VERSION_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_TABLE AS C " +
                        "ON L.$LECTURE_VERSION_LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                        "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                        "AND L.$LECTURE_VERSION_LECTURE_ID = :lectureId " +
                        "AND L.$LECTURE_VERSION_ID = :version"
        )
        override fun getSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Optional<LectureVersion>

        override fun deleteSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Int =
                createClassDAO().deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId, stageId, ClassMiscUnitType.LECTURE)

        @SqlUpdate(
                "UPDATE :schema.$LECTURE_STAGE_TABLE AS L SET $LECTURE_STAGE_VOTES = :votes " +
                        "FROM :schema.$CLASS_MISC_UNIT_STAGE_TABLE AS C " +
                        "WHERE L.$LECTURE_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                        "AND L.$LECTURE_STAGE_ID = :stageId"
        )
        override fun updateVotesOnStagedLecture(stageId: Int, votes: Int): Int

        @SqlQuery(
                "SELECT L.$LECTURE_ID, " +
                        "L.$LECTURE_VERSION, " +
                        "L.$LECTURE_CREATED_BY, " +
                        "L.$LECTURE_WEEK_DAY, " +
                        "L.$LECTURE_BEGINS, " +
                        "L.$LECTURE_DURATION, " +
                        "L.$LECTURE_LOCATION, " +
                        "L.$LECTURE_VOTES, " +
                        "L.$LECTURE_TIMESTAMP, " +
                        "L.$LECTURE_LOG_ID, " +
                        "CC.$COURSE_CLASS_CLASS_ID, " +
                        "CC.$COURSE_CLASS_COURSE_ID " +
                        "FROM :schema.$LECTURE_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_TABLE AS C " +
                        "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                        "INNER JOIN :schema.$COURSE_CLASS_TABLE as CC " +
                        "ON C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = CC.$COURSE_CLASS_ID " +
                        "WHERE L.$LECTURE_LOG_ID = :logId"
        )
        override fun getLectureByLogId(logId: Int): Optional<Lecture>

        @SqlQuery(
                "SELECT L.$LECTURE_REPORT_REPORT_ID," +
                        "L.$LECTURE_REPORT_LECTURE_ID," +
                        "L.$LECTURE_REPORT_WEEK_DAY," +
                        "L.$LECTURE_REPORT_BEGINS," +
                        "L.$LECTURE_REPORT_DURATION," +
                        "L.$LECTURE_REPORT_LOCATION," +
                        "L.$LECTURE_REPORTED_BY," +
                        "L.$LECTURE_REPORT_VOTES," +
                        "L.$LECTURE_REPORT_TIMESTAMP, " +
                        "L.$LECTURE_REPORT_LOG_ID, " +
                        "CC.$COURSE_CLASS_CLASS_ID, " +
                        "CC.$COURSE_CLASS_COURSE_ID " +
                        "FROM :schema.$LECTURE_REPORT_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_TABLE AS C " +
                        "ON L.$LECTURE_REPORT_LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                        "INNER JOIN :schema.$COURSE_CLASS_TABLE as CC " +
                        "ON C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = CC.$COURSE_CLASS_ID " +
                        "WHERE L.$LECTURE_REPORT_LOG_ID = :logId"
        )
        override fun getLectureReportByLogId(logId: Int): Optional<LectureReport>

        @SqlQuery(
                "SELECT L.$LECTURE_STAGE_ID, " +
                        "L.$LECTURE_STAGE_WEEK_DAY, " +
                        "L.$LECTURE_STAGE_BEGINS, " +
                        "L.$LECTURE_STAGE_DURATION, " +
                        "L.$LECTURE_STAGE_CREATED_BY, " +
                        "L.$LECTURE_STAGE_TIMESTAMP, " +
                        "L.$LECTURE_STAGE_LOCATION, " +
                        "L.$LECTURE_STAGE_VOTES, " +
                        "L.$LECTURE_STAGE_LOG_ID, " +
                        "CC.$COURSE_CLASS_CLASS_ID, " +
                        "CC.$COURSE_CLASS_COURSE_ID " +
                        "FROM :schema.$LECTURE_STAGE_TABLE AS L " +
                        "INNER JOIN :schema.$CLASS_MISC_UNIT_STAGE_TABLE AS C " +
                        "ON L.$LECTURE_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                        "INNER JOIN :schema.$COURSE_CLASS_TABLE as CC " +
                        "ON C.$CLASS_MISC_UNIT_STAGE_COURSE_CLASS_ID = CC.$COURSE_CLASS_ID " +
                        "WHERE L.$LECTURE_STAGE_LOG_ID = :logId"
        )
        override fun getLectureStageByLogId(logId: Int): Optional<LectureStage>

    }

}