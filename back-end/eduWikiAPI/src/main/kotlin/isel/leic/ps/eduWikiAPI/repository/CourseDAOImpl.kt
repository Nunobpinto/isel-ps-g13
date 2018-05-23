package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

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
        // COURSE FIELDS
        const val COURSE_ID = "course_id"
        const val ORG_ID = "organization_id"
        const val COURSE_VERSION = "course_version"
        const val COURSE_FULL_NAME = "course_full_name"
        const val COURSE_SHORT_NAME = "course_short_name"
        const val COURSE_VOTE = "votes"
        const val COURSE_TIMESTAMP = "time_stamp"
        const val COURSE_REPORT_ID = "report_id"
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

    override fun getCourse(courseId: Int) = dbi.withHandle<Course, Exception> {
        it.createQuery(
                "select " +
                        "$COURSE_TABLE.$COURSE_ID, " +
                        "$COURSE_TABLE.$ORG_ID, " +
                        "$COURSE_TABLE.$COURSE_VERSION, " +
                        "$COURSE_TABLE.$COURSE_CREATED_BY, " +
                        "$COURSE_TABLE.$COURSE_FULL_NAME, " +
                        "$COURSE_TABLE.$COURSE_SHORT_NAME, " +
                        "$COURSE_TABLE.$COURSE_VOTE, " +
                        "$COURSE_PROGRAMME_TABLE.$PROGRAMME_ID, " +
                        "$COURSE_PROGRAMME_TABLE.$COURSE_PROGRAMME_VERSION, " +
                        "$COURSE_PROGRAMME_TABLE.$LECTURED_TERM, " +
                        "$COURSE_PROGRAMME_TABLE.$OPTIONAL, " +
                        "$COURSE_PROGRAMME_TABLE.$CREDITS " +
                        "from $COURSE_TABLE " +
                        "inner join $COURSE_PROGRAMME_TABLE on " +
                        "where $COURSE_ID = :courseId"
        ).bind("courseId", courseId).mapTo(Course::class.java).findOnly()
    }


    override fun getAllCourses(): List<Course> = dbi.withHandle<List<Course>, Exception> {
        it.createQuery(
                "select $COURSE_ID, $ORG_ID, $COURSE_ID, $COURSE_CREATED_BY, $COURSE_FULL_NAME, $COURSE_SHORT_NAME, $COURSE_VOTE " +
                        "from $COURSE_TABLE"
        ).mapTo(Course::class.java).list()
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
        it.execute(dsl
                .insertInto(table(COURSE_TABLE),
                        field(COURSE_ID),
                        field(ORG_ID),
                        field(COURSE_VERSION),
                        field(COURSE_CREATED_BY),
                        field(COURSE_FULL_NAME),
                        field(COURSE_SHORT_NAME),
                        field(COURSE_VOTE)
                )
                .values(
                        course.id,
                        course.organizationId,
                        course.version,
                        course.createdBy,
                        course.fullName,
                        course.shortName,
                        course.votes
                ).sql
        )
    }

    override fun voteOnCourse(courseId: Int, voteType: Int) = dbi.useTransaction<Exception> {
        val votes: Int = it.createQuery(dsl
                .select(field(COURSE_VOTE))
                .from(table(COURSE_TABLE))
                .where(field(COURSE_ID).eq(courseId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(COURSE_TABLE))
                .set(field(COURSE_VOTE), if (voteType == -1) votes.dec() else votes.inc())
                .where(field(COURSE_ID).eq(courseId))
                .sql
        )
    }

    override fun getCourseStage(courseStageId: Int): CourseStage = dbi.withHandle<CourseStage, Exception> {
        it.createQuery(dsl
                .select(
                        field(COURSE_ID),
                        field(ORG_ID),
                        field(COURSE_FULL_NAME),
                        field(COURSE_SHORT_NAME),
                        field(COURSE_CREATED_BY),
                        field(COURSE_VOTE),
                        field(COURSE_TIMESTAMP)
                )
                .from(table(COURSE_STAGE_TABLE))
                .where(field(COURSE_ID).eq(courseStageId))
                .sql
        ).mapTo(CourseStage::class.java).findOnly()
    }

    override fun getAllCourseStages(): List<CourseStage> = dbi.withHandle<List<CourseStage>, Exception> {
        it.createQuery(
                "select $COURSE_ID, $ORG_ID, $COURSE_CREATED_BY, $COURSE_FULL_NAME, $COURSE_SHORT_NAME, $COURSE_VOTE " +
                        "from $COURSE_TABLE "
        ).mapTo(CourseStage::class.java).toList()
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
                        field(COURSE_VOTE),
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
                .select(field(COURSE_VOTE))
                .from(table(COURSE_STAGE_TABLE))
                .where(field(COURSE_ID).eq(courseStageId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(COURSE_STAGE_TABLE))
                .set(field(COURSE_VOTE), if (voteType == -1) votes.dec() else votes.inc())
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

    override fun reportCourse(courseReport: CourseReport) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(COURSE_REPORT_TABLE),
                        field(COURSE_REPORT_ID),
                        field(COURSE_ID),
                        field(COURSE_FULL_NAME),
                        field(COURSE_SHORT_NAME),
                        field(COURSE_CREATED_BY),
                        field(COURSE_VOTE))
                .values(
                        courseReport.reportId,
                        courseReport.courseId,
                        courseReport.courseFullName,
                        courseReport.courseShortName,
                        courseReport.reportedBy,
                        courseReport.votes
                ).sql
        )
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

    override fun getAllReportsOnCourse(courseId: Int): List<CourseReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}