package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Course
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

    fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course>

    fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Optional<Course>

    fun updateCourse(course: Course): Int

    fun updateCourseProgramme(programmeId: Int, courseId: Int, updatedCourse: Course): Int

    fun createCourse(course: Course): Optional<Course>

    fun voteOnCourse(courseId: Int, vote: Vote): Int

    fun voteOnCourseOfProgramme(programmeId: Int, vote: Vote, courseId: Int): Int

    fun addCourseToProgramme(programmeId: Int, course: Course): Optional<Course>

    fun deleteSpecificCourse(courseId: Int): Int

    fun deleteAllCourses(): Int

    fun deleteSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Int

    /**
     * Stage entities queries
     */

    fun getAllCourseStageEntries(): List<CourseStage>

    fun getCourseSpecificStageEntry(courseStageId: Int): Optional<CourseStage>

    fun getStagedCoursesOfProgramme(programmeId: Int): List<CourseProgrammeStage>

    fun getSpecificStagedCourseProgramme(courseProgrammeStageId: Int): Optional<CourseProgrammeStage>

    fun createStagedCourse(courseStage: CourseStage): Optional<CourseStage>

    fun createStagingCourseOfProgramme(courseProgrammeStage: CourseProgrammeStage): Optional<CourseProgrammeStage>

    fun voteOnStagedCourse(courseStageId: Int, inputVote: Vote): Int

    fun voteOnCourseProgrammeStaged(stageId: Int, vote: Vote): Int

    fun deleteStagedCourse(courseStageId: Int): Int

    fun deleteAllStagedCourses(): Int

    fun deleteStagedCourseOfProgramme(courseProgrammeStageId: Int): Int

    fun deleteSpecificStagedCourseOfProgramme(programmeId: Int, courseId: Int, stageId: Int): Int


    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion>

    fun getVersionOfSpecificCourse(courseId: Int, versionId: Int): Optional<CourseVersion>

    fun getAllVersionsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeVersion>

    fun getSpecificVersionOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, versionId: Int): Optional<CourseProgrammeVersion>

    fun addCourseProgrammeToVersion(courseProgramme: Course): Optional<CourseProgrammeVersion>

    fun createCourseVersion(courseVersion: CourseVersion): Optional<CourseVersion>

    fun deleteVersionOfCourse(courseId: Int, version: Int): Int

    fun deleteAllVersionsOfCourse(versionCourseId: Int): Int

    fun deleteSpecificVersionOfCourseOfProgramme(programmeId: Int, courseId: Int, versionId: Int): Int

    /**
     * Report entity queries
     */

    fun getAllReportsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport>

    fun getSpecificReportOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, reportId: Int): Optional<CourseProgrammeReport>

    fun getAllReportsOnCourse(courseId: Int): List<CourseReport>

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): Optional<CourseReport>

    fun voteOnReportOfCourse(reportId: Int, vote: Vote): Int

    fun voteOnReportOfCourseProgramme(programmeId: Int, reportId: Int, vote: Vote): Int

    fun reportCourseOnProgramme(programmeId: Int, courseId: Int, courseProgrammeReport: CourseProgrammeReport): Optional<CourseProgrammeReport>

    fun reportCourse(courseId: Int, courseReport: CourseReport): Int

    fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun deleteReportOnCourse(reportId: Int): Int

    fun deleteAllReportsOnCourse(courseId: Int): Int

    fun deleteSpecificReportOfCourseOfProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    /**
     * Courses On Class queries
     */
    fun getCoursesOfClass(classId: Int): List<Course>

    fun getSpecificCourseOfClass(classId: Int, courseId: Int): Optional<Course>

    fun voteOnCourseInClass(classId: Int, courseId: Int, valueOf: Vote): Int

    fun deleteAllCoursesInClass(classId: Int): Int

    fun deleteSpecificCourseInClass(classId: Int, courseId: Int): Int
    
    fun getAllReportsOfCourseInClass(classId: Int, courseId: Int): List<CourseClassReport>

    fun getSpecificReportOfCourseInClass(classId: Int, courseId: Int, reportId: Int): Optional<CourseClassReport>

    fun voteOnReportOfCourseClass(classId: Int, courseId: Int, reportId: Int, valueOf: Vote): Int

    fun deleteAllCourseReportsInClass(classId: Int, courseId: Int): Int

    fun deleteSpecificCourseReportInClass(classId: Int, courseId: Int, reportId: Int): Int

    fun getAllCoursesStagedInClass(classId: Int): List<CourseClassStage>

    fun getSpecificStagedCourseClass(classId: Int, stageId: Int): Optional<CourseClassStage>

    fun voteOnStagedCourseInClass(classId: Int, stageId: Int, vote: Vote): Int

    fun deleteAllStagedCoursesInClass(classId: Int): Int

    fun deleteSpecificStagedCourseInClass(classId: Int, stageId: Int): Int
}