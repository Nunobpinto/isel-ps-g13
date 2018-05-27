package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
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

    fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course>

    fun addCourseToProgramme(programmeId: Int, course: Course) : Int

    fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Course

    fun voteOnCourseOfProgramme(programmeId: Int, vote: Vote, courseId: Int): Int

    fun updateCourseProgramme(programmeId: Int, courseId: Int, updatedCourse: Course)

    /**
     * Stage entities queries
     */

    fun deleteStagedCourse(courseStageId: Int) : Int

    fun deleteAllCourseStages() : Int

    fun createStagingCourse(courseStage: CourseStage) : Int

    fun voteOnStagedCourse(courseStageId: Int, inputVote: Vote) : Int

    fun getAllCourseStageEntries(): List<CourseStage>

    fun getCourseSpecificStageEntry(stageId: Int): CourseStage

    fun createStagingCourseOfProgramme(courseProgrammeStage: CourseProgrammeStage): Int

    fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): CourseProgrammeStage

    fun deleteStagedCourseOfProgramme(programmeId: Int,stageId: Int) : Int

    fun voteOnCourseProgrammeStaged(programmeId: Int, stageId: Int, vote: Vote): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion>

    fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): CourseVersion

    fun getVersionCourse(versionCourseId: Int, version: Int) : CourseVersion

    fun deleteVersionCourse(versionCourseId: Int, version: Int) : Int

    fun deleteAllVersionCourses(versionCourseId: Int) : Int

    fun createVersionCourse(courseVersion: CourseVersion)

    fun getAllVersionsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseVersion>

    fun getSpecificVersionOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, versionId: Int): CourseVersion

    fun addCourseProgrammeToVersion(courseProgramme: Course): Int

    fun addToCourseVersion(updatedCourse: Course) : Int

    /**
     * Report entity queries
     */

    fun reportCourse(courseId: Int, courseReport: CourseReport)

    fun deleteReportOnCourse(reportId: Int) : Int

    fun deleteAllReportsOnCourse(courseId : Int) : Int

    fun getAllReportsOnCourse(courseId: Int) : List<CourseReport>

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReport

    fun voteOnReportedCourse(reportId: Int, vote: Vote) : Int

    fun reportCourseOnProgramme(programmeId: Int, courseId: Int, courseProgrammeReport: CourseProgrammeReport): Int

    fun getAllReportsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport>

    fun getSpecificReportOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeReport

    fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun voteOnReportOfCourseProgramme(programmeId: Int, reportId: Int, vote: Vote): Int

    fun getStagedCoursesOfProgramme(programmeId: Int): List<CourseProgrammeStage>

}