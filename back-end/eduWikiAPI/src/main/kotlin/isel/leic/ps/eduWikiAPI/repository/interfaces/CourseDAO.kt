package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.enums.CourseMiscUnitType
import isel.leic.ps.eduWikiAPI.domain.model.*
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseReport
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

    fun getClassesOfSpecificCourseInTerm(courseId: Int, termId: Int): List<Class>

    fun getSpecificClassOfSpecificCourseInTerm(courseId: Int, termId: Int, classId: Int): Optional<Class>

    fun updateCourse(course: Course): Course

    fun createCourse(course: Course): Course

    fun deleteSpecificCourse(courseId: Int): Int

    fun updateVotesOnCourse(courseId: Int, votes: Int): Int

    fun createCourseTerm(courseId: Int, termId: Int, timestamp: Timestamp): CourseTerm

    /**
     * Stage entities queries
     */

    fun getAllCourseStageEntries(): List<CourseStage>

    fun getCourseSpecificStageEntry(courseStageId: Int): Optional<CourseStage>

    fun createStagingCourse(courseStage: CourseStage): CourseStage

    fun deleteStagedCourse(courseStageId: Int): Int

    fun updateVotesOnStagedCourse(stageId: Int, votes: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfSpecificCourse(courseId: Int): List<CourseVersion>

    fun getVersionOfSpecificCourse(courseId: Int, version: Int): Optional<CourseVersion>

    fun createCourseVersion(courseVersion: CourseVersion): CourseVersion

    /**
     * Report entity queries
     */

    fun getAllReportsOnCourse(courseId: Int): List<CourseReport>

    fun getSpecificReportOfCourse(courseId: Int, reportId: Int): Optional<CourseReport>

    fun reportCourse(courseId: Int, courseReport: CourseReport): CourseReport

    fun deleteReportOnCourse(reportId: Int): Int

    fun updateVotesOnReportedCourse(reportId: Int, votes: Int): Int

    /**
     * Course Programme entity queries
     */

    fun getAllCoursesOnSpecificProgramme(programmeId: Int): List<CourseProgramme>

    fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Optional<CourseProgramme>

    fun updateCourseProgramme(programmeId: Int, courseId: Int, course: CourseProgramme): CourseProgramme

    fun addCourseToProgramme(programmeId: Int, courseProgramme: CourseProgramme): CourseProgramme

    fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int): Int

    fun updateVotesOnCourseProgramme(programmeId: Int, courseId: Int, votes: Int) : Int

    fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): List<CourseProgrammeStage>

    fun getSpecificStagedCourseProgramme(programmeId: Int, stageId: Int): Optional<CourseProgrammeStage>

    fun createStagingCourseOfProgramme(courseProgrammeStage: CourseProgrammeStage): CourseProgrammeStage

    fun deleteStagedCourseProgramme(stageId: Int): Int

    fun deleteSpecificStagedCourseProgramme(programmeId: Int, stageId: Int): Int

    fun updateVotesOnStagedCourseProgramme(programmeId: Int, stageId: Int, votes: Int): Int

    fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeVersion>

    fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): Optional<CourseProgrammeVersion>

    fun createCourseProgrammeVersion(courseProgrammeVersion: CourseProgrammeVersion): CourseProgrammeVersion

    fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport>

    fun getSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Optional<CourseProgrammeReport>

    fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, courseProgrammeReport: CourseProgrammeReport): CourseProgrammeReport

    fun deleteReportOnCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun updateVotesOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, votes: Int): Int

    /**
     * Course Misc Unit Queries
     */

    fun createCourseMiscUnit(courseId: Int, termId: Int, miscType: CourseMiscUnitType): CourseMiscUnit

    fun deleteSpecificCourseMiscUnitEntry(courseId: Int, termId: Int, courseMiscUnitId: Int): Int

    fun deleteAllCourseMiscUnitsFromTypeOfCourseInTerm(courseId: Int, termId: Int, miscType: CourseMiscUnitType): Int

    fun createStagingCourseMiscUnit(courseId: Int, termId: Int, miscType: CourseMiscUnitType): CourseMiscUnitStage

    fun deleteSpecificStagedCourseMiscUnitEntry(courseId: Int, termId: Int, stageId: Int): Int

    fun getCourseByLogId(logId: Int): Optional<Course>

    fun getCourseReportByLogId(logId: Int): Optional<CourseReport>

    fun getCourseStageByLogId(logId: Int): Optional<CourseStage>

    fun getCourseProgrammeByLogId(logId: Int): Optional<CourseProgramme>

    fun getCourseProgrammeReportByLogId(logId: Int): Optional<CourseProgrammeReport>

    fun getCourseProgrammeStageByLogId(logId: Int): Optional<CourseProgrammeStage>

}