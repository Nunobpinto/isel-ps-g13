package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.CourseClass
import isel.leic.ps.eduWikiAPI.domain.model.Term
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseProgrammeVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import java.util.*

interface CourseDAO {

    /**
     * Main entities queries
     */

    fun getAllCourses(): List<Course>

    fun getSpecificCourse(courseId: Int): Optional<Course>

    fun getTermsOfCourse(courseId: Int): List<Term>

    fun getSpecificTermOfCourse(courseId: Int, termId: Int): Optional<Term>

    fun getAllCoursesOnSpecificProgramme(programmeId: Int): List<Course>

    fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Optional<Course>

    fun updateCourse(course: Course): Int

    fun updateCourseProgramme(programmeId: Int, courseId: Int, course: Course): Optional<Course>

    fun createCourse(course: Course): Optional<Course>

    fun voteOnCourse(courseId: Int, vote: Vote): Int

    fun voteOnCourseProgramme(programmeId: Int, courseId: Int, vote: Vote): Int

    fun addCourseToProgramme(programmeId: Int, course: Course): Optional<Course>

    fun deleteSpecificCourse(courseId: Int): Int

    fun deleteAllCourses(): Int

    fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int): Int

    /**
     * Stage entities queries
     */

    fun getAllCourseStageEntries(): List<CourseStage>

    fun getCourseSpecificStageEntry(courseStageId: Int): Optional<CourseStage>

    fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): List<CourseProgrammeStage>

    fun getSpecificStagedCourseProgramme(programmeId: Int, stageId: Int): Optional<CourseProgrammeStage>

    fun createStagedCourse(courseStage: CourseStage): Optional<CourseStage>

    fun createStagingCourseOfProgramme(courseProgrammeStage: CourseProgrammeStage): Optional<CourseProgrammeStage>

    fun voteOnStagedCourse(stageId: Int, inputVote: Vote): Int

    fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, vote: Vote): Int

    fun deleteStagedCourse(courseStageId: Int): Int

    fun deleteAllStagedCourses(): Int

    fun deleteStagedCourseProgramme(courseProgrammeStageId: Int): Int

    fun deleteSpecificStagedCourseProgramme(programmeId: Int, courseId: Int, stageId: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion>

    fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): Optional<CourseVersion>

    fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeVersion>

    fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): Optional<CourseProgrammeVersion>

    fun createCourseProgrammeVersion(courseProgramme: Course): Optional<CourseProgrammeVersion>

    fun createCourseVersion(courseVersion: CourseVersion): Optional<CourseVersion>

    fun deleteVersionOfCourse(courseId: Int, version: Int): Int

    fun deleteAllVersionsOfCourse(versionCourseId: Int): Int

    fun deleteSpecificVersionOfCourseProgramme(programmeId: Int, courseId: Int, version: Int): Int

    /**
     * Report entity queries
     */

    fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport>

    fun getSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Optional<CourseProgrammeReport>

    fun getAllReportsOnCourse(courseId: Int): List<CourseReport>

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): Optional<CourseReport>

    fun voteOnReportOfCourse(reportId: Int, vote: Vote): Int

    fun voteOnReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, vote: Vote): Int

    fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, courseProgrammeReport: CourseProgrammeReport): Optional<CourseProgrammeReport>

    fun reportCourse(courseId: Int, courseReport: CourseReport): Optional<CourseReport>

    fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun deleteReportOnCourse(reportId: Int): Int

    fun deleteAllReportsOnCourse(courseId: Int): Int

    fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

}