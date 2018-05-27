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
        const val CLASS_TABLE = "class"
        const val CLASS_REPORT_TABLE = "class_report"
        const val CLASS_STAGE_TABLE = "class_stage"
        const val CLASS_VERSION_TABLE = "class_stage"
        // FIELDS
        const val CLASS_ID = "class_id"
        const val CLASS_VERSION = "class_version"
    }

    @Autowired
    lateinit var dbi: Jdbi

    override fun getClass(classId: Int): Class = Class()

    override fun getAllClasses(): List<Class> = listOf()

    override fun getAlLClassesInTerm(termId: Int): List<Class> = listOf()

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

    override fun voteOnClass(courseId: Int, voteType: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getClassStage(classId: Int): ClassStage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllClassStages(): List<ClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllClassStagesInTerm(termId: Int): List<ClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteClassStage(classId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllClassesStages(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllClassesInTermStages(termId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllReportsOnCourse(classId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class>  = dbi.withHandle<List<Class>, Exception>{
        val select = "select * from ${CourseDAOImpl.COURSE_CLASS_TABLE}" +
                "where ${CourseDAOImpl.COURSE_ID} = :courseId and ${TermDAOImpl.TERM_ID} = :termId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .mapTo(Class::class.java)
                .list()
    }

    override fun getClassOnSpecificTermOfCourse(courseId: Int, termId: Int, classId: Int): Class  = dbi.withHandle<Class, Exception>{
        val select = "select * from ${CourseDAOImpl.COURSE_CLASS_TABLE}" +
                "where ${CourseDAOImpl.COURSE_ID} = :courseId and ${TermDAOImpl.TERM_ID} = :termId and $CLASS_ID = :classId"
        it.createQuery(select)
                .bind("courseId", courseId)
                .bind("termId", termId)
                .bind("classId", classId)
                .mapTo(Class::class.java)
                .findOnly()
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

    override fun deleteAllReports(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllReportsOfClass(classId: Int): List<ClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getReportOfClass(classId: Int, reportId: Int): ClassReport {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion> = dbi.withHandle<List<ClassVersion>, Exception> {
        val select = "select * from $CLASS_VERSION_TABLE where $CLASS_ID = :classId"
        it.createQuery(select)
                .bind("classId", classId)
                .mapTo(ClassVersion::class.java)
                .list()
    }

    override fun getVersionOfSpecificClass(classId: Int, version: Int): ClassVersion = dbi.withHandle<ClassVersion, Exception> {
        val select = "select * from $CLASS_VERSION_TABLE where $CLASS_ID = :classId and $CLASS_VERSION = :version"
        it.createQuery(select)
                .bind("classId", classId)
                .bind("version", version)
                .mapTo(ClassVersion::class.java)
                .findOnly()
    }

}