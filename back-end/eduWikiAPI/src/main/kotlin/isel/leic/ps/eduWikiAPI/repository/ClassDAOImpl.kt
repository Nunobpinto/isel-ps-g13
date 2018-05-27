package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import org.jdbi.v3.core.Jdbi
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ClassDAOImpl : ClassDAO {

    companion object {
        //TABLE NAMES
        const val WRK_ASS_TABLE = "work_assignment"
        const val WRK_ASS_VERSION_TABLE = "work_assignment_version"
        const val WRK_ASS_REPORT_TABLE = "work_assignment_report"
        const val WRK_ASS_STAGE_TABLE = "work_assignment_stage"
        // FIELDS
        const val WRK_ASS_VERSION = "work_assignment_version"
        const val WRK_ASS_SHEET = "sheet"
        const val WRK_ASS_SUPPLEMENT = "supplement"
        const val WRK_ASS_DUE_DATE = "due_date"
        const val WRK_ASS_INDIVIDUAL = "individual"
        const val WRK_ASS_LATE_DELIVERY = "late_delivery"
        const val WRK_ASS_MULTIPLE_DELIVERIES = "multiple_deliveries"
        const val WRK_ASS_REQUIRES_REPORT = "requires_report"
        const val WRK_ASS_VOTE = "votes"
        const val WRK_ASS_TIMESTAMP = "time_stamp"
        const val WRK_ASS_REPORT_ID = "report_id"
        const val WRK_ASS_CREATED_BY = "created_by"
    }

    @Autowired
    lateinit var dsl: DSLContext
    @Autowired
    lateinit var dbi: Jdbi

    override fun getClass(classId: Int, termId: Int): Class {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllClasses(): List<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAlLClassesInTerm(termId: Int): List<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteClass(classId: Int, termId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllClasses(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateClass(klass: Class): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createClass(klass: Class) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnClass(courseId: Int, termId: Int, voteType: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class>  = dbi.withHandle<List<Class>, Exception>{
        val select = "select * from ${CourseDAOImpl.COURSE_CLASS_TABLE}" +
                "where ${CourseDAOImpl.COURSE_ID} = :courseId and ${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(Class::class.java)
                .list()
    }

    override fun getClassStage(classId: Int, termId: Int): ClassStage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllClassStages(): List<ClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllClassStagesInTerm(termId: Int): List<ClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteClassStage(classId: Int, termId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllProgrammeStages(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllProgrammeStages(termId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createClassStage(classStage: ClassStage) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnClassStage(classId: Int, termId: Int, voteType: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getVersionClass(versionClassId: Int, versionTermId: Int, version: Int): ClassVersion {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllVersionCourses(): List<ClassVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteVersionCourse(versionCourseId: Int, versionTermId: Int, version: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllVersionCourses(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createVersionCourse(classVersion: ClassVersion) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reportClass(classReport: ClassReport) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteReportOnClass(reportId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllReportsOnCourse(classId: Int, termId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllReports(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}