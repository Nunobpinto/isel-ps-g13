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

    fun createClass(klass: Class): Class

    fun deleteSpecificClass(classId: Int): Int

    fun updateClass(updatedClass: Class): Class

    fun getClassVotes(classId: Int): Int

    fun updateClassVotes(classId: Int, votes: Int): Int

    fun getAllClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class>

    fun getClassOnSpecificTermOfCourse(courseId: Int, termId: Int, classId: Int): Optional<Class>

    fun getClassMiscUnit(courseClassId: Int): Optional<ClassMiscUnit>

    fun getTermIdFromSpecificClass(classId: Int): Int

    fun getClassMiscUnitId(courseClassId: Int): Int

    fun createClassMiscUnit(courseClassId: Int, miscType: ClassMiscUnitType): ClassMiscUnit

    fun deleteAllClassMiscUnitsFromTypeOfCourseInClass(courseClassId: Int, miscType: ClassMiscUnitType): Int

    fun deleteSpecificClassMiscUnitFromTypeOnCourseInClass(courseClassId: Int, classMiscUnitId: Int, miscType: ClassMiscUnitType): Int


    /**
     * Stage entities queries
     */

    fun getAllStagedClasses(): List<ClassStage>

    fun getSpecificStagedClass(stageId: Int): Optional<ClassStage>

    fun createStagedClass(classStage: ClassStage): ClassStage

    fun deleteSpecificStagedClass(stageId: Int): Int

    fun deleteAllStagedClasses(): Int

    fun deleteAllStagedClassMiscUnitsFromTypeOfCourseInClass(courseClassId: Int, miscType: ClassMiscUnitType): Int

    fun deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId: Int, stageId: Int, miscType: ClassMiscUnitType): Int

    fun getStagedClassVotes(stageId: Int): Int

    fun updateStagedClassVotes(stageId: Int, votes: Int): Int

    fun createStagingClassMiscUnit(courseClassId: Int, miscType: ClassMiscUnitType): ClassMiscUnitStage

    /**
     * Report entities queries
     */

    fun getAllReportsFromClass(classId: Int): List<ClassReport>

    fun getSpecificReportFromClass(classId: Int, reportId: Int): Optional<ClassReport>

    fun reportClass(classId: Int, report: ClassReport): ClassReport

    fun deleteAllReportsInClass(classId: Int): Int

    fun deleteSpecificReportInClass(classId: Int, reportId: Int) : Int

    fun getReportedClassVotes(classId: Int, reportId: Int): Int

    fun updateReportedClassVotes(classId: Int, reportId: Int, votes: Int): Int

    /**
     * Version entities queries
     */
    fun createClassVersion(classVersion: ClassVersion): ClassVersion

    fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion>

    fun getVersionOfSpecificClass(classId: Int, versionId: Int): Optional<ClassVersion>

    fun deleteAllVersionsOfClass(classId: Int): Int

    fun deleteSpecificVersionOfClass(classId: Int, versionId: Int): Int

    /**
     * Courses On Class queries
     */
    fun getAllCoursesOfClass(classId: Int): List<CourseClass>

    fun getSpecificCourseOfClass(classId: Int, courseId: Int): Optional<CourseClass>

    fun deleteAllCoursesInClass(classId: Int): Int

    fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int

    fun getAllReportsOfCourseInClass(courseClassId: Int): List<CourseClassReport>

    fun getSpecificReportOfCourseInClass(reportId: Int): Optional<CourseClassReport>

    fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int

    fun getStageEntriesOfCoursesInClass(classId: Int): List<CourseClassStage>

    fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage>

    fun addCourseToClass(courseClass: CourseClass): CourseClass

    fun deleteStagedEntriesOfCourseInSpecificClass(classId: Int): Int

    fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int

    fun reportCourseInClass(courseClassReport: CourseClassReport): CourseClassReport

    fun getCourseClass(classId: Int, courseId: Int): Optional<CourseClass>

    fun updateCourseClass(updatedCourseClass: CourseClass): CourseClass

    fun deleteSpecificReportOnCourseClass(courseClassId: Int, reportId: Int) : Int

    fun createStagingCourseInClass(courseClassStage: CourseClassStage): CourseClassStage

    fun getCourseClassStage(stageId: Int): Optional<CourseClassStage>

    fun getCourseClassId(classId: Int, courseId: Int) : Int

    fun getCourseClassVotes(classId: Int, courseId: Int): Int

    fun updateCourseClassVotes(classId: Int, courseId: Int, votes: Int): Int

    fun getReportedCourseClassVotes(classId: Int, courseId: Int, reportId: Int): Int

    fun updateReportedCourseClassVotes(classId: Int, courseId: Int, reportId: Int, votes: Int): Int

    fun getStagedCourseClassVotes(classId: Int, stageId: Int): Int

    fun updateStagedCourseClassVotes(classId: Int, stageId: Int, votes: Int): Int

    fun getCourseCLassFromId(courseClassId: Int?): CourseClass

}