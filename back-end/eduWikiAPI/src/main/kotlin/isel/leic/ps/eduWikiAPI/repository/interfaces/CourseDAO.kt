package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.report.ExamReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion

interface CourseDAO {

    /**
     * Main entities queries
     */

    fun getAllCourses() : List<Course>

    fun getSpecificCourse(courseId: Int) : Course

    fun getTermsOfCourse(courseId: Int): List<Term>

    fun getSpecificTermOfCourse(courseId: Int, termId: Int): Term

    fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course>

    fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Course

    fun updateCourse(course: Course) : Int

    fun updateCourseProgramme(programmeId: Int, courseId: Int, updatedCourse: Course)

    fun createCourse(course: Course) : Int

    fun voteOnCourse(courseId: Int, inputVote: VoteInputModel) : Int

    fun voteOnCourseOfProgramme(programmeId: Int, vote: Vote, courseId: Int): Int

    fun addCourseToProgramme(programmeId: Int, course: Course) : Int

    fun deleteCourse(courseId: Int) : Int

    fun deleteAllCourses() : Int

    /**
     * Stage entities queries
     */

    fun getAllCourseStageEntries(): List<CourseStage>

    fun getCourseSpecificStageEntry(stageId: Int): CourseStage

    fun getStagedCoursesOfProgramme(programmeId: Int): List<CourseProgrammeStage>

    fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): CourseProgrammeStage

    fun createStagingCourse(courseStage: CourseStage) : Int

    fun createStagingCourseOfProgramme(courseProgrammeStage: CourseProgrammeStage): Int

    fun voteOnStagedCourse(courseStageId: Int, inputVote: Vote) : Int

    fun voteOnCourseProgrammeStaged(programmeId: Int, stageId: Int, vote: Vote): Int

    fun deleteStagedCourse(courseStageId: Int) : Int

    fun deleteAllCourseStages() : Int

    fun deleteStagedCourseOfProgramme(programmeId: Int,stageId: Int) : Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion>

    fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): CourseVersion

    fun getAllVersionsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseVersion>

    fun getSpecificVersionOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, versionId: Int): CourseVersion

    fun addCourseProgrammeToVersion(courseProgramme: Course): Int

    fun addToCourseVersion(updatedCourse: Course) : Int

    fun deleteVersionCourse(versionCourseId: Int, version: Int) : Int

    fun deleteAllVersionCourses(versionCourseId: Int) : Int

    /**
     * Report entity queries
     */

    fun getAllReportsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport>

    fun getSpecificReportOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeReport

    fun getAllReportsOnCourse(courseId: Int) : List<CourseReport>

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): CourseReport

    fun voteOnReportedCourse(reportId: Int, vote: Vote) : Int

    fun voteOnReportOfCourseProgramme(programmeId: Int, reportId: Int, vote: Vote): Int

    fun reportCourseOnProgramme(programmeId: Int, courseId: Int, courseProgrammeReport: CourseProgrammeReport): Int

    fun reportCourse(courseId: Int, courseReport: CourseReport) : Int

    fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun deleteReportOnCourse(reportId: Int) : Int

    fun deleteAllReportsOnCourse(courseId : Int) : Int

}