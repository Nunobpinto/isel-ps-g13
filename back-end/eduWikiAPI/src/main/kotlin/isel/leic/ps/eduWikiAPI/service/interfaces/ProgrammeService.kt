package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.CourseProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.CourseProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion

interface ProgrammeService {

    fun createProgramme(inputProgramme: ProgrammeInputModel)

    fun getSpecificProgramme(programmeId: Int): Programme

    fun createStagingProgramme(inputProgramme: ProgrammeInputModel)

    fun getSpecificStagedProgramme(stageId: Int) : ProgrammeStage

    fun createProgrammeFromStaged(stageId: Int)

    fun getStagedProgrammes() : List<ProgrammeStage>

    fun getAllProgrammes(): List<Programme>

    fun getAllReportsOfProgramme(programmeId: Int): List<ProgrammeReport>

    fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReport

    fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course>

    fun addCourseToProgramme(programmeId: Int, input: CourseProgrammeInputModel): Int

    fun voteOnProgramme(programmeId: Int, inputVote: VoteInputModel)

    fun reportProgramme(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel)

    fun voteOnReportedProgramme(reportId: Int, inputVote: VoteInputModel)

    fun updateReportedProgramme(programmeId: Int, reportId: Int)

    fun voteOnStagedProgramme(stageId: Int, inputVote: VoteInputModel)

    fun deleteAllProgrammes()

    fun deleteSpecificProgramme(programmeId: Int): Int

    fun deleteAllStagedProgrammes()

    fun deleteStagedProgramme(stageId : Int) : Int

    fun deleteAllReportsOnProgramme(programmeId: Int)

    fun deleteReportOnProgramme(programmeId: Int, reportId: Int) : Int

    fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel)

    fun getAllVersions(programmeId: Int): List<ProgrammeVersion>

    fun getVersion(programmeId: Int, versionId: Int): ProgrammeVersion

    fun deleteAllVersions(programmeId: Int): Int

    fun deleteSpecificVersion(programmeId: Int, versionId: Int): Int

    fun reportCourseOnProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel): Int

    fun getSpecificCourseOnSpecificProgramme(programmeId: Int, courseId: Int): Course

    fun getAllVersionsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseVersion>

    fun getSpecificVersionOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, versionId: Int): CourseVersion

    fun getAllReportsOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport>

    fun getSpecificReportOfCourseOnSpecificProgramme(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeReport

    fun voteOnCourseProgramme(programmeId: Int, courseId: Int, inputVote: VoteInputModel): Int

    fun createStagedCourseOfProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): Int

    fun createCourseProgrammeFromStaged(programmeId: Int, stageId: Int): Int

    fun voteOnCourseProgrammeStaged(programmeId: Int, stageId: Int, inputVote: VoteInputModel): Int

    fun voteOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, inputVote: VoteInputModel): Int

    fun getStagedCoursesOfProgramme(programmeId: Int): List<CourseProgrammeStage>

    fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): CourseProgrammeStage

    fun updateReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun deleteSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Int

    fun deleteSpecificVersionOfCourseOfProgramme(programmeId: Int, courseId: Int, versionId: Int): Int

    fun deleteSpecificReportOfCourseOfProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun deleteSpecificStagedCourseOfProgramme(programmeId: Int, courseId: Int, stageId: Int): Int
}