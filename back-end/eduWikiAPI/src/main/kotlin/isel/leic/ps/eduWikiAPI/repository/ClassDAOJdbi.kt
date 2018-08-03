package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_STAGE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_STAGE_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_MISC_UNIT_TYPE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_TABLE
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.CLASS_TERM_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_ID
import isel.leic.ps.eduWikiAPI.repository.ClassDAOJdbi.Companion.COURSE_CLASS_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_CREATED_BY
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_FULL_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_ORGANIZATION_ID
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_SHORT_NAME
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_TABLE
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_TIMESTAMP
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_VERSION
import isel.leic.ps.eduWikiAPI.repository.CourseDAOJdbi.Companion.COURSE_VOTES
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import org.jdbi.v3.core.Handle
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

interface ClassDAOJdbi : ClassDAO {

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
        const val COURSE_CLASS_REPORT_ID = "course_class_report_id"
        const val COURSE_CLASS_REPORTED_BY = "reported_by"
        const val COURSE_CLASS_COURSE_ID = "course_id"
        const val COURSE_CLASS_STAGE_ID = "course_class_stage_id"
        const val COURSE_CLASS_CREATED_BY = "created_by"
    }

    @SqlQuery(
            "SELECT * FROM $CLASS_TABLE"
    )
    override fun getAllClasses(): List<Class>

    @SqlQuery(
            "SELECT * FROM $CLASS_TABLE WHERE $CLASS_ID = :classId"
    )
    override fun getSpecificClass(classId: Int): Optional<Class>

    @SqlUpdate(
            "INSERT INTO $CLASS_TABLE ( " +
                    "$CLASS_ID, " +
                    "$CLASS_VERSION, " +
                    "$CLASS_CREATED_BY, " +
                    "$CLASS_NAME, " +
                    "$CLASS_TERM_ID, " +
                    "$CLASS_VOTES, " +
                    "$CLASS_TIMESTAMP " +
                    ") " +
                    "VALUES(:klass.classId, :klass.version, :klass.createdBy, " +
                    ":klass.className, :klass.termId, :klass.votes, :klass.timestamp)"
    )
    @GetGeneratedKeys
    override fun createClass(klass: Class): Class

    @SqlUpdate(
            "DELETE FROM $CLASS_TABLE WHERE $CLASS_ID = :classId"
    )
    override fun deleteSpecificClass(classId: Int): Int

    @SqlUpdate(
            "UPDATE $CLASS_TABLE SET " +
                    "$CLASS_VERSION = :updatedClass.version, " +
                    "$CLASS_CREATED_BY = :updatedClass.createdBy, " +
                    "$CLASS_NAME = :updatedClass.className, " +
                    "$CLASS_TERM_ID = :updatedClass.termId, " +
                    "$CLASS_VOTES = :updatedClass.votes, " +
                    "$CLASS_TIMESTAMP = :updatedClass.timestamp " +
                    "WHERE $CLASS_ID = :classId"
    )
    @GetGeneratedKeys
    override fun updateClass(updatedClass: Class): Class

    @SqlQuery(
            "SELECT $CLASS_VOTES FROM $CLASS_TABLE " +
                    "WHERE $CLASS_ID = :classId"
    )
    override fun getClassVotes(classId: Int): Int

    @SqlUpdate(
            "UPDATE $CLASS_TABLE set $CLASS_VOTES = :votes " +
                    "WHERE $CLASS_ID = :classId"
    )
    override fun updateClassVotes(classId: Int, votes: Int): Int

    @SqlQuery(
            "SELECT $CLASS_TERM_ID " +
                    "FROM $CLASS_TABLE " +
                    "WHERE $CLASS_ID = :classId"
    )
    override fun getTermIdFromSpecificClass(classId: Int): Int

    @SqlQuery(
            "SELECT C.$CLASS_ID, " +
                    "C.$CLASS_VERSION, " +
                    "C.$CLASS_CREATED_BY, " +
                    "C.$CLASS_NAME, " +
                    "C.$CLASS_TERM_ID, " +
                    "C.$CLASS_VOTES, " +
                    "C.$CLASS_TIMESTAMP" +
                    "FROM $CLASS_TABLE AS C " +
                    "INNER JOIN $COURSE_CLASS_TABLE AS CC " +
                    "ON C.$CLASS_ID = CC.$COURSE_CLASS_ID " +
                    "WHERE CC.$COURSE_CLASS_COURSE_ID = :courseId " +
                    "AND CC.$COURSE_CLASS_TERM_ID = :termId"
    )
    override fun getAllClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class>

    @SqlQuery(
            "SELECT C.$CLASS_ID, " +
                    "C.$CLASS_VERSION, " +
                    "C.$CLASS_CREATED_BY, " +
                    "C.$CLASS_NAME, " +
                    "C.$CLASS_TERM_ID, " +
                    "C.$CLASS_VOTES, " +
                    "C.$CLASS_TIMESTAMP " +
                    "FROM $CLASS_TABLE AS C " +
                    "INNER JOIN $COURSE_CLASS_TABLE AS CC " +
                    "ON C.$CLASS_ID = CC.$COURSE_CLASS_ID " +
                    "WHERE CC.$COURSE_CLASS_COURSE_ID = :courseId " +
                    "AND CC.$COURSE_CLASS_TERM_ID = :termId " +
                    "AND CC.$COURSE_CLASS_CLASS_ID = :classId"
    )
    override fun getClassOnSpecificTermOfCourse(courseId: Int, termId: Int, classId: Int): Optional<Class>

    @SqlQuery(
            "SELECT * FROM $CLASS_STAGE_TABLE"
    )
    override fun getAllStagedClasses(): List<ClassStage>

    @SqlQuery(
            "SELECT * FROM $CLASS_STAGE_TABLE " +
                    "WHERE $CLASS_STAGE_ID = :stageId"
    )
    override fun getSpecificStagedClass(stageId: Int): Optional<ClassStage>

    @SqlUpdate(
            "INSERT INTO $CLASS_STAGE_TABLE ( " +
                    "$CLASS_TERM_ID, " +
                    "$CLASS_NAME, " +
                    "$CLASS_CREATED_BY, " +
                    "$CLASS_VOTES, " +
                    "$CLASS_TIMESTAMP " +
                    ") " +
                    "VALUES(:classStage.termId, :classStage.className, :classStage.createdBy, " +
                    ":classStage.votes, :classStage.timestamp)"
    )
    @GetGeneratedKeys
    override fun createStagedClass(classStage: ClassStage): ClassStage

    @SqlUpdate(
            "DELETE FROM $CLASS_STAGE_TABLE WHERE $CLASS_STAGE_ID = :stageId"
    )
    override fun deleteSpecificStagedClass(stageId: Int): Int

    @SqlQuery(
            "SELECT $CLASS_VOTES FROM $CLASS_STAGE_TABLE " +
                    "WHERE $CLASS_STAGE_ID = :stageId"
    )
    override fun getStagedClassVotes(stageId: Int): Int

    @SqlUpdate(
            "UPDATE $CLASS_STAGE_TABLE SET $CLASS_VOTES = :votes " +
                    "WHERE $CLASS_STAGE_ID = :stageId"
    )
    override fun updateStagedClassVotes(stageId: Int, votes: Int): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_STAGE_TABLE"
    )
    override fun deleteAllStagedClasses(): Int

    @SqlQuery(
            "SELECT * FROM $CLASS_REPORT_TABLE WHERE $CLASS_ID = :classId"
    )
    override fun getAllReportsFromClass(classId: Int): List<ClassReport>

    @SqlQuery(
            "SELECT * FROM $CLASS_REPORT_TABLE " +
                    "WHERE $CLASS_ID = :classId " +
                    "AND $CLASS_REPORT_ID = :reportId"
    )
    override fun getSpecificReportFromClass(classId: Int, reportId: Int): Optional<ClassReport>

    @SqlQuery(
            "INSERT INTO $CLASS_REPORT_TABLE ( " +
                    "$CLASS_REPORT_ID, " +
                    "$CLASS_ID, " +
                    "$CLASS_TERM_ID, " +
                    "$CLASS_NAME, " +
                    "$CLASS_REPORTED_BY, " +
                    "$CLASS_VOTES, " +
                    "$CLASS_TIMESTAMP " +
                    ") " +
                    "VALUES(:report.reportId, :classId, :report.termId, :report.className, " +
                    ":report.reportedBy, :report.votes, :report.timestamp)"
    )
    @GetGeneratedKeys
    override fun reportClass(classId: Int, report: ClassReport): ClassReport

    @SqlQuery(
            "SELECT $CLASS_VOTES FROM $CLASS_REPORT_TABLE " +
                    "WHERE $CLASS_REPORT_ID = :reportId " +
                    "AND $CLASS_ID = :classId "
    )
    override fun getReportedClassVotes(classId: Int, reportId: Int): Int

    @SqlUpdate(
            "UPDATE $CLASS_REPORT_TABLE SET $CLASS_VOTES = :votes " +
                    "WHERE $CLASS_REPORT_ID = :reportId " +
                    "AND $CLASS_ID = :classId"
    )
    override fun updateReportedClassVotes(classId: Int, reportId: Int, votes: Int): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_REPORT_TABLE WHERE $CLASS_ID = :classId"
    )
    override fun deleteAllReportsInClass(classId: Int): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_REPORT_TABLE " +
                    "WHERE $CLASS_REPORT_ID = :reportId " +
                    "AND $CLASS_ID = :classId"
    )
    override fun deleteSpecificReportInClass(classId: Int, reportId: Int): Int

    @SqlUpdate(
            "INSERT INTO $CLASS_VERSION_TABLE ( " +
                    "$CLASS_ID, " +
                    "$CLASS_TERM_ID, " +
                    "$CLASS_VERSION, " +
                    "$CLASS_NAME, " +
                    "$CLASS_CREATED_BY, " +
                    "$CLASS_TIMESTAMP " +
                    ") " +
                    "VALUES(:classVersion.classId, :classVersion.termId, :classVersion.version, " +
                    ":classVersion.className, :classVersion.createdBy, :classVersion.timestamp)"
    )
    @GetGeneratedKeys
    override fun createClassVersion(classVersion: ClassVersion): ClassVersion

    @SqlQuery(
            "SELECT * FROM $CLASS_VERSION_TABLE WHERE $CLASS_ID = :classId"
    )
    override fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion>

    @SqlQuery(
            "SELECT * FROM $CLASS_VERSION_TABLE " +
                    "WHERE $CLASS_ID = :classId " +
                    "AND $CLASS_VERSION = :versionId"
    )
    override fun getVersionOfSpecificClass(classId: Int, versionId: Int): Optional<ClassVersion>

    @SqlUpdate(
            "DELETE FROM $CLASS_VERSION_TABLE WHERE $CLASS_ID = :classId"
    )
    override fun deleteAllVersionsOfClass(classId: Int): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_VERSION_TABLE " +
                    "WHERE $CLASS_ID = :classId " +
                    "AND $CLASS_VERSION = :versionId"
    )
    override fun deleteSpecificVersionOfClass(classId: Int, versionId: Int): Int

    @SqlQuery(
            "SELECT * FROM $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId"
    )
    override fun getClassMiscUnit(courseClassId: Int): Optional<ClassMiscUnit>

    @SqlQuery("SELECT * FROM $COURSE_CLASS_TABLE WHERE $COURSE_CLASS_CLASS_ID = :classId")
    override fun getAllCoursesOfClass(classId: Int): List<CourseClass>

    @SqlQuery("SELECT * FROM $COURSE_CLASS_TABLE " +
            "WHERE $COURSE_CLASS_CLASS_ID = :classId AND $COURSE_CLASS_COURSE_ID = :courseId"
    )
    override fun getSpecificCourseOfClass(classId: Int, courseId: Int): Optional<CourseClass>

    @SqlQuery(
            "SELECT $COURSE_CLASS_VOTES " +
                    "FROM $COURSE_CLASS_TABLE" +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId " +
                    "AND $COURSE_CLASS_COURSE_ID = :courseId "
    )
    override fun getCourseClassVotes(classId: Int, courseId: Int): Int

    @SqlUpdate(
            "UPDATE $COURSE_CLASS_TABLE " +
                    "SET $COURSE_CLASS_VOTES = :votes " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId " +
                    "AND $COURSE_CLASS_COURSE_ID = :courseId"
    )
    override fun updateCourseClassVotes(classId: Int, courseId: Int, votes: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_CLASS_TABLE " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId"
    )
    override fun deleteAllCoursesInClass(classId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_CLASS_TABLE " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId " +
                    "AND $COURSE_CLASS_COURSE_ID = :courseId"
    )
    override fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int

    @SqlQuery("SELECT * FROM $COURSE_CLASS_REPORT_TABLE WHERE $COURSE_CLASS_ID = :courseClassId ")
    override fun getAllReportsOfCourseInClass(courseClassId: Int): List<CourseClassReport>

    @SqlQuery("SELECT * FROM $COURSE_CLASS_REPORT_TABLE WHERE $COURSE_CLASS_REPORT_ID = :reportId")
    override fun getSpecificReportOfCourseInClass(reportId: Int): Optional<CourseClassReport>

    @SqlQuery(
            "SELECT $COURSE_CLASS_VOTES FROM $COURSE_CLASS_REPORT_TABLE " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId" +
                    "AND $COURSE_CLASS_COURSE_ID = :courseId" +
                    "AND $COURSE_CLASS_REPORT_ID = :reportId"
    )
    override fun getReportedCourseClassVotes(classId: Int, courseId: Int, reportId: Int): Int

    @SqlUpdate(
            "UPDATE $COURSE_CLASS_REPORT_TABLE SET $COURSE_CLASS_VOTES = :votes " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId " +
                    "AND $COURSE_CLASS_COURSE_ID = :courseId" +
                    "AND $COURSE_CLASS_REPORT_ID = :reportId"
    )
    override fun updateReportedCourseClassVotes(classId: Int, courseId: Int, reportId: Int, votes: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_CLASS_REPORT_TABLE " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId " +
                    "AND $COURSE_CLASS_COURSE_ID = :courseId"
    )
    override fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_CLASS_REPORT_TABLE " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId " +
                    "AND $COURSE_CLASS_COURSE_ID = :courseId " +
                    "AND $COURSE_CLASS_REPORT_ID = :reportId"
    )
    override fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int

    @SqlQuery(
            "SELECT * FROM $COURSE_CLASS_STAGE_TABLE " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId"
    )
    override fun getStageEntriesOfCoursesInClass(classId: Int): List<CourseClassStage>

    @SqlQuery(
            "SELECT * FROM $COURSE_CLASS_STAGE_TABLE " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId " +
                    "AND $COURSE_CLASS_STAGE_ID = :stageId"
    )
    override fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage>

    @SqlQuery(
            "SELECT $COURSE_CLASS_VOTES FROM $COURSE_CLASS_STAGE_TABLE " +
                    "WHERE $COURSE_CLASS_STAGE_ID = :stageId" +
                    "AND $COURSE_CLASS_CLASS_ID = :classId"
    )
    override fun getStagedCourseClassVotes(classId: Int, stageId: Int): Int

    @SqlUpdate(
            "UPDATE $COURSE_CLASS_STAGE_TABLE SET $COURSE_CLASS_VOTES = :votes " +
                    "WHERE $COURSE_CLASS_STAGE_ID = :stageId" +
                    "AND $COURSE_CLASS_CLASS_ID = :classId"
    )
    override fun updateStagedCourseClassVotes(classId: Int, stageId: Int, votes: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_CLASS_STAGE_TABLE " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId"
    )
    override fun deleteStagedEntriesOfCourseInSpecificClass(classId: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_CLASS_STAGE_TABLE " +
                    "WHERE $COURSE_CLASS_STAGE_ID = :stageId " +
                    "AND $COURSE_CLASS_CLASS_ID = :classId"
    )
    override fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int

    @SqlUpdate(
            "INSERT INTO $COURSE_CLASS_TABLE ( " +
                    "$COURSE_CLASS_COURSE_ID, " +
                    "$COURSE_CLASS_CLASS_ID, " +
                    "$COURSE_CLASS_TERM_ID, " +
                    "$COURSE_CLASS_CREATED_BY, " +
                    "$COURSE_CLASS_VOTES, " +
                    "$COURSE_CLASS_TIMESTAMP " +
                    ") " +
                    "VALUES(:courseClass.courseId, :courseClass.classId, :courseClass.termId, " +
                    ":courseClass.createdBy, :courseClass.votes, :courseClass.timestamp)"
    )
    @GetGeneratedKeys
    override fun addCourseToClass(courseClass: CourseClass): CourseClass

    @SqlUpdate(
            "INSERT INTO $COURSE_CLASS_REPORT_TABLE ( " +
                    "$COURSE_CLASS_ID, " +
                    "$COURSE_CLASS_COURSE_ID, " +
                    "$COURSE_CLASS_CLASS_ID, " +
                    "$COURSE_CLASS_TERM_ID, " +
                    "$COURSE_CLASS_REPORTED_BY, " +
                    "$COURSE_CLASS_VOTES, " +
                    "$COURSE_CLASS_TIMESTAMP " +
                    ") " +
                    "VALUES(:courseClassReport.courseClassId, :courseClassReport.courseId, :courseClassReport.classId, " +
                    ":courseClassReport.termId, :courseClassReport.reportedBy, :courseClassReport.votes, :courseClassReport.timestamp)"
    )
    @GetGeneratedKeys
    override fun reportCourseInClass(courseClassReport: CourseClassReport): CourseClassReport

    @SqlQuery(
            "SELECT * FROM $COURSE_CLASS_TABLE " +
                    "WHERE $COURSE_CLASS_COURSE_ID = :courseId " +
                    "AND $COURSE_CLASS_CLASS_ID = :classId "
    )
    override fun getCourseClass(classId: Int, courseId: Int): Optional<CourseClass>

    @SqlUpdate(
            "UPDATE $COURSE_CLASS_TABLE SET " +
                    "$COURSE_CLASS_COURSE_ID = :updatedCourseClass.courseId, " +
                    "$COURSE_CLASS_CLASS_ID = :updatedCourseClass.classId, " +
                    "$COURSE_CLASS_TERM_ID = :updatedCourseClass.termId, " +
                    "$COURSE_CLASS_VOTES = :updatedCourseClass.votes, " +
                    "$COURSE_CLASS_TIMESTAMP = :updatedCourseClass.timestamp " +
                    "WHERE $COURSE_CLASS_ID = :updatedCourseClass.courseClassId"
    )
    @GetGeneratedKeys
    override fun updateCourseClass(updatedCourseClass: CourseClass): CourseClass

    @SqlUpdate(
            "DELETE FROM $COURSE_CLASS_REPORT_TABLE " +
                    "WHERE $COURSE_CLASS_ID = :courseClassId " +
                    "AND $COURSE_CLASS_REPORT_ID = :reportId"
    )
    override fun deleteSpecificReportOnCourseClass(courseClassId: Int, reportId: Int): Int

    @SqlUpdate(
            "INSERT INTO $COURSE_CLASS_STAGE_TABLE ( " +
                    "$COURSE_CLASS_COURSE_ID, " +
                    "$COURSE_CLASS_CLASS_ID, " +
                    "$COURSE_CLASS_TERM_ID, " +
                    "$COURSE_CLASS_CREATED_BY, " +
                    "$COURSE_CLASS_VOTES, " +
                    "$COURSE_CLASS_TIMESTAMP " +
                    ")" +
                    "VALUES(:courseClassStage.courseId, :courseClassStage.classId, :courseClassStage.termId, " +
                    ":courseClassStage.createdBy, :courseClassStage.votes, :courseClassStage.timestamp)"
    )
    @GetGeneratedKeys
    override fun createStagingCourseInClass(courseClassStage: CourseClassStage): CourseClassStage

    @SqlQuery(
            "SELECT * FROM $COURSE_CLASS_STAGE_TABLE " +
                    "WHERE $COURSE_CLASS_STAGE_ID = :stageId"
    )
    override fun getCourseClassStage(stageId: Int): Optional<CourseClassStage>

    @SqlQuery(
            "SELECT $COURSE_CLASS_ID FROM $COURSE_CLASS_TABLE " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId " +
                    "AND $COURSE_CLASS_COURSE_ID = :courseId"
    )
    override fun getCourseClassId(classId: Int, courseId: Int): Int

    @SqlQuery(
            "SELECT $CLASS_MISC_UNIT_ID FROM $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $COURSE_CLASS_ID = :courseClassId"
    )
    override fun getClassMiscUnitId(courseClassId: Int): Int

    @SqlUpdate(
            "INSERT INTO $CLASS_MISC_UNIT_TABLE ( " +
                    "$CLASS_MISC_UNIT_TYPE, " +
                    "$CLASS_MISC_UNIT_COURSE_CLASS_ID " +
                    ") " +
                    "VALUES (:miscType, :courseClassId)"
    )
    @GetGeneratedKeys
    override fun createClassMiscUnit(courseClassId: Int, miscType: String): ClassMiscUnit

    @SqlUpdate(
            "INSERT INTO $CLASS_MISC_UNIT_STAGE_TABLE ( " +
                    "$CLASS_MISC_UNIT_TYPE, " +
                    "$CLASS_MISC_UNIT_COURSE_CLASS_ID " +
                    ") " +
                    "VALUES(:miscType, :courseClassId)"
    )
    @GetGeneratedKeys
    override fun createStagingClassMiscUnit(courseClassId: Int, s: String): ClassMiscUnitStage

    @SqlUpdate(
            "DELETE FROM $CLASS_MISC_UNIT_STAGE_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $CLASS_MISC_UNIT_TYPE = :miscType"
    )
    override fun deleteAllStagedClassMiscUnitsFromTypeOfCourseInClass(courseClassId: Int, miscType: String): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_MISC_UNIT_STAGE_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $CLASS_MISC_UNIT_STAGE_ID = :stageId " +
                    "AND $CLASS_MISC_UNIT_TYPE = :miscType"
    )
    override fun deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId: Int, stageId: Int, miscType: String): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $CLASS_MISC_UNIT_TYPE = :miscType"
    )
    override fun deleteAllClassMiscUnitsFromTypeOfCourseInClass(courseClassId: Int, miscType: String): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $CLASS_MISC_UNIT_TYPE = :miscType " +
                    "AND $CLASS_MISC_UNIT_ID = :classMiscUnitId"
    )
    override fun deleteSpecificClassMiscUnitFromTypeOnCourseInClass(courseClassId: Int, classMiscUnitId: Int, miscType: String): Int

    @SqlQuery("SELECT * FROM $COURSE_CLASS_TABLE WHERE $COURSE_CLASS_ID = :courseClassId ")
    override fun getCourseCLassFromId(courseClassId: Int?): CourseClass

}