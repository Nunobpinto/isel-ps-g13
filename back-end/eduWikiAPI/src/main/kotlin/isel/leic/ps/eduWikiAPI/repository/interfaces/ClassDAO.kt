package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Class
import isel.leic.ps.eduWikiAPI.domain.model.report.ClassReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ClassStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ClassVersion

interface ClassDAO {
    /**
     * Main entities queries
     */

    fun getClass(classId: Int, termId: Int) : Class

    fun getAllClasses() : List<Class>

    fun getAlLClassesInTerm(termId:Int) : List<Class>

    fun deleteClass(classId: Int, termId: Int) : Int

    fun deleteAllClasses() : Int

    fun updateClass(klass: Class, user: String) : Int

    fun createClass(klass: Class)

    fun voteOnClass(courseId: Int, termId: Int, voteType: Int)

    /**
     * Stage entities queries
     */
    fun getClassStage(classId: Int, termId: Int) : ClassStage

    fun getAllClassStages() : List<ClassStage>

    fun getAllClassStagesInTerm(termId: Int) : List<ClassStage>

    fun deleteClassStage(classId: Int, termId: Int) : Int

    fun deleteAllProgrammeStages() : Int

    fun deleteAllProgrammeStages(termId: Int) : Int

    fun createClassStage(classStage: ClassStage)

    fun voteOnClassStage(classId: Int, termId: Int, voteType: Int)


    /**
     * Version entities queries
     */

    fun getVersionClass(versionClassId: Int, versionTermId: Int, version: Int) : ClassVersion

    fun getAllVersionCourses() : List<ClassVersion>

    fun deleteVersionCourse(versionCourseId: Int, versionTermId: Int, version: Int) : Int

    fun deleteAllVersionCourses() : Int

    fun createVersionCourse(classVersion: ClassVersion)

    /**
     * Report entity queries
     */

    fun reportClass(classReport: ClassReport)

    fun deleteReportOnClass(reportId: Int) : Int

    fun deleteAllReportsOnCourse(classId: Int, termId: Int) : Int

    fun deleteAllReports() : Int

}