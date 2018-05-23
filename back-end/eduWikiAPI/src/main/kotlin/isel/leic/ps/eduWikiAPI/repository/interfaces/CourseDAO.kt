package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion

interface CourseDAO {

    /**
     * Main entities queries
     */

    fun getCourse(courseId: Int) : Course

    fun getAllCourses() : List<Course>

    fun deleteCourse(courseId: Int) : Int

    fun deleteAllCourses() : Int

    fun updateCourse(course: Course) : Int

    fun createCourse(course: Course)

    fun voteOnCourse(courseId: Int, voteType: Int)

    /**
     * Stage entities queries
     */

    fun getCourseStage(courseStageId: Int) : CourseStage

    fun getAllCourseStages() : List<CourseStage>

    fun deleteCourseStage(courseStageId: Int) : Int

    fun deleteAllCourseStages() : Int

    fun createCourseStage(courseStage: CourseStage)

    fun voteOnCourseStage(courseStageId: Int, voteType: Int)

    /**
     * Version entities queries
     */

    fun getVersionCourse(versionCourseId: Int, version: Int) : CourseVersion

    fun getAllVersionCourses() : List<CourseVersion>

    fun deleteVersionCourse(versionCourseId: Int, version: Int) : Int

    fun deleteAllVersionCourses() : Int

    fun createVersionCourse(courseVersion: CourseVersion)

    /**
     * Report entity queries
     */

    fun reportCourse(courseReport: CourseReport)

    fun deleteReportOnCourse(reportId: Int) : Int

    fun deleteAllReportsOnCourse(courseId : Int) : Int

    fun deleteAllReports() : Int

    fun getAllReportsOnCourse(courseId: Int) : List<CourseReport>

}