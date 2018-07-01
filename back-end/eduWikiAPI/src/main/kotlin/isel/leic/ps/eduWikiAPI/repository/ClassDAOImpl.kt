package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import org.jdbi.v3.core.Handle
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
        const val CLASS_REPORT_ID = "reportId"
        const val CLASS_REPORTED_BY = "reported_by"
        const val CLASS_STAGE_ID = "course_stage_id"
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

    override fun getAllStagedClasses(): List<ClassStage> =
            handle.createQuery(
                    "select * from $CLASS_STAGE_TABLE"
            )
                    .mapTo(ClassStage::class.java)
                    .list()

    override fun getSpecificStagedClass(stageId: Int): Optional<ClassStage> =
            handle.createQuery(
                    "select * from $CLASS_STAGE_TABLE where $CLASS_STAGE_ID = :stageId"
            )
                    .bind("stageId", stageId)
                    .mapTo(ClassStage::class.java)
                    .findFirst()

    override fun createStagedClass(classStage: ClassStage): Optional<ClassStage> =
            handle.createUpdate(
                    "insert into $CLASS_STAGE_TABLE " +
                            "($CLASS_TERM_ID, $CLASS_NAME, $CLASS_CREATED_BY, " +
                            "$CLASS_VOTES, $CLASS_TIMESTAMP) " +
                            "values(:termId, :className, :createdBy, :votes, :timestamp)"
            )
                    .bind("termId", classStage.termId)
                    .bind("className", classStage.className)
                    .bind("createdBy", classStage.createdBy)
                    .bind("votes", classStage.votes)
                    .bind("timestamp", classStage.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ClassStage::class.java)
                    .findFirst()

    override fun deleteSpecificStagedClass(stageId: Int): Int =
            handle.createUpdate("delete from $CLASS_STAGE_TABLE where $CLASS_STAGE_ID = :stageId")
                    .bind("stageId", stageId)
                    .execute()

    override fun voteOnStagedClass(stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $CLASS_VOTES from $CLASS_STAGE_TABLE where $CLASS_STAGE_ID = :stageId")
                .bind("stageId", stageId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate("update $CLASS_STAGE_TABLE set $CLASS_VOTES = :votes where $CLASS_STAGE_ID = :stageId")
                .bind("votes", votes)
                .bind("stageId", stageId)
                .execute()
    }

    override fun deleteAllStagedClasses(): Int =
            handle.createUpdate("delete from $CLASS_STAGE_TABLE").execute()

    override fun getAllReportsFromClass(classId: Int): List<ClassReport> =
            handle.createQuery("select * from $CLASS_REPORT_TABLE where $CLASS_ID = :classId")
                    .bind("classId", classId)
                    .mapTo(ClassReport::class.java)
                    .list()

    override fun getSpecificReportFromClass(classId: Int, reportId: Int): Optional<ClassReport> =
            handle.createQuery(
                    "select * from $CLASS_REPORT_TABLE " +
                            "where $CLASS_ID = :classId and $CLASS_REPORT_ID = :reportId"
            )
                    .bind("classId", classId)
                    .bind("reportId", reportId)
                    .mapTo(ClassReport::class.java)
                    .findFirst()

    override fun reportClass(classId: Int, report: ClassReport): Optional<ClassReport> =
            handle.createUpdate(
                    "insert into $CLASS_REPORT_TABLE " +
                            "($CLASS_REPORT_ID, $CLASS_ID, $CLASS_TERM_ID," +
                            "$CLASS_NAME, $CLASS_REPORTED_BY, $CLASS_VOTES, $CLASS_TIMESTAMP) " +
                            "values(:reportId, :classId, :termId, :className, :reportedBy, :votes, :timestamp)"
            )
                    .bind("reportId", report.reportId)
                    .bind("classId", classId)
                    .bind("termId", report.termId)
                    .bind("className", report.className)
                    .bind("reportedBy", report.reportedBy)
                    .bind("votes", report.votes)
                    .bind("timestamp", report.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ClassReport::class.java)
                    .findFirst()

    override fun voteOnReportOfClass(classId: Int, reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery("select $CLASS_VOTES from $CLASS_REPORT_TABLE where " +
                "$CLASS_REPORT_ID = :reportId and $CLASS_ID = :classId")
                .bind("reportId", reportId)
                .bind("classId", classId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate("update $CLASS_REPORT_TABLE set $CLASS_VOTES = :votes where " +
                "$CLASS_REPORT_ID = :reportId and $CLASS_ID = :classId")
                .bind("votes", votes)
                .bind("reportId", reportId)
                .bind("classId", classId)
                .execute()
    }

    override fun deleteAllReportsInClass(classId: Int): Int =
            handle.createUpdate("delete from $CLASS_REPORT_TABLE where $CLASS_ID = :classId")
                    .bind("classId", classId)
                    .execute()

    override fun deleteSpecificReportInClass(classId: Int, reportId: Int): Int =
            handle.createUpdate("delete from $CLASS_REPORT_TABLE where " +
                    "$CLASS_REPORT_ID = :reportId and $CLASS_ID = :classId")
                    .bind("reportId", reportId)
                    .bind("classId", classId)
                    .execute()

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

    override fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion> =
            handle.createQuery(
                    "select * from $CLASS_VERSION_TABLE where $CLASS_ID = :classId"
            )
                    .bind("classId", classId)
                    .mapTo(ClassVersion::class.java)
                    .list()

    override fun getVersionOfSpecificClass(classId: Int, versionId: Int): Optional<ClassVersion> =
            handle.createQuery("select * from $CLASS_VERSION_TABLE where $CLASS_ID = :classId and $CLASS_VERSION = :versionId")
                    .bind("classId", classId)
                    .bind("versionId", versionId)
                    .mapTo(ClassVersion::class.java)
                    .findFirst()

    override fun deleteAllVersionsOfClass(classId: Int): Int =
            handle.createUpdate("delete from $CLASS_VERSION_TABLE where $CLASS_ID = :classId")
                    .bind("classId", classId)
                    .execute()

    override fun deleteSpecificVersionOfClass(classId: Int, versionId: Int): Int =
            handle.createUpdate("delete from $CLASS_VERSION_TABLE where $CLASS_ID = :classId and $CLASS_VERSION = :versionId")
                    .bind("classId", classId)
                    .bind("versionId", versionId)
                    .execute()

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}