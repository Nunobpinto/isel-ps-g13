package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.inputModel.ClassInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ClassDAOImpl : ClassDAO {

    companion object {
        //TABLE NAMES
        const val CLASS_TABLE = "class"
        const val COURSE_CLASS_TABLE = "course_class"
        const val CLASS_REPORT_TABLE = "class_report"
        const val CLASS_STAGE_TABLE = "class_stage"
        const val CLASS_VERSION_TABLE = "class_stage"
        // FIELDS
        const val CLASS_TERM_ID = "term_id"
        const val CLASS_ID = "class_id"
        const val CLASS_VERSION = "class_version"
        const val CLASS_NAME = "class_name"
        const val CLASS_CREATED_BY = "created_by"
        const val CLASS_TIMESTAMP = "timestamp"
        const val CLASS_VOTES = "votes"
        const val CLASS_STAGE_ID = "course_stage_id"
        // COURSE_CLASS_FIELDS
        const val CRS_CLASS_CLASS_ID = "class_id"
        const val CRS_CLASS_TERM_ID = "term_id"
        const val CRS_CLASS_COURSE_ID = "course_id"
        const val CRS_CLASS_VOTES = "votes"
        const val CRS_CLASS_TIMESTAMP = "time_stamp"
    }

    @Autowired
    lateinit var handle: Handle

    override fun getSpecificClass(classId: Int): Optional<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllClasses(): List<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createClass(clazz: Class): Optional<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificClass(classId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateClass(updatedClass: Class): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnClass(classId: Int, vote: Vote): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getClassOnSpecificTermOfCourse(courseId: Int, termId: Int, classId: Int): Optional<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllStagedClasses(): List<ClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificStagedClass(stageId: Int): Optional<ClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createStagingClass(inputClass: ClassInputModel): Optional<ClassStage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificStagedClass(stageId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnStagedClass(stageId: Int, vote: Vote): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllStagedClasses(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllReportsFromClass(classId: Int): List<ClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificReportFromClass(classId: Int, reportId: Int): Optional<ClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reportClass(classId: Int, report: ClassReport): Optional<ClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun voteOnReportOfClass(classId: Int, reportId: Int, vote: Vote): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllReportsInClass(classId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificReportOfClass(reportId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createClassVersion(classVersion: ClassVersion): Optional<ClassVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getVersionOfSpecificClass(classId: Int, versionId: Int): Optional<ClassVersion> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllVersionsOfClass(courseId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSpecificVersionOfClass(courseId: Int, versionId: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}