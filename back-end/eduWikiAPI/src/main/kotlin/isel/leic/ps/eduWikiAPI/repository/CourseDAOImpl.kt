package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.report.WorkAssignmentReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ExamStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.WorkAssignmentStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import org.jdbi.v3.core.Jdbi
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

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
        const val COURSE_CLASS_TABLE = "course_class"
        const val COURSE_TERM_TABLE = "course_term"
        const val COURSE_MISC_UNIT_TABLE = "course_misc_unit"
        const val COURSE_MISC_UNIT_STAGE_TABLE = "course_misc_unit_stage"


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
        const val COURSE_PROGRAMME_VERSION = "course_programme_id"
        const val LECTURED_TERM = "course_lectured_term"
        const val OPTIONAL = "course_optional"
        const val CREDITS = "course_credits"

    }

    @Autowired
    lateinit var dbi: Jdbi

    override fun getSpecificCourse(courseId: Int): Course = dbi.withHandle<Course, Exception> {
        it.createQuery("select * from $COURSE_TABLE where $COURSE_ID = :courseId")
                .bind("courseId", courseId)
                .mapTo(Course::class.java)
                .findOnly()
    }

    override fun getAllCourses(): List<Course> = dbi.withHandle<List<Course>, Exception> {
        it.createQuery("select * from $COURSE_TABLE")
                .mapTo(Course::class.java)
                .list()
    }


    override fun deleteCourse(courseId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(COURSE_TABLE))
                .where(field(COURSE_ID).eq(courseId))
                .sql
        )
    }

    override fun deleteAllCourses(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(COURSE_TABLE))
                .sql
        )
    }

    override fun updateCourse(course: Course): Int = TODO("dynamically update org by filled values in Organization parameter")

    override fun createCourse(course: Course) = dbi.useHandle<Exception> {
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

    override fun voteOnCourse(courseId: Int, inputVote: VoteInputModel) = dbi.useTransaction<Exception> {
        val voteQuery = "select $COURSE_VOTES from $COURSE_TABLE where $COURSE_ID :courseId"
        var votes = it.createQuery(voteQuery)
                .bind("courseId", courseId)
                .mapTo(Int::class.java).findOnly()
        votes = if (inputVote.vote.equals(Vote.Down)) --votes else ++votes
        val updateQuery = "update $COURSE_TABLE set $COURSE_VOTES = :votes where $COURSE_ID = :courseId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("programmeId", courseId)
                .execute()
    }

    override fun deleteCourseStage(courseStageId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(COURSE_STAGE_TABLE))
                .where(field(COURSE_ID).eq(courseStageId))
                .sql
        )
    }

    override fun deleteAllCourseStages(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(COURSE_STAGE_TABLE))
                .sql
        )
    }

    override fun createCourseStage(courseStage: CourseStage) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(
                        table(COURSE_STAGE_TABLE),
                        field(COURSE_ID),
                        field(ORG_ID),
                        field(COURSE_FULL_NAME),
                        field(COURSE_SHORT_NAME),
                        field(COURSE_CREATED_BY),
                        field(COURSE_VOTES),
                        field(COURSE_TIMESTAMP)
                )
                .values(
                        courseStage.courseId,
                        courseStage.organizationId,
                        courseStage.fullName,
                        courseStage.shortName,
                        courseStage.createdBy,
                        courseStage.votes,
                        courseStage.timestamp
                ).sql
        )
    }

    override fun voteOnCourseStage(courseStageId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field(COURSE_VOTES))
                .from(table(COURSE_STAGE_TABLE))
                .where(field(COURSE_ID).eq(courseStageId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(COURSE_STAGE_TABLE))
                .set(field(COURSE_VOTES), if (voteType == -1) votes.dec() else votes.inc())
                .where(field(COURSE_ID).eq(courseStageId))
                .sql
        )
    }

    override fun getVersionCourse(versionCourseId: Int, version: Int): CourseVersion = dbi.withHandle<CourseVersion, Exception> {
        it.createQuery(dsl
                .select(
                        field(COURSE_ID),
                        field(ORG_ID),
                        field(COURSE_FULL_NAME),
                        field(COURSE_SHORT_NAME),
                        field(COURSE_CREATED_BY),
                        field(COURSE_TIMESTAMP),
                        field(COURSE_VERSION)
                )
                .from(table(COURSE_VERSION_TABLE))
                .where(field(COURSE_ID).eq(versionCourseId).and(field(COURSE_VERSION).eq(version)))
                .sql
        ).mapTo(CourseVersion::class.java).first()
    }

    override fun getAllVersionCourses(): List<CourseVersion> = dbi.withHandle<List<CourseVersion>, Exception> {
        it.createQuery(dsl
                .select(
                        field(COURSE_ID),
                        field(ORG_ID),
                        field(COURSE_FULL_NAME),
                        field(COURSE_SHORT_NAME),
                        field(COURSE_CREATED_BY),
                        field(COURSE_TIMESTAMP),
                        field(COURSE_VERSION)
                )
                .from(table(COURSE_VERSION_TABLE))
                .sql
        ).mapTo(CourseVersion::class.java).list()
    }

    override fun deleteVersionCourse(versionCourseId: Int, version: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(COURSE_VERSION_TABLE))
                .where(field(COURSE_ID).eq(versionCourseId).and(field(COURSE_VERSION).eq(version)))
                .sql
        )
    }

    override fun deleteAllVersionCourses(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(COURSE_VERSION_TABLE))
                .sql
        )
    }

    override fun createVersionCourse(courseVersion: CourseVersion) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(COURSE_VERSION_TABLE),
                        field(COURSE_ID),
                        field(ORG_ID),
                        field(COURSE_FULL_NAME),
                        field(COURSE_SHORT_NAME),
                        field(COURSE_CREATED_BY),
                        field(COURSE_TIMESTAMP),
                        field(COURSE_VERSION))
                .values(
                        courseVersion.courseId,
                        courseVersion.organizationId,
                        courseVersion.fullName,
                        courseVersion.shortName,
                        courseVersion.createdBy,
                        courseVersion.timestamp,
                        courseVersion.version
                ).sql
        )
    }

    override fun reportCourse(courseId: Int, courseReport: CourseReport) = dbi.useHandle<Exception> {
        val insert = "insert into $COURSE_REPORT_TABLE " +
                "($COURSE_ID, $COURSE_FULL_NAME, " +
                "$COURSE_SHORT_NAME, $COURSE_REPORTED_BY, $COURSE_TIMESTAMP) " +
                "values(:courseId, :fullName, :shortName, :reportedBy, :timestamp)"
        it.createUpdate(insert)
                .bind("courseId", courseId)
                .bind("fullName", courseReport.courseFullName)
                .bind("shortName", courseReport.courseShortName)
                .bind("reportedBy", courseReport.reportedBy)
                .bind("timestamp", courseReport.timestamp)
                .execute()
    }

    override fun deleteReportOnCourse(reportId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(COURSE_REPORT_TABLE))
                .where(field(COURSE_REPORT_ID).eq(reportId))
                .sql
        )
    }

    override fun deleteAllReportsOnCourse(courseId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(COURSE_REPORT_TABLE))
                .where(field(COURSE_ID).eq(courseId))
                .sql
        )
    }

    override fun deleteAllReports(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl.delete(table(COURSE_REPORT_TABLE))
                .sql
        )
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

    override fun getCourseSpecificStageEntry(stageId: Int): CourseStage = dbi.withHandle<CourseStage, Exception> {
        val select = "select * from $COURSE_STAGE_TABLE" +
                "where $COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("courseId", stageId)
                .mapTo(CourseStage::class.java)
                .findOnly()
    }

    override fun getTermsOfCourse(courseId: Int): List<Term> = dbi.withHandle<List<Term>, Exception> {
        val select = "select ${TermDAOImpl.TERM_ID}, ${TermDAOImpl.TERM_SHORT_NAME}," +
                "${TermDAOImpl.TERM_YEAR}, ${TermDAOImpl.TERM_TYPE}" +
                "from ${TermDAOImpl.TERM_TABLE} as T" +
                "inner join $COURSE_TERM_TABLE as C on T.${TermDAOImpl.TERM_ID} = C.${TermDAOImpl.TERM_ID}" +
                "where T.$COURSE_ID = :courseId"
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
                "where T.$COURSE_ID = :courseId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .mapTo(Term::class.java)
                .findOnly()
    }

    override fun getAllExamsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<Exam> = dbi.withHandle<List<Exam>, Exception> {
        val select = "select * from ${ExamDAOImpl.EXM_TABLE} as E" +
                "inner join $COURSE_MISC_UNIT_TABLE as C " +
                "on E.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID" +
                "where C.$COURSE_ID = :courseId and C.${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(Exam::class.java)
                .list()
    }

    override fun getSpecificExamFromSpecificTermOfCourse(courseId: Int, termId: Int, examId: Int): Exam = dbi.withHandle<Exam, Exception> {
        val select = "select * from ${ExamDAOImpl.EXM_TABLE} as E" +
                "inner join $COURSE_MISC_UNIT_TABLE as C " +
                "on E.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID" +
                "where C.$COURSE_ID = :courseId " +
                "and C.${TermDAOImpl.TERM_ID} = :termId " +
                "and $COURSE_MISC_UNIT_ID = :examId"

        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("examId", examId)
                .mapTo(Exam::class.java)
                .findOnly()
    }

    override fun getStageEntriesFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int): List<ExamStage> = dbi.withHandle<List<ExamStage>, Exception> {
        val select = "select * from ${ExamDAOImpl.EXM_STAGE_TABLE} as E" +
                "inner join $COURSE_MISC_UNIT_TABLE as C " +
                "on E.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID" +
                "where C.$COURSE_ID = :courseId and C.${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(ExamStage::class.java)
                .list()
    }

    override fun getStageEntryFromExamOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): ExamStage = dbi.withHandle<ExamStage, Exception> {
        val select = "select * from ${ExamDAOImpl.EXM_STAGE_TABLE} as E" +
                "inner join $COURSE_MISC_UNIT_TABLE as C " +
                "on E.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID" +
                "where C.$COURSE_ID = :courseId " +
                "and C.${TermDAOImpl.TERM_ID} = :termId " +
                "and $COURSE_MISC_UNIT_ID = :stageId"

        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("stageId", stageId)
                .mapTo(ExamStage::class.java)
                .findOnly()
    }

    override fun getAllReportsOnExamOnSpecificTermOfCourse(examId: Int): List<ExamReport> = dbi.withHandle<List<ExamReport>, Exception> {
        val select = "select * from ${ExamDAOImpl.EXM_REPORT_TABLE}" +
                "where $COURSE_MISC_UNIT_ID = :examId"
        it.createQuery(select)
                .bind("examId", examId)
                .mapTo(ExamReport::class.java)
                .list()
    }

    override fun getSpecificReportOnExamOnSpecificTermOfCourse(reportId: Int): ExamReport = dbi.withHandle<ExamReport, Exception> {
        val select = "select * from ${ExamDAOImpl.EXM_REPORT_TABLE}" +
                "where $COURSE_REPORT_ID = :reportId"
        it.createQuery(select)
                .bind("examId", reportId)
                .mapTo(ExamReport::class.java)
                .findOnly()
    }

    override fun getAllWorkAssignmentsFromSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignment> = dbi.withHandle<List<WorkAssignment>, Exception> {
        val select = "select * from ${WorkAssignmentDAOImpl.WRK_ASS_TABLE} as W" +
                "inner join $COURSE_MISC_UNIT_TABLE as C " +
                "on W.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID" +
                "where C.$COURSE_ID = :courseId and C.${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(WorkAssignment::class.java)
                .list()
    }

    override fun getSpecificWorkAssignmentFromSpecificTermOfCourse(workAssignmentId: Int): WorkAssignment = dbi.withHandle<WorkAssignment, Exception> {
        val select = "select * from ${WorkAssignmentDAOImpl.WRK_ASS_TABLE}" +
                "where $COURSE_MISC_UNIT_ID = :workItemId"
        it.createQuery(select)
                .bind("courseId", workAssignmentId)
                .mapTo(WorkAssignment::class.java)
                .findOnly()
    }


    override fun getStageEntriesFromWorkItemOnSpecificTermOfCourse(courseId: Int, termId: Int): List<WorkAssignmentStage> = dbi.withHandle<List<WorkAssignmentStage>, Exception> {
        val select = "select * from ${WorkAssignmentDAOImpl.WRK_ASS_STAGE_TABLE} as W" +
                "inner join $COURSE_MISC_UNIT_TABLE as C " +
                "on W.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID" +
                "where C.$COURSE_ID = :courseId and C.${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(WorkAssignmentStage::class.java)
                .list()
    }

    override fun getStageEntryFromWorkItemOnSpecificTermOfCourse(courseId: Int, termId: Int, stageId: Int): WorkAssignmentStage = dbi.withHandle<WorkAssignmentStage, Exception> {
        val select = "select * from ${WorkAssignmentDAOImpl.WRK_ASS_STAGE_TABLE} as W" +
                "inner join $COURSE_MISC_UNIT_TABLE as C " +
                "on W.$COURSE_MISC_UNIT_ID = C.$COURSE_MISC_UNIT_ID" +
                "where C.$COURSE_ID = :courseId " +
                "and C.${TermDAOImpl.TERM_ID} = :termId " +
                "and $COURSE_MISC_UNIT_ID = :stageId"

        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("stageId", stageId)
                .mapTo(WorkAssignmentStage::class.java)
                .findOnly()
    }

    override fun getAllReportsOnWorkUnitOnSpecificTermOfCourse(courseId: Int, termId: Int, workAssignmentId: Int): List<WorkAssignmentReport> = dbi.withHandle<List<WorkAssignmentReport>, Exception>{
        val select = "select * from ${WorkAssignmentDAOImpl.WRK_ASS_REPORT_TABLE}" +
                "where $COURSE_MISC_UNIT_ID = :workAssignmentId"
        it.createQuery(select)
                .bind("examId", workAssignmentId)
                .mapTo(WorkAssignmentReport::class.java)
                .list()
    }

    override fun getSpecificReportFromWorkItemOnSpecificTermOfCourse(reportId: Int): WorkAssignmentReport = dbi.withHandle<WorkAssignmentReport, Exception>{
        val select = "select * from ${WorkAssignmentDAOImpl.WRK_ASS_REPORT_TABLE}" +
                "where $COURSE_REPORT_ID = :reportId"
        it.createQuery(select)
                .bind("reportId", reportId)
                .mapTo(WorkAssignmentReport::class.java)
                .findOnly()
    }

    override fun getClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class> = dbi.withHandle<List<Class>, Exception>{
        val select = "select * from $COURSE_CLASS_TABLE" +
                "where $COURSE_ID = :courseId and ${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(Class::class.java)
                .list()
    }

    override fun voteOnReportedCourse(reportId: Int, inputVote: VoteInputModel) = dbi.useHandle<Exception>{
        val voteQuery = "select $COURSE_VOTES from $COURSE_REPORT_TABLE where $COURSE_REPORT_ID = :reportId"
        var votes = it.createQuery(voteQuery)
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (inputVote.vote.equals(Vote.Down)) --votes else ++votes
        val updateQuery = "update $COURSE_REPORT_TABLE set $COURSE_VOTES = :votes where $COURSE_REPORT_ID = :reportId"
        it.createUpdate(updateQuery)
                .bind("votes", votes)
                .bind("reportId", reportId)
                .execute()
    }


}