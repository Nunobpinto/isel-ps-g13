package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.ClassInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
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

    /**
     * Stage entities queries
     */

    fun getAllStagedClasses(): List<ClassStage>

    fun getSpecificStagedClass(stageId: Int): Optional<ClassStage>

    fun createStagingClass(inputClass: ClassInputModel): Optional<ClassStage>

    fun deleteSpecificStagedClass(stageId: Int): Int

    fun voteOnStagedClass(stageId: Int, vote: Vote): Int

    fun deleteAllStagedClasses(): Int

    /**
     * Report entities queries
     */

    fun getAllReportsFromClass(classId: Int): List<ClassReport>

    fun getSpecificReportFromClass(classId: Int, reportId: Int): Optional<ClassReport>

    fun reportClass(classId: Int, report: ClassReport): Optional<ClassReport>

    fun voteOnReportOfClass(classId: Int, reportId: Int, vote: Vote): Int

    fun deleteAllReportsInClass(classId: Int): Int

    fun deleteSpecificReportOfClass(reportId: Int) : Int


    /**
     * Version entities queries
     */
    fun createClassVersion(classVersion: ClassVersion): Optional<ClassVersion>

    fun getAllVersionsOfSpecificClass(classId: Int): List<ClassVersion>

    fun getVersionOfSpecificClass(classId: Int, versionId: Int): Optional<ClassVersion>

    fun deleteAllVersionsOfClass(courseId: Int): Int

    fun deleteSpecificVersionOfClass(courseId: Int, versionId: Int): Int

    fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport>

}