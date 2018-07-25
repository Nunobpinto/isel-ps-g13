package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseProgrammeVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import org.jdbi.v3.core.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CourseDAOImpl : CourseDAO {

    companion object {
        //TABLE NAMES
        const val COURSE_TABLE = "course"
        const val COURSE_VERSION_TABLE = "course_version"
        const val COURSE_REPORT_TABLE = "course_report"
        const val COURSE_STAGE_TABLE = "course_stage"
        const val COURSE_PROGRAMME_TABLE = "course_programme"
        const val COURSE_PROGRAMME_STAGE_TABLE = "course_programme_stage"
        const val COURSE_PROGRAMME_REPORT_TABLE = "course_programme_report"
        const val COURSE_PROGRAMME_VERSION_TABLE = "course_programme_version"
        const val COURSE_TERM_TABLE = "course_term"
        const val COURSE_MISC_UNIT_TABLE = "course_misc_unit"
        const val COURSE_MISC_UNIT_STAGE_TABLE = "course_misc_unit_stage"

        // COURSE FIELDS
        const val COURSE_MISC_UNIT_ID = "course_misc_unit_id"
        const val COURSE_MISC_UNIT_STAGE_ID = "course_misc_unit_stage_id"
        const val COURSE_MISC_UNIT_COURSE_ID = "course_id"
        const val COURSE_MISC_UNIT_TERM_ID = "term_id"
        const val COURSE_STAGE_ID = "course_stage_id"
        const val COURSE_ID = "course_id"
        const val COURSE_VERSION = "course_version"
        const val COURSE_FULL_NAME = "course_full_name"
        const val COURSE_SHORT_NAME = "course_short_name"
        const val COURSE_VOTES = "votes"
        const val COURSE_TIMESTAMP = "time_stamp"
        const val COURSE_REPORT_ID = "course_report_id"
        const val COURSE_REPORTED_BY = "reported_by"
        const val COURSE_CREATED_BY = "created_by"
        const val COURSE_MISC_UNIT_TYPE = "misc_type"
        const val COURSE_ORGANIZATION_ID = "organization_id"
        // COURSE_PROGRAMME FIELDS
        const val COURSE_PROGRAMME_STAGE_ID = "course_programme_stage_id"
        const val COURSE_PROGRAMME_PROGRAMME_ID = "programme_id"
        const val COURSE_PROGRAMME_COURSE_ID = "course_id"
        const val COURSE_PROGRAMME_VERSION = "course_programme_version"
        const val COURSE_PROGRAMME_LECTURED_TERM = "course_lectured_term"
        const val COURSE_PROGRAMME_OPTIONAL = "course_optional"
        const val COURSE_PROGRAMME_CREDITS = "course_credits"
        const val COURSE_PROGRAMME_VOTES = "votes"
        const val COURSE_PROGRAMME_TIMESTAMP = "time_stamp"
        const val COURSE_PROGRAMME_CREATED_BY = "created_by"
        const val COURSE_PROGRAMME_REPORTED_BY = "reported_by"
        const val COURSE_PROGRAMME_REPORT_ID = "course_programme_report_id"
        // COURSE_TERM
        const val COURSE_TERM_TERM_ID = "term_id"
        const val COURSE_TERM_COURSE_ID = "course_id"
        const val COURSE_TERM_TIMESTAMP = "time_stamp"

    }

    @Autowired
    lateinit var handle: Handle

    override fun getAllCourses(): List<Course> =
            handle.createQuery("select * from $COURSE_TABLE")
                    .mapTo(Course::class.java)
                    .list()

    override fun getSpecificCourse(courseId: Int) =
            handle.createQuery("select * from $COURSE_TABLE where $COURSE_ID = :courseId")
                    .bind("courseId", courseId)
                    .mapTo(Course::class.java)
                    .findFirst()

    override fun deleteSpecificCourse(courseId: Int) =
            handle.createUpdate("delete from $COURSE_TABLE where $COURSE_ID = :courseId")
                    .bind("courseId", courseId)
                    .execute()

    override fun deleteAllCourses() =
            handle.createUpdate("delete from $COURSE_TABLE").execute()

    override fun updateCourse(course: Course) =
            handle.createUpdate(
                    "update $COURSE_TABLE SET " +
                            "$COURSE_ORGANIZATION_ID = :orgId, $COURSE_VERSION = :version, " +
                            "$COURSE_CREATED_BY = :createdBy, $COURSE_FULL_NAME = :fullName, " +
                            "$COURSE_SHORT_NAME = :shortName, $COURSE_VOTES = :votes, " +
                            "$COURSE_TIMESTAMP = :timestamp " +
                            "where $COURSE_ID = :courseId"
            )
                    .bind("orgId", course.organizationId)
                    .bind("version", course.version)
                    .bind("createdBy", course.createdBy)
                    .bind("fullName", course.fullName)
                    .bind("shortName", course.shortName)
                    .bind("votes", course.votes)
                    .bind("timestamp", course.timestamp)
                    .bind("courseId", course.courseId)
                    .execute()

    override fun createCourse(course: Course) =
            handle.createUpdate(
                    "insert into $COURSE_TABLE " +
                            "($COURSE_ORGANIZATION_ID, $COURSE_VERSION, $COURSE_CREATED_BY, " +
                            "$COURSE_FULL_NAME, $COURSE_SHORT_NAME, $COURSE_VOTES, $COURSE_TIMESTAMP) " +
                            "values(:organizationId, :version, :createdBy, :fullName, :shortName, :votes, :timestamp)"
            )
                    .bind("organizationId", course.organizationId)
                    .bind("version", course.version)
                    .bind("createdBy", course.createdBy)
                    .bind("fullName", course.fullName)
                    .bind("shortName", course.shortName)
                    .bind("votes", course.votes)
                    .bind("timestamp", course.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Course::class.java)
                    .findFirst()

    override fun voteOnCourse(courseId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $COURSE_VOTES from $COURSE_TABLE where $COURSE_ID = :courseId")
                .bind("courseId", courseId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate("update $COURSE_TABLE set $COURSE_VOTES = :votes where $COURSE_ID = :courseId")
                .bind("votes", votes)
                .bind("courseId", courseId)
                .execute()
    }

    override fun deleteStagedCourse(courseStageId: Int) =
            handle.createUpdate("delete from $COURSE_STAGE_TABLE where $COURSE_STAGE_ID = :courseId")
                    .bind("courseId", courseStageId)
                    .execute()

    override fun deleteAllStagedCourses() =
            handle.createUpdate("delete from $COURSE_STAGE_TABLE").execute()

    override fun createStagedCourse(courseStage: CourseStage) =
            handle.createUpdate(
                    "insert into $COURSE_STAGE_TABLE " +
                            "($COURSE_ORGANIZATION_ID, $COURSE_FULL_NAME, " +
                            "$COURSE_SHORT_NAME, $COURSE_CREATED_BY, $COURSE_VOTES, $COURSE_TIMESTAMP) " +
                            "values(:organizationId, :fullName, :shortName, :createdBy, :votes, :timestamp)"
            )
                    .bind("organizationId", courseStage.organizationId)
                    .bind("fullName", courseStage.fullName)
                    .bind("shortName", courseStage.shortName)
                    .bind("createdBy", courseStage.createdBy)
                    .bind("votes", courseStage.votes)
                    .bind("timestamp", courseStage.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseStage::class.java)
                    .findFirst()

    override fun voteOnStagedCourse(stageId: Int, inputVote: Vote): Int {
        var votes = handle.createQuery("select $COURSE_VOTES from $COURSE_STAGE_TABLE where $COURSE_STAGE_ID = :courseId")
                .bind("courseId", stageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (inputVote == Vote.Down) --votes else ++votes
        return handle.createUpdate("update $COURSE_STAGE_TABLE set $COURSE_VOTES = :votes where $COURSE_STAGE_ID = :courseId")
                .bind("votes", votes)
                .bind("courseId", stageId)
                .execute()
    }

    override fun deleteVersionOfCourse(courseId: Int, version: Int) =
            handle.createUpdate("delete from $COURSE_VERSION_TABLE where $COURSE_ID = :courseId and $COURSE_VERSION = :version")
                    .bind("courseId", courseId)
                    .bind("version", version)
                    .execute()

    override fun reportCourse(courseId: Int, courseReport: CourseReport) =
            handle.createUpdate(
                    "insert into $COURSE_REPORT_TABLE (" +
                            "$COURSE_ID, " +
                            "$COURSE_FULL_NAME, " +
                            "$COURSE_SHORT_NAME, " +
                            "$COURSE_REPORTED_BY, " +
                            "$COURSE_VOTES, " +
                            "$COURSE_TIMESTAMP) " +
                            "values(:courseId, :fullName, :shortName, " +
                            ":reportedBy, :votes, :timestamp)"
            )
                    .bind("courseId", courseId)
                    .bind("fullName", courseReport.fullName)
                    .bind("shortName", courseReport.shortName)
                    .bind("reportedBy", courseReport.reportedBy)
                    .bind("votes", courseReport.votes)
                    .bind("timestamp", courseReport.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseReport::class.java)
                    .findFirst()

    override fun deleteReportOnCourse(reportId: Int) =
            handle.createUpdate("delete from $COURSE_REPORT_TABLE where $COURSE_REPORT_ID = :reportId")
                    .bind("reportId", reportId)
                    .execute()

    override fun deleteAllReportsOnCourse(courseId: Int) =
            handle.createUpdate("delete from $COURSE_REPORT_TABLE where $COURSE_ID = :courseId")
                    .bind("courseId", courseId)
                    .execute()

    override fun getAllReportsOnCourse(courseId: Int) =
            handle.createQuery("select * from $COURSE_REPORT_TABLE where $COURSE_ID = :courseId")
                    .bind("courseId", courseId)
                    .mapTo(CourseReport::class.java)
                    .list()

    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int) =
            handle.createQuery(
                    "select * from $COURSE_REPORT_TABLE" +
                            "where $COURSE_ID = :courseId and $COURSE_REPORT_ID = :reportId"
            )
                    .bind("courseId", courseId)
                    .bind("reportId", reportId)
                    .mapTo(CourseReport::class.java)
                    .findFirst()

    override fun getAllCourseStageEntries() =
            handle.createQuery("select * from $COURSE_STAGE_TABLE")
                    .mapTo(CourseStage::class.java)
                    .list()

    override fun getCourseSpecificStageEntry(courseStageId: Int) =
            handle.createQuery(
                    "select * from $COURSE_STAGE_TABLE " +
                            "where $COURSE_STAGE_ID = :stageId"
            )
                    .bind("stageId", courseStageId)
                    .mapTo(CourseStage::class.java)
                    .findFirst()

    override fun getTermsOfCourse(courseId: Int) =
            handle.createQuery(
                    "select T.${TermDAOImpl.TERM_ID}, T.${TermDAOImpl.TERM_SHORT_NAME}, " +
                            "T.${TermDAOImpl.TERM_YEAR}, T.${TermDAOImpl.TERM_TYPE}, T.${TermDAOImpl.TERM_TIMESTAMP} " +
                            "from ${TermDAOImpl.TERM_TABLE} as T " +
                            "inner join $COURSE_TERM_TABLE as C on T.${TermDAOImpl.TERM_ID} = C.${COURSE_TERM_TERM_ID} " +
                            "where C.$COURSE_ID = :courseId"
            )
                    .bind("courseId", courseId)
                    .mapTo(Term::class.java)
                    .list()

    override fun getSpecificTermOfCourse(courseId: Int, termId: Int) =
            handle.createQuery(
                    "select T.${TermDAOImpl.TERM_ID}, T.${TermDAOImpl.TERM_SHORT_NAME}, " +
                            "T.${TermDAOImpl.TERM_YEAR}, T.${TermDAOImpl.TERM_TYPE} " +
                            "from ${TermDAOImpl.TERM_TABLE} as T " +
                            "inner join $COURSE_TERM_TABLE as C on T.${TermDAOImpl.TERM_ID} = C.${TermDAOImpl.TERM_ID} " +
                            "where C.$COURSE_ID = :courseId"
            )
                    .bind("courseId", courseId)
                    .mapTo(Term::class.java)
                    .findFirst()

    override fun voteOnReportOfCourse(reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $COURSE_VOTES from $COURSE_REPORT_TABLE where $COURSE_REPORT_ID = :reportId")
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate("update $COURSE_REPORT_TABLE set $COURSE_VOTES = :votes where $COURSE_REPORT_ID = :reportId")
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getAllCoursesOnSpecificProgramme(programmeId: Int) =
            handle.createQuery(
                    "select cp.$COURSE_PROGRAMME_COURSE_ID, " +
                            "cp.$COURSE_PROGRAMME_PROGRAMME_ID, " +
                            "cp.$COURSE_PROGRAMME_VERSION, " +
                            "cp.$COURSE_PROGRAMME_LECTURED_TERM, " +
                            "c.$COURSE_CREATED_BY, " +
                            "c.$COURSE_FULL_NAME, " +
                            "c.$COURSE_SHORT_NAME, " +
                            "c.$COURSE_TIMESTAMP, " +
                            "c.$COURSE_VOTES, " +
                            "cp.$COURSE_PROGRAMME_OPTIONAL, " +
                            "cp.$COURSE_PROGRAMME_CREDITS " +
                            "from $COURSE_TABLE as c " +
                            "inner join $COURSE_PROGRAMME_TABLE as cp " +
                            "on c.$COURSE_ID = cp.$COURSE_PROGRAMME_COURSE_ID " +
                            "where cp.$COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
            )
                    .bind("programmeId", programmeId)
                    .mapTo(Course::class.java)
                    .list()

    override fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int) =
            handle.createQuery(
                    "select cp.$COURSE_PROGRAMME_COURSE_ID, " +
                            "cp.$COURSE_PROGRAMME_PROGRAMME_ID, " +
                            "cp.$COURSE_PROGRAMME_VERSION, " +
                            "cp.$COURSE_PROGRAMME_LECTURED_TERM, " +
                            "c.$COURSE_CREATED_BY, " +
                            "c.$COURSE_FULL_NAME, " +
                            "c.$COURSE_SHORT_NAME, " +
                            "c.$COURSE_TIMESTAMP, " +
                            "cp.$COURSE_VOTES, " +
                            "cp.$COURSE_PROGRAMME_OPTIONAL, " +
                            "cp.$COURSE_PROGRAMME_CREDITS " +
                            "from $COURSE_TABLE as c " +
                            "inner join $COURSE_PROGRAMME_TABLE as cp " +
                            "on c.$COURSE_ID = cp.$COURSE_PROGRAMME_COURSE_ID " +
                            "where cp.$COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                            "and cp.$COURSE_PROGRAMME_COURSE_ID = :courseId"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .mapTo(Course::class.java)
                    .findFirst()

    override fun addCourseToProgramme(programmeId: Int, course: Course) =
            handle.createUpdate(
                    "insert into $COURSE_PROGRAMME_TABLE " +
                            "($COURSE_PROGRAMME_COURSE_ID, " +
                            "$COURSE_PROGRAMME_PROGRAMME_ID, " +
                            "$COURSE_PROGRAMME_LECTURED_TERM, " +
                            "$COURSE_PROGRAMME_OPTIONAL, " +
                            "$COURSE_PROGRAMME_CREDITS, " +
                            "$COURSE_PROGRAMME_TIMESTAMP, " +
                            "$COURSE_PROGRAMME_CREATED_BY) " +
                            "values (:courseId, :programmeId, :lecturedTerm, " +
                            ":optional, :credits, :timestamp, :createdBy)"
            )
                    .bind("courseId", course.courseId)
                    .bind("programmeId", programmeId)
                    .bind("lecturedTerm", course.lecturedTerm)
                    .bind("optional", course.optional)
                    .bind("credits", course.credits)
                    .bind("timestamp", course.timestamp)
                    .bind("createdBy", course.createdBy)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Course::class.java)
                    .findFirst()

    override fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, courseProgrammeReport: CourseProgrammeReport) =
            handle.createUpdate("insert into $COURSE_PROGRAMME_REPORT_TABLE (" +
                    "$COURSE_PROGRAMME_COURSE_ID, " +
                    "$COURSE_PROGRAMME_PROGRAMME_ID, " +
                    "$COURSE_PROGRAMME_LECTURED_TERM, " +
                    "$COURSE_PROGRAMME_OPTIONAL, " +
                    "$COURSE_PROGRAMME_CREDITS, " +
                    "$COURSE_PROGRAMME_TIMESTAMP, " +
                    "$COURSE_PROGRAMME_REPORTED_BY," +
                    "$COURSE_PROGRAMME_VOTES " +
                    ") " +
                    "values (:courseId, :programmeId, :lecturedTerm, " +
                    ":optional, :credits, :timestamp, :reportedBy, :votes)"
            )
                    .bind("courseId", courseId)
                    .bind("programmeId", programmeId)
                    .bind("lecturedTerm", courseProgrammeReport.lecturedTerm)
                    .bind("optional", courseProgrammeReport.optional)
                    .bind("credits", courseProgrammeReport.credits)
                    .bind("timestamp", courseProgrammeReport.timestamp)
                    .bind("reportedBy", courseProgrammeReport.reportedBy)
                    .bind("votes", courseProgrammeReport.votes)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseProgrammeReport::class.java)
                    .findFirst()

    override fun deleteAllVersionsOfCourse(versionCourseId: Int) =
            handle.createUpdate("delete from $COURSE_VERSION_TABLE where $COURSE_ID = :courseId")
                    .bind("courseId", versionCourseId)
                    .execute()

    override fun getAllVersionsOfSpecificCourse(courseId: Int) =
            handle.createQuery("select * from $COURSE_VERSION_TABLE where $COURSE_ID = :courseId")
                    .bind("courseId", courseId)
                    .mapTo(CourseVersion::class.java)
                    .list()

    override fun getVersionOfSpecificCourse(courseId: Int, version: Int) =
            handle.createQuery(
                    "select * from $COURSE_VERSION_TABLE " +
                            "where $COURSE_ID = :courseId and $COURSE_VERSION = :version"
            )
                    .bind("courseId", courseId)
                    .bind("version", version)
                    .mapTo(CourseVersion::class.java)
                    .findFirst()

    override fun createCourseVersion(courseVersion: CourseVersion): Optional<CourseVersion> =
            handle.createUpdate(
                    "insert into $COURSE_VERSION_TABLE " +
                            "($COURSE_ID, $COURSE_ORGANIZATION_ID, $COURSE_VERSION, $COURSE_FULL_NAME, $COURSE_SHORT_NAME, " +
                            "$COURSE_CREATED_BY, $COURSE_TIMESTAMP)" +
                            "values (:courseId, :orgId, :version, :fullName, " +
                            ":shortName, :createdBy, :timestamp)"
            )
                    .bind("courseId", courseVersion.courseId)
                    .bind("orgId", courseVersion.organizationId)
                    .bind("version", courseVersion.version)
                    .bind("fullName", courseVersion.fullName)
                    .bind("shortName", courseVersion.shortName)
                    .bind("createdBy", courseVersion.createdBy)
                    .bind("timestamp", courseVersion.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseVersion::class.java)
                    .findFirst()

    override fun voteOnCourseProgramme(programmeId: Int, courseId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $COURSE_VOTES from $COURSE_PROGRAMME_TABLE " +
                        "where $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                        "and $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
        )
                .bind("courseId", courseId)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $COURSE_PROGRAMME_TABLE set " +
                        "$COURSE_PROGRAMME_VOTES = :votes " +
                        "where $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                        "and $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
        )
                .bind("votes", votes)
                .bind("courseId", courseId)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun updateCourseProgramme(programmeId: Int, courseId: Int, course: Course): Optional<Course> =
            handle.createUpdate(
                    "update $COURSE_PROGRAMME_TABLE SET " +
                            "$COURSE_PROGRAMME_VERSION = :version, " +
                            "$COURSE_PROGRAMME_CREATED_BY = :createdBy, " +
                            "$COURSE_PROGRAMME_LECTURED_TERM = :lecturedTerm, " +
                            "$COURSE_PROGRAMME_OPTIONAL = :optional, " +
                            "$COURSE_PROGRAMME_VOTES = :votes, " +
                            "$COURSE_PROGRAMME_TIMESTAMP = :timestamp, " +
                            "$COURSE_PROGRAMME_CREDITS = :credits " +
                            "where $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                            "and $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
            )
                    .bind("version", course.version)
                    .bind("createdBy", course.createdBy)
                    .bind("lecturedTerm", course.lecturedTerm)
                    .bind("optional", course.optional)
                    .bind("votes", course.votes)
                    .bind("timestamp", course.timestamp)
                    .bind("credits", course.credits)
                    .bind("courseId", courseId)
                    .bind("programmeId", programmeId)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Course::class.java)
                    .findFirst()

    override fun createStagingCourseOfProgramme(courseProgrammeStage: CourseProgrammeStage) =
            handle.createUpdate(
                    "insert into $COURSE_PROGRAMME_STAGE_TABLE " +
                            "($COURSE_PROGRAMME_COURSE_ID, " +
                            "$COURSE_PROGRAMME_PROGRAMME_ID, " +
                            "$COURSE_PROGRAMME_LECTURED_TERM, " +
                            "$COURSE_PROGRAMME_OPTIONAL, " +
                            "$COURSE_PROGRAMME_CREATED_BY, " +
                            "$COURSE_PROGRAMME_CREDITS, " +
                            "$COURSE_PROGRAMME_VOTES, " +
                            "$COURSE_PROGRAMME_TIMESTAMP) " +
                            "values(:courseId, :programmeId, :lecturedTerm, :optional, :credits, :votes, :createdBy, :timestamp)"
            )
                    .bind("courseId", courseProgrammeStage.courseId)
                    .bind("programmeId", courseProgrammeStage.programmeId)
                    .bind("lecturedTerm", courseProgrammeStage.lecturedTerm)
                    .bind("optional", courseProgrammeStage.optional)
                    .bind("credits", courseProgrammeStage.credits)
                    .bind("votes", courseProgrammeStage.votes)
                    .bind("createdBy", courseProgrammeStage.createdBy)
                    .bind("timestamp", courseProgrammeStage.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseProgrammeStage::class.java)
                    .findFirst()

    override fun getSpecificStagedCourseProgramme(programmeId: Int, stageId: Int) =
            handle.createQuery(
                    "select * from $COURSE_PROGRAMME_STAGE_TABLE " +
                            "where $COURSE_PROGRAMME_STAGE_ID = :stageId" +
                            "and $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
            )
                    .bind("stageId", stageId)
                    .bind("programmeId", programmeId)
                    .mapTo(CourseProgrammeStage::class.java)
                    .findFirst()

    override fun deleteStagedCourseProgramme(courseProgrammeStageId: Int) =
            handle.createUpdate("delete from $COURSE_PROGRAMME_STAGE_TABLE where $COURSE_PROGRAMME_STAGE_ID = :stageId")
                    .bind("stageId", courseProgrammeStageId)
                    .execute()

    override fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $COURSE_VOTES from $COURSE_PROGRAMME_STAGE_TABLE " +
                        "where $COURSE_PROGRAMME_STAGE_ID = :stageId " +
                        "and $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
        )
                .bind("stageId", stageId)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $COURSE_PROGRAMME_STAGE_TABLE " +
                        "set $COURSE_PROGRAMME_VOTES = :votes " +
                        "where $COURSE_PROGRAMME_STAGE_ID = :stageId " +
                        "and $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
        )
                .bind("votes", votes)
                .bind("stageId", stageId)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int) =
            handle.createQuery(
                    "select * from $COURSE_PROGRAMME_VERSION_TABLE " +
                            "where $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                            "and $COURSE_PROGRAMME_COURSE_ID = :courseId"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .mapTo(CourseProgrammeVersion::class.java)
                    .list()

    override fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int) =
            handle.createQuery(
                    "select * from $COURSE_PROGRAMME_VERSION_TABLE " +
                            "where $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                            "and $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                            "and $COURSE_PROGRAMME_VERSION = :version"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .bind("version", version)
                    .mapTo(CourseProgrammeVersion::class.java)
                    .findFirst()

    override fun createCourseProgrammeVersion(courseProgramme: Course) =
            handle.createUpdate(
                    "insert into $COURSE_PROGRAMME_VERSION_TABLE (" +
                            "$COURSE_PROGRAMME_COURSE_ID, " +
                            "$COURSE_PROGRAMME_PROGRAMME_ID, " +
                            "$COURSE_PROGRAMME_VERSION, " +
                            "$COURSE_PROGRAMME_LECTURED_TERM, " +
                            "$COURSE_PROGRAMME_OPTIONAL, " +
                            "$COURSE_PROGRAMME_CREDITS, " +
                            "$COURSE_PROGRAMME_CREATED_BY, " +
                            "$COURSE_PROGRAMME_TIMESTAMP," +
                            ")" +
                            "values (:courseId, :progId, :version, :lectured, " +
                            ":optional, :credits, :createdBy, :timestamp)"
            )
                    .bind("courseId", courseProgramme.courseId)
                    .bind("progId", courseProgramme.programmeId)
                    .bind("version", courseProgramme.version)
                    .bind("lectured", courseProgramme.lecturedTerm)
                    .bind("optional", courseProgramme.optional)
                    .bind("credits", courseProgramme.credits)
                    .bind("createdBy", courseProgramme.createdBy)
                    .bind("timestamp", courseProgramme.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseProgrammeVersion::class.java)
                    .findFirst()

    override fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int) =
            handle.createQuery(
                    "select * from $COURSE_PROGRAMME_REPORT_TABLE " +
                            "where $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                            "and $COURSE_PROGRAMME_COURSE_ID = :courseId"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .mapTo(CourseProgrammeReport::class.java)
                    .list()

    override fun getSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Optional<CourseProgrammeReport> =
            handle.createQuery(
                    "select * from $COURSE_PROGRAMME_REPORT_TABLE " +
                            "where $COURSE_PROGRAMME_REPORT_ID= :reportId " +
                            "and $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                            "and $COURSE_PROGRAMME_COURSE_ID = :courseId"
            )
                    .bind("reportId", reportId)
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .mapTo(CourseProgrammeReport::class.java)
                    .findFirst()

    override fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int =
            handle.createUpdate(
                    "delete from $COURSE_PROGRAMME_REPORT_TABLE " +
                            "where $COURSE_PROGRAMME_REPORT_ID = :reportId" +
                            "and $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                            "and $COURSE_PROGRAMME_PROGRAMME_ID = programmeId"
            )
                    .bind("reportId", reportId)
                    .bind("courseId", courseId)
                    .bind("programmeId", programmeId)
                    .execute()

    override fun voteOnReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $COURSE_PROGRAMME_VOTES from $COURSE_PROGRAMME_REPORT_TABLE " +
                        "where $COURSE_PROGRAMME_REPORT_ID = :reportId " +
                        "and $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                        "and $COURSE_PROGRAMME_COURSE_ID = :courseId"
        )
                .bind("reportId", reportId)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $COURSE_PROGRAMME_REPORT_TABLE " +
                        "set $COURSE_PROGRAMME_VOTES = :votes " +
                        "where $COURSE_PROGRAMME_REPORT_ID = :reportId " +
                        "and $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                        "and $COURSE_PROGRAMME_COURSE_ID = :courseId"
        )
                .bind("votes", votes)
                .bind("reportId", reportId)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .execute()
    }

    override fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int) =
            handle.createQuery(
                    "select * from $COURSE_PROGRAMME_STAGE_TABLE " +
                            "where $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
            )
                    .bind("programmeId", programmeId)
                    .mapTo(CourseProgrammeStage::class.java)
                    .list()

    override fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int) =
            handle.createUpdate(
                    "delete from $COURSE_PROGRAMME_TABLE " +
                            "where $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                            "and $COURSE_PROGRAMME_COURSE_ID = :courseId"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .execute()

    override fun deleteSpecificStagedCourseProgramme(programmeId: Int, courseId: Int, stageId: Int) =
            handle.createUpdate(
                    "delete from $COURSE_PROGRAMME_STAGE_TABLE " +
                            "where $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                            "and $COURSE_PROGRAMME_COURSE_ID = :courseId" +
                            "and $COURSE_PROGRAMME_STAGE_ID = :stageId"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .bind("stageId", stageId)
                    .execute()

    override fun deleteSpecificVersionOfCourseProgramme(programmeId: Int, courseId: Int, version: Int) =
            handle.createUpdate(
                    "delete from $COURSE_PROGRAMME_VERSION_TABLE " +
                            "where $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                            "and $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                            "and $COURSE_PROGRAMME_VERSION = :version"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .bind("version", version)
                    .execute()

    override fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int) =
            handle.createUpdate(
                    "delete from $COURSE_PROGRAMME_REPORT_TABLE " +
                            "where $COURSE_PROGRAMME_REPORT_ID = :reportId " +
                            "and $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                            "and $COURSE_PROGRAMME_COURSE_ID = :courseId"
            )
                    .bind("reportId", reportId)
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .execute()
}


