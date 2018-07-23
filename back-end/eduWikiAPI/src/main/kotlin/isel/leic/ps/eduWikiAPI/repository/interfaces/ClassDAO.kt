package isel.leic.ps.eduWikiAPI.repository.interfaces

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

    fun createClass(klass: Class): Optional<Class>

    fun deleteSpecificClass(classId: Int): Int

    fun updateClass(updatedClass: Class): Int

    fun voteOnClass(classId: Int, vote: Vote): Int

    fun getAllClassesOnSpecificTermOfCourse(courseId: Int, termId: Int): List<Class>

    fun getClassOnSpecificTermOfCourse(courseId: Int, termId: Int, classId: Int): Optional<Class>

    fun getClassMiscUnit(courseClassId: Int): Optional<ClassMiscUnit>

    fun getTermIdFromSpecificClass(classId: Int): Int

    fun getClassMiscUnitId(courseClassId: Int): Int

    fun createClassMiscUnit(courseClassId: Int, miscType: String): Optional<ClassMiscUnit>


    /**
     * Stage entities queries
     */

    fun getAllStagedClasses(): List<ClassStage>

    fun getSpecificStagedClass(stageId: Int): Optional<ClassStage>

    fun createStagedClass(classStage: ClassStage): Optional<ClassStage>

    fun deleteSpecificStagedClass(stageId: Int): Int

    fun voteOnStagedClass(stageId: Int, vote: Vote): Int

    fun deleteAllStagedClasses(): Int

    fun deleteAllClassMiscUnitsFromTypeOfCourseInClass(courseClassId: Int, miscType: String): Int

    fun deleteSpecificStagedClassMiscUnitFromTypeOfCourseInClass(courseClassId: Int, stageId: Int): Int

    /**
     * Report entities queries
     */

    fun getAllReportsFromClass(classId: Int): List<ClassReport>

    fun getSpecificReportFromClass(classId: Int, reportId: Int): Optional<ClassReport>

    fun reportClass(classId: Int, report: ClassReport): Optional<ClassReport>

    fun voteOnReportOfClass(classId: Int, reportId: Int, vote: Vote): Int

    fun deleteAllReportsInClass(classId: Int): Int

    fun deleteSpecificReportInClass(classId: Int, reportId: Int) : Int


    /**
     * Version entities queries
     */
    fun createClassVersion(classVersion: ClassVersion): Optional<ClassVersion>

    fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion>

    fun getVersionOfSpecificClass(classId: Int, versionId: Int): Optional<ClassVersion>

    fun deleteAllVersionsOfClass(classId: Int): Int

    fun deleteSpecificVersionOfClass(classId: Int, versionId: Int): Int


    /**
     * Courses On Class queries
     */
    fun getAllCoursesOfClass(classId: Int): List<Course>

    fun getSpecificCourseOfClass(classId: Int, courseId: Int): Optional<Course>

    fun voteOnCourseInClass(classId: Int, courseId: Int, vote: Vote): Int

    fun deleteAllCoursesInClass(classId: Int): Int

    fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int

    fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReport>

    fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport>

    fun voteOnReportOfCourseClass(classId: Int, courseId: Int, reportId: Int, vote: Vote): Int

    fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int

    fun getStageEntriesOfCoursesInClass(classId: Int): List<CourseClassStage>

    fun getSpecificStagedCourseInClass(classId: Int, stageId: Int): Optional<CourseClassStage>

    fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: Vote): Int

    fun addCourseToClass(courseClass: CourseClass): Optional<CourseClass>

    fun deleteStagedEntriesOfCourseInSpecificClass(classId: Int): Int

    fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int

    fun reportCourseInClass(courseClassReport: CourseClassReport): Optional<CourseClassReport>

    fun getCourseClass(classId: Int, courseId: Int): Optional<CourseClass>

    fun updateCourseClass(updatedCourseClass: CourseClass): Optional<CourseClass>

    fun deleteSpecificReportOnCourseClass(courseClassId: Int, reportId: Int) : Int

    fun createStagingCourseInClass(courseClassStage: CourseClassStage): Optional<CourseClassStage>

    fun getCourseClassStage(stageId: Int): Optional<CourseClassStage>

    fun getCourseClassId(classId: Int, courseId: Int) : Int

    fun createStagingClassMiscUnit(courseClassId: Int, s: String): Optional<ClassMiscUnitStage>

}