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

    override fun getAllClasses(): List<Class> =
            handle.createQuery("select * from $CLASS_TABLE")
                    .mapTo(Class::class.java)
                    .list()

    override fun getSpecificClass(classId: Int): Optional<Class> =
            handle.createQuery("select * from $CLASS_TABLE where ${CLASS_ID} = :classId")
                    .bind("classId", classId)
                    .mapTo(Class::class.java)
                    .findFirst()

    override fun createClass(klass: Class): Optional<Class> =
            handle.createUpdate("insert into $CLASS_TABLE (" +
                    "$CLASS_ID, $CLASS_VERSION, $CLASS_CREATED_BY" +
                    "$CLASS_NAME, $CLASS_TERM_ID, $CLASS_VOTES, " +
                    "$CLASS_TIMESTAMP)" +
                    "values(:classId, :version, :createdBy, :className, :termId, :votes, :timestamp)"
            )
                    .bind("classId", klass.id)
                    .bind("version", klass.version)
                    .bind("createdBy", klass.createdBy)
                    .bind("className", klass.fullName)
                    .bind("termId", klass.termId)
                    .bind("votes", klass.votes)
                    .bind("timestamp", klass.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(Class::class.java)
                    .findFirst()

    override fun deleteSpecificClass(classId: Int): Int =
            handle.createUpdate("delete from $CLASS_TABLE where $CLASS_ID = :classId")
                    .bind("classId", classId)
                    .execute()

    override fun updateClass(updatedClass: Class): Int =
            handle.createUpdate(
                    "update $CLASS_TABLE SET " +
                            "$CLASS_VERSION = :version, $CLASS_CREATED_BY = :createdBy," +
                            "$CLASS_NAME = :fullName, $CLASS_TERM_ID = termId, $CLASS_VOTES = votes," +
                            "$CLASS_TIMESTAMP = timestamp" +
                            "where $CLASS_ID = :classId"
            )
                    .bind("version", updatedClass.version)
                    .bind("createdBy", updatedClass.createdBy)
                    .bind("fullName", updatedClass.fullName)
                    .bind("termId", updatedClass.termId)
                    .bind("votes", updatedClass.votes)
                    .bind("timestamp", updatedClass.timestamp)
                    .bind("classId", updatedClass.id)
                    .execute()

    override fun voteOnClass(classId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $CLASS_VOTES from $CLASS_TABLE where $CLASS_ID = :classId")
                .bind("classId", classId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate("update $CLASS_TABLE set $CLASS_VOTES = :votes where $CLASS_ID = :classId")
                .bind("votes", votes)
                .bind("classId", classId)
                .execute()
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

    override fun getAllReportsFromClass(classId: Int): List<ClassReport> =
            handle.createQuery("select * from $CLASS_REPORT_TABLE where $CLASS_ID = :classId")
                    .bind("classId", classId)
                    .mapTo(ClassReport::class.java)
                    .list()

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

    override fun createClassVersion(classVersion: ClassVersion): Optional<ClassVersion> =
            handle.createUpdate("insert into $CLASS_VERSION_TABLE (" +
                    "$CLASS_ID, $CLASS_TERM_ID, $CLASS_VERSION," +
                    "$CLASS_NAME, $CLASS_CREATED_BY, $CLASS_TIMESTAMP)" +
                    "values(:classId, :termId, :version, :className, :createdBy, :timestamp)"
            )
                    .bind("classId", classVersion.classId)
                    .bind("termId", classVersion.termId)
                    .bind("version", classVersion.version)
                    .bind("className", classVersion.className)
                    .bind("createdBy", classVersion.createdBy)
                    .bind("timestamp", classVersion.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ClassVersion::class.java)
                    .findFirst()

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