package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_ID
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_TYPE
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_YEAR
import isel.leic.ps.eduWikiAPI.repository.TermDAOImpl.Companion.TERM_TABLE

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
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

        const val COURSE_PROG_TABLE = "course_programme"
        const val COURSE_PROG_STAGE_TABLE = "course_programme_stage"
        const val COURSE_PROG_REPORT_TABLE = "course_programme_report"
        const val COURSE_PROG_VERSION_TABLE = "course_programme_version"

        const val COURSE_CLASS_TABLE = "course_class"
        const val COURSE_TERM_TABLE = "course_term"
        const val COURSE_MISC_UNIT_TABLE = "course_misc_unit"
        const val COURSE_MISC_UNIT_STAGE_TABLE = "course_misc_unit_stage"
        const val COURSE_MISC_TYPE = "misc_type"
        // COURSE FIELDS
        const val COURSE_MISC_UNIT_ID = "id"
        const val COURSE_STAGE_ID = "course_stage_id"
        const val COURSE_ID = "course_id"
        const val COURSE_ORG_ID = "organization_id"
        const val COURSE_VERSION = "course_version"
        const val COURSE_FULL_NAME = "course_full_name"
        const val COURSE_SHORT_NAME = "course_short_name"
        const val COURSE_VOTES = "votes"
        const val COURSE_TIMESTAMP = "time_stamp"
        const val COURSE_REPORT_ID = "report_id"
        const val COURSE_REPORTED_BY = "reported_by"
        const val COURSE_CREATED_BY = "created_by"
        // COURSE_PROGRAMME FIELDS
        const val COURSE_PROG_STAGE_ID = "course_programme_stage_id"
        const val COURSE_PROG_PROG_ID = "programme_id"
        const val COURSE_PROG_COURSE_ID = "course_id"
        const val COURSE_PROG_VERSION = "course_programme_version"
        const val COURSE_PROG_LECTURED_TERM = "course_lectured_term"
        const val COURSE_PROG_OPTIONAL = "course_optional"
        const val COURSE_PROG_CREDITS = "course_credits"
        const val COURSE_PROG_VOTES = "votes"
        const val COURSE_PROG_TIMESTAMP = "time_stamp"
        const val COURSE_PROG_CREATED_BY = "created_by"
        const val COURSE_PROG_REPORTED_BY = "reported_by"
        const val COURSE_PROG_REPORT_ID = "report_id"

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
                            "$COURSE_ORG_ID = :orgId, $COURSE_VERSION = :version, " +
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
                    .bind("courseId", course.id)
                    .execute()

    override fun createCourse(course: Course) =
            handle.createUpdate(
                    "insert into $COURSE_TABLE " +
                            "($COURSE_ORG_ID, $COURSE_VERSION, $COURSE_CREATED_BY, " +
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
                            "($COURSE_ORG_ID, $COURSE_FULL_NAME, " +
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

    override fun voteOnStagedCourse(courseStageId: Int, inputVote: Vote): Int {
        var votes = handle.createQuery("select $COURSE_VOTES from $COURSE_STAGE_TABLE where $COURSE_STAGE_ID = :courseId")
                .bind("courseId", courseStageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (inputVote == Vote.Down) --votes else ++votes
        return handle.createUpdate("update $COURSE_STAGE_TABLE set $COURSE_VOTES = :votes where $COURSE_STAGE_ID = :courseId")
                .bind("votes", votes)
                .bind("courseId", courseStageId)
                .execute()
    }

    override fun deleteVersionOfCourse(courseId: Int, version: Int) =
            handle.createUpdate("delete from $COURSE_VERSION_TABLE where $COURSE_ID = :id and $COURSE_VERSION = :version")
                    .bind("id", courseId)
                    .bind("version", version)
                    .execute()


    override fun reportCourse(courseId: Int, courseReport: CourseReport) =
            handle.createUpdate(
                    "insert into $COURSE_REPORT_TABLE " +
                            "($COURSE_ID, $COURSE_FULL_NAME, " +
                            "$COURSE_SHORT_NAME, $COURSE_REPORTED_BY, $COURSE_VOTES, $COURSE_TIMESTAMP) " +
                            "values(:courseId, :fullName, :shortName, :reportedBy, :votes, :timestamp)"
            )
                    .bind("courseId", courseId)
                    .bind("fullName", courseReport.courseFullName)
                    .bind("shortName", courseReport.courseShortName)
                    .bind("reportedBy", courseReport.reportedBy)
                    .bind("votes", courseReport.votes)
                    .bind("timestamp", courseReport.timestamp)
                    .execute()

    override fun deleteReportOnCourse(reportId: Int) =
            handle.createUpdate("delete from $COURSE_REPORT_TABLE where $COURSE_REPORT_ID = :id")
                    .bind("id", reportId)
                    .execute()

    override fun deleteAllReportsOnCourse(courseId: Int) =
            handle.createUpdate("delete from $COURSE_REPORT_TABLE").execute()

    override fun getAllReportsOnCourse(courseId: Int) =
            handle.createQuery("select * from $COURSE_REPORT_TABLE where $COURSE_ID = :courseId")
                    .bind("courseId", courseId)
                    .mapTo(CourseReport::class.java)
                    .list()

    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int) =
            handle.createQuery(
                    "select * from $COURSE_REPORT_TABLE " +
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
                            "where $COURSE_STAGE_ID = :courseId"
            )
                    .bind("courseId", courseStageId)
                    .mapTo(CourseStage::class.java)
                    .findFirst()

    override fun getTermsOfCourse(courseId: Int) =
            handle.createQuery(
                    "select T.$TERM_ID, $TERM_SHORT_NAME, " +
                            "$TERM_YEAR, $TERM_TYPE " +
                            "from $TERM_TABLE as T " +
                            "inner join $COURSE_TERM_TABLE as C on T.$TERM_ID = C.$TERM_ID " +
                            "where C.$COURSE_ID = :courseId"
            )
                    .bind("courseId", courseId)
                    .mapTo(Term::class.java)
                    .list()

    override fun getSpecificTermOfCourse(courseId: Int, termId: Int) =
            handle.createQuery(
                    "select T.$TERM_ID, T.$TERM_SHORT_NAME, " +
                            "T.$TERM_YEAR, T.$TERM_TYPE " +
                            "from $TERM_TABLE as T " +
                            "inner join $COURSE_TERM_TABLE as C on T.$TERM_ID = C.$TERM_ID " +
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

    override fun getCoursesOnSpecificProgramme(programmeId: Int) =
            handle.createQuery(
                    "select cp.$COURSE_ID, cp.$COURSE_PROG_PROG_ID, cp.$COURSE_PROG_VERSION, " +
                            "cp.$COURSE_PROG_LECTURED_TERM, c.$COURSE_CREATED_BY, c.$COURSE_FULL_NAME, c.$COURSE_SHORT_NAME, c.$COURSE_TIMESTAMP, c.$COURSE_VOTES, " +
                            "cp.$COURSE_PROG_OPTIONAL, cp.$COURSE_PROG_CREDITS " +
                            "from $COURSE_TABLE as c " +
                            "inner join $COURSE_PROG_TABLE as cp on c.$COURSE_ID = cp.$COURSE_ID " +
                            "where cp.$COURSE_PROG_PROG_ID = :programmeId"
            )
                    .bind("programmeId", programmeId)
                    .mapTo(Course::class.java)
                    .list()

    override fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int) =
            handle.createQuery(
                    "select cp.$COURSE_ID, cp.$COURSE_PROG_PROG_ID, cp.$COURSE_PROG_VERSION, " +
                            "cp.$COURSE_PROG_LECTURED_TERM, c.$COURSE_CREATED_BY, c.$COURSE_FULL_NAME, c.$COURSE_SHORT_NAME, c.$COURSE_TIMESTAMP, cp.$COURSE_VOTES, " +
                            "cp.$COURSE_PROG_OPTIONAL, cp.$COURSE_PROG_CREDITS " +
                            "from $COURSE_TABLE as c " +
                            "inner join $COURSE_PROG_TABLE as cp on c.$COURSE_ID = cp.$COURSE_ID " +
                            "where cp.$COURSE_PROG_PROG_ID = :programmeId and cp.$COURSE_ID = :courseId"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .mapTo(Course::class.java)
                    .findFirst()

    override fun addCourseToProgramme(programmeId: Int, course: Course) =
            handle.createUpdate(
                    "insert into $COURSE_PROG_TABLE " +
                            "($COURSE_PROG_COURSE_ID, $COURSE_PROG_PROG_ID, $COURSE_PROG_LECTURED_TERM, $COURSE_PROG_OPTIONAL, " +
                            "$COURSE_PROG_CREDITS, $COURSE_PROG_TIMESTAMP, $COURSE_PROG_CREATED_BY) " +
                            "values (:courseId, :programmeId, :term, :optional, :credits, :timestamp, :created_by)"
            )
                    .bind("courseId", course.id)
                    .bind("programmeId", programmeId)
                    .bind("term", course.lecturedTerm)
                    .bind("optional", course.optional)
                    .bind("credits", course.credits)
                    .bind("timestamp", course.timestamp)
                    .bind("created_by", course.createdBy)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Course::class.java)
                    .findFirst()

    override fun reportCourseOnProgramme(programmeId: Int, courseId: Int, courseProgrammeReport: CourseProgrammeReport) =
            handle.createUpdate("insert into $COURSE_PROG_REPORT_TABLE (" +
                    "$COURSE_PROG_COURSE_ID, " +
                    "$COURSE_PROG_PROG_ID, " +
                    "$COURSE_PROG_LECTURED_TERM, " +
                    "$COURSE_PROG_OPTIONAL, " +
                    "$COURSE_PROG_CREDITS, " +
                    "$COURSE_PROG_TIMESTAMP, " +
                    "$COURSE_PROG_REPORTED_BY) " +
                    "values (:courseId, :programmeId, :lectured, :optional, :credits, :timestamp, :reportedBy)")
                    .bind("courseId", courseId)
                    .bind("programmeId", programmeId)
                    .bind("lectured", courseProgrammeReport.lecturedTerm)
                    .bind("optional", courseProgrammeReport.optional)
                    .bind("credits", courseProgrammeReport.credits)
                    .bind("timestamp", courseProgrammeReport.timestamp)
                    .bind("reportedBy", courseProgrammeReport.reportedBy)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseProgrammeReport::class.java)
                    .findFirst()

    override fun deleteAllVersionsOfCourse(versionCourseId: Int) =
            handle.createUpdate("delete from $COURSE_VERSION_TABLE where $COURSE_ID = :id")
                    .bind("id", versionCourseId)
                    .execute()

    override fun getAllVersionsOfSpecificCourse(courseId: Int) =
            handle.createQuery("select * from $COURSE_VERSION_TABLE where $COURSE_ID = :courseId")
                    .bind("courseId", courseId)
                    .mapTo(CourseVersion::class.java)
                    .list()

    override fun getVersionOfSpecificCourse(courseId: Int, versionId: Int) =
            handle.createQuery("select * from $COURSE_VERSION_TABLE where $COURSE_ID = :courseId and $COURSE_VERSION = :versionId")
                    .bind("courseId", courseId)
                    .bind("versionId", versionId)
                    .mapTo(CourseVersion::class.java)
                    .findFirst()

    override fun createCourseVersion(courseVersion: CourseVersion): Optional<CourseVersion> =
            handle.createUpdate(
                    "insert into $COURSE_VERSION_TABLE " +
                            "($COURSE_ID, $COURSE_ORG_ID, $COURSE_VERSION, $COURSE_FULL_NAME, $COURSE_SHORT_NAME, " +
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

    override fun voteOnCourseOfProgramme(programmeId: Int, vote: Vote, courseId: Int): Int {
        var votes = handle.createQuery("select $COURSE_VOTES from $COURSE_PROG_TABLE where $COURSE_PROG_COURSE_ID = :courseId and $COURSE_PROG_PROG_ID =:programmeId")
                .bind("courseId", courseId)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) votes.dec() else votes.inc()
        val updateQuery = "update $COURSE_PROG_TABLE set $COURSE_PROG_VOTES = :votes where $COURSE_PROG_COURSE_ID = :courseId and $COURSE_PROG_PROG_ID = :programmeId"
        return handle.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("courseId", courseId)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun updateCourseProgramme(programmeId: Int, courseId: Int, updatedCourse: Course) =
            handle.createUpdate(
                    "update $COURSE_PROG_TABLE SET " +
                            "$COURSE_PROG_VERSION = :version, " +
                            "$COURSE_PROG_CREATED_BY = :createdBy, $COURSE_PROG_LECTURED_TERM = :lecture, " +
                            "$COURSE_PROG_OPTIONAL = :optional, $COURSE_PROG_VOTES = :votes, " +
                            "$COURSE_PROG_TIMESTAMP = :timestamp, $COURSE_PROG_CREDITS = :credits " +
                            "where $COURSE_PROG_COURSE_ID = :courseId and $COURSE_PROG_PROG_ID = :programmeId"
            )
                    .bind("version", updatedCourse.version)
                    .bind("createdBy", updatedCourse.createdBy)
                    .bind("lecture", updatedCourse.lecturedTerm)
                    .bind("optional", updatedCourse.optional)
                    .bind("votes", updatedCourse.votes)
                    .bind("timestamp", updatedCourse.timestamp)
                    .bind("credits", updatedCourse.credits)
                    .bind("courseId", courseId)
                    .bind("programmeId", programmeId)
                    .execute()

    override fun createStagingCourseOfProgramme(courseProgrammeStage: CourseProgrammeStage) =
            handle.createUpdate(
                    "insert into $COURSE_PROG_STAGE_TABLE " +
                            "($COURSE_PROG_COURSE_ID, $COURSE_PROG_PROG_ID, $COURSE_PROG_LECTURED_TERM, $COURSE_PROG_OPTIONAL, " +
                            "$COURSE_PROG_CREDITS, $COURSE_PROG_VOTES) " +
                            "values(:courseId, :programmeId, :term, :optional, :credits)"
            )
                    .bind("courseId", courseProgrammeStage.courseId)
                    .bind("programmeId", courseProgrammeStage.programmeId)
                    .bind("term", courseProgrammeStage.lecturedTerm)
                    .bind("optional", courseProgrammeStage.optional)
                    .bind("credits", courseProgrammeStage.credits)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseProgrammeStage::class.java)
                    .findFirst()

    override fun getSpecificStagedCourseProgramme(courseProgrammeStageId: Int) =
            handle.createQuery(
                    "select * from $COURSE_PROG_STAGE_TABLE where $COURSE_PROG_STAGE_ID = :courseProgrammeId"
            )
                    .bind("courseProgrammeId", courseProgrammeStageId)
                    .mapTo(CourseProgrammeStage::class.java)
                    .findFirst()

    override fun deleteStagedCourseOfProgramme(courseProgrammeStageId: Int) =
            handle.createUpdate("delete from $COURSE_PROG_STAGE_TABLE where $COURSE_PROG_STAGE_ID = :courseProgrammeId")
                    .bind("courseProgrammeId", courseProgrammeStageId)
                    .execute()

    override fun voteOnCourseProgrammeStaged(stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $COURSE_VOTES from $COURSE_PROG_STAGE_TABLE where $COURSE_PROG_STAGE_ID = :courseProgrammeStageId")
                .bind("courseProgrammeStageId", stageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) votes.dec() else votes.inc()
        return handle.createUpdate("update $COURSE_PROG_STAGE_TABLE set $COURSE_PROG_VOTES = :votes where $COURSE_PROG_STAGE_ID = :courseProgrammeStageId")
                .bind("votes", votes)
                .bind("courseProgrammeStageId", stageId)
                .execute()
    }

    override fun getAllVersionsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int) =
            handle.createQuery(
                    "select * from $COURSE_PROG_VERSION_TABLE where $COURSE_PROG_PROG_ID = :programmeId and $COURSE_PROG_COURSE_ID = :courseId"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .mapTo(CourseProgrammeVersion::class.java)
                    .list()

    override fun getSpecificVersionOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, versionId: Int) =
            handle.createQuery(
                    "select * from $COURSE_PROG_VERSION_TABLE where $COURSE_PROG_PROG_ID = :programmeId and $COURSE_PROG_COURSE_ID = :courseId and $COURSE_PROG_VERSION = :versionId"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .bind("versionId", versionId)
                    .mapTo(CourseProgrammeVersion::class.java)
                    .findFirst()

    //TODO: mudar par receber um courseProgrammeVersion
    override fun addCourseProgrammeToVersion(courseProgramme: Course) =
            handle.createUpdate(
                    "insert into $COURSE_PROG_VERSION_TABLE " +
                            "($COURSE_PROG_COURSE_ID, $COURSE_PROG_PROG_ID, $COURSE_PROG_VERSION, $COURSE_PROG_LECTURED_TERM, $COURSE_PROG_OPTIONAL, " +
                            "$COURSE_PROG_CREDITS, $COURSE_PROG_CREATED_BY, $COURSE_PROG_TIMESTAMP)" +
                            "values (:courseId, :progId, :version, :lectured, " +
                            ":optional, :credits, :createdBy, :timestamp)"
            )
                    .bind("courseId", courseProgramme.id)
                    .bind("progId", courseProgramme.programmeId)
                    .bind("version", courseProgramme.version)
                    .bind("lectured", courseProgramme.lecturedTerm)
                    .bind("optional", courseProgramme.optional)
                    .bind("createdBy", courseProgramme.createdBy)
                    .bind("timestamp", courseProgramme.timestamp)
                    .bind("credits", courseProgramme.credits)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseProgrammeVersion::class.java)
                    .findFirst()

    override fun getAllReportsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int) =
            handle.createQuery(
                    "select * from $COURSE_PROG_REPORT_TABLE where $COURSE_PROG_PROG_ID = :programmeId and $COURSE_PROG_COURSE_ID = :courseId"
            )
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .mapTo(CourseProgrammeReport::class.java)
                    .list()

    override fun getSpecificReportOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, reportId: Int) =
            handle.createQuery("select * from $COURSE_PROG_REPORT_TABLE where $COURSE_REPORT_ID= :reportId")
                    .bind("reportId", reportId)
                    .mapTo(CourseProgrammeReport::class.java)
                    .findFirst()

    override fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int) =
            handle.createUpdate("delete from $COURSE_PROG_REPORT_TABLE where $COURSE_REPORT_ID = :reportId").bind("reportId", reportId).execute()

    override fun voteOnReportOfCourseProgramme(programmeId: Int, reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $COURSE_PROG_VOTES from $COURSE_PROG_REPORT_TABLE where $COURSE_REPORT_ID= :reportId")
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) votes.dec() else votes.inc()
        return handle.createUpdate("update $COURSE_PROG_REPORT_TABLE set $COURSE_PROG_VOTES = :votes where $COURSE_REPORT_ID= :reportId")
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getStagedCoursesOfProgramme(programmeId: Int) =
            handle.createQuery(
                    "select * from $COURSE_PROG_STAGE_TABLE where $COURSE_PROG_PROG_ID = :programmeId"
            )
                    .bind("programmeId", programmeId)
                    .mapTo(CourseProgrammeStage::class.java)
                    .list()

    override fun deleteSpecificCourseOfProgramme(programmeId: Int, courseId: Int) =
            handle.createUpdate("delete from $COURSE_PROG_TABLE where $COURSE_PROG_PROG_ID = :programmeId and $COURSE_PROG_COURSE_ID = :courseId")
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .execute()

    override fun deleteSpecificStagedCourseOfProgramme(programmeId: Int, courseId: Int, stageId: Int) =
            handle.createUpdate("delete from $COURSE_PROG_STAGE_TABLE where $COURSE_PROG_PROG_ID = :programmeId and $COURSE_PROG_COURSE_ID = :courseId")
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .execute()

    override fun deleteSpecificVersionOfCourseOfProgramme(programmeId: Int, courseId: Int, versionId: Int) =
            handle.createUpdate("delete from $COURSE_PROG_VERSION_TABLE where $COURSE_PROG_PROG_ID = :programmeId and $COURSE_PROG_COURSE_ID = :courseId and $COURSE_PROG_VERSION = :version")
                    .bind("programmeId", programmeId)
                    .bind("courseId", courseId)
                    .bind("version", versionId)
                    .execute()

    override fun deleteSpecificReportOfCourseOfProgramme(programmeId: Int, courseId: Int, reportId: Int) =
            handle.createUpdate("delete from $COURSE_PROG_REPORT_TABLE where $COURSE_PROG_REPORT_ID = :reportId")
                    .bind("reportId", reportId)
                    .execute()

    override fun getCoursesOfClass(classId: Int): List<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificCourseOfClass(classId: Int, courseId: Int): Optional<Course> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnCourseInClass(classId: Int, courseId: Int, valueOf: Vote): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllCoursesInClass(classId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnReportOfCourseClass(classId: Int, courseId: Int, reportId: Int, valueOf: Vote): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllCoursesStagedInClass(classId: Int): List<CourseClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificStagedCourseClass(classId: Int, stageId: Int): Optional<CourseClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: Vote): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllStagedCoursesInClass(classId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

