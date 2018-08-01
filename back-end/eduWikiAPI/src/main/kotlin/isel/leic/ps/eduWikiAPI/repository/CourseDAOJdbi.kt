package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseProgrammeVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.repository.TermDAOJdbi.Companion.TERM_ID
import isel.leic.ps.eduWikiAPI.repository.TermDAOJdbi.Companion.TERM_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.TermDAOJdbi.Companion.TERM_TABLE
import isel.leic.ps.eduWikiAPI.repository.TermDAOJdbi.Companion.TERM_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.TermDAOJdbi.Companion.TERM_TYPE
import isel.leic.ps.eduWikiAPI.repository.TermDAOJdbi.Companion.TERM_YEAR
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import org.jdbi.v3.core.Handle
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

interface CourseDAOJdbi : CourseDAO {

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

    @SqlQuery(
            "SELECT * FROM $COURSE_TABLE"
    )
    override fun getAllCourses(): List<Course>

    @SqlQuery(
            "SELECT * FROM $COURSE_TABLE WHERE $COURSE_ID = :courseId"
    )
    override fun getSpecificCourse(courseId: Int): Optional<Course>

    @SqlUpdate(
            "DELETE FROM $COURSE_TABLE WHERE $COURSE_ID = :courseId"
    )
    override fun deleteSpecificCourse(courseId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_TABLE"
    )
    override fun deleteAllCourses(): Int

    @SqlUpdate(
            "UPDATE $COURSE_TABLE SET " +
                    "$COURSE_ORGANIZATION_ID = :course.orgId, " +
                    "$COURSE_VERSION = :course.version, " +
                    "$COURSE_CREATED_BY = :course.createdBy, " +
                    "$COURSE_FULL_NAME = :course.fullName, " +
                    "$COURSE_SHORT_NAME = :course.shortName, " +
                    "$COURSE_VOTES = :course.votes, " +
                    "$COURSE_TIMESTAMP = :course.timestamp " +
                    "WHERE $COURSE_ID = :courseId"
    )
    @GetGeneratedKeys
    override fun updateCourse(course: Course): Course

    @SqlUpdate(
            "INSERT INTO $COURSE_TABLE ( " +
                    "$COURSE_ORGANIZATION_ID, " +
                    "$COURSE_VERSION, " +
                    "$COURSE_CREATED_BY, " +
                    "$COURSE_FULL_NAME, " +
                    "$COURSE_SHORT_NAME, " +
                    "$COURSE_VOTES, " +
                    "$COURSE_TIMESTAMP " +
                    ") " +
                    "VALUES(:course.organizationId, :course.version, :course.createdBy, " +
                    ":course.fullName, :course.shortName, :course.votes, :course.timestamp)"
    )
    @GetGeneratedKeys
    override fun createCourse(course: Course): Course

    override fun voteOnCourse(courseId: Int, vote: Vote): Int {
        var votes = handle.createQuery("SELECT $COURSE_VOTES FROM $COURSE_TABLE WHERE $COURSE_ID = :courseId")
                .bind("courseId", courseId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate("UPDATE $COURSE_TABLE SET $COURSE_VOTES = :votes WHERE $COURSE_ID = :courseId")
                .bind("votes", votes)
                .bind("courseId", courseId)
                .execute()
    }

    @SqlUpdate(
            "DELETE FROM $COURSE_STAGE_TABLE WHERE $COURSE_STAGE_ID = :courseStageId"
    )
    override fun deleteStagedCourse(courseStageId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_STAGE_TABLE"
    )
    override fun deleteAllStagedCourses(): Int

    @SqlUpdate(
            "INSERT INTO $COURSE_STAGE_TABLE ( " +
                    "$COURSE_ORGANIZATION_ID, " +
                    "$COURSE_FULL_NAME, " +
                    "$COURSE_SHORT_NAME, " +
                    "$COURSE_CREATED_BY, " +
                    "$COURSE_VOTES, " +
                    "$COURSE_TIMESTAMP " +
                    ") " +
                    "VALUES(:courseStage.organizationId, :courseStage.fullName, :courseStage.shortName, " +
                    ":courseStage.createdBy, :courseStage.votes, :courseStage.timestamp)"
    )
    @GetGeneratedKeys
    override fun createStagedCourse(courseStage: CourseStage): CourseStage

    override fun voteOnStagedCourse(stageId: Int, inputVote: Vote): Int {
        var votes = handle.createQuery("SELECT $COURSE_VOTES FROM $COURSE_STAGE_TABLE WHERE $COURSE_STAGE_ID = :courseId")
                .bind("courseId", stageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (inputVote == Vote.Down) --votes else ++votes
        return handle.createUpdate("UPDATE $COURSE_STAGE_TABLE SET $COURSE_VOTES = :votes WHERE $COURSE_STAGE_ID = :courseId")
                .bind("votes", votes)
                .bind("courseId", stageId)
                .execute()
    }

    @SqlUpdate(
            "DELETE FROM $COURSE_VERSION_TABLE " +
                    "WHERE $COURSE_ID = :courseId " +
                    "AND $COURSE_VERSION = :version"
    )
    override fun deleteVersionOfCourse(courseId: Int, version: Int): Int

    @SqlUpdate(
            "INSERT INTO $COURSE_REPORT_TABLE ( " +
                    "$COURSE_ID, " +
                    "$COURSE_FULL_NAME, " +
                    "$COURSE_SHORT_NAME, " +
                    "$COURSE_REPORTED_BY, " +
                    "$COURSE_VOTES, " +
                    "$COURSE_TIMESTAMP " +
                    ") " +
                    "VALUES(:courseId, :courseReport.fullName, :courseReport.shortName, " +
                    ":courseReport.reportedBy, :courseReport.votes, :courseReport.timestamp)"
    )
    @GetGeneratedKeys
    override fun reportCourse(courseId: Int, courseReport: CourseReport): CourseReport

    @SqlUpdate(
            "DELETE FROM $COURSE_REPORT_TABLE " +
                    "WHERE $COURSE_REPORT_ID = :reportId"
    )
    override fun deleteReportOnCourse(reportId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_REPORT_TABLE " +
                    "WHERE $COURSE_ID = :courseId"
    )
    override fun deleteAllReportsOnCourse(courseId: Int): Int

    @SqlQuery(
            "SELECT * FROM $COURSE_REPORT_TABLE " +
                    "WHERE $COURSE_ID = :courseId"
    )
    override fun getAllReportsOnCourse(courseId: Int): List<CourseReport>

    @SqlQuery(
            "SELECT * FROM $COURSE_REPORT_TABLE " +
                    "WHERE $COURSE_ID = :courseId " +
                    "AND $COURSE_REPORT_ID = :reportId"
    )
    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int): Optional<CourseReport>

    @SqlQuery(
            "SELECT * FROM $COURSE_STAGE_TABLE"
    )
    override fun getAllCourseStageEntries(): List<CourseStage>

    @SqlQuery(
            "SELECT * FROM $COURSE_STAGE_TABLE " +
                    "WHERE $COURSE_STAGE_ID = :courseStageId"
    )
    override fun getCourseSpecificStageEntry(courseStageId: Int): Optional<CourseStage>

    @SqlQuery(
            "SELECT T.$TERM_ID, " +
                    "T.$TERM_SHORT_NAME, " +
                    "T.$TERM_YEAR, " +
                    "T.$TERM_TYPE, " +
                    "T.$TERM_TIMESTAMP " +
                    "FROM $TERM_TABLE AS T " +
                    "INNER JOIN $COURSE_TERM_TABLE AS C " +
                    "ON T.$TERM_ID = C.$COURSE_TERM_TERM_ID " +
                    "WHERE C.$COURSE_ID = :courseId"
    )
    override fun getTermsOfCourse(courseId: Int): List<Term>

    @SqlQuery(
            "SELECT T.$TERM_ID, " +
                    "T.$TERM_SHORT_NAME, " +
                    "T.$TERM_YEAR, " +
                    "T.$TERM_TYPE " +
                    "FROM $TERM_TABLE AS T " +
                    "INNER JOIN $COURSE_TERM_TABLE AS C " +
                    "ON T.$TERM_ID = C.$TERM_ID " +
                    "WHERE C.$COURSE_TERM_COURSE_ID = :courseId " +
                    "AND C.$COURSE_TERM_TERM_ID = :termId"
    )
    override fun getSpecificTermOfCourse(courseId: Int, termId: Int): Optional<Term>

    override fun voteOnReportOfCourse(reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery("SELECT $COURSE_VOTES FROM $COURSE_REPORT_TABLE WHERE $COURSE_REPORT_ID = :reportId")
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate("UPDATE $COURSE_REPORT_TABLE SET $COURSE_VOTES = :votes WHERE $COURSE_REPORT_ID = :reportId")
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    @SqlQuery(
            "SELECT cp.$COURSE_PROGRAMME_COURSE_ID, " +
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
                    "FROM $COURSE_TABLE AS c " +
                    "INNER JOIN $COURSE_PROGRAMME_TABLE AS cp " +
                    "ON c.$COURSE_ID = cp.$COURSE_PROGRAMME_COURSE_ID " +
                    "WHERE cp.$COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
    )
    override fun getAllCoursesOnSpecificProgramme(programmeId: Int): List<Course>

    @SqlQuery(
            "SELECT cp.$COURSE_PROGRAMME_COURSE_ID, " +
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
                    "FROM $COURSE_TABLE AS c " +
                    "INNER JOIN $COURSE_PROGRAMME_TABLE AS cp " +
                    "ON c.$COURSE_ID = cp.$COURSE_PROGRAMME_COURSE_ID " +
                    "WHERE cp.$COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                    "AND cp.$COURSE_PROGRAMME_COURSE_ID = :courseId"
    )
    override fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Optional<Course>

    @SqlQuery(
            "INSERT INTO $COURSE_PROGRAMME_TABLE ( " +
                    "$COURSE_PROGRAMME_COURSE_ID, " +
                    "$COURSE_PROGRAMME_PROGRAMME_ID, " +
                    "$COURSE_PROGRAMME_LECTURED_TERM, " +
                    "$COURSE_PROGRAMME_OPTIONAL, " +
                    "$COURSE_PROGRAMME_CREDITS, " +
                    "$COURSE_PROGRAMME_TIMESTAMP, " +
                    "$COURSE_PROGRAMME_CREATED_BY " +
                    ") " +
                    "VALUES (:course.courseId, :programmeId, :course.lecturedTerm, " +
                    ":course.optional, :course.credits, :course.timestamp, :course.createdBy)"
    )
    @GetGeneratedKeys
    override fun addCourseToProgramme(programmeId: Int, course: Course): Course

    @SqlUpdate(
            "INSERT INTO $COURSE_PROGRAMME_REPORT_TABLE ( " +
                    "$COURSE_PROGRAMME_COURSE_ID, " +
                    "$COURSE_PROGRAMME_PROGRAMME_ID, " +
                    "$COURSE_PROGRAMME_LECTURED_TERM, " +
                    "$COURSE_PROGRAMME_OPTIONAL, " +
                    "$COURSE_PROGRAMME_CREDITS, " +
                    "$COURSE_PROGRAMME_TIMESTAMP, " +
                    "$COURSE_PROGRAMME_REPORTED_BY, " +
                    "$COURSE_PROGRAMME_VOTES " +
                    ") " +
                    "VALUES (:courseId, :programmeId, :courseProgrammeReport.lecturedTerm, " +
                    ":courseProgrammeReport.optional, :courseProgrammeReport.credits, :courseProgrammeReport.timestamp, " +
                    ":courseProgrammeReport.reportedBy, :courseProgrammeReport.votes)"
    )
    @GetGeneratedKeys
    override fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, courseProgrammeReport: CourseProgrammeReport): CourseProgrammeReport

    @SqlUpdate(
            "DELETE FROM $COURSE_VERSION_TABLE WHERE $COURSE_ID = :courseId"
    )
    override fun deleteAllVersionsOfCourse(courseId: Int): Int

    @SqlQuery(
            "SELECT * FROM $COURSE_VERSION_TABLE WHERE $COURSE_ID = :courseId"
    )
    override fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion>

    @SqlQuery(
            "SELECT * FROM $COURSE_VERSION_TABLE " +
                    "WHERE $COURSE_ID = :courseId " +
                    "AND $COURSE_VERSION = :version"
    )
    override fun getVersionOfSpecificCourse(courseId: Int, version: Int): Optional<CourseVersion>

    @SqlUpdate(
            "INSERT INTO $COURSE_VERSION_TABLE ( " +
                    "$COURSE_ID, " +
                    "$COURSE_ORGANIZATION_ID, " +
                    "$COURSE_VERSION, " +
                    "$COURSE_FULL_NAME, " +
                    "$COURSE_SHORT_NAME, " +
                    "$COURSE_CREATED_BY, " +
                    "$COURSE_TIMESTAMP " +
                    ") " +
                    "VALUES (:courseVersion.courseId, :courseVersion.orgId, :courseVersion.version, " +
                    ":courseVersion.fullName, :courseVersion.shortName, " +
                    ":courseVersion.createdBy, :courseVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createCourseVersion(courseVersion: CourseVersion): CourseVersion

    override fun voteOnCourseProgramme(programmeId: Int, courseId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "SELECT $COURSE_VOTES FROM $COURSE_PROGRAMME_TABLE " +
                        "WHERE $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                        "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
        )
                .bind("courseId", courseId)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "UPDATE $COURSE_PROGRAMME_TABLE SET " +
                        "$COURSE_PROGRAMME_VOTES = :votes " +
                        "WHERE $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                        "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
        )
                .bind("votes", votes)
                .bind("courseId", courseId)
                .bind("programmeId", programmeId)
                .execute()
    }

    @SqlUpdate(
            "UPDATE $COURSE_PROGRAMME_TABLE SET " +
                    "$COURSE_PROGRAMME_VERSION = :version, " +
                    "$COURSE_PROGRAMME_CREATED_BY = :createdBy, " +
                    "$COURSE_PROGRAMME_LECTURED_TERM = :lecturedTerm, " +
                    "$COURSE_PROGRAMME_OPTIONAL = :optional, " +
                    "$COURSE_PROGRAMME_VOTES = :votes, " +
                    "$COURSE_PROGRAMME_TIMESTAMP = :timestamp, " +
                    "$COURSE_PROGRAMME_CREDITS = :credits " +
                    "WHERE $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                    "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
    )
    @GetGeneratedKeys
    override fun updateCourseProgramme(programmeId: Int, courseId: Int, course: Course): Course

    @SqlUpdate(
            "INSERT INTO $COURSE_PROGRAMME_STAGE_TABLE ( " +
                    "$COURSE_PROGRAMME_COURSE_ID, " +
                    "$COURSE_PROGRAMME_PROGRAMME_ID, " +
                    "$COURSE_PROGRAMME_LECTURED_TERM, " +
                    "$COURSE_PROGRAMME_OPTIONAL, " +
                    "$COURSE_PROGRAMME_CREATED_BY, " +
                    "$COURSE_PROGRAMME_CREDITS, " +
                    "$COURSE_PROGRAMME_VOTES, " +
                    "$COURSE_PROGRAMME_TIMESTAMP " +
                    ") " +
                    "VALUES(:courseProgrammeStage.courseId, :courseProgrammeStage.programmeId, " +
                    ":courseProgrammeStage.lecturedTerm, :courseProgrammeStage.optional, " +
                    ":courseProgrammeStage.credits, :courseProgrammeStage.votes, " +
                    ":courseProgrammeStage.createdBy, :courseProgrammeStage.timestamp)"
    )
    @GetGeneratedKeys
    override fun createStagingCourseOfProgramme(courseProgrammeStage: CourseProgrammeStage): CourseProgrammeStage

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_STAGE_TABLE " +
                    "WHERE $COURSE_PROGRAMME_STAGE_ID = :stageId" +
                    "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
    )
    override fun getSpecificStagedCourseProgramme(programmeId: Int, stageId: Int): Optional<CourseProgrammeStage>

    @SqlUpdate(
            "DELETE FROM $COURSE_PROGRAMME_STAGE_TABLE " +
                    "WHERE $COURSE_PROGRAMME_STAGE_ID = :stageId"
    )
    override fun deleteStagedCourseProgramme(stageId: Int): Int

    override fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "SELECT $COURSE_VOTES FROM $COURSE_PROGRAMME_STAGE_TABLE " +
                        "WHERE $COURSE_PROGRAMME_STAGE_ID = :stageId " +
                        "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
        )
                .bind("stageId", stageId)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "UPDATE $COURSE_PROGRAMME_STAGE_TABLE " +
                        "SET $COURSE_PROGRAMME_VOTES = :votes " +
                        "WHERE $COURSE_PROGRAMME_STAGE_ID = :stageId " +
                        "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
        )
                .bind("votes", votes)
                .bind("stageId", stageId)
                .bind("programmeId", programmeId)
                .execute()
    }

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_VERSION_TABLE " +
                    "WHERE $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_COURSE_ID = :courseId"
    )
    override fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeVersion>

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_VERSION_TABLE " +
                    "WHERE $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                    "AND $COURSE_PROGRAMME_VERSION = :version"
    )
    override fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): Optional<CourseProgrammeVersion>

    @SqlQuery(
            "INSERT INTO $COURSE_PROGRAMME_VERSION_TABLE (" +
                    "$COURSE_PROGRAMME_COURSE_ID, " +
                    "$COURSE_PROGRAMME_PROGRAMME_ID, " +
                    "$COURSE_PROGRAMME_VERSION, " +
                    "$COURSE_PROGRAMME_LECTURED_TERM, " +
                    "$COURSE_PROGRAMME_OPTIONAL, " +
                    "$COURSE_PROGRAMME_CREDITS, " +
                    "$COURSE_PROGRAMME_CREATED_BY, " +
                    "$COURSE_PROGRAMME_TIMESTAMP," +
                    ")" +
                    "VALUES (:courseProgrammeVersion.courseId, :courseProgrammeVersion.progId, :courseProgrammeVersion.version, " +
                    ":courseProgrammeVersion.lectured, :courseProgrammeVersion.optional, :courseProgrammeVersion.credits, " +
                    ":courseProgrammeVersion.createdBy, :courseProgrammeVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createCourseProgrammeVersion(courseProgrammeVersion: CourseProgrammeVersion): CourseProgrammeVersion

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_REPORT_TABLE " +
                    "WHERE $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_COURSE_ID = :courseId"
    )
    override fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport>

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_REPORT_TABLE " +
                    "WHERE $COURSE_PROGRAMME_REPORT_ID= :reportId " +
                    "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_COURSE_ID = :courseId"
    )
    override fun getSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Optional<CourseProgrammeReport>

    @SqlUpdate(
            "DELETE FROM $COURSE_PROGRAMME_REPORT_TABLE " +
                    "WHERE $COURSE_PROGRAMME_REPORT_ID = :reportId" +
                    "AND $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                    "AND $COURSE_PROGRAMME_PROGRAMME_ID = programmeId"
    )
    override fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    override fun voteOnReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "SELECT $COURSE_PROGRAMME_VOTES FROM $COURSE_PROGRAMME_REPORT_TABLE " +
                        "WHERE $COURSE_PROGRAMME_REPORT_ID = :reportId " +
                        "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                        "AND $COURSE_PROGRAMME_COURSE_ID = :courseId"
        )
                .bind("reportId", reportId)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "UPDATE $COURSE_PROGRAMME_REPORT_TABLE " +
                        "SET $COURSE_PROGRAMME_VOTES = :votes " +
                        "WHERE $COURSE_PROGRAMME_REPORT_ID = :reportId " +
                        "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                        "AND $COURSE_PROGRAMME_COURSE_ID = :courseId"
        )
                .bind("votes", votes)
                .bind("reportId", reportId)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .execute()
    }

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_STAGE_TABLE " +
                    "WHERE $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
    )
    override fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): List<CourseProgrammeStage>

    @SqlUpdate(
            "DELETE FROM $COURSE_PROGRAMME_TABLE " +
                    "WHERE $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_COURSE_ID = :courseId"
    )
    override fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_PROGRAMME_STAGE_TABLE " +
                    "WHERE $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_COURSE_ID = :courseId" +
                    "AND $COURSE_PROGRAMME_STAGE_ID = :stageId"
    )
    override fun deleteSpecificStagedCourseProgramme(programmeId: Int, courseId: Int, stageId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_PROGRAMME_VERSION_TABLE " +
                    "WHERE $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                    "AND $COURSE_PROGRAMME_VERSION = :version"
    )
    override fun deleteSpecificVersionOfCourseProgramme(programmeId: Int, courseId: Int, version: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_PROGRAMME_REPORT_TABLE " +
                    "WHERE $COURSE_PROGRAMME_REPORT_ID = :reportId " +
                    "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_COURSE_ID = :courseId"
    )
    override fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

}

