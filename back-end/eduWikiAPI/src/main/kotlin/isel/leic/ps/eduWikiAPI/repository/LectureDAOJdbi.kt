package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.enums.ClassMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.Lecture
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.LectureStage
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_TYPE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.LectureDAO
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import java.util.*

interface LectureDAOJdbi : LectureDAO {

    companion object {
        //TABLE NAMES
        const val LECTURE_TABLE = "lecture"
        const val LECTURE_REPORT_TABLE = "lecture_report"
        const val LECTURE_STAGE_TABLE = "lecture_stage"
        const val LECTURE_VERSION_TABLE = "lecture_version"
        // FIELDS
        const val LECTURE_ID = "lecture_id"
        const val LECTURE_VERSION = "lecture_version"
        const val LECTURE_STAGE_ID = "lecture_stage_id"
        const val LECTURE_REPORTED_BY = "reported_by"
        const val LECTURE_REPORT_ID = "lecture_report_id"
        const val LECTURE_WEEK_DAY = "weekday"
        const val LECTURE_BEGINS = "begins"
        const val LECTURE_DURATION = "duration"
        const val LECTURE_LOCATION = "location"
        const val LECTURE_VOTES = "votes"
        const val LECTURE_TIMESTAMP = "time_stamp"
        const val LECTURE_CREATED_BY = "created_by"
    }

    @CreateSqlObject
    fun createClassDAO(): ClassDAOJdbi

    @SqlQuery(
            "SELECT L.$LECTURE_ID, " +
                    "L.$LECTURE_VERSION, " +
                    "L.$LECTURE_CREATED_BY, " +
                    "L.$LECTURE_WEEK_DAY, " +
                    "L.$LECTURE_BEGINS, " +
                    "L.$LECTURE_DURATION, " +
                    "L.$LECTURE_LOCATION, " +
                    "L.$LECTURE_VOTES, " +
                    "L.$LECTURE_TIMESTAMP " +
                    "FROM $LECTURE_TABLE AS L " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
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
                    "L.$LECTURE_TIMESTAMP " +
                    "FROM $LECTURE_TABLE AS L " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_CLASS_ID = :courseClassId " +
                    "AND L.$LECTURE_ID = :lectureId"
    )
    override fun getSpecificLectureFromCourseInClass(courseClassId: Int, lectureId: Int): Optional<Lecture>

    @SqlUpdate(
            "INSERT INTO $LECTURE_TABLE (" +
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

    @SqlQuery(
            "SELECT L.$LECTURE_VOTES " +
                    "FROM $LECTURE_TABLE AS L " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_CLASS_ID = :courseClassId " +
                    "AND L.$LECTURE_ID = :lectureId"
    )
    override fun getVotesOnLecture(courseClassId: Int, lectureId: Int): Int

    @SqlUpdate(
            "UPDATE $LECTURE_TABLE AS L set L.$LECTURE_VOTES = :votes " +
                    "FROM $CLASS_MISC_UNIT_TABLE AS C " +
                    "WHERE L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                    "AND L.$LECTURE_ID = :lectureId"
    )
    override fun updateVotesOnLecture(lectureId: Int, votes: Int): Int

    override fun deleteAllLecturesOfCourseInClass(courseClassId: Int): Int =
            createClassDAO().deleteAllClassMiscUnitsFromTypeOfCourseInClass(courseClassId, ClassMiscUnitType.LECTURE)

    override fun deleteSpecificLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int =
            createClassDAO().deleteSpecificClassMiscUnitFromTypeOnCourseInClass(courseClassId, lectureId, ClassMiscUnitType.LECTURE)

    @SqlQuery(
            "SELECT L.$LECTURE_REPORT_ID, " +
                    "L.$LECTURE_ID, " +
                    "L.$LECTURE_WEEK_DAY, " +
                    "L.$LECTURE_BEGINS, " +
                    "L.$LECTURE_DURATION, " +
                    "L.$LECTURE_LOCATION, " +
                    "L.$LECTURE_REPORTED_BY, " +
                    "L.$LECTURE_VOTES, " +
                    "L.$LECTURE_TIMESTAMP " +
                    "FROM $LECTURE_REPORT_TABLE AS L " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND L.$LECTURE_ID = :lectureId"
    )
    override fun getAllReportsOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int): List<LectureReport>

    @SqlQuery(
            "SELECT L.$LECTURE_REPORT_ID," +
                    "L.$LECTURE_ID," +
                    "L.$LECTURE_WEEK_DAY," +
                    "L.$LECTURE_BEGINS," +
                    "L.$LECTURE_DURATION," +
                    "L.$LECTURE_LOCATION," +
                    "L.$LECTURE_REPORTED_BY," +
                    "L.$LECTURE_VOTES," +
                    "L.$LECTURE_TIMESTAMP" +
                    "FROM $LECTURE_REPORT_TABLE AS L" +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C" +
                    "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID" +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND L.$LECTURE_ID = :lectureId" +
                    "AND L.$LECTURE_REPORT_ID = :reportId"
    )
    override fun getSpecificReportOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Optional<LectureReport>

    @SqlUpdate(
            "DELETE FROM $LECTURE_REPORT_TABLE" +
                    "USING $CLASS_MISC_UNIT_TABLE" +
                    "WHERE $LECTURE_ID = $CLASS_MISC_UNIT_ID" +
                    "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId" +
                    "AND $LECTURE_ID = :lectureId"
    )
    override fun deleteAllReportsOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int

    @SqlUpdate(
            "DELETE FROM $LECTURE_REPORT_TABLE " +
                    "USING $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $LECTURE_ID = $CLASS_MISC_UNIT_ID " +
                    "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $LECTURE_ID = :lectureId " +
                    "AND $LECTURE_REPORT_ID = :reportId"
    )
    override fun deleteSpecificReportOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Int

    @SqlUpdate(
            "INSERT INTO $LECTURE_REPORT_TABLE (" +
                    "$LECTURE_ID, " +
                    "$LECTURE_WEEK_DAY, " +
                    "$LECTURE_BEGINS, " +
                    "$LECTURE_DURATION, " +
                    "$LECTURE_LOCATION, " +
                    "$LECTURE_REPORTED_BY, " +
                    "$LECTURE_VOTES, " +
                    "$LECTURE_TIMESTAMP " +
                    ") " +
                    "VALUES (:lectureReport.lectureId, :lectureReport.weekDay, :lectureReport.begins, :lectureReport.duration, " +
                    ":lectureReport.location, :lectureReport.reportedBy, :lectureReport.votes, :lectureReport.timestamp)"
    )
    @GetGeneratedKeys
    override fun createReportOnLecture(lectureReport: LectureReport): LectureReport

    @SqlQuery(
            "SELECT $LECTURE_VOTES FROM $LECTURE_REPORT_TABLE " +
                    "WHERE $LECTURE_ID = :lectureId " +
                    "AND $LECTURE_REPORT_ID = :reportId"
    )
    override fun getVotesOnReportedLecture(lectureId: Int, reportId: Int): Int

    @SqlUpdate(
            "UPDATE $LECTURE_REPORT_TABLE set $LECTURE_VOTES = :votes " +
                    "WHERE $LECTURE_ID = :lectureId" +
                    "AND $LECTURE_REPORT_ID = :reportId"
    )
    override fun updateVotesOnReportedLecture(lectureId: Int, reportId: Int, votes: Int): Int

    @SqlUpdate(
            "UPDATE $LECTURE_TABLE set " +
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
            "INSERT INTO $LECTURE_VERSION_TABLE (" +
                    "$LECTURE_ID, " +
                    "$LECTURE_VERSION, " +
                    "$LECTURE_CREATED_BY, " +
                    "$LECTURE_WEEK_DAY, " +
                    "$LECTURE_BEGINS, " +
                    "$LECTURE_DURATION, " +
                    "$LECTURE_TIMESTAMP " +
                    ")" +
                    "VALUES (:lectureVersion.lectureId, :lectureVersion.version, :lectureVersion.createdBy, :lectureVersion.weekDay, " +
                    ":lectureVersion.begins, :lectureVersion.duration, :lectureVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createLectureVersion(lectureVersion: LectureVersion): LectureVersion

    @SqlQuery(
            "SELECT L.$LECTURE_STAGE_ID, " +
                    "L.$LECTURE_WEEK_DAY, " +
                    "L.$LECTURE_BEGINS, " +
                    "L.$LECTURE_DURATION, " +
                    "L.$LECTURE_CREATED_BY, " +
                    "L.$LECTURE_TIMESTAMP, " +
                    "L.$LECTURE_VOTES " +
                    "FROM $LECTURE_STAGE_TABLE AS L " +
                    "INNER JOIN $CLASS_MISC_UNIT_STAGE_TABLE AS C " +
                    "ON L.$LECTURE_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId"
    )
    override fun getAllStagedLecturesOfCourseInClass(courseClassId: Int): List<LectureStage>

    @SqlQuery(
            "SELECT L.$LECTURE_STAGE_ID, " +
                    "L.$LECTURE_WEEK_DAY, " +
                    "L.$LECTURE_BEGINS, " +
                    "L.$LECTURE_DURATION, " +
                    "L.$LECTURE_CREATED_BY, " +
                    "L.$LECTURE_TIMESTAMP, " +
                    "L.$LECTURE_VOTES " +
                    "FROM $LECTURE_STAGE_TABLE AS L " +
                    "INNER JOIN $CLASS_MISC_UNIT_STAGE_TABLE AS C " +
                    "ON L.$LECTURE_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID" +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND L.$LECTURE_STAGE_ID = :stageId"
    )
    override fun getSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Optional<LectureStage>

    @SqlUpdate(
            "INSERT INTO $LECTURE_STAGE_TABLE (" +
                    "$LECTURE_STAGE_ID, " +
                    "$LECTURE_WEEK_DAY, " +
                    "$LECTURE_BEGINS, " +
                    "$LECTURE_DURATION, " +
                    "$LECTURE_CREATED_BY, " +
                    "$LECTURE_TIMESTAMP, " +
                    "$LECTURE_LOCATION " +
                    ")" +
                    "VALUES(:stagedClassMiscUnitId, :lectureStage.weekDay, :lectureStage.begins, :lectureStage.duration," +
                    ":lectureStage.createdBy, :lectureStage.timestamp, :lectureStage.location)"
    )
    @GetGeneratedKeys
    fun createStagingLecture(stagedClassMiscUnitId: Int, lectureStage: LectureStage): LectureStage

    @Transaction
    override fun createStagingLectureOnCourseInClass(courseClassId: Int, lectureStage: LectureStage): LectureStage {
        val classMiscUnitStage = createClassDAO().createStagingClassMiscUnit(courseClassId, ClassMiscUnitType.LECTURE)
        return createStagingLecture(classMiscUnitStage.stageId, lectureStage)
    }

    @SqlQuery(
            "SELECT L.$LECTURE_ID," +
                    "L.$LECTURE_VERSION, " +
                    "L.$LECTURE_CREATED_BY, " +
                    "L.$LECTURE_WEEK_DAY, " +
                    "L.$LECTURE_BEGINS" +
                    "L.$LECTURE_DURATION, " +
                    "L.$LECTURE_LOCATION, " +
                    "L.$LECTURE_VOTES, " +
                    "L.$LECTURE_TIMESTAMP, " +
                    "FROM $LECTURE_VERSION_TABLE AS L " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C" +
                    "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID" +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND L.$LECTURE_ID = :lectureId"
    )
    override fun getAllVersionsOfLectureOfCourseInclass(courseClassId: Int, lectureId: Int): List<LectureVersion>

    @SqlQuery(
            "SELECT L.$LECTURE_ID, " +
                    "L.$LECTURE_VERSION, " +
                    "L.$LECTURE_CREATED_BY, " +
                    "L.$LECTURE_WEEK_DAY, " +
                    "L.$LECTURE_BEGINS, " +
                    "L.$LECTURE_DURATION, " +
                    "L.$LECTURE_LOCATION, " +
                    "L.$LECTURE_VOTES, " +
                    "L.$LECTURE_TIMESTAMP " +
                    "FROM $LECTURE_VERSION_TABLE AS L " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND L.$LECTURE_ID = :lectureId" +
                    "AND L.$LECTURE_VERSION = :version"
    )
    override fun getSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Optional<LectureVersion>

    @SqlUpdate(
            "DELETE FROM $LECTURE_VERSION_TABLE " +
                    "USING $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $LECTURE_ID = $CLASS_MISC_UNIT_ID " +
                    "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $LECTURE_ID = :lectureId"
    )
    override fun deleteAllVersionsOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int

    @SqlUpdate(
            "DELETE FROM $LECTURE_VERSION_TABLE " +
                    "USING $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $LECTURE_ID = $CLASS_MISC_UNIT_ID " +
                    "AND $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $LECTURE_ID = :lectureId " +
                    "AND $LECTURE_VERSION = :version"
    )
    override fun deleteSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Int

    override fun deleteAllStagedLecturesOfCourseInClass(courseClassId: Int): Int =
            createClassDAO().deleteAllStagedClassMiscUnitsFromTypeOfCourseInClass(courseClassId, ClassMiscUnitType.LECTURE)

    override fun deleteSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Int =
            createClassDAO().deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId, stageId, ClassMiscUnitType.LECTURE)

    @SqlQuery(
            "SELECT L.$LECTURE_VOTES" +
                    "FROM $LECTURE_STAGE_TABLE AS L" +
                    "INNER JOIN $CLASS_MISC_UNIT_STAGE_TABLE AS C" +
                    "ON L.$LECTURE_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID" +
                    "WHERE C.$COURSE_CLASS_ID = :courseClassId" +
                    "AND L.$LECTURE_STAGE_ID = :stageId"
    )
    override fun getVotesOnStagedLecture(courseClassId: Int, stageId: Int): Int

    @SqlUpdate(
            "UPDATE $LECTURE_STAGE_TABLE AS L SET L.$LECTURE_VOTES = :votes " +
                    "FROM $CLASS_MISC_UNIT_STAGE_TABLE AS C" +
                    "WHERE L.$LECTURE_STAGE_ID = C.$CLASS_MISC_UNIT_STAGE_ID " +
                    "AND L.$LECTURE_STAGE_ID = :stageId"
    )
    override fun updateVotesOnStagedLecture(stageId: Int, votes: Int): Int

}