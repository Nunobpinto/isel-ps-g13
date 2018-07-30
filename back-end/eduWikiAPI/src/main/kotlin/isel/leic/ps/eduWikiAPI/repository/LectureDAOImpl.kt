package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Lecture
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.LectureStage
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.CLASS_MISC_UNIT_TYPE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOImpl.Companion.COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.interfaces.LectureDAO
import org.jdbi.v3.core.Handle
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

interface LectureDAOImpl : LectureDAO {

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
                    "AND C.$CLASS_MISC_UNIT_TYPE = Lecture"
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
                    "AND C.$CLASS_MISC_UNIT_TYPE = Lecture " +
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
                    "VALUES (:lecture.lectureId, :lecture.version, :lecture.createdBy, :lecture.weekDay, " +
                    ":lecture.begins, :lecture.duration, :lecture.location, :lecture.votes, :lecture.timestamp)"
    )
    @GetGeneratedKeys
    override fun createLecture(lecture: Lecture): Optional<Lecture>

    @SqlQuery(
            "SELECT L.$LECTURE_VOTES" +
                    "FROM $LECTURE_TABLE AS L " +
                    "INNER JOIN $CLASS_MISC_UNIT_TABLE AS C " +
                    "ON L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                    "WHERE C.$COURSE_CLASS_ID = :courseClassId " +
                    "AND L.$LECTURE_ID = :lectureId"
    )
    fun getVotesOnLecture(courseClassId: Int, lectureId: Int): Int

    @SqlUpdate(
            "UPDATE $LECTURE_TABLE AS L set L.$LECTURE_VOTES = :votes " +
                    "FROM $CLASS_MISC_UNIT_TABLE AS C " +
                    "WHERE L.$LECTURE_ID = C.$CLASS_MISC_UNIT_ID " +
                    "AND L.$LECTURE_ID = :lectureId"
    )
    fun updateVotesOnLecture(lectureId: Int, votes: Int): Int

    @Transaction
    override fun voteOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int, vote: Vote): Int {
        var votes = getVotesOnLecture(courseClassId, lectureId)
        votes = if(vote == Vote.Down) -- votes else ++ votes
        return updateVotesOnLecture(lectureId, votes)
    }

    @SqlUpdate(
            "DELETE FROM $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $CLASS_MISC_UNIT_TYPE = Lecture"
    )
    override fun deleteAllLecturesOfCourseInClass(courseClassId: Int): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $CLASS_MISC_UNIT_TYPE = Lecture " +
                    "AND $CLASS_MISC_UNIT_ID = :lectureId"
    )
    override fun deleteSpecificLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int

    override fun getAllReportsOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int): List<LectureReport> =
            handle.createQuery(
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
                    .bind("courseClassId", courseClassId)
                    .bind("lectureId", lectureId)
                    .mapTo(LectureReport::class.java)
                    .list()

    override fun getSpecificReportOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Optional<LectureReport> =
            handle.createQuery(
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
                            "INNER JOIN ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} AS C" +
                            "ON L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "WHERE C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "AND L.$LECTURE_ID = :lectureId" +
                            "AND L.$LECTURE_REPORT_ID = :reportId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("lectureId", lectureId)
                    .bind("reportId", reportId)
                    .mapTo(LectureReport::class.java)
                    .findFirst()

    override fun deleteAllReportsOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int =
            handle.createUpdate(
                    "DELETE FROM $LECTURE_REPORT_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "WHERE $LECTURE_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "AND ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "AND ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "AND $LECTURE_ID = :lectureId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .execute()

    override fun deleteSpecificReportOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Int =
            handle.createUpdate(
                    "DELETE FROM $LECTURE_REPORT_TABLE " +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} " +
                            "WHERE $LECTURE_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID} " +
                            "AND ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "AND ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType " +
                            "AND $LECTURE_ID = :lectureId " +
                            "AND $LECTURE_REPORT_ID = :reportId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .bind("reportId", reportId)
                    .execute()

    override fun createReportOnLecture(lectureReport: LectureReport): Optional<LectureReport> =
            handle.createUpdate(
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
                            "VALUES (:lectureId, :weekDay, :begins, :duration, " +
                            ":location, :reportedBy, :votes, :timestamp)"
            )
                    .bind("lectureId", lectureReport.lectureId)
                    .bind("weekDay", lectureReport.weekDay)
                    .bind("begins", lectureReport.begins)
                    .bind("duration", lectureReport.duration)
                    .bind("location", lectureReport.location)
                    .bind("reportedBy", lectureReport.reportedBy)
                    .bind("votes", lectureReport.votes)
                    .bind("timestamp", lectureReport.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(LectureReport::class.java)
                    .findFirst()

    override fun voteOnReportOfLecture(lectureId: Int, reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "SELECT $LECTURE_VOTES FROM $LECTURE_REPORT_TABLE " +
                        "WHERE $LECTURE_ID = :lectureId" +
                        "AND $LECTURE_REPORT_ID = :reportId"
        )
                .bind("lectureId", lectureId)
                .bind("reportId", reportId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if(vote == Vote.Down) -- votes else ++ votes

        return handle.createUpdate(
                "UPDATE $LECTURE_REPORT_TABLE set $LECTURE_VOTES = :votes " +
                        "WHERE $LECTURE_ID = :lectureId" +
                        "AND $LECTURE_REPORT_ID = :reportId"
        )
                .bind("votes", votes)
                .bind("lectureId", lectureId)
                .bind("reportId", reportId)
                .execute()
    }

    override fun updateLecture(lecture: Lecture): Optional<Lecture> =
            handle.createUpdate(
                    "UPDATE $LECTURE_TABLE set " +
                            "$LECTURE_VERSION = :version, " +
                            "$LECTURE_CREATED_BY = :createdBy," +
                            "$LECTURE_WEEK_DAY = :weekDay," +
                            "$LECTURE_BEGINS = :begins," +
                            "$LECTURE_DURATION = :duration, " +
                            "$LECTURE_LOCATION = :location, " +
                            "$LECTURE_VOTES = :votes" +
                            "$LECTURE_TIMESTAMP = :timestamp " +
                            "WHERE $LECTURE_ID = :lectureId"
            )
                    .bind("version", lecture.version)
                    .bind("createdBy", lecture.createdBy)
                    .bind("weekDay", lecture.weekDay)
                    .bind("begins", lecture.begins)
                    .bind("duration", lecture.duration)
                    .bind("location", lecture.location)
                    .bind("votes", lecture.votes)
                    .bind("timestamp", lecture.timestamp)
                    .bind("lectureId", lecture.lectureId)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Lecture::class.java)
                    .findFirst()

    override fun createLectureVersion(lectureVersion: LectureVersion): Optional<LectureVersion> =
            handle.createUpdate(
                    "INSERT INTO $LECTURE_VERSION_TABLE (" +
                            "$LECTURE_ID = :lectureId, " +
                            "$LECTURE_VERSION = :version, " +
                            "$LECTURE_CREATED_BY = :createdBy, " +
                            "$LECTURE_WEEK_DAY = :weekDay, " +
                            "$LECTURE_BEGINS = :begins, " +
                            "$LECTURE_DURATION = :duration," +
                            "$LECTURE_TIMESTAMP = :timestamp " +
                            ")" +
                            "VALUES (:lectureId, :version, :createdBy, :weekDay, " +
                            ":begins, :duration, :timestamp)"
            )
                    .bind("lectureId", lectureVersion.lectureId)
                    .bind("version", lectureVersion.version)
                    .bind("createdBy", lectureVersion.createdBy)
                    .bind("weekDay", lectureVersion.weekDay)
                    .bind("begins", lectureVersion.begins)
                    .bind("duration", lectureVersion.duration)
                    .bind("timestamp", lectureVersion.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(LectureVersion::class.java)
                    .findFirst()

    override fun getAllStagedLecturesOfCourseInClass(courseClassId: Int): List<LectureStage> =
            handle.createQuery(
                    "SELECT L.$LECTURE_STAGE_ID," +
                            "L.$LECTURE_WEEK_DAY, " +
                            "L.$LECTURE_BEGINS, " +
                            "L.$LECTURE_DURATION, " +
                            "L.$LECTURE_CREATED_BY, " +
                            "L.$LECTURE_TIMESTAMP, " +
                            "L:$LECTURE_VOTES " +
                            "FROM $LECTURE_STAGE_TABLE AS L " +
                            "INNER JOIN ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} AS C " +
                            "ON L.$LECTURE_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID}" +
                            "WHERE C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "AND C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .mapTo(LectureStage::class.java)
                    .list()

    override fun getSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Optional<LectureStage> =
            handle.createQuery(
                    "SELECT L.$LECTURE_STAGE_ID," +
                            "L.$LECTURE_WEEK_DAY, " +
                            "L.$LECTURE_BEGINS, " +
                            "L.$LECTURE_DURATION, " +
                            "L.$LECTURE_CREATED_BY, " +
                            "L.$LECTURE_TIMESTAMP, " +
                            "L:$LECTURE_VOTES " +
                            "FROM $LECTURE_STAGE_TABLE AS L " +
                            "INNER JOIN ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} AS C " +
                            "ON L.$LECTURE_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID}" +
                            "WHERE C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "AND C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "AND L.$LECTURE_STAGE_ID = :stageId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("stageId", stageId)
                    .mapTo(LectureStage::class.java)
                    .findFirst()

    override fun createStagingLecture(lectureStage: LectureStage): Optional<LectureStage> =
            handle.createUpdate(
                    "INSERT INTO $LECTURE_STAGE_TABLE (" +
                            "$LECTURE_STAGE_ID, " +
                            "$LECTURE_WEEK_DAY, " +
                            "$LECTURE_BEGINS, " +
                            "$LECTURE_DURATION, " +
                            "$LECTURE_CREATED_BY, " +
                            "$LECTURE_TIMESTAMP, " +
                            "$LECTURE_LOCATION " +
                            ")" +
                            "VALUES(:stageId, :weekDay, :begins, :duration," +
                            ":createdBy, :timestamp, :location)"
            )
                    .bind("stageId", lectureStage.stageId)
                    .bind("weekDay", lectureStage.weekDay)
                    .bind("begins", lectureStage.begins)
                    .bind("duration", lectureStage.duration)
                    .bind("createdBy", lectureStage.createdBy)
                    .bind("timestamp", lectureStage.timestamp)
                    .bind("location", lectureStage.location)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(LectureStage::class.java)
                    .findFirst()

    override fun voteOnStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "SELECT L.$LECTURE_VOTES" +
                        "FROM $LECTURE_STAGE_TABLE AS L" +
                        "INNER JOIN ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} AS C" +
                        "ON L.$LECTURE_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID}" +
                        "WHERE C.${ClassDAOImpl.COURSE_CLASS_ID} = :courseClassId" +
                        "AND C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                        "AND L.$LECTURE_STAGE_ID = :stageId"
        )
                .bind("courseClassId", courseClassId)
                .bind("miscType", "Lecture")
                .bind("stageId", stageId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if(vote == Vote.Down) -- votes else ++ votes

        return handle.createUpdate(
                "UPDATE $LECTURE_STAGE_TABLE AS L set L.$LECTURE_VOTES = :votes " +
                        "FROM ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} AS C" +
                        "WHERE L.$LECTURE_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID} " +
                        "AND L.$LECTURE_STAGE_ID = :stageId"
        )
                .bind("votes", votes)
                .bind("lectureId", stageId)
                .execute()
    }

    override fun getAllVersionsOfLectureOfCourseInclass(courseClassId: Int, lectureId: Int): List<LectureVersion> =
            handle.createQuery(
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
                            "INNER JOIN ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} AS C" +
                            "ON L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "WHERE C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "AND C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "AND L.$LECTURE_ID = :lectureId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .mapTo(LectureVersion::class.java)
                    .list()

    override fun getSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Optional<LectureVersion> =
            handle.createQuery(
                    "SELECT L.$LECTURE_ID," +
                            "L.$LECTURE_VERSION, " +
                            "L.$LECTURE_CREATED_BY, " +
                            "L.$LECTURE_WEEK_DAY, " +
                            "L.$LECTURE_BEGINS" +
                            "L.$LECTURE_DURATION, " +
                            "L.$LECTURE_LOCATION, " +
                            "L:$LECTURE_VOTES, " +
                            "L.$LECTURE_TIMESTAMP, " +
                            "FROM $LECTURE_VERSION_TABLE AS L " +
                            "INNER JOIN ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} AS C" +
                            "ON L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "WHERE C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "AND C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "AND L.$LECTURE_ID = :lectureId" +
                            "AND L.$LECTURE_VERSION = :version"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .bind("version", version)
                    .mapTo(LectureVersion::class.java)
                    .findFirst()

    override fun deleteAllVersionsOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int =
            handle.createUpdate(
                    "DELETE FROM $LECTURE_VERSION_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "WHERE $LECTURE_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "AND ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "AND ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "AND $LECTURE_ID = :lectureId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .execute()

    override fun deleteSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Int =
            handle.createUpdate(
                    "DELETE FROM $LECTURE_VERSION_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "WHERE $LECTURE_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "AND ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "AND ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "AND $LECTURE_ID = :lectureId" +
                            "AND $LECTURE_VERSION = :version"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .bind("version", version)
                    .execute()


}