package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion

interface CourseDAO {

    /**
     * Main entities queries
     */

    fun getSpecificCourse(courseId: Int) : Course

    fun getAllCourses() : List<Course>

    fun deleteCourse(courseId: Int) : Int

    fun deleteAllCourses() : Int

    fun updateCourse(course: Course) : Int

    fun createCourse(course: Course) : Int

    fun voteOnCourse(courseId: Int, inputVote: VoteInputModel) : Int

    fun getTermsOfCourse(courseId: Int): List<Term>

    fun getSpecificTermOfCourse(courseId: Int, termId: Int): Term

    /**
     * Stage entities queries
     */

    fun deleteStagedCourse(courseStageId: Int) : Int

    fun deleteAllCourseStages() : Int

    fun createStagingCourse(courseStage: CourseStage) : Int

    fun voteOnStagedCourse(courseStageId: Int, inputVote: VoteInputModel) : Int

    fun getAllCourseStageEntries(): List<CourseStage>

    fun getCourseSpecificStageEntry(stageId: Int): CourseStage

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

    fun reportCourse(courseId: Int, courseReport: CourseReport)

    fun deleteReportOnCourse(reportId: Int) : Int

    fun deleteAllReportsOnCourse(courseId : Int) : Int

    fun deleteAllReports() : Int

    fun getAllReportsOnCourse(courseId: Int) : List<CourseReport>

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReport

    fun voteOnReportedCourse(reportId: Int, inputVote: VoteInputModel) : Int

}