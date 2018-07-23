package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
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
        const val CLASS_REPORT_TABLE = "class_report"
        const val CLASS_STAGE_TABLE = "class_stage"
        const val CLASS_VERSION_TABLE = "class_version"
        const val CLASS_MISC_UNIT_TABLE = "class_misc_unit"
        const val CLASS_MISC_UNIT_STAGE_TABLE = "class_misc_unit_stage"
        const val COURSE_CLASS_REPORT_TABLE = "course_class_report"
        const val COURSE_CLASS_TABLE = "course_class"
        const val COURSE_CLASS_STAGE_TABLE = "course_class_stage"
        // CLASS FIELDS
        const val CLASS_TERM_ID = "term_id"
        const val CLASS_ID = "class_id"
        const val CLASS_VERSION = "class_version"
        const val CLASS_NAME = "class_name"
        const val CLASS_CREATED_BY = "created_by"
        const val CLASS_TIMESTAMP = "time_stamp"
        const val CLASS_VOTES = "votes"
        const val CLASS_REPORT_ID = "class_report_id"
        const val CLASS_REPORTED_BY = "reported_by"
        const val CLASS_STAGE_ID = "class_stage_id"
        // CLASS_MISC_UNIT FIELDS
        const val CLASS_MISC_UNIT_TYPE = "misc_type"
        const val CLASS_MISC_UNIT_ID = "class_misc_unit_id"
        const val CLASS_MISC_UNIT_COURSE_CLASS_ID = "course_class_id"
        const val CLASS_MISC_UNIT_STAGE_ID = "class_misc_unit_stage_id"
        // COURSE_CLASS_FIELDS
        const val COURSE_CLASS_ID = "course_class_id"
        const val COURSE_CLASS_TERM_ID = "term_id"
        const val COURSE_CLASS_CLASS_ID = "class_id"
        const val COURSE_CLASS_VOTES = "votes"
        const val COURSE_CLASS_TIMESTAMP = "time_stamp"
        const val COURSE_CLASS_REPORT_ID = "report_id"
        const val COURSE_CLASS_REPORTED_BY = "reported_by"
        const val COURSE_CLASS_COURSE_ID = "course_id"
        const val COURSE_CLASS_STAGE_ID = "course_class_stage_id"
        const val COURSE_CLASS_CREATED_BY = "created_by"
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
                    "$CLASS_ID, " +
                    "$CLASS_VERSION, " +
                    "$CLASS_CREATED_BY, " +
                    "$CLASS_NAME, " +
                    "$CLASS_TERM_ID, " +
                    "$CLASS_VOTES, " +
                    "$CLASS_TIMESTAMP " +
                    ")" +
                    "values(:classId, :version, :createdBy, :className, :termId, :votes, :timestamp)"
            )
                    .bind("classId", klass.classId)
                    .bind("version", klass.version)
                    .bind("createdBy", klass.createdBy)
                    .bind("className", klass.className)
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
                            "$CLASS_NAME = :className, $CLASS_TERM_ID = termId, $CLASS_VOTES = votes," +
                            "$CLASS_TIMESTAMP = timestamp" +
                            "where $CLASS_ID = :classId"
            )
                    .bind("version", updatedClass.version)
                    .bind("createdBy", updatedClass.createdBy)
                    .bind("className", updatedClass.className)
                    .bind("termId", updatedClass.termId)
                    .bind("votes", updatedClass.votes)
                    .bind("timestamp", updatedClass.timestamp)
                    .bind("classId", updatedClass.classId)
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

    override fun getTermIdFromSpecificClass(classId: Int): Int =
            handle.createQuery(
                    "select ${ClassDAOImpl.CLASS_TERM_ID} " +
                            "from ${ClassDAOImpl.CLASS_TABLE} " +
                            "where ${ClassDAOImpl.CLASS_ID} = :classId"
            )
                    .bind("classId", classId)
                    .mapTo(Int::class.java).findOnly()

    override fun getAllClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getClassOnSpecificTermOfCourse(courseId: Int, termId: Int, classId: Int): Optional<Class> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllStagedClasses(): List<ClassStage> =
            handle.createQuery("select * from $CLASS_STAGE_TABLE")
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
                    "insert into $CLASS_STAGE_TABLE (" +
                            "$CLASS_TERM_ID, " +
                            "$CLASS_NAME, " +
                            "$CLASS_CREATED_BY, " +
                            "$CLASS_VOTES, " +
                            "$CLASS_TIMESTAMP " +
                            ") " +
                            "values(:termId, :className, :reportedBy, :votes, :timestamp)"
            )
                    .bind("termId", classStage.termId)
                    .bind("className", classStage.className)
                    .bind("reportedBy", classStage.createdBy)
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
        return handle.createUpdate(
                "update $CLASS_STAGE_TABLE set $CLASS_VOTES = :votes " +
                        "where $CLASS_STAGE_ID = :stageId"
        )
                .bind("votes", votes)
                .bind("stageId", stageId)
                .execute()
    }

    override fun deleteAllStagedClasses(): Int =
            handle.createUpdate("delete from $CLASS_STAGE_TABLE").execute()

    override fun getAllReportsFromClass(classId: Int): List<ClassReport> =
            handle.createQuery(
                    "select * from $CLASS_REPORT_TABLE where $CLASS_ID = :classId"
            )
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
                    "insert into $CLASS_REPORT_TABLE (" +
                            "$CLASS_REPORT_ID, " +
                            "$CLASS_ID, " +
                            "$CLASS_TERM_ID," +
                            "$CLASS_NAME, " +
                            "$CLASS_REPORTED_BY, " +
                            "$CLASS_VOTES, " +
                            "$CLASS_TIMESTAMP " +
                            ") " +
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
        var votes = handle.createQuery(
                "select $CLASS_VOTES from $CLASS_REPORT_TABLE " +
                        "where $CLASS_REPORT_ID = :reportId " +
                        "and $CLASS_ID = :classId " +
                        ")"
        )
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
            handle.createUpdate(
                    "delete from $CLASS_REPORT_TABLE " +
                            "where $CLASS_REPORT_ID = :reportId " +
                            "and $CLASS_ID = :classId"
            )
                    .bind("reportId", reportId)
                    .bind("classId", classId)
                    .execute()

    override fun createClassVersion(classVersion: ClassVersion): Optional<ClassVersion> =
            handle.createUpdate(
                    "insert into $CLASS_VERSION_TABLE (" +
                            "$CLASS_ID, " +
                            "$CLASS_TERM_ID, " +
                            "$CLASS_VERSION," +
                            "$CLASS_NAME, " +
                            "$CLASS_CREATED_BY, " +
                            "$CLASS_TIMESTAMP " +
                            ")" +
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
            handle.createQuery("select * from $CLASS_VERSION_TABLE where $CLASS_ID = :classId")
                    .bind("classId", classId)
                    .mapTo(ClassVersion::class.java)
                    .list()

    override fun getVersionOfSpecificClass(classId: Int, versionId: Int): Optional<ClassVersion> =
            handle.createQuery(
                    "select * from $CLASS_VERSION_TABLE " +
                            "where $CLASS_ID = :classId " +
                            "and $CLASS_VERSION = :versionId"
            )
                    .bind("classId", classId)
                    .bind("versionId", versionId)
                    .mapTo(ClassVersion::class.java)
                    .findFirst()

    override fun deleteAllVersionsOfClass(classId: Int): Int =
            handle.createUpdate("delete from $CLASS_VERSION_TABLE where $CLASS_ID = :classId")
                    .bind("classId", classId)
                    .execute()

    override fun deleteSpecificVersionOfClass(classId: Int, versionId: Int): Int =
            handle.createUpdate(
                    "delete from $CLASS_VERSION_TABLE " +
                            "where $CLASS_ID = :classId " +
                            "and $CLASS_VERSION = :versionId")
                    .bind("classId", classId)
                    .bind("versionId", versionId)
                    .execute()

    override fun getClassMiscUnit(courseClassId: Int): Optional<ClassMiscUnit> =
            handle.createQuery(
                    "select * from $CLASS_MISC_UNIT_TABLE " +
                            "where $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId"
            )
                    .bind("courseClassId", courseClassId)
                    .mapTo(ClassMiscUnit::class.java)
                    .findFirst()

    override fun getAllCoursesOfClass(classId: Int): List<Course> =
            handle.createQuery(
                    "select C.${CourseDAOImpl.COURSE_ID}, " +
                            "C.${CourseDAOImpl.COURSE_ORGANIZATION_ID}, " +
                            "C.${CourseDAOImpl.COURSE_VERSION}, " +
                            "C.${CourseDAOImpl.COURSE_CREATED_BY}, " +
                            "C.${CourseDAOImpl.COURSE_FULL_NAME}, " +
                            "C.${CourseDAOImpl.COURSE_SHORT_NAME}," +
                            "C.${CourseDAOImpl.COURSE_VOTES}," +
                            "C.${CourseDAOImpl.COURSE_TIMESTAMP} " +
                            "from ${CourseDAOImpl.COURSE_TABLE} as C " +
                            "inner join $COURSE_CLASS_TABLE as CC " +
                            "on C.${CourseDAOImpl.COURSE_ID} = CC.$COURSE_CLASS_COURSE_ID" +
                            "where CC.$COURSE_CLASS_CLASS_ID = :classId"
            )
                    .bind("classId", classId)
                    .mapTo(Course::class.java)
                    .list()

    override fun getSpecificCourseOfClass(classId: Int, courseId: Int): Optional<Course> =
            handle.createQuery(
                    "select C.${CourseDAOImpl.COURSE_ID}, " +
                            "C.${CourseDAOImpl.COURSE_ORGANIZATION_ID}, " +
                            "C.${CourseDAOImpl.COURSE_VERSION}, " +
                            "C.${CourseDAOImpl.COURSE_CREATED_BY}, " +
                            "C.${CourseDAOImpl.COURSE_FULL_NAME}, " +
                            "C.${CourseDAOImpl.COURSE_SHORT_NAME}, " +
                            "C.${CourseDAOImpl.COURSE_VOTES}, " +
                            "C.${CourseDAOImpl.COURSE_TIMESTAMP} from ${CourseDAOImpl.COURSE_TABLE} as C " +
                            "inner join $COURSE_CLASS_TABLE as CC " +
                            "on C.${CourseDAOImpl.COURSE_ID} = CC.$COURSE_CLASS_COURSE_ID" +
                            "where CC.$COURSE_CLASS_CLASS_ID = :classId " +
                            "and CC.$COURSE_CLASS_COURSE_ID = :courseId"
            )
                    .bind("classId", classId)
                    .bind("courseId", courseId)
                    .mapTo(Course::class.java)
                    .findFirst()

    override fun voteOnCourseInClass(classId: Int, courseId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $COURSE_CLASS_VOTES " +
                        "from $COURSE_CLASS_TABLE" +
                        "where $COURSE_CLASS_CLASS_ID = :classId " +
                        "and $COURSE_CLASS_COURSE_ID = :courseId "
        )
                .bind("classId", classId)
                .bind("courseId", courseId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) votes.dec() else votes.inc()
        return handle.createUpdate(
                "update $COURSE_CLASS_TABLE " +
                        "set $COURSE_CLASS_VOTES = :votes " +
                        "where $COURSE_CLASS_CLASS_ID = :classId " +
                        "and $COURSE_CLASS_COURSE_ID = :courseId"
        )
                .bind("classId", classId)
                .bind("courseId", courseId)
                .execute()
    }

    override fun deleteAllCoursesInClass(classId: Int): Int =
            handle.createUpdate(
                    "delete from $COURSE_CLASS_TABLE " +
                            "where $COURSE_CLASS_CLASS_ID = :classId"
            )
                    .bind("classId", classId)
                    .execute()

    override fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int =
            handle.createUpdate(
                    "delete from $COURSE_CLASS_TABLE " +
                            "where $COURSE_CLASS_CLASS_ID = :classId " +
                            "and $COURSE_CLASS_COURSE_ID = :courseId"
            )
                    .bind("classId", classId)
                    .bind("courseId", courseId)
                    .execute()

    override fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReport> =
            handle.createQuery(
                    "select * from $COURSE_CLASS_REPORT_TABLE " +
                            "where $COURSE_CLASS_CLASS_ID = :classId " +
                            "and $COURSE_CLASS_COURSE_ID = :courseId"
            )
                    .bind("classId", classId)
                    .bind("courseId", courseId)
                    .mapTo(CourseClassReport::class.java)
                    .list()

    override fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport> =
            handle.createQuery(
                    "select * from $COURSE_CLASS_REPORT_TABLE " +
                            "where $COURSE_CLASS_CLASS_ID = :classId " +
                            "and $COURSE_CLASS_COURSE_ID = :courseId " +
                            "and $COURSE_CLASS_REPORT_ID = :reportId"
            )
                    .bind("classId", classId)
                    .bind("courseId", courseId)
                    .bind("reportId", reportId)
                    .mapTo(CourseClassReport::class.java)
                    .findFirst()

    override fun voteOnReportOfCourseClass(classId: Int, courseId: Int, reportId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $COURSE_CLASS_VOTES from $COURSE_CLASS_REPORT_TABLE " +
                        "where $COURSE_CLASS_CLASS_ID = :classId" +
                        "and $COURSE_CLASS_COURSE_ID = :courseId" +
                        "and $COURSE_CLASS_REPORT_ID = :reportId"
        )
                .bind("classId", classId)
                .bind("courseId", courseId)
                .bind("reportId", reportId)
                .mapTo(Int::class.java).findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes

        return handle.createUpdate(
                "update $COURSE_CLASS_REPORT_TABLE set $COURSE_CLASS_VOTES = :votes " +
                        "where $COURSE_CLASS_CLASS_ID = :classId " +
                        "and $COURSE_CLASS_COURSE_ID = :courseId" +
                        "and $COURSE_CLASS_REPORT_ID = :reportId"
        )
                .bind("votes", votes)
                .bind("classId", classId)
                .bind("courseId", courseId)
                .bind("reportId", reportId)
                .execute()
    }

    override fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int =
            handle.createUpdate(
                    "delete from $COURSE_CLASS_REPORT_TABLE " +
                            "where $COURSE_CLASS_CLASS_ID = :classId" +
                            "and $COURSE_CLASS_COURSE_ID = :courseId"
            )
                    .bind("classId", classId)
                    .bind("courseId", courseId)
                    .execute()

    override fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int =
            handle.createUpdate(
                    "delete from $COURSE_CLASS_REPORT_TABLE " +
                            "where $COURSE_CLASS_CLASS_ID = :classId " +
                            "and $COURSE_CLASS_COURSE_ID = :courseId " +
                            "and $COURSE_CLASS_REPORT_ID = :reportId"
            )
                    .bind("classId", classId)
                    .bind("courseId", courseId)
                    .bind("reportId", reportId)
                    .execute()

    override fun getStageEntriesOfCoursesInClass(classId: Int): List<CourseClassStage> =
            handle.createQuery(
                    "select * from $COURSE_CLASS_STAGE_TABLE" +
                            "where $COURSE_CLASS_CLASS_ID = :classId"
            )
                    .bind("classId", classId)
                    .mapTo(CourseClassStage::class.java)
                    .toList()


    override fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage> =
            handle.createQuery(
                    "select * from $COURSE_CLASS_STAGE_TABLE" +
                            "where $COURSE_CLASS_CLASS_ID = :classId" +
                            "and $COURSE_CLASS_STAGE_ID = :stageId"
            )
                    .bind("classId", classId)
                    .bind("stageId", stageId)
                    .mapTo(CourseClassStage::class.java)
                    .findFirst()

    override fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: Vote): Int {
        var votes = handle.createQuery(
                "select $COURSE_CLASS_VOTES from $COURSE_CLASS_STAGE_TABLE " +
                        "where $COURSE_CLASS_STAGE_ID = :stageId" +
                        "and $COURSE_CLASS_CLASS_ID = :classId"
        )
                .bind("stageId", stageId)
                .bind("classId", classId)
                .mapTo(Int::class.java)
                .findOnly()
        votes = if (vote == Vote.Down) --votes else ++votes
        return handle.createUpdate(
                "update $COURSE_CLASS_STAGE_TABLE set $COURSE_CLASS_VOTES = :votes " +
                        "where $COURSE_CLASS_STAGE_ID = :stageId" +
                        "and $COURSE_CLASS_CLASS_ID = :classId"
        )
                .bind("votes", votes)
                .bind("stageId", stageId)
                .bind("classId", classId)
                .execute()
    }

    override fun deleteStagedEntriesOfCourseInSpecificClass(classId: Int): Int =
            handle.createUpdate(
                    "delete from $COURSE_CLASS_STAGE_TABLE" +
                            "where $COURSE_CLASS_CLASS_ID = :classId"
            )
                    .bind("classId", classId)
                    .execute()

    override fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int =
            handle.createUpdate(
                    "delete from $COURSE_CLASS_STAGE_TABLE" +
                            "where $COURSE_CLASS_STAGE_ID = :stageId" +
                            "and $COURSE_CLASS_CLASS_ID = :classId"
            )
                    .bind("stageId", stageId)
                    .bind("classId", classId)
                    .execute()

    override fun addCourseToClass(courseClass: CourseClass): Optional<CourseClass> =
            handle.createUpdate(
                    "insert into $COURSE_CLASS_TABLE" +
                            "($COURSE_CLASS_COURSE_ID, " +
                            "$COURSE_CLASS_CLASS_ID," +
                            "$COURSE_CLASS_TERM_ID, " +
                            "$COURSE_CLASS_CREATED_BY, " +
                            "$COURSE_CLASS_VOTES, " +
                            "$COURSE_CLASS_TIMESTAMP) " +
                            "values(:courseId, :classId, :termId, :createdBy, :votes, :timestamp)")
                    .bind("courseId", courseClass.courseId)
                    .bind("classId", courseClass.classId)
                    .bind("termId", courseClass.termId)
                    .bind("createdBy", courseClass.createdBy)
                    .bind("votes", courseClass.votes)
                    .bind("timestamp", courseClass.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseClass::class.java)
                    .findFirst()


    override fun reportCourseInClass(courseClassReport: CourseClassReport): Optional<CourseClassReport> =
            handle.createUpdate(
                    "insert into $COURSE_CLASS_REPORT_TABLE (" +
                            "$COURSE_CLASS_ID, " +
                            "$COURSE_CLASS_COURSE_ID, " +
                            "$COURSE_CLASS_CLASS_ID, " +
                            "$COURSE_CLASS_TERM_ID, " +
                            "$COURSE_CLASS_REPORTED_BY, " +
                            "$COURSE_CLASS_VOTES, " +
                            "$COURSE_CLASS_TIMESTAMP " +
                            ") " +
                            "values(:courseClassId, :classId, :courseId, :termId, :reportedBy, :votes, :timestamp)"
            )
                    .bind("courseClassId", courseClassReport.courseClassId)
                    .bind("classId", courseClassReport.classId)
                    .bind("courseId", courseClassReport.courseId)
                    .bind("termId", courseClassReport.termId)
                    .bind("reportedBy", courseClassReport.reportedBy)
                    .bind("votes", courseClassReport.votes)
                    .bind("timestamp", courseClassReport.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseClassReport::class.java)
                    .findFirst()

    override fun getCourseClass(classId: Int, courseId: Int): Optional<CourseClass> =
            handle.createQuery("select * from $COURSE_CLASS_TABLE " +
                    "where $COURSE_CLASS_COURSE_ID = :courseId " +
                    "and $COURSE_CLASS_CLASS_ID = :classId "
            )
                    .bind("courseId", courseId)
                    .bind("classId", classId)
                    .mapTo(CourseClass::class.java)
                    .findFirst()

    override fun updateCourseClass(updatedCourseClass: CourseClass): Optional<CourseClass> =
            handle.createUpdate("update $COURSE_CLASS_TABLE SET " +
                    "$COURSE_CLASS_COURSE_ID = :courseId, " +
                    "$COURSE_CLASS_CLASS_ID = :classId, " +
                    "$COURSE_CLASS_TERM_ID = :termId, " +
                    "$COURSE_CLASS_VOTES = :votes, " +
                    "$COURSE_CLASS_TIMESTAMP = :timestamp " +
                    "where $COURSE_CLASS_ID = :courseClassId"
            )
                    .bind("courseId", updatedCourseClass.courseId)
                    .bind("classId", updatedCourseClass.classId)
                    .bind("termId", updatedCourseClass.termId)
                    .bind("votes", updatedCourseClass.votes)
                    .bind("timestamp", updatedCourseClass.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseClass::class.java)
                    .findFirst()

    override fun deleteSpecificReportOnCourseClass(courseClassId: Int, reportId: Int): Int =
            handle.createUpdate("delete from $COURSE_CLASS_TABLE " +
                    "where $COURSE_CLASS_ID = :courseClassId" +
                    "and $COURSE_CLASS_REPORT_ID = reportId"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("reportId", reportId)
                    .execute()

    override fun createStagingCourseInClass(courseClassStage: CourseClassStage): Optional<CourseClassStage> =
            handle.createUpdate(
                    "insert into $COURSE_CLASS_STAGE_TABLE (" +
                            "$COURSE_CLASS_COURSE_ID, " +
                            "$COURSE_CLASS_CLASS_ID, " +
                            "$COURSE_CLASS_TERM_ID, " +
                            "$COURSE_CLASS_CREATED_BY, " +
                            "$COURSE_CLASS_VOTES, " +
                            "$COURSE_CLASS_TIMESTAMP " +
                            ")" +
                            "values(:courseId, :classId, :termId, :createdBy, :votes, :timestamp)"
            )
                    .bind("courseId", courseClassStage.courseId)
                    .bind("classId", courseClassStage.classId)
                    .bind("termId", courseClassStage.termId)
                    .bind("createdBy", courseClassStage.createdBy)
                    .bind("votes", courseClassStage.votes)
                    .bind("timestamp", courseClassStage.timestamp)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(CourseClassStage::class.java)
                    .findFirst()

    override fun getCourseClassStage(stageId: Int): Optional<CourseClassStage> =
            handle.createQuery(
                    "select * from $COURSE_CLASS_STAGE_TABLE" +
                            "where $COURSE_CLASS_STAGE_ID = :stageId"
            )
                    .bind("stageId", stageId)
                    .mapTo(CourseClassStage::class.java)
                    .findFirst()

    override fun getCourseClassId(classId: Int, courseId: Int): Int =
            handle.createQuery(
                    "select ${ClassDAOImpl.COURSE_CLASS_ID} from ${ClassDAOImpl.COURSE_CLASS_TABLE}" +
                            "where ${ClassDAOImpl.COURSE_CLASS_CLASS_ID} = :classId" +
                            "and ${ClassDAOImpl.COURSE_CLASS_COURSE_ID} = :courseId"
            )
                    .bind("classId", classId)
                    .bind("courseId", courseId)
                    .mapTo(Int::class.java)
                    .findOnly()

    override fun getClassMiscUnitId(courseClassId: Int): Int =
            handle.createQuery(
                    "select $CLASS_MISC_UNIT_ID from $CLASS_MISC_UNIT_TABLE" +
                            "where $COURSE_CLASS_ID = :courseClassId"

            )
                    .bind("courseClassId", courseClassId)
                    .mapTo(Int::class.java)
                    .findOnly()

    override fun createClassMiscUnit(courseClassId: Int, miscType: String): Optional<ClassMiscUnit> =
            handle.createUpdate(
                    "insert into $CLASS_MISC_UNIT_TABLE (" +
                            "$CLASS_MISC_UNIT_TYPE, " +
                            "$CLASS_MISC_UNIT_COURSE_CLASS_ID " +
                            ")" +
                            "values (:miscType, :courseClassId)"
            )
                    .bind("miscType", miscType)
                    .bind("courseClassId", courseClassId)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ClassMiscUnit::class.java)
                    .findFirst()

    override fun createStagingClassMiscUnit(courseClassId: Int, s: String): Optional<ClassMiscUnitStage> =
            handle.createUpdate(
                    "insert into $CLASS_MISC_UNIT_STAGE_TABLE (" +
                            "$CLASS_MISC_UNIT_TYPE, " +
                            "$CLASS_MISC_UNIT_COURSE_CLASS_ID " +
                            ") " +
                            "values(:miscType, :courseClassId)"
            )
                    .bind("miscType", "Lecture")
                    .bind("courseClassId", courseClassId)
                    .executeAndReturnGeneratedKeys()
                    .mapTo(ClassMiscUnitStage::class.java)
                    .findFirst()

    override fun deleteAllClassMiscUnitsFromTypeOfCourseInClass(courseClassId: Int, miscType: String): Int =
            handle.createUpdate(
                    "delete from ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE}" +
                            "where ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                            "and ${ClassDAOImpl.CLASS_MISC_UNIT_TYPE} = :miscType"
            )
                    .bind("courseClassId", courseClassId)
                    .bind("miscType", miscType)
                    .execute()

    override fun deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId: Int, stageId: Int): Int =
        handle.createUpdate(
                "delete from ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_TABLE}" +
                        "where ${ClassDAOImpl.CLASS_MISC_UNIT_COURSE_CLASS_ID} = :courseClassId" +
                        "and ${ClassDAOImpl.CLASS_MISC_UNIT_STAGE_ID} = :stageId"
        )
                .bind("courseClassId", courseClassId)
                .bind("stageId", stageId)
                .execute()


}