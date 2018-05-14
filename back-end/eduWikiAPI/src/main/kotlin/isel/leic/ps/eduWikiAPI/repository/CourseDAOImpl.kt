package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.CourseDAO
import org.jdbi.v3.core.Jdbi
import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.beans.factory.annotation.Autowired

class CourseDAOImpl : CourseDAO {

    companion object {
        //TABLE NAMES
        const val CRS_TABLE = "course"
        const val CRS_VERSION_TABLE = "course_version"
        const val CRS_REPORT_TABLE = "course_report"
        // FIELDS
        const val CRS_ID = "course_id"
        const val ORG_ID = "organization_id"
        const val CRS_VERSION = "course_version"
        const val CRS_FULL_NAME = "course_full_name"
        const val CRS_SHORT_NAME = "course_short_name"
        const val CRS_VOTE = "votes"
        const val CRS_TIMESTAMP = "time_stamp"
        const val CRS_REPORT_ID = "report_id"
        const val CRS_CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var dsl: DSLContext
    @Autowired
    lateinit var dbi: Jdbi

    override fun getCourse(courseId: Int): Course = dbi.withHandle<Course, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_ID),
                        field(ORG_ID),
                        field(CRS_VERSION),
                        field(CRS_CREATED_BY),
                        field(CRS_FULL_NAME),
                        field(CRS_SHORT_NAME),
                        field(CRS_VOTE)
                )
                .from(table(CRS_TABLE))
                .where(field(CRS_ID).eq(courseId))
                .sql
        ).mapTo(Course::class.java).findOnly()
    }

    override fun getAllCourses(): List<Course> = dbi.withHandle<List<Course>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_ID),
                        field(ORG_ID),
                        field(CRS_VERSION),
                        field(CRS_CREATED_BY),
                        field(CRS_FULL_NAME),
                        field(CRS_SHORT_NAME),
                        field(CRS_VOTE)
                )
                .from(table(CRS_TABLE))
                .sql
        ).mapTo(Course::class.java).list()
    }

    override fun deleteCourse(courseId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(CRS_TABLE))
                .where(field(CRS_ID).eq(courseId))
                .sql
        )
    }

    override fun deleteAllCourses(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(CRS_TABLE))
                .sql
        )
    }

    override fun updateCourse(course: Course, user: String): Int = TODO("dynamically update org by filled values in Organization parameter")

    override fun createCourse(course: Course, user: String) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(CRS_TABLE),
                        field(CRS_ID),
                        field(ORG_ID),
                        field(CRS_VERSION),
                        field(CRS_CREATED_BY),
                        field(CRS_FULL_NAME),
                        field(CRS_SHORT_NAME),
                        field(CRS_VOTE)
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
                .select(field(CRS_VOTE))
                .from(table(CRS_TABLE))
                .where(field(CRS_ID).eq(courseId))
                .sql
        ).mapTo(Int::class.java).findOnly()
        it.execute(dsl
                .update(table(CRS_TABLE))
                .set(field(CRS_VOTE), if (voteType == -1) votes.dec() else votes.inc())
                .where(field(CRS_ID).eq(courseId))
                .sql
        )
    }

    override fun getVersionCourse(versionCourseId: Int, version: Int): CourseVersion = dbi.withHandle<CourseVersion, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_ID),
                        field(ORG_ID),
                        field(CRS_FULL_NAME),
                        field(CRS_SHORT_NAME),
                        field(CRS_CREATED_BY),
                        field(CRS_TIMESTAMP),
                        field(CRS_VERSION)
                )
                .from(table(CRS_VERSION_TABLE))
                .where(field(CRS_ID).eq(versionCourseId).and(field(CRS_VERSION).eq(version)))
                .sql
        ).mapTo(CourseVersion::class.java).first()
    }

    override fun getAllVersionCourses(): List<CourseVersion> = dbi.withHandle<List<CourseVersion>, Exception> {
        it.createQuery(dsl
                .select(
                        field(CRS_ID),
                        field(ORG_ID),
                        field(CRS_FULL_NAME),
                        field(CRS_SHORT_NAME),
                        field(CRS_CREATED_BY),
                        field(CRS_TIMESTAMP),
                        field(CRS_VERSION)
                )
                .from(table(CRS_VERSION_TABLE))
                .sql
        ).mapTo(CourseVersion::class.java).list()
    }

    override fun deleteVersionCourse(versionCourseId: Int, version: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(CRS_VERSION_TABLE))
                .where(field(CRS_ID).eq(versionCourseId).and(field(CRS_VERSION).eq(version)))
                .sql
        )
    }

    override fun deleteAllVersionCourses(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(CRS_VERSION_TABLE))
                .sql
        )
    }

    override fun createVersionCourse(courseVersion: CourseVersion) = dbi.useHandle<Exception> {
        it.execute(dsl
                .insertInto(table(CRS_VERSION_TABLE),
                        field(CRS_ID),
                        field(ORG_ID),
                        field(CRS_FULL_NAME),
                        field(CRS_SHORT_NAME),
                        field(CRS_CREATED_BY),
                        field(CRS_TIMESTAMP),
                        field(CRS_VERSION))
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
                .insertInto(table(CRS_REPORT_TABLE),
                        field(CRS_REPORT_ID),
                        field(CRS_ID),
                        field(CRS_FULL_NAME),
                        field(CRS_SHORT_NAME),
                        field(CRS_CREATED_BY),
                        field(CRS_VOTE))
                .values(
                        courseReport.reportId,
                        courseReport.courseId,
                        courseReport.courseFullName,
                        courseReport.courseShortName,
                        courseReport.createdBy,
                        courseReport.votes
                ).sql
        )
    }

    override fun deleteReportOnCourse(reportId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(CRS_REPORT_TABLE))
                .where(field(CRS_REPORT_ID).eq(reportId))
                .sql
        )
    }

    override fun deleteAllReportsOnCourse(courseId: Int): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl
                .delete(table(CRS_REPORT_TABLE))
                .where(field(CRS_ID).eq(courseId))
                .sql
        )
    }

    override fun deleteAllReports(): Int = dbi.withHandle<Int, Exception> {
        it.execute(dsl.
                delete(table(CRS_REPORT_TABLE))
                .sql
        )
    }

}