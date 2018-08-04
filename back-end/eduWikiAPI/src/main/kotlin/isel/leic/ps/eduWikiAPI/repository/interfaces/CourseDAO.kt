package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseClassReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseClassStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseMiscUnitStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseProgrammeVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import java.sql.Timestamp
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

    fun updateCourse(course: Course): Course

    fun updateCourseProgramme(programmeId: Int, courseId: Int, course: Course): Course

    fun createCourse(course: Course): Course

    fun addCourseToProgramme(programmeId: Int, course: Course): Course

    fun deleteSpecificCourse(courseId: Int): Int

    fun deleteAllCourses(): Int

    fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int): Int

    fun getVotesOnCourse(courseId: Int): Int

    fun updateVotesOnCourse(courseId: Int, votes: Int): Int

    fun createCourseMiscUnit(courseId: Int, termId: Int, miscType: String): CourseMiscUnit

    fun createCourseTerm(courseId: Int, termId: Int, timestamp: Timestamp): CourseTerm

    fun deleteSpecificCourseMiscUnitEntry(courseMiscUnitId: Int): Int

    fun deleteAllCourseMiscUnitsFromTypeOfCourseInTerm(courseId: Int, termId: Int, miscType: String): Int

    fun getVotesOnCourseProgramme(programmeId: Int, courseId: Int): Int

    fun updateVotesOnCourseProgramme(programmeId: Int, courseId: Int, votes: Int) : Int

    /**
     * Stage entities queries
     */

    fun getAllCourseStageEntries(): List<CourseStage>

    fun getCourseSpecificStageEntry(courseStageId: Int): Optional<CourseStage>

    fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): List<CourseProgrammeStage>

    fun getSpecificStagedCourseProgramme(programmeId: Int, stageId: Int): Optional<CourseProgrammeStage>

    fun createStagingCourse(courseStage: CourseStage): CourseStage

    fun createStagingCourseOfProgramme(courseProgrammeStage: CourseProgrammeStage): CourseProgrammeStage

    fun deleteStagedCourse(courseStageId: Int): Int

    fun deleteAllStagedCourses(): Int

    fun deleteStagedCourseProgramme(stageId: Int): Int

    fun deleteSpecificStagedCourseProgramme(programmeId: Int, courseId: Int, stageId: Int): Int

    fun getVotesOnStagedCourse(stageId: Int): Int

    fun updateVotesOnStagedCourse(stageId: Int, votes: Int): Int

    fun createStagingCourseMiscUnit(courseId: Int, termId: Int, miscType: String): CourseMiscUnitStage

    fun deleteAllStagedCourseMiscUnitsFromTypeOfCourseInTerm(courseId: Int, termId: Int, miscType: String ): Int

    fun deleteSpecificStagedCourseMiscUnitEntry(courseId: Int, termId: Int, stageId: Int): Int

    fun getVotesOnStagedCourseProgramme(programmeId: Int, stageId: Int): Int

    fun updateVotesOnStagedCourseProgramme(programmeId: Int, stageId: Int, votes: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion>

    fun getVersionOfSpecificCourse(courseId: Int, version: Int): Optional<CourseVersion>

    fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeVersion>

    fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): Optional<CourseProgrammeVersion>

    fun createCourseProgrammeVersion(courseProgrammeVersion: CourseProgrammeVersion): CourseProgrammeVersion

    fun createCourseVersion(courseVersion: CourseVersion): CourseVersion

    fun deleteVersionOfCourse(courseId: Int, version: Int): Int

    fun deleteAllVersionsOfCourse(courseId: Int): Int

    fun deleteSpecificVersionOfCourseProgramme(programmeId: Int, courseId: Int, version: Int): Int

    /**
     * Report entity queries
     */

    fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport>

    fun getSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Optional<CourseProgrammeReport>

    fun getAllReportsOnCourse(courseId: Int): List<CourseReport>

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): Optional<CourseReport>

    fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, courseProgrammeReport: CourseProgrammeReport): CourseProgrammeReport

    fun reportCourse(courseId: Int, courseReport: CourseReport): CourseReport

    fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun deleteReportOnCourse(reportId: Int): Int

    fun deleteAllReportsOnCourse(courseId: Int): Int

    fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun getVotesOnReportedCourse(reportId: Int): Int

    fun updateVotesOnReportedCourse(reportId: Int, votes: Int): Int

    fun getVotesOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun updateVotesOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, votes: Int): Int

}