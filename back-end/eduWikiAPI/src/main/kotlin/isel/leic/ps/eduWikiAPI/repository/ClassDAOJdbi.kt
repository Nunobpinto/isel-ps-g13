package isel.leic.ps.eduWikiAPI.repository

import isel.leic.ps.eduWikiAPI.domain.enums.ClassMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import isel.leic.ps.eduWikiAPI.repository.interfaces.ClassDAO
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
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
        const val CLASS_LOG_ID = "log_id"
        const val CLASS_TERM_ID = "term_id"
        const val CLASS_ID = "class_id"
        const val CLASS_VERSION = "class_version"
        const val CLASS_NAME = "class_name"
        const val CLASS_CREATED_BY = "created_by"
        const val CLASS_TIMESTAMP = "time_stamp"
        const val CLASS_VOTES = "votes"
        // CLASS REPORT FIELDS
        const val CLASS_REPORT_LOG_ID = "log_id"
        const val CLASS_REPORT_ID = "class_report_id"
        const val CLASS_REPORTED_BY = "reported_by"
        // CLASS STAGE FIELDS
        const val CLASS_STAGE_LOG_ID = "log_id"
        const val CLASS_STAGE_TERM_ID = "term_id"
        const val CLASS_STAGE_ID = "class_stage_id"
        const val CLASS_STAGE_NAME = "class_name"
        const val CLASS_STAGE_CREATED_BY = "created_by"
        const val CLASS_STAGE_TIMESTAMP = "time_stamp"
        const val CLASS_STAGE_VOTES = "votes"
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
        const val COURSE_CLASS_LOG_ID = "log_id"
        const val COURSE_CLASS_TIMESTAMP = "time_stamp"
        const val COURSE_CLASS_COURSE_ID = "course_id"
        const val COURSE_CLASS_CREATED_BY = "created_by"
        // COURSE CLASS REPORT FIELDS
        const val COURSE_CLASS_REPORT_ID = "course_class_report_id"
        const val COURSE_CLASS_REPORT_COURSE_CLASS_ID = "course_class_id"
        const val COURSE_CLASS_REPORT_TERM_ID = "term_id"
        const val COURSE_CLASS_REPORT_CLASS_ID = "class_id"
        const val COURSE_CLASS_REPORT_COURSE_ID = "course_id"
        const val COURSE_CLASS_REPORT_DELETE_FLAG = "delete_permanently"
        const val COURSE_CLASS_REPORT_VOTES = "votes"
        const val COURSE_CLASS_REPORT_LOG_ID = "log_id"
        const val COURSE_CLASS_REPORT_TIMESTAMP = "time_stamp"
        const val COURSE_CLASS_REPORT_REPORTED_BY = "reported_by"
        // COURSE CLASS STAGE FIELDS
        const val COURSE_CLASS_STAGE_ID = "course_class_stage_id"
        const val COURSE_CLASS_STAGE_CLASS_ID = "course_class_id"
        const val COURSE_CLASS_STAGE_COURSE_ID = "course_id"
        const val COURSE_CLASS_STAGE_CREATED_BY = "created_by"
        const val COURSE_CLASS_STAGE_TERM_ID = "term_id"
        const val COURSE_CLASS_STAGE_TIMESTAMP = "time_stamp"
        const val COURSE_CLASS_STAGE_VOTES = "votes"
        const val COURSE_CLASS_STAGE_LOG_ID = "log_id"
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

    @SqlUpdate(
            "UPDATE $CLASS_STAGE_TABLE SET $CLASS_VOTES = :votes " +
                    "WHERE $CLASS_STAGE_ID = :stageId"
    )
    override fun updateStagedClassVotes(stageId: Int, votes: Int): Int

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

    @SqlUpdate(
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

    @SqlUpdate(
            "UPDATE $CLASS_REPORT_TABLE SET $CLASS_VOTES = :votes " +
                    "WHERE $CLASS_REPORT_ID = :reportId " +
                    "AND $CLASS_ID = :classId"
    )
    override fun updateReportedClassVotes(classId: Int, reportId: Int, votes: Int): Int

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

    @SqlQuery("SELECT * FROM $COURSE_CLASS_TABLE WHERE $COURSE_CLASS_CLASS_ID = :classId")
    override fun getAllCoursesOfClass(classId: Int): List<CourseClass>

    @SqlUpdate(
            "UPDATE $COURSE_CLASS_TABLE " +
                    "SET $COURSE_CLASS_VOTES = :votes " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId " +
                    "AND $COURSE_CLASS_COURSE_ID = :courseId"
    )
    override fun updateCourseClassVotes(classId: Int, courseId: Int, votes: Int): Int

    @SqlUpdate(
            "DELETE FROM $COURSE_CLASS_TABLE " +
                    "WHERE $COURSE_CLASS_CLASS_ID = :classId " +
                    "AND $COURSE_CLASS_COURSE_ID = :courseId"
    )
    override fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int

    @SqlQuery("SELECT * FROM $COURSE_CLASS_REPORT_TABLE WHERE $COURSE_CLASS_ID = :courseClassId ")
    override fun getAllReportsOfCourseInClass(courseClassId: Int): List<CourseClassReport>

    @SqlQuery(
            "SELECT * FROM $COURSE_CLASS_REPORT_TABLE " +
                    "WHERE $COURSE_CLASS_REPORT_ID = :reportId " +
                    "AND $COURSE_CLASS_REPORT_COURSE_ID = :courseId " +
                    "AND $COURSE_CLASS_REPORT_CLASS_ID = :reportId"
    )
    override fun getSpecificReportOfCourseInClass(reportId: Int, classId: Int, courseId: Int): Optional<CourseClassReport>

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

    @SqlUpdate(
            "UPDATE $COURSE_CLASS_STAGE_TABLE SET $COURSE_CLASS_VOTES = :votes " +
                    "WHERE $COURSE_CLASS_STAGE_ID = :stageId" +
                    "AND $COURSE_CLASS_CLASS_ID = :classId"
    )
    override fun updateStagedCourseClassVotes(classId: Int, stageId: Int, votes: Int): Int

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
                    "$COURSE_CLASS_REPORT_ID, " +
                    "$COURSE_CLASS_REPORT_COURSE_ID, " +
                    "$COURSE_CLASS_REPORT_CLASS_ID, " +
                    "$COURSE_CLASS_REPORT_TERM_ID, " +
                    "$COURSE_CLASS_REPORT_REPORTED_BY, " +
                    "$COURSE_CLASS_REPORT_VOTES, " +
                    "$COURSE_CLASS_REPORT_TIMESTAMP, " +
                    "$COURSE_CLASS_REPORT_DELETE_FLAG " +
                    ") " +
                    "VALUES(:courseClassReport.courseClassId, :courseClassReport.courseId, :courseClassReport.classId, " +
                    ":courseClassReport.termId, :courseClassReport.reportedBy, :courseClassReport.votes, :courseClassReport.timestamp," +
                    ":courseClassReport.deleltePermanently)"
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
            "DELETE FROM $COURSE_CLASS_REPORT_TABLE " +
                    "WHERE $COURSE_CLASS_ID = :courseClassId " +
                    "AND $COURSE_CLASS_REPORT_ID = :reportId"
    )
    override fun deleteSpecificReportOnCourseClass(courseClassId: Int, reportId: Int): Int

    @SqlUpdate(
            "INSERT INTO $COURSE_CLASS_STAGE_TABLE ( " +
                    "$COURSE_CLASS_STAGE_COURSE_ID, " +
                    "$COURSE_CLASS_STAGE_CLASS_ID, " +
                    "$COURSE_CLASS_STAGE_TERM_ID, " +
                    "$COURSE_CLASS_STAGE_CREATED_BY, " +
                    "$COURSE_CLASS_STAGE_VOTES, " +
                    "$COURSE_CLASS_STAGE_TIMESTAMP " +
                    ")" +
                    "VALUES(:courseClassStage.courseId, :courseClassStage.classId, :courseClassStage.termId, " +
                    ":courseClassStage.createdBy, :courseClassStage.votes, :courseClassStage.timestamp)"
    )
    @GetGeneratedKeys
    override fun createStagingCourseInClass(courseClassStage: CourseClassStage): CourseClassStage

    @SqlUpdate(
            "INSERT INTO $CLASS_MISC_UNIT_TABLE ( " +
                    "$CLASS_MISC_UNIT_TYPE, " +
                    "$CLASS_MISC_UNIT_COURSE_CLASS_ID " +
                    ") " +
                    "VALUES (:miscType, :courseClassId)"
    )
    @GetGeneratedKeys
    override fun createClassMiscUnit(courseClassId: Int, miscType: ClassMiscUnitType): ClassMiscUnit

    @SqlUpdate(
            "INSERT INTO $CLASS_MISC_UNIT_STAGE_TABLE ( " +
                    "$CLASS_MISC_UNIT_TYPE, " +
                    "$CLASS_MISC_UNIT_COURSE_CLASS_ID " +
                    ") " +
                    "VALUES(:miscType, :courseClassId)"
    )
    @GetGeneratedKeys
    override fun createStagingClassMiscUnit(courseClassId: Int, miscType: ClassMiscUnitType): ClassMiscUnitStage

    @SqlUpdate(
            "DELETE FROM $CLASS_MISC_UNIT_STAGE_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $CLASS_MISC_UNIT_TYPE = :miscType"
    )
    override fun deleteAllStagedClassMiscUnitsFromTypeOfCourseInClass(courseClassId: Int, miscType: ClassMiscUnitType): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_MISC_UNIT_STAGE_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $CLASS_MISC_UNIT_STAGE_ID = :stageId " +
                    "AND $CLASS_MISC_UNIT_TYPE = :miscType"
    )
    override fun deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId: Int, stageId: Int, miscType: ClassMiscUnitType): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $CLASS_MISC_UNIT_TYPE = :miscType"
    )
    override fun deleteAllClassMiscUnitsFromTypeOfCourseInClass(courseClassId: Int, miscType: ClassMiscUnitType): Int

    @SqlUpdate(
            "DELETE FROM $CLASS_MISC_UNIT_TABLE " +
                    "WHERE $CLASS_MISC_UNIT_COURSE_CLASS_ID = :courseClassId " +
                    "AND $CLASS_MISC_UNIT_TYPE = :miscType " +
                    "AND $CLASS_MISC_UNIT_ID = :classMiscUnitId"
    )
    override fun deleteSpecificClassMiscUnitFromTypeOnCourseInClass(courseClassId: Int, classMiscUnitId: Int, miscType: ClassMiscUnitType): Int

    @SqlQuery("SELECT * FROM $COURSE_CLASS_TABLE WHERE $COURSE_CLASS_ID = :courseClassId ")
    override fun getCourseClassFromId(courseClassId: Int?): CourseClass

}