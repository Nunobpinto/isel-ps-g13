package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.enums.CourseMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseMiscUnitStage
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
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.sql.Timestamp
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
        const val COURSE_ID = "course_id"
        const val COURSE_ORGANIZATION_ID = "organization_id"
        const val COURSE_LOG_ID = "log_id"
        const val COURSE_VERSION = "course_version"
        const val COURSE_FULL_NAME = "course_full_name"
        const val COURSE_SHORT_NAME = "course_short_name"
        const val COURSE_VOTES = "votes"
        const val COURSE_TIMESTAMP = "time_stamp"
        const val COURSE_CREATED_BY = "created_by"

        // COURSE REPORT FIELDS
        const val COURSE_REPORT_ID = "course_report_id"
        const val COURSE_REPORT_COURSE_ID = "course_id"
        const val COURSE_REPORT_LOG_ID = "log_id"
        const val COURSE_REPORTED_BY = "reported_by"
        const val COURSE_REPORT_FULL_NAME = "course_full_name"
        const val COURSE_REPORT_SHORT_NAME = "course_short_name"
        const val COURSE_REPORT_VOTES = "votes"
        const val COURSE_REPORT_TIMESTAMP = "time_stamp"

        // COURSE STAGE FIELDS
        const val COURSE_STAGE_ID = "course_stage_id"
        const val COURSE_STAGE_ORG_ID = "organization_id"
        const val COURSE_STAGE_LOG_ID = "log_id"
        const val COURSE_STAGE_CREATED_BY = "created_by"
        const val COURSE_STAGE_FULL_NAME = "course_full_name"
        const val COURSE_STAGE_SHORT_NAME = "course_short_name"
        const val COURSE_STAGE_VOTES = "votes"
        const val COURSE_STAGE_TIMESTAMP = "time_stamp"

        // COURSE VERSION FIELDS
        const val COURSE_VERSION_COURSE_ID = "course_id"
        const val COURSE_VERSION_ID = "course_version"
        const val COURSE_VERSION_ORG_ID = "organization_id"
        const val COURSE_VERSION_CREATED_BY = "created_by"
        const val COURSE_VERSION_FULL_NAME = "course_full_name"
        const val COURSE_VERSION_SHORT_NAME = "course_short_name"
        const val COURSE_VERSION_TIMESTAMP = "time_stamp"

        // COURSE_PROGRAMME FIELDS
        const val COURSE_PROGRAMME_COURSE_ID = "course_id"
        const val COURSE_PROGRAMME_PROGRAMME_ID = "programme_id"
        const val COURSE_PROGRAMME_VERSION = "course_programme_version"
        const val COURSE_PROGRAMME_LECTURED_TERM = "course_lectured_term"
        const val COURSE_PROGRAMME_OPTIONAL = "course_optional"
        const val COURSE_PROGRAMME_CREDITS = "course_credits"
        const val COURSE_PROGRAMME_TIMESTAMP = "time_stamp"
        const val COURSE_PROGRAMME_CREATED_BY = "created_by"
        const val COURSE_PROGRAMME_VOTES = "votes"
        const val COURSE_PROGRAMME_LOG_ID = "log_id"

        // COURSE_PROGRAMME REPORT FIELDS
        const val COURSE_PROGRAMME_REPORT_ID = "course_programme_report_id"
        const val COURSE_PROGRAMME_REPORT_COURSE_ID = "course_id"
        const val COURSE_PROGRAMME_REPORT_PROGRAMME_ID = "programme_id"
        const val COURSE_PROGRAMME_REPORT_LECTURED_TERM = "course_lectured_term"
        const val COURSE_PROGRAMME_REPORT_OPTIONAL = "course_optional"
        const val COURSE_PROGRAMME_REPORT_CREDITS = "course_credits"
        const val COURSE_PROGRAMME_REPORT_DELETE_FLAG = "to_delete"
        const val COURSE_PROGRAMME_REPORT_TIMESTAMP = "time_stamp"
        const val COURSE_PROGRAMME_REPORTED_BY = "reported_by"
        const val COURSE_PROGRAMME_REPORT_VOTES = "votes"
        const val COURSE_PROGRAMME_REPORT_LOG_ID = "log_id"

        // COURSE_PROGRAMME STAGE FIELDS
        const val COURSE_PROGRAMME_STAGE_ID = "course_programme_stage_id"
        const val COURSE_PROGRAMME_STAGE_COURSE_ID = "course_id"
        const val COURSE_PROGRAMME_STAGE_PROGRAMME_ID = "programme_id"
        const val COURSE_PROGRAMME_STAGE_LECTURED_TERM = "course_lectured_term"
        const val COURSE_PROGRAMME_STAGE_OPTIONAL = "course_optional"
        const val COURSE_PROGRAMME_STAGE_CREDITS = "course_credits"
        const val COURSE_PROGRAMME_STAGE_CREATED_BY = "created_by"
        const val COURSE_PROGRAMME_STAGE_VOTES = "votes"
        const val COURSE_PROGRAMME_STAGE_TIMESTAMP = "time_stamp"
        const val COURSE_PROGRAMME_STAGE_LOG_ID = "log_id"

        // COURSE_PROGRAMME VERSION FIELDS
        const val COURSE_PROGRAMME_VERSION_COURSE_ID = "course_id"
        const val COURSE_PROGRAMME_VERSION_PROGRAMME_ID = "programme_id"
        const val COURSE_PROGRAMME_VERSION_ID = "course_programme_version"
        const val COURSE_PROGRAMME_VERSION_LECTURED_TERM = "course_lectured_term"
        const val COURSE_PROGRAMME_VERSION_OPTIONAL = "course_optional"
        const val COURSE_PROGRAMME_VERSION_CREDITS = "course_credits"
        const val COURSE_PROGRAMME_VERSION_TIMESTAMP = "time_stamp"
        const val COURSE_PROGRAMME_VERSION_CREATED_BY = "created_by"

        // COURSE_TERM
        const val COURSE_TERM_TERM_ID = "term_id"
        const val COURSE_TERM_COURSE_ID = "course_id"
        const val COURSE_TERM_TIMESTAMP = "time_stamp"

        // COURSE_MISC_UNIT FIELDS
        const val COURSE_MISC_UNIT_ID = "course_misc_unit_id"
        const val COURSE_MISC_UNIT_TYPE = "misc_type"
        const val COURSE_MISC_UNIT_COURSE_ID = "course_id"
        const val COURSE_MISC_UNIT_TERM_ID = "term_id"

        // COURSE_MISC_UNIT_STAGE_TABLE FIELDS
        const val COURSE_MISC_UNIT_STAGE_ID = "course_misc_unit_stage_id"
        const val COURSE_MISC_UNIT_STAGE_TYPE = "misc_type"
        const val COURSE_MISC_UNIT_STAGE_COURSE_ID = "course_id"
        const val COURSE_MISC_UNIT_STAGE_TERM_ID = "term_id"
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

    @SqlUpdate(
            "UPDATE $COURSE_TABLE SET $COURSE_VOTES = :votes " +
                    "WHERE $COURSE_ID = :courseId"
    )
    override fun updateVotesOnCourse(courseId: Int, votes: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_STAGE_TABLE WHERE $COURSE_STAGE_ID = :courseStageId"
    )
    override fun deleteStagedCourse(courseStageId: Int): Int

    @SqlUpdate(
            "INSERT INTO $COURSE_STAGE_TABLE ( " +
                    "$COURSE_STAGE_ORG_ID, " +
                    "$COURSE_STAGE_FULL_NAME, " +
                    "$COURSE_STAGE_SHORT_NAME, " +
                    "$COURSE_STAGE_CREATED_BY, " +
                    "$COURSE_STAGE_VOTES, " +
                    "$COURSE_STAGE_TIMESTAMP " +
                    ") " +
                    "VALUES(:courseStage.organizationId, :courseStage.fullName, :courseStage.shortName, " +
                    ":courseStage.createdBy, :courseStage.votes, :courseStage.timestamp)"
    )
    @GetGeneratedKeys
    override fun createStagingCourse(courseStage: CourseStage): CourseStage

    @SqlUpdate(
            "UPDATE $COURSE_STAGE_TABLE SET $COURSE_STAGE_VOTES = :votes " +
                    "WHERE $COURSE_STAGE_ID = :stageId"
    )
    override fun updateVotesOnStagedCourse(stageId: Int, votes: Int): Int

    @SqlUpdate(
            "INSERT INTO $COURSE_REPORT_TABLE ( " +
                    "$COURSE_REPORT_COURSE_ID, " +
                    "$COURSE_REPORT_FULL_NAME, " +
                    "$COURSE_REPORT_SHORT_NAME, " +
                    "$COURSE_REPORTED_BY, " +
                    "$COURSE_REPORT_VOTES, " +
                    "$COURSE_REPORT_TIMESTAMP " +
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

    @SqlQuery(
            "SELECT * FROM $COURSE_REPORT_TABLE " +
                    "WHERE $COURSE_REPORT_COURSE_ID = :courseId"
    )
    override fun getAllReportsOnCourse(courseId: Int): List<CourseReport>

    @SqlQuery(
            "SELECT * FROM $COURSE_REPORT_TABLE " +
                    "WHERE $COURSE_REPORT_COURSE_ID = :courseId " +
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

    @SqlUpdate(
            "UPDATE $COURSE_REPORT_TABLE SET $COURSE_REPORT_VOTES = :votes " +
                    "WHERE $COURSE_REPORT_ID = :reportId"
    )
    override fun updateVotesOnReportedCourse(reportId: Int, votes: Int): Int

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_TABLE " +
                    "WHERE $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
    )
    override fun getAllCoursesOnSpecificProgramme(programmeId: Int): List<CourseProgramme>

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_TABLE " +
                    "WHERE $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_COURSE_ID = :courseId"
    )
    override fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Optional<CourseProgramme>

    @SqlUpdate(
            "INSERT INTO $COURSE_PROGRAMME_TABLE ( " +
                    "$COURSE_PROGRAMME_COURSE_ID, " +
                    "$COURSE_PROGRAMME_PROGRAMME_ID, " +
                    "$COURSE_PROGRAMME_LECTURED_TERM, " +
                    "$COURSE_PROGRAMME_OPTIONAL, " +
                    "$COURSE_PROGRAMME_CREDITS, " +
                    "$COURSE_PROGRAMME_TIMESTAMP, " +
                    "$COURSE_PROGRAMME_CREATED_BY) " +
                    "VALUES (:courseProgramme.courseId, :programmeId, :courseProgramme.lecturedTerm, " +
                    ":courseProgramme.optional, :courseProgramme.credits, :courseProgramme.timestamp, :courseProgramme.createdBy)"
    )
    @GetGeneratedKeys
    override fun addCourseToProgramme(programmeId: Int, courseProgramme: CourseProgramme): CourseProgramme

    @SqlUpdate(
            "INSERT INTO $COURSE_PROGRAMME_REPORT_TABLE ( " +
                    "$COURSE_PROGRAMME_REPORT_COURSE_ID, " +
                    "$COURSE_PROGRAMME_REPORT_PROGRAMME_ID, " +
                    "$COURSE_PROGRAMME_REPORT_LECTURED_TERM, " +
                    "$COURSE_PROGRAMME_REPORT_OPTIONAL, " +
                    "$COURSE_PROGRAMME_REPORT_CREDITS, " +
                    "$COURSE_PROGRAMME_REPORT_TIMESTAMP, " +
                    "$COURSE_PROGRAMME_REPORTED_BY, " +
                    "$COURSE_PROGRAMME_REPORT_DELETE_FLAG, " +
                    "$COURSE_PROGRAMME_REPORT_VOTES " +
                    ") " +
                    "VALUES (:courseId, :programmeId, :courseProgrammeReport.lecturedTerm, " +
                    ":courseProgrammeReport.optional, :courseProgrammeReport.credits, :courseProgrammeReport.timestamp, " +
                    ":courseProgrammeReport.reportedBy, :courseProgrammeReport.deleteFlag, " +
                    ":courseProgrammeReport.votes)"
    )
    @GetGeneratedKeys
    override fun reportSpecificCourseOnProgramme(
            programmeId: Int,
            courseId: Int,
            courseProgrammeReport: CourseProgrammeReport
    ): CourseProgrammeReport

    @SqlQuery(
            "SELECT * FROM $COURSE_VERSION_TABLE WHERE $COURSE_VERSION_COURSE_ID = :courseId"
    )
    override fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion>

    @SqlQuery(
            "SELECT * FROM $COURSE_VERSION_TABLE " +
                    "WHERE $COURSE_VERSION_COURSE_ID = :courseId " +
                    "AND $COURSE_VERSION_ID = :version"
    )
    override fun getVersionOfSpecificCourse(courseId: Int, version: Int): Optional<CourseVersion>

    @SqlUpdate(
            "INSERT INTO $COURSE_VERSION_TABLE ( " +
                    "$COURSE_VERSION_COURSE_ID, " +
                    "$COURSE_VERSION_ORG_ID, " +
                    "$COURSE_VERSION_ID, " +
                    "$COURSE_VERSION_FULL_NAME, " +
                    "$COURSE_VERSION_SHORT_NAME, " +
                    "$COURSE_VERSION_CREATED_BY, " +
                    "$COURSE_VERSION_TIMESTAMP " +
                    ") " +
                    "VALUES (:courseVersion.courseId, :courseVersion.orgId, :courseVersion.version, " +
                    ":courseVersion.fullName, :courseVersion.shortName, " +
                    ":courseVersion.createdBy, :courseVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createCourseVersion(courseVersion: CourseVersion): CourseVersion

    @SqlUpdate(
            "UPDATE $COURSE_PROGRAMME_TABLE SET " +
                    "$COURSE_PROGRAMME_VOTES = :votes " +
                    "WHERE $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                    "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
    )
    override fun updateVotesOnCourseProgramme(programmeId: Int, courseId: Int, votes: Int) : Int

    @SqlUpdate(
            "UPDATE $COURSE_PROGRAMME_TABLE SET " +
                    "$COURSE_PROGRAMME_VERSION = :course.version, " +
                    "$COURSE_PROGRAMME_CREATED_BY = :course.createdBy, " +
                    "$COURSE_PROGRAMME_LECTURED_TERM = :course.lecturedTerm, " +
                    "$COURSE_PROGRAMME_OPTIONAL = :course.optional, " +
                    "$COURSE_PROGRAMME_VOTES = :course.votes, " +
                    "$COURSE_PROGRAMME_TIMESTAMP = :course.timestamp, " +
                    "$COURSE_PROGRAMME_CREDITS = :course.credits " +
                    "WHERE $COURSE_PROGRAMME_COURSE_ID = :courseId " +
                    "AND $COURSE_PROGRAMME_PROGRAMME_ID = :programmeId"
    )
    @GetGeneratedKeys
    override fun updateCourseProgramme(programmeId: Int, courseId: Int, course: CourseProgramme): CourseProgramme

    @SqlUpdate(
            "INSERT INTO $COURSE_PROGRAMME_STAGE_TABLE ( " +
                    "$COURSE_PROGRAMME_STAGE_COURSE_ID, " +
                    "$COURSE_PROGRAMME_STAGE_PROGRAMME_ID, " +
                    "$COURSE_PROGRAMME_STAGE_LECTURED_TERM, " +
                    "$COURSE_PROGRAMME_STAGE_OPTIONAL, " +
                    "$COURSE_PROGRAMME_STAGE_CREATED_BY, " +
                    "$COURSE_PROGRAMME_STAGE_CREDITS, " +
                    "$COURSE_PROGRAMME_STAGE_VOTES, " +
                    "$COURSE_PROGRAMME_STAGE_TIMESTAMP " +
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
                    "AND $COURSE_PROGRAMME_STAGE_PROGRAMME_ID = :programmeId"
    )
    override fun getSpecificStagedCourseProgramme(programmeId: Int, stageId: Int): Optional<CourseProgrammeStage>

    @SqlUpdate(
            "DELETE FROM $COURSE_PROGRAMME_STAGE_TABLE " +
                    "WHERE $COURSE_PROGRAMME_STAGE_ID = :stageId"
    )
    override fun deleteStagedCourseProgramme(stageId: Int): Int

    @SqlUpdate(
            "UPDATE $COURSE_PROGRAMME_STAGE_TABLE " +
                    "SET $COURSE_PROGRAMME_STAGE_VOTES = :votes " +
                    "WHERE $COURSE_PROGRAMME_STAGE_ID = :stageId " +
                    "AND $COURSE_PROGRAMME_STAGE_PROGRAMME_ID = :programmeId"
    )
    override fun updateVotesOnStagedCourseProgramme(programmeId: Int, stageId: Int, votes: Int): Int

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_VERSION_TABLE " +
                    "WHERE $COURSE_PROGRAMME_VERSION_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_VERSION_COURSE_ID = :courseId"
    )
    override fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeVersion>

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_VERSION_TABLE " +
                    "WHERE $COURSE_PROGRAMME_VERSION_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_VERSION_COURSE_ID = :courseId " +
                    "AND $COURSE_PROGRAMME_VERSION_ID = :version"
    )
    override fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): Optional<CourseProgrammeVersion>

    @SqlUpdate(
            "INSERT INTO $COURSE_PROGRAMME_VERSION_TABLE (" +
                    "$COURSE_PROGRAMME_VERSION_COURSE_ID, " +
                    "$COURSE_PROGRAMME_VERSION_PROGRAMME_ID, " +
                    "$COURSE_PROGRAMME_VERSION_ID, " +
                    "$COURSE_PROGRAMME_VERSION_LECTURED_TERM, " +
                    "$COURSE_PROGRAMME_VERSION_OPTIONAL, " +
                    "$COURSE_PROGRAMME_VERSION_CREDITS, " +
                    "$COURSE_PROGRAMME_VERSION_CREATED_BY, " +
                    COURSE_PROGRAMME_VERSION_TIMESTAMP +
                    ")" +
                    "VALUES (:courseProgrammeVersion.courseId, :courseProgrammeVersion.programmeId, :courseProgrammeVersion.version, " +
                    ":courseProgrammeVersion.lecturedTerm, :courseProgrammeVersion.optional, :courseProgrammeVersion.credits, " +
                    ":courseProgrammeVersion.createdBy, :courseProgrammeVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createCourseProgrammeVersion(courseProgrammeVersion: CourseProgrammeVersion): CourseProgrammeVersion

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_REPORT_TABLE " +
                    "WHERE $COURSE_PROGRAMME_REPORT_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_REPORT_COURSE_ID = :courseId"
    )
    override fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport>

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_REPORT_TABLE " +
                    "WHERE $COURSE_PROGRAMME_REPORT_ID= :reportId " +
                    "AND $COURSE_PROGRAMME_REPORT_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_REPORT_COURSE_ID = :courseId"
    )
    override fun getSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Optional<CourseProgrammeReport>

    @SqlUpdate(
            "DELETE FROM $COURSE_PROGRAMME_REPORT_TABLE " +
                    "WHERE $COURSE_PROGRAMME_REPORT_ID = :reportId " +
                    "AND $COURSE_PROGRAMME_REPORT_COURSE_ID = :courseId " +
                    "AND $COURSE_PROGRAMME_REPORT_PROGRAMME_ID = :programmeId"
    )
    override fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    @SqlUpdate(
            "UPDATE $COURSE_PROGRAMME_REPORT_TABLE " +
                    "SET $COURSE_PROGRAMME_REPORT_VOTES = :votes " +
                    "WHERE $COURSE_PROGRAMME_REPORT_ID = :reportId " +
                    "AND $COURSE_PROGRAMME_REPORT_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_REPORT_COURSE_ID = :courseId"
    )
    override fun updateVotesOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, votes: Int): Int

    @SqlQuery(
            "SELECT * FROM $COURSE_PROGRAMME_STAGE_TABLE " +
                    "WHERE $COURSE_PROGRAMME_STAGE_PROGRAMME_ID = :programmeId"
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
                    "WHERE $COURSE_PROGRAMME_STAGE_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_STAGE_ID = :stageId"
    )
    override fun deleteSpecificStagedCourseProgramme(programmeId: Int, stageId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_PROGRAMME_REPORT_TABLE " +
                    "WHERE $COURSE_PROGRAMME_REPORT_ID = :reportId " +
                    "AND $COURSE_PROGRAMME_REPORT_PROGRAMME_ID = :programmeId " +
                    "AND $COURSE_PROGRAMME_REPORT_COURSE_ID = :courseId"
    )
    override fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    @SqlUpdate(
            "INSERT INTO $COURSE_MISC_UNIT_TABLE ( " +
                    "$COURSE_MISC_UNIT_TYPE, " +
                    "$COURSE_MISC_UNIT_COURSE_ID, " +
                    "$COURSE_MISC_UNIT_TERM_ID " +
                    ") " +
                    "values(:miscType, :courseId, :termId)"
    )
    @GetGeneratedKeys
    override fun createCourseMiscUnit(courseId: Int, termId: Int, miscType: CourseMiscUnitType): CourseMiscUnit

    @SqlUpdate(
            "INSERT INTO $COURSE_TERM_TABLE ( " +
                    "$COURSE_TERM_COURSE_ID, " +
                    "$COURSE_TERM_TERM_ID, " +
                    "$COURSE_TERM_TIMESTAMP " +
                    ") " +
                    "values(:courseId, :termId, :timestamp)"
    )
    @GetGeneratedKeys
    override fun createCourseTerm(courseId: Int, termId: Int, timestamp: Timestamp): CourseTerm

    @SqlUpdate(
            "INSERT INTO $COURSE_MISC_UNIT_STAGE_TABLE ( " +
                    "$COURSE_MISC_UNIT_STAGE_COURSE_ID, " +
                    "$COURSE_MISC_UNIT_STAGE_TERM_ID, " +
                    "$COURSE_MISC_UNIT_STAGE_TYPE " +
                    ") " +
                    "values(:courseId, :termId, :miscType)"
    )
    @GetGeneratedKeys
    override fun createStagingCourseMiscUnit(courseId: Int, termId: Int, miscType: CourseMiscUnitType): CourseMiscUnitStage

    @SqlUpdate(
            "DELETE FROM $COURSE_MISC_UNIT_TABLE " +
                    "WHERE $COURSE_MISC_UNIT_ID = :courseMiscUnitId " +
                    "AND $COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND $COURSE_MISC_UNIT_TERM_ID = :termId"
    )
    override fun deleteSpecificCourseMiscUnitEntry(courseId: Int, termId: Int, courseMiscUnitId: Int): Int


    @SqlUpdate(
            "DELETE FROM $COURSE_MISC_UNIT_TABLE " +
                    "WHERE $COURSE_MISC_UNIT_COURSE_ID = :courseId " +
                    "AND $COURSE_MISC_UNIT_TERM_ID = :termId " +
                    "AND $COURSE_MISC_UNIT_TYPE = :miscType"
    )
    override fun deleteAllCourseMiscUnitsFromTypeOfCourseInTerm(courseId: Int, termId: Int, miscType: CourseMiscUnitType): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_MISC_UNIT_STAGE_TABLE " +
                    "WHERE $COURSE_MISC_UNIT_STAGE_COURSE_ID = :stageId " +
                    "WHERE $COURSE_MISC_UNIT_STAGE_COURSE_ID = :courseId " +
                    "AND $COURSE_MISC_UNIT_STAGE_TERM_ID = :termId "
    )
    override fun deleteSpecificStagedCourseMiscUnitEntry(courseId: Int, termId: Int, stageId: Int): Int

}