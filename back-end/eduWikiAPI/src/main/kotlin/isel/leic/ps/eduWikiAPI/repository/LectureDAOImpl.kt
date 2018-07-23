package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Lecture
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.LectureReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.LectureStage
import isel.leic.ps.eduWikiAPI.domain.model.version.LectureVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.LectureDAO
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    lateinit var handle: Handle

    override fun getAllLecturesFromCourseInClass(courseClassId: Int): List<Lecture> =
            handle.createQuery(
                    "select L.$LECTURE_ID, " +
                            "L.$LECTURE_VERSION, " +
                            "L.$LECTURE_CREATED_BY, " +
                            "L.$LECTURE_WEEK_DAY, " +
                            "L.$LECTURE_BEGINS, " +
                            "L.$LECTURE_DURATION, " +
                            "L.$LECTURE_LOCATION, " +
                            "L.$LECTURE_VOTES, " +
                            "L.$LECTURE_TIMESTAMP " +
                            "from $LECTURE_TABLE as L" +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                            "on L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "where C.${ClassDAOImpl.COURSE_CLASS_ID} = :courseClassId" +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .mapTo(Lecture::class.java)
                    .list()

    override fun getSpecificLectureFromCourseInClass(courseClassId: Int, lectureId: Int): Optional<Lecture> =
            handle.createQuery(
                    "select L.$LECTURE_ID, " +
                            "L.$LECTURE_VERSION, " +
                            "L.$LECTURE_CREATED_BY, " +
                            "L.$LECTURE_WEEK_DAY, " +
                            "L.$LECTURE_BEGINS, " +
                            "L.$LECTURE_DURATION, " +
                            "L.$LECTURE_LOCATION, " +
                            "L.$LECTURE_VOTES, " +
                            "L.$LECTURE_TIMESTAMP " +
                            "from $LECTURE_TABLE as L" +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                            "on L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "where C.${ClassDAOImpl.COURSE_CLASS_ID} = :courseClassId" +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and L.$LECTURE_ID = :lectureId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .mapTo(Lecture::class.java)
                    .findFirst()

    override fun createLecture(lecture: Lecture): Optional<Lecture> =
            handle.createUpdate(
                    "insert into $LECTURE_TABLE (" +
                            "$LECTURE_ID, " +
                            "$LECTURE_VERSION, " +
                            "$LECTURE_CREATED_BY, " +
                            "$LECTURE_WEEK_DAY, " +
                            "$LECTURE_BEGINS, " +
                            "$LECTURE_DURATION, " +
                            "$LECTURE_LOCATION, " +
                            "$LECTURE_VOTES, " +
                            "$LECTURE_TIMESTAMP " +
                            ")" +
                            "values (:lectureId, :version, :createdBy, :weekDay" +
                            ":begins, :duration, :location, :votes, :timestamp)"
            )
                    .bind("lectureId", lecture.lectureId)
                    .bind("version", lecture.version)
                    .bind("createdBy", lecture.createdBy)
                    .bind("weekDay", lecture.weekDay)
                    .bind("begins", lecture.begins)
                    .bind("duration", lecture.duration)
                    .bind("location", lecture.location)
                    .bind("votes", lecture.votes)
                    .bind("timestamp", lecture.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Lecture::class.java)
                    .findFirst()

    override fun voteOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select L.$LECTURE_VOTES" +
                        "from $LECTURE_TABLE as L" +
                        "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                        "on L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                        "where C.${ClassDAOImpl.COURSE_CLASS_ID} = :courseClassId" +
                        "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                        "and L.$LECTURE_ID = :lectureId"
        )
                .bind("courseClassId", courseClassId)
                .bind("miscType", "Lecture")
                .bind("lectureId", lectureId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $LECTURE_TABLE as L set L.$LECTURE_VOTES = :votes " +
                        "from ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                        "where L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID} " +
                        "and L.$LECTURE_ID = :lectureId"
        )
                .bind("votes", votes)
                .bind("lectureId", lectureId)
                .execute()
    }

    override fun deleteAllLecturesOfCourseInClass(courseClassId: Int): Int =
            handle.createUpdate(
                    "delete from ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .execute()

    override fun deleteSpecificLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int =
            handle.createUpdate(
                    "delete from ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_ID} = :lectureId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .execute()

    override fun getAllReportsOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int): List<LectureReport> =
            handle.createQuery(
                    "select L.$LECTURE_REPORT_ID," +
                            "L.$LECTURE_ID," +
                            "L.$LECTURE_WEEK_DAY," +
                            "L.$LECTURE_BEGINS," +
                            "L.$LECTURE_DURATION," +
                            "L.$LECTURE_LOCATION," +
                            "L.$LECTURE_REPORTED_BY," +
                            "L.$LECTURE_VOTES," +
                            "L.$LECTURE_TIMESTAMP" +
                            "from $LECTURE_REPORT_TABLE as L" +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                            "on L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "and L.$LECTURE_ID = :lectureId"
                    )
                    .bind("courseClassId", courseClassId)
                    .bind("lectureId", lectureId)
                    .mapTo(LectureReport::class.java)
                    .list()

    override fun getSpecificReportOfLectureFromCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Optional<LectureReport> =
            handle.createQuery(
                    "select L.$LECTURE_REPORT_ID," +
                            "L.$LECTURE_ID," +
                            "L.$LECTURE_WEEK_DAY," +
                            "L.$LECTURE_BEGINS," +
                            "L.$LECTURE_DURATION," +
                            "L.$LECTURE_LOCATION," +
                            "L.$LECTURE_REPORTED_BY," +
                            "L.$LECTURE_VOTES," +
                            "L.$LECTURE_TIMESTAMP" +
                            "from $LECTURE_REPORT_TABLE as L" +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                            "on L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "and L.$LECTURE_ID = :lectureId" +
                            "and L.$LECTURE_REPORT_ID = :reportId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("lectureId", lectureId)
                    .bind("reportId", reportId)
                    .mapTo(LectureReport::class.java)
                    .findFirst()

    override fun deleteAllReportsOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int): Int =
            handle.createUpdate(
                    "delete from $LECTURE_REPORT_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where $LECTURE_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and $LECTURE_ID = :lectureId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .execute()

    override fun deleteSpecificReportOnLectureOfCourseInClass(courseClassId: Int, lectureId: Int, reportId: Int): Int =
            handle.createUpdate(
                    "delete from $LECTURE_REPORT_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where $LECTURE_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and $LECTURE_ID = :lectureId" +
                            "and $LECTURE_REPORT_ID = :reportId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .bind("reportId", reportId)
                    .execute()

    override fun createReportOnLecture(lectureReport: LectureReport): Optional<LectureReport> =
            handle.createUpdate(
                    "insert into $LECTURE_REPORT_TABLE (" +
                            "$LECTURE_ID, " +
                            "$LECTURE_WEEK_DAY, " +
                            "$LECTURE_BEGINS, " +
                            "$LECTURE_DURATION, " +
                            "$LECTURE_LOCATION, " +
                            "$LECTURE_REPORTED_BY, " +
                            "$LECTURE_VOTES, " +
                            "$LECTURE_TIMESTAMP " +
                            ") " +
                            "values (:lectureId, :weekDay, :begins, :duration, " +
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
                "select $LECTURE_VOTES from $LECTURE_REPORT_TABLE " +
                        "where $LECTURE_ID = :lectureId" +
                        "and $LECTURE_REPORT_ID = :reportId"
        )
                .bind("lectureId", lectureId)
                .bind("reportId", reportId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $LECTURE_REPORT_TABLE set $LECTURE_VOTES = :votes " +
                        "where $LECTURE_ID = :lectureId" +
                        "and $LECTURE_REPORT_ID = :reportId"
        )
                .bind("votes", votes)
                .bind("lectureId", lectureId)
                .bind("reportId", reportId)
                .execute()
    }

    override fun updateLecture(lecture: Lecture): Optional<Lecture> =
            handle.createUpdate(
                    "update $LECTURE_TABLE set " +
                            "$LECTURE_VERSION = :version, " +
                            "$LECTURE_CREATED_BY = :createdBy," +
                            "$LECTURE_WEEK_DAY = :weekDay," +
                            "$LECTURE_BEGINS = :begins," +
                            "$LECTURE_DURATION = :duration, " +
                            "$LECTURE_LOCATION = :location, " +
                            "$LECTURE_VOTES = :votes" +
                            "$LECTURE_TIMESTAMP = :timestamp " +
                            "where $LECTURE_ID = :lectureId"
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
                    "insert into $LECTURE_VERSION_TABLE (" +
                            "$LECTURE_ID = :lectureId, " +
                            "$LECTURE_VERSION = :version, " +
                            "$LECTURE_CREATED_BY = :createdBy, " +
                            "$LECTURE_WEEK_DAY = :weekDay, " +
                            "$LECTURE_BEGINS = :begins, " +
                            "$LECTURE_DURATION = :duration," +
                            "$LECTURE_TIMESTAMP = :timestamp " +
                            ")" +
                            "values (:lectureId, :version, :createdBy, :weekDay, " +
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
                    "select L.$LECTURE_STAGE_ID," +
                            "L.$LECTURE_WEEK_DAY, " +
                            "L.$LECTURE_BEGINS, " +
                            "L.$LECTURE_DURATION, " +
                            "L.$LECTURE_CREATED_BY, " +
                            "L.$LECTURE_TIMESTAMP, " +
                            "L:$LECTURE_VOTES " +
                            "from $LECTURE_STAGE_TABLE as L " +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} as C " +
                            "on L.$LECTURE_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID}" +
                            "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .mapTo(LectureStage::class.java)
                    .list()

    override fun getSpecificStagedLectureOfCourseInClass(courseClassId: Int, stageId: Int): Optional<LectureStage> =
            handle.createQuery(
                    "select L.$LECTURE_STAGE_ID," +
                            "L.$LECTURE_WEEK_DAY, " +
                            "L.$LECTURE_BEGINS, " +
                            "L.$LECTURE_DURATION, " +
                            "L.$LECTURE_CREATED_BY, " +
                            "L.$LECTURE_TIMESTAMP, " +
                            "L:$LECTURE_VOTES " +
                            "from $LECTURE_STAGE_TABLE as L " +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} as C " +
                            "on L.$LECTURE_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID}" +
                            "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and L.$LECTURE_STAGE_ID = :stageId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("stageId", stageId)
                    .mapTo(LectureStage::class.java)
                    .findFirst()

    override fun createStagingLecture(lectureStage: LectureStage): Optional<LectureStage> =
            handle.createUpdate(
                    "insert into $LECTURE_STAGE_TABLE (" +
                            "$LECTURE_STAGE_ID, " +
                            "$LECTURE_WEEK_DAY, " +
                            "$LECTURE_BEGINS, " +
                            "$LECTURE_DURATION, " +
                            "$LECTURE_CREATED_BY, " +
                            "$LECTURE_TIMESTAMP, " +
                            "$LECTURE_LOCATION " +
                            ")" +
                            "values(:stageId, :weekDay, :begins, :duration," +
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
                "select L.$LECTURE_VOTES" +
                        "from $LECTURE_STAGE_TABLE as L" +
                        "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} as C" +
                        "on L.$LECTURE_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID}" +
                        "where C.${ClassDAOImpl.COURSE_CLASS_ID} = :courseClassId" +
                        "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                        "and L.$LECTURE_STAGE_ID = :stageId"
        )
                .bind("courseClassId", courseClassId)
                .bind("miscType", "Lecture")
                .bind("stageId", stageId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $LECTURE_STAGE_TABLE as L set L.$LECTURE_VOTES = :votes " +
                        "from ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE} as C" +
                        "where L.$LECTURE_STAGE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID} " +
                        "and L.$LECTURE_STAGE_ID = :stageId"
        )
                .bind("votes", votes)
                .bind("lectureId", stageId)
                .execute()
    }

    override fun getAllVersionsOfLectureOfCourseInclass(courseClassId: Int, lectureId: Int): List<LectureVersion> =
        handle.createQuery(
                "select L.$LECTURE_ID," +
                        "L.$LECTURE_VERSION, " +
                        "L.$LECTURE_CREATED_BY, " +
                        "L.$LECTURE_WEEK_DAY, " +
                        "L.$LECTURE_BEGINS" +
                        "L.$LECTURE_DURATION, " +
                        "L.$LECTURE_LOCATION, " +
                        "L:$LECTURE_VOTES, " +
                        "L.$LECTURE_TIMESTAMP, " +
                        "from $LECTURE_VERSION_TABLE as L " +
                        "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                        "on L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                        "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                        "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                        "and L.$LECTURE_ID = :lectureId"
        )
                .bind("courseClassId", courseClassId)
                .bind("miscType", "Lecture")
                .bind("lectureId", lectureId)
                .mapTo(LectureVersion::class.java)
                .list()

    override fun getSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Optional<LectureVersion> =
            handle.createQuery(
                    "select L.$LECTURE_ID," +
                            "L.$LECTURE_VERSION, " +
                            "L.$LECTURE_CREATED_BY, " +
                            "L.$LECTURE_WEEK_DAY, " +
                            "L.$LECTURE_BEGINS" +
                            "L.$LECTURE_DURATION, " +
                            "L.$LECTURE_LOCATION, " +
                            "L:$LECTURE_VOTES, " +
                            "L.$LECTURE_TIMESTAMP, " +
                            "from $LECTURE_VERSION_TABLE as L " +
                            "inner join ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE} as C" +
                            "on L.$LECTURE_ID = C.${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "where C.${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId " +
                            "and C.${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and L.$LECTURE_ID = :lectureId" +
                            "and L.$LECTURE_VERSION = :version"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .bind("version", version)
                    .mapTo(LectureVersion::class.java)
                    .findFirst()

    override fun deleteAllVersionsOfLectureOfCourseInTerm(courseClassId: Int, lectureId: Int): Int =
            handle.createUpdate(
                    "delete from $LECTURE_VERSION_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where $LECTURE_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and $LECTURE_ID = :lectureId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .execute()

    override fun deleteSpecificVersionOfLectureOfCourseInClass(courseClassId: Int, lectureId: Int, version: Int): Int =
            handle.createUpdate(
                    "delete from $LECTURE_VERSION_TABLE" +
                            "using ${ClassDAOImpl.CLASS_MISC_UNIT_TABLE}" +
                            "where $LECTURE_ID = ${ClassDAOImpl.CLASS_MISC_UNIT_ID}" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType" +
                            "and $LECTURE_ID = :lectureId" +
                            "and $LECTURE_VERSION = :version"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", "Lecture")
                    .bind("lectureId", lectureId)
                    .bind("version", version)
                    .execute()


}