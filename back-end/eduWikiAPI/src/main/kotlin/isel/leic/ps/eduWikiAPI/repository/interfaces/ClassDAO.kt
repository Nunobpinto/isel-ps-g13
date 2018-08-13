package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.enums.ClassMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion
import java.util.*

interface ClassDAO {

    /**
     * Main entities queries
     */

    fun getAllClasses() : List<Class>

    fun getSpecificClass(classId: Int): Optional<Class>

    fun getTermIdFromSpecificClass(classId: Int): Int

    fun createClass(klass: Class): Class

    fun createClassMiscUnit(courseClassId: Int, miscType: ClassMiscUnitType): ClassMiscUnit

    fun updateClass(updatedClass: Class): Class

    fun updateClassVotes(classId: Int, votes: Int): Int

    fun deleteSpecificClass(classId: Int): Int

    fun deleteAllClassMiscUnitsFromTypeOfCourseInClass(courseClassId: Int, miscType: ClassMiscUnitType): Int

    fun deleteSpecificClassMiscUnitFromTypeOnCourseInClass(courseClassId: Int, classMiscUnitId: Int, miscType: ClassMiscUnitType): Int


    /**
     * Stage entities queries
     */

    fun getAllStagedClasses(): List<ClassStage>

    fun getSpecificStagedClass(stageId: Int): Optional<ClassStage>

    fun createStagedClass(classStage: ClassStage): ClassStage

    fun createStagingClassMiscUnit(courseClassId: Int, miscType: ClassMiscUnitType): ClassMiscUnitStage

    fun updateStagedClassVotes(stageId: Int, votes: Int): Int

    fun deleteSpecificStagedClass(stageId: Int): Int

    fun deleteAllStagedClassMiscUnitsFromTypeOfCourseInClass(courseClassId: Int, miscType: ClassMiscUnitType): Int

    fun deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId: Int, stageId: Int, miscType: ClassMiscUnitType): Int

    /**
     * Report entities queries
     */

    fun getAllReportsFromClass(classId: Int): List<ClassReport>

    fun getSpecificReportFromClass(classId: Int, reportId: Int): Optional<ClassReport>

    fun reportClass(classId: Int, report: ClassReport): ClassReport

    fun updateReportedClassVotes(classId: Int, reportId: Int, votes: Int): Int

    fun deleteSpecificReportInClass(classId: Int, reportId: Int) : Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion>

    fun getVersionOfSpecificClass(classId: Int, versionId: Int): Optional<ClassVersion>

    fun createClassVersion(classVersion: ClassVersion): ClassVersion

    /**
     * Courses On Class queries
     */

    fun getAllCoursesOfClass(classId: Int): List<CourseClass>

    fun getCourseClass(classId: Int, courseId: Int): Optional<CourseClass>

    fun getCourseClassFromId(courseClassId: Int?): CourseClass

    fun getSpecificReportOfCourseInClass(reportId: Int, classId: Int, courseId: Int): Optional<CourseClassReport>

    fun getAllReportsOfCourseInClass(courseClassId: Int): List<CourseClassReport>

    fun getStageEntriesOfCoursesInClass(classId: Int): List<CourseClassStage>

    fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage>

    fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int

    fun addCourseToClass(courseClass: CourseClass): CourseClass

    fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int

    fun reportCourseInClass(courseClassReport: CourseClassReport): CourseClassReport

    fun deleteSpecificReportOnCourseClass(courseClassId: Int, reportId: Int) : Int

    fun createStagingCourseInClass(courseClassStage: CourseClassStage): CourseClassStage

    fun updateCourseClassVotes(classId: Int, courseId: Int, votes: Int): Int

    fun updateReportedCourseClassVotes(classId: Int, courseId: Int, reportId: Int, votes: Int): Int

    fun updateStagedCourseClassVotes(classId: Int, stageId: Int, votes: Int): Int


}