package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import org.jdbi.v3.core.Jdbi
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
        const val COURSE_CLASS_TABLE = "course_class"
        const val COURSE_TERM_TABLE = "course_term"
        const val COURSE_MISC_UNIT_TABLE = "course_misc_unit"
        const val COURSE_MISC_UNIT_STAGE_TABLE = "course_misc_unit_stage"
        const val COURSE_MISC_TYPE = "misc_type"
        // COURSE FIELDS
        const val COURSE_MISC_UNIT_ID = "id"
        const val COURSE_ID = "course_id"
        const val COURSE_VERSION = "course_version"
        const val COURSE_FULL_NAME = "course_full_name"
        const val COURSE_SHORT_NAME = "course_short_name"
        const val COURSE_VOTES = "votes"
        const val COURSE_TIMESTAMP = "time_stamp"
        const val COURSE_REPORT_ID = "report_id"
        const val COURSE_REPORTED_BY = "reported_by"
        const val COURSE_CREATED_BY = "created_by"
        // COURSE_PROGRAMME FIELDS
        const val PROGRAMME_ID = "programme_id"
        const val LECTURED_TERM = "course_lectured_term"
        const val OPTIONAL = "course_optional"
        const val CREDITS = "course_credits"
        const val COURSE_PROGRAMME_VERSION = "course_programme_version"

    }

    @Autowired
    lateinit var dbi: Jdbi

    override fun getAllCourses(): List<Course> = dbi.withHandle<List<Course>, Exception> {
        it.createQuery("select * from $COURSE_TABLE")
                .mapTo(Course::class.java)
                .list()
    }

    override fun getSpecificCourse(courseId: Int): Optional<Course> = dbi.withHandle<Optional<Course>, Exception> {
        it.createQuery("select * from $COURSE_TABLE where $COURSE_ID = :courseId")
                .bind("courseId", courseId)
                .mapTo(Course::class.java)
                .findFirst()
    }

    override fun deleteSpecificCourse(courseId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_TABLE where $COURSE_ID = :courseId"
        it.createUpdate(delete)
                .bind("courseId", courseId)
                .execute()
    }

    override fun deleteAllCourses(): Int = dbi.withHandle<Int, Exception> {
        it.createUpdate("delete from $COURSE_TABLE").execute()
    }

    override fun updateCourse(course: Course): Int = dbi.inTransaction<Int, Exception> {
        val update = "update $COURSE_TABLE SET " +
                "${OrganizationDAOImpl.ORG_ID} = :orgId, $COURSE_VERSION = :version, " +
                "$COURSE_CREATED_BY = :createdBy, $COURSE_FULL_NAME = :fullName, " +
                "$COURSE_SHORT_NAME = :shortName, $COURSE_VOTES = :votes, " +
                "$COURSE_TIMESTAMP = :timestamp"
        "where $COURSE_ID = :courseId"

        it.createUpdate(update)
                .bind("orgId", course.organizationId)
                .bind("version", course.version)
                .bind("createdBy", course.createdBy)
                .bind("fullName", course.fullName)
                .bind("shortName", course.shortName)
                .bind("votes", course.votes)
                .bind("timestamp", course.timestamp)
                .execute()
    }

    override fun createCourse(course: Course): Int = dbi.withHandle<Int, Exception> {
        val insert = "insert into $COURSE_TABLE" +
                "(${OrganizationDAOImpl.ORG_ID}, $COURSE_VERSION, $COURSE_CREATED_BY, " +
                "$COURSE_FULL_NAME, $COURSE_SHORT_NAME, $COURSE_VOTES, $COURSE_TIMESTAMP)" +
                "values(:organizationId, :version, :createdBy, :fullName, :shortName, :votes, :timestamp)"
        it.createUpdate(insert)
                .bind("organizationId", course.organizationId)
                .bind("version", course.version)
                .bind("createdBy", course.createdBy)
                .bind("fullName", course.fullName)
                .bind("shortName", course.shortName)
                .bind("votes", course.votes)
                .bind("timestamp", course.timestamp)
                .execute()
    }

    override fun voteOnCourse(courseId: Int, vote: Vote): Int = dbi.inTransaction<Int, Exception> {
        val voteQuery = "select $COURSE_VOTES from $COURSE_TABLE where $COURSE_ID = :courseId"
        var votes = it.createQuery(voteQuery)
                .bind("courseId", courseId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $COURSE_TABLE set $COURSE_VOTES = :votes where $COURSE_ID = :courseId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("courseId", courseId)
                .execute()
    }

    override fun deleteStagedCourse(courseStageId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_STAGE_TABLE where $COURSE_ID = :courseId"
        it.createUpdate(delete)
                .bind("courseId", courseStageId)
                .execute()
    }

    override fun deleteAllStagedCourses(): Int = dbi.withHandle<Int, Exception> {
        it.createUpdate("delete from $COURSE_STAGE_TABLE").execute()
    }


    override fun createStagingCourse(courseStage: CourseStage): Int = dbi.withHandle<Int, Exception> {
        val insert = "insert into $COURSE_STAGE_TABLE " +
                "(${OrganizationDAOImpl.ORG_ID}, $COURSE_FULL_NAME" +
                "$COURSE_SHORT_NAME, $COURSE_CREATED_BY, $COURSE_VOTES,, $COURSE_TIMESTAMP) " +
                "values(:organizationId, :fullName, :shortName, :createdBy, :votes, :timestamp)"
        it.createUpdate(insert)
                .bind("organizationId", courseStage.organizationId)
                .bind("fullName", courseStage.fullName)
                .bind("shortName", courseStage.shortName)
                .bind("createdBy", courseStage.createdBy)
                .bind("votes", courseStage.votes)
                .bind("timestamp", courseStage.timestamp)
                .execute()
    }

    override fun voteOnStagedCourse(courseStageId: Int, inputVote: Vote): Int = dbi.inTransaction<Int, Exception> {
        val voteQuery = "select $COURSE_VOTES from $COURSE_STAGE_TABLE where $COURSE_ID = :courseId"
        var votes = it.createQuery(voteQuery)
                .bind("courseId", courseStageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (inputVote == Vote.Down) --votes else ++votes
        val updateQuery = "update $COURSE_STAGE_TABLE set $COURSE_VOTES = :votes where $COURSE_ID = :courseId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("courseId", courseStageId)
                .execute()
    }

    override fun deleteVersionOfCourse(courseId: Int, version: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_VERSION_TABLE where $COURSE_ID = :id and $COURSE_VERSION = :version"
        it.createUpdate(delete)
                .bind("id", courseId)
                .bind("version", version)
                .execute()
    }


    override fun reportCourse(courseId: Int, courseReport: CourseReport): Int = dbi.withHandle<Int, Exception> {
        val insert = "insert into $COURSE_REPORT_TABLE " +
                "($COURSE_ID}, $COURSE_FULL_NAME" +
                "$COURSE_SHORT_NAME, $COURSE_REPORTED_BY, $COURSE_VOTES, $COURSE_TIMESTAMP) " +
                "values(:courseId, :fullName, :shortName, :reportedBy, :votes, :timestamp)"
        it.createUpdate(insert)
                .bind("courseId", courseId)
                .bind("fullName", courseReport.courseFullName)
                .bind("shortName", courseReport.courseShortName)
                .bind("reportedBy", courseReport.reportedBy)
                .bind("votes", courseReport.votes)
                .bind("timestamp", courseReport.timestamp)
                .execute()
    }

    override fun deleteReportOnCourse(reportId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_REPORT_TABLE where $COURSE_REPORT_ID = :id"
        it.createUpdate(delete)
                .bind("id", reportId)
                .execute()
    }

    override fun deleteAllReportsOnCourse(courseId: Int): Int = dbi.withHandle<Int, Exception> {
        it.createUpdate("delete from $COURSE_REPORT_TABLE").execute()
    }

    override fun getAllReportsOnCourse(courseId: Int): List<CourseReport> = dbi.withHandle<List<CourseReport>, Exception> {
        val select = "select * from $COURSE_REPORT_TABLE where $COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .mapTo(CourseReport::class.java)
                .list()
    }

    override fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReport = dbi.withHandle<CourseReport, Exception> {
        val select = "select * from $COURSE_REPORT_TABLE " +
                "where $COURSE_ID = :courseId and $COURSE_REPORT_ID = :reportId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("reportId", reportId)
                .mapTo(CourseReport::class.java)
                .findOnly()
    }

    override fun getAllCourseStageEntries(): List<CourseStage> = dbi.withHandle<List<CourseStage>, Exception> {
        val select = "select * from $COURSE_STAGE_TABLE"
        it.createQuery(select)
                .mapTo(CourseStage::class.java)
                .list()
    }

    override fun getCourseSpecificStageEntry(courseStageId: Int): CourseStage = dbi.withHandle<CourseStage, Exception> {
        val select = "select * from $COURSE_STAGE_TABLE" +
                "where $COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("courseId", courseStageId)
                .mapTo(CourseStage::class.java)
                .findOnly()
    }

    override fun getTermsOfCourse(courseId: Int): List<Term> = dbi.withHandle<List<Term>, Exception> {
        val select = "select T.${TermDAOImpl.TERM_ID}, ${TermDAOImpl.TERM_SHORT_NAME}," +
                "${TermDAOImpl.TERM_YEAR}, ${TermDAOImpl.TERM_TYPE} " +
                "from ${TermDAOImpl.TERM_TABLE} as T " +
                "inner join $COURSE_TERM_TABLE as C on T.${TermDAOImpl.TERM_ID} = C.${TermDAOImpl.TERM_ID} " +
                "where C.$COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .mapTo(Term::class.java)
                .list()
    }

    override fun getSpecificTermOfCourse(courseId: Int, termId: Int): Term = dbi.withHandle<Term, Exception> {
        val select = "select ${TermDAOImpl.TERM_ID}, ${TermDAOImpl.TERM_SHORT_NAME}," +
                "${TermDAOImpl.TERM_YEAR}, ${TermDAOImpl.TERM_TYPE}" +
                "from ${TermDAOImpl.TERM_TABLE} as T" +
                "inner join $COURSE_TERM_TABLE as C on T.${TermDAOImpl.TERM_ID} = C.${TermDAOImpl.TERM_ID}" +
                "where C.$COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .mapTo(Term::class.java)
                .findOnly()
    }

    override fun voteOnReportedCourse(reportId: Int, vote: Vote): Int = dbi.withHandle<Int, Exception> {
        val voteQuery = "select $COURSE_VOTES from $COURSE_REPORT_TABLE where $COURSE_REPORT_ID = :reportId"
        var votes = it.createQuery(voteQuery)
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        val updateQuery = "update $COURSE_REPORT_TABLE set $COURSE_VOTES = :votes where $COURSE_REPORT_ID = :reportId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }

    override fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course> = dbi.withHandle<List<Course>, Exception> {
        val select = "select cp.$COURSE_ID, cp.$PROGRAMME_ID, cp.${ProgrammeDAOImpl.CRS_PROG_VERSION}," +
                " cp.$LECTURED_TERM, c.$COURSE_CREATED_BY, c.$COURSE_FULL_NAME, c.$COURSE_SHORT_NAME, c.$COURSE_TIMESTAMP, c.$COURSE_VOTES " +
                ", cp.$OPTIONAL, cp.$CREDITS " +
                "from course as c " +
                "inner join $COURSE_PROGRAMME_TABLE as cp on c.$COURSE_ID = cp.$COURSE_ID " +
                "where cp.$PROGRAMME_ID = :programmeId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .mapTo(Course::class.java)
                .list()

    }

    override fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Course = dbi.withHandle<Course, Exception> {
        val select = "select cp.$COURSE_ID, cp.$PROGRAMME_ID, cp.${ProgrammeDAOImpl.CRS_PROG_VERSION}," +
                " cp.$LECTURED_TERM, c.$COURSE_CREATED_BY, c.$COURSE_FULL_NAME, c.$COURSE_SHORT_NAME, c.$COURSE_TIMESTAMP, cp.$COURSE_VOTES " +
                ", cp.$OPTIONAL, cp.$CREDITS " +
                "from course as c " +
                "inner join $COURSE_PROGRAMME_TABLE as cp on c.$COURSE_ID = cp.$COURSE_ID " +
                "where cp.$PROGRAMME_ID = :programmeId and cp.$COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .mapTo(Course::class.java)
                .findOnly()

    }

    override fun addCourseToProgramme(programmeId: Int, course: Course): Int = dbi.withHandle<Int, Exception> {
        val insert = "insert into ${ProgrammeDAOImpl.CRS_PROG_TABLE} " +
                "(${ProgrammeDAOImpl.CRS_ID}, ${ProgrammeDAOImpl.PROG_ID}, ${ProgrammeDAOImpl.CRS_LECT_TERM}, ${ProgrammeDAOImpl.CRS_OPT}, " +
                "${ProgrammeDAOImpl.CRS_CRED}, $COURSE_TIMESTAMP, $COURSE_CREATED_BY) " +
                "values (:courseId, :programmeId, :term, :optional, :credits, :timestamp, :created_by)"
        it.createUpdate(insert)
                .bind("courseId", course.id)
                .bind("programmeId", programmeId)
                .bind("term", course.lecturedTerm)
                .bind("optional", course.optional)
                .bind("credits", course.credits)
                .bind("timestamp", course.timestamp)
                .bind("created_by", course.createdBy)
                .execute()
    }

    override fun reportCourseOnProgramme(programmeId: Int, courseId: Int, courseProgrammeReport: CourseProgrammeReport): Int = dbi.withHandle<Int, Exception> {
        val insert = "insert into $COURSE_PROGRAMME_REPORT_TABLE (" +
                "$COURSE_ID, $PROGRAMME_ID, $LECTURED_TERM, $OPTIONAL, $CREDITS, $COURSE_TIMESTAMP, $COURSE_REPORTED_BY)" +
                "values (:courseId, :programmeId, :lectured, :optional, :credits, :timestamp, :reportedBy)"
        it.createUpdate(insert)
                .bind("courseId", courseId)
                .bind("programmeId", programmeId)
                .bind("lectured", courseProgrammeReport.lecturedTerm)
                .bind("optional", courseProgrammeReport.optional)
                .bind("credits", courseProgrammeReport.credits)
                .bind("timestamp", courseProgrammeReport.timestamp)
                .bind("reportedBy", courseProgrammeReport.reportedBy)
                .execute()
    }

    override fun deleteAllVersionsOfCourse(versionCourseId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_VERSION_TABLE where $COURSE_ID = :id"
        it.createUpdate(delete)
                .bind("id", versionCourseId)
                .execute()
    }

    override fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion> = dbi.withHandle<List<CourseVersion>, Exception> {
        val select = "select * from $COURSE_VERSION_TABLE where $COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .mapTo(CourseVersion::class.java)
                .list()
    }

    override fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): CourseVersion = dbi.withHandle<CourseVersion, Exception> {
        val select = "select * from $COURSE_VERSION_TABLE where $COURSE_ID = :courseId and $COURSE_VERSION = :versionId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("versionId", versionId)
                .mapTo(CourseVersion::class.java)
                .findOnly()
    }

    override fun addToCourseVersion(updatedCourse: Course): Int = dbi.inTransaction<Int, Exception> {
        val insert = "insert into $COURSE_VERSION_TABLE " +
                "($COURSE_ID, ${OrganizationDAOImpl.ORG_ID}, $COURSE_VERSION, $COURSE_FULL_NAME, $COURSE_SHORT_NAME, " +
                "$COURSE_CREATED_BY, $COURSE_TIMESTAMP)" +
                "values (:courseId, :orgId, :version, :fullName, " +
                ":shortName, :createdBy, :timestamp)"
        it.createUpdate(insert)
                .bind("courseId", updatedCourse.id)
                .bind("orgId", updatedCourse.organizationId)
                .bind("version", updatedCourse.version)
                .bind("fullName", updatedCourse.fullName)
                .bind("shortName", updatedCourse.shortName)
                .bind("createdBy", updatedCourse.createdBy)
                .bind("timestamp", updatedCourse.timestamp)
                .execute()
    }

    override fun voteOnCourseOfProgramme(programmeId: Int, vote: Vote, courseId: Int): Int = dbi.inTransaction<Int, Exception> {
        val voteQuery = "select $COURSE_VOTES from $COURSE_PROGRAMME_TABLE where $COURSE_ID = :courseId and $PROGRAMME_ID =:programmeId"
        var votes = it.createQuery(voteQuery)
                .bind("courseId", courseId)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) votes.dec() else votes.inc()
        val updateQuery = "update $COURSE_PROGRAMME_TABLE set $COURSE_VOTES = :votes where $COURSE_ID = :courseId and $PROGRAMME_ID = :programmeId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("courseId", courseId)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun updateCourseProgramme(programmeId: Int, courseId: Int, updatedCourse: Course) = dbi.useHandle<Exception> {
        val update = "update $COURSE_PROGRAMME_TABLE SET " +
                " $COURSE_VERSION = :version, " +
                "$COURSE_CREATED_BY = :createdBy, $LECTURED_TERM = :lecture, " +
                "$OPTIONAL = :optional, $COURSE_VOTES = :votes, " +
                "$COURSE_TIMESTAMP = :timestamp, $CREDITS = :credits "
        "where $COURSE_ID = :courseId"

        it.createUpdate(update)
                .bind("version", updatedCourse.version)
                .bind("createdBy", updatedCourse.createdBy)
                .bind("lecture", updatedCourse.lecturedTerm)
                .bind("optional", updatedCourse.optional)
                .bind("votes", updatedCourse.votes)
                .bind("timestamp", updatedCourse.timestamp)
                .bind("timestamp", updatedCourse.credits)
                .execute()
    }

    override fun createStagingCourseOfProgramme(courseProgrammeStage: CourseProgrammeStage): Int = dbi.withHandle<Int, Exception> {
        val insert = "insert into $COURSE_PROGRAMME_STAGE_TABLE " +
                "(${ProgrammeDAOImpl.CRS_ID}, ${ProgrammeDAOImpl.PROG_ID}, ${ProgrammeDAOImpl.CRS_LECT_TERM}, ${ProgrammeDAOImpl.CRS_OPT}, " +
                "${ProgrammeDAOImpl.CRS_CRED}, ${ProgrammeDAOImpl.PROG_VOTES}) " +
                "values(:courseId, :programmeId, :term, :optional, :credits)"
        it.createUpdate(insert)
                .bind("courseId", courseProgrammeStage.courseId)
                .bind("programmeId", courseProgrammeStage.programmeId)
                .bind("term", courseProgrammeStage.lecturedTerm)
                .bind("optional", courseProgrammeStage.optional)
                .bind("credits", courseProgrammeStage.credits)
                .execute()
    }

    override fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): CourseProgrammeStage = dbi.withHandle<CourseProgrammeStage, Exception> {
        val select = "select cp.$COURSE_ID, cp.$PROGRAMME_ID, cp.${ProgrammeDAOImpl.CRS_PROG_VERSION}," +
                " cp.$LECTURED_TERM, c.$COURSE_CREATED_BY, c.$COURSE_FULL_NAME, c.$COURSE_SHORT_NAME, c.$COURSE_TIMESTAMP, c.$COURSE_VOTES " +
                ", cp.$OPTIONAL, cp.$CREDITS " +
                "from course as c " +
                "inner join $COURSE_PROGRAMME_STAGE_TABLE as cp on c.$COURSE_ID = cp.$COURSE_ID " +
                "where cp.$PROGRAMME_ID = :programmeId and cp.$COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .bind("courseId", stageId)
                .mapTo(CourseProgrammeStage::class.java)
                .findOnly()

    }

    override fun deleteStagedCourseOfProgramme(programmeId: Int, stageId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_PROGRAMME_STAGE_TABLE where $PROGRAMME_ID = :programmeId and $COURSE_ID = :courseId"
        it.createUpdate(delete)
                .bind("programmeId", programmeId)
                .bind("courseId", stageId)
                .execute()
    }

    override fun voteOnCourseProgrammeStaged(programmeId: Int, stageId: Int, vote: Vote): Int = dbi.inTransaction<Int, Exception> {
        val voteQuery = "select $COURSE_VOTES from $COURSE_PROGRAMME_STAGE_TABLE where $COURSE_ID = :courseId and $PROGRAMME_ID =:programmeId"
        var votes = it.createQuery(voteQuery)
                .bind("courseId", stageId)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) votes.dec() else votes.inc()
        val updateQuery = "update $COURSE_PROGRAMME_STAGE_TABLE set $COURSE_VOTES = :votes where $COURSE_ID = :courseId and $PROGRAMME_ID = :programmeId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("courseId", stageId)
                .bind("programmeId", programmeId)
                .execute()
    }

    override fun getAllVersionsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseVersion> = dbi.withHandle<List<CourseVersion>, Exception> {
        val select = "select cp.$COURSE_ID, cp.$PROGRAMME_ID, cp.${ProgrammeDAOImpl.CRS_PROG_VERSION}," +
                " cp.$LECTURED_TERM, c.$COURSE_CREATED_BY, c.$COURSE_FULL_NAME, c.$COURSE_SHORT_NAME, c.$COURSE_TIMESTAMP, c.$COURSE_VOTES " +
                ", cp.$OPTIONAL, cp.$CREDITS " +
                "from course as c " +
                "inner join $COURSE_PROGRAMME_VERSION_TABLE as cp on c.$COURSE_ID = cp.$COURSE_ID " +
                "where cp.$PROGRAMME_ID = :programmeId and cp.$COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .mapTo(CourseVersion::class.java)
                .list()

    }

    override fun getSpecificVersionOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, versionId: Int): CourseVersion = dbi.withHandle<CourseVersion, Exception> {
        val select = "select cp.$COURSE_ID, cp.$PROGRAMME_ID, cp.${ProgrammeDAOImpl.CRS_PROG_VERSION}," +
                " cp.$LECTURED_TERM, c.$COURSE_CREATED_BY, c.$COURSE_FULL_NAME, c.$COURSE_SHORT_NAME, c.$COURSE_TIMESTAMP, c.$COURSE_VOTES " +
                ", cp.$OPTIONAL, cp.$CREDITS " +
                "from course as c " +
                "inner join $COURSE_PROGRAMME_VERSION_TABLE as cp on c.$COURSE_ID = cp.$COURSE_ID " +
                "where cp.$PROGRAMME_ID = :programmeId and cp.$COURSE_ID = :courseId and cp.$COURSE_PROGRAMME_VERSION = :versionId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .bind("versionId", versionId)
                .mapTo(CourseVersion::class.java)
                .findOnly()

    }

    override fun addCourseProgrammeToVersion(courseProgramme: Course): Int = dbi.inTransaction<Int, Exception> {
        val insert = "insert into $COURSE_PROGRAMME_VERSION_TABLE " +
                "($COURSE_ID, $PROGRAMME_ID, $COURSE_PROGRAMME_VERSION, $LECTURED_TERM, $OPTIONAL, " +
                "$COURSE_CREATED_BY, $COURSE_TIMESTAMP, $CREDITS)" +
                "values (:courseId, :progId, :version, :lectured, " +
                ":optional, :createdBy, :timestamp, :credits)"
        it.createUpdate(insert)
                .bind("courseId", courseProgramme.id)
                .bind("progId", courseProgramme.programmeId)
                .bind("version", courseProgramme.version)
                .bind("lectured", courseProgramme.lecturedTerm)
                .bind("optional", courseProgramme.optional)
                .bind("createdBy", courseProgramme.createdBy)
                .bind("timestamp", courseProgramme.timestamp)
                .bind("credits", courseProgramme.credits)
                .execute()
    }

    override fun getAllReportsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport> = dbi.withHandle<List<CourseProgrammeReport>, Exception> {
        val select = "select cp.$COURSE_ID, cp.$PROGRAMME_ID, cp.${ProgrammeDAOImpl.CRS_PROG_VERSION}," +
                " cp.$LECTURED_TERM, c.$COURSE_CREATED_BY, c.$COURSE_FULL_NAME, c.$COURSE_SHORT_NAME, c.$COURSE_TIMESTAMP, c.$COURSE_VOTES " +
                ", cp.$OPTIONAL, cp.$CREDITS " +
                "from course as c " +
                "inner join $COURSE_PROGRAMME_REPORT_TABLE as cp on c.$COURSE_ID = cp.$COURSE_ID " +
                "where cp.$PROGRAMME_ID = :programmeId and cp.$COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .mapTo(CourseProgrammeReport::class.java)
                .list()

    }

    override fun getSpecificReportOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeReport = dbi.withHandle<CourseProgrammeReport, Exception> {
        val select = "select cp.$COURSE_ID, cp.$PROGRAMME_ID, cp.${ProgrammeDAOImpl.CRS_PROG_VERSION}," +
                " cp.$LECTURED_TERM, c.$COURSE_CREATED_BY, c.$COURSE_FULL_NAME, c.$COURSE_SHORT_NAME, c.$COURSE_TIMESTAMP, c.$COURSE_VOTES " +
                "from course as c " +
                "inner join $COURSE_PROGRAMME_REPORT_TABLE as cp on c.$COURSE_ID = cp.$COURSE_ID " +
                "where cp.$COURSE_REPORT_ID = :reportId"
        it.createQuery(select)
                .bind("reportId", reportId)
                .mapTo(CourseProgrammeReport::class.java)
                .findOnly()

    }

    override fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_PROGRAMME_REPORT_TABLE where $COURSE_REPORT_ID = :reportId"
        it.createUpdate(delete).bind("reportId", reportId).execute()
    }

    override fun voteOnReportOfCourseProgramme(programmeId: Int, reportId: Int, vote: Vote): Int = dbi.inTransaction<Int, Exception> {
        val voteQuery = "select $COURSE_VOTES from $COURSE_PROGRAMME_REPORT_TABLE where $COURSE_ID = :courseId and $PROGRAMME_ID =:programmeId"
        var votes = it.createQuery(voteQuery)
                .bind("courseId", reportId)
                .bind("programmeId", programmeId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) votes.dec() else votes.inc()
        val updateQuery = "update $COURSE_PROGRAMME_REPORT_TABLE set $COURSE_VOTES = :votes where $COURSE_ID = :courseId and $PROGRAMME_ID = :programmeId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("courseId", reportId)
                .bind("programmeId", programmeId)
                .execute()
    }


    override fun getStagedCoursesOfProgramme(programmeId: Int): List<CourseProgrammeStage> = dbi.withHandle<List<CourseProgrammeStage>, Exception> {
        val select = "select cp.$COURSE_ID, cp.$PROGRAMME_ID, cp.${ProgrammeDAOImpl.CRS_PROG_VERSION}," +
                " cp.$LECTURED_TERM, c.$COURSE_CREATED_BY, c.$COURSE_SHORT_NAME, c.$COURSE_TIMESTAMP, cp.$COURSE_VOTES " +
                "from course as c " +
                "inner join $COURSE_PROGRAMME_STAGE_TABLE as cp on c.$COURSE_ID = cp.$COURSE_ID " +
                "where cp.$PROGRAMME_ID = :programmeId"
        it.createQuery(select)
                .bind("programmeId", programmeId)
                .mapTo(CourseProgrammeStage::class.java)
                .list()

    }

    override fun deleteSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_PROGRAMME_TABLE where $PROGRAMME_ID = :programmeId and $COURSE_ID = :courseId"
        it.createUpdate(delete)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .execute()
    }

    override fun deleteSpecificStagedCourseOfProgramme(programmeId: Int, courseId: Int, stageId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_PROGRAMME_STAGE_TABLE where $PROGRAMME_ID = :programmeId and $COURSE_ID = :courseId"
        it.createUpdate(delete)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .execute()
    }

    override fun deleteSpecificVersionOfCourseOfProgramme(programmeId: Int, courseId: Int, versionId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_PROGRAMME_VERSION_TABLE where $PROGRAMME_ID = :programmeId and $COURSE_ID = :courseId and $COURSE_PROGRAMME_VERSION = :version"
        it.createUpdate(delete)
                .bind("programmeId", programmeId)
                .bind("courseId", courseId)
                .bind("version", versionId)
                .execute()
    }

    override fun deleteSpecificReportOfCourseOfProgramme(programmeId: Int, courseId: Int, reportId: Int): Int = dbi.withHandle<Int, Exception> {
        val delete = "delete from $COURSE_PROGRAMME_REPORT_TABLE where $COURSE_REPORT_ID = :reportId"
        it.createUpdate(delete)
                .bind("reportId", reportId)
                .execute()
    }

}

