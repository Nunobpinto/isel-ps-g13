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
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseProgrammeVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.CourseVersion
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import java.util.*

interface ProgrammeService {

    fun getAllProgrammes(): List<Programme>

    fun getSpecificProgramme(programmeId: Int): Optional<Programme>

    fun createProgramme(inputProgramme: ProgrammeInputModel): Programme

    fun createStagingProgramme(inputProgramme: ProgrammeInputModel): ProgrammeStage

    fun getSpecificStageEntryOfProgramme(stageId: Int) : Optional<ProgrammeStage>

    fun createProgrammeFromStaged(stageId: Int): Programme

    fun getAllProgrammeStageEntries() : List<ProgrammeStage>

    fun getAllReportsOfSpecificProgramme(programmeId: Int): List<ProgrammeReport>

    fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): Optional<ProgrammeReport>

    fun getAllCoursesOnSpecificProgramme(programmeId: Int): List<Course>

    fun addCourseToProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): Course

    fun voteOnProgramme(programmeId: Int, vote: VoteInputModel): Int

    fun reportProgramme(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel): ProgrammeReport

    fun voteOnReportedProgramme(programmeId: Int, reportId: Int, vote: VoteInputModel): Int

    fun updateProgrammeFromReport(programmeId: Int, reportId: Int): Programme

    fun voteOnStagedProgramme(stageId: Int, vote: VoteInputModel): Int

    fun deleteAllProgrammes(): Int

    fun deleteSpecificProgramme(programmeId: Int): Int

    fun deleteAllStagedProgrammes(): Int

    fun deleteSpecificStagedProgramme(stageId : Int): Int

    fun deleteAllReportsOnProgramme(programmeId: Int): Int

    fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int) : Int

    fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel): Programme

    fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion>

    fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): Optional<ProgrammeVersion>

    fun deleteAllProgrammeVersions(programmeId: Int): Int

    fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int): Int

    fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel): CourseProgrammeReport

    fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): Optional<Course>

    fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeVersion>

    fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): Optional<CourseProgrammeVersion>

    fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReport>

    fun getSpecificReportOfCourseOnProgramme(programmeId: Int, courseId: Int, reportId: Int): Optional<CourseProgrammeReport>

    fun voteOnCourseProgramme(programmeId: Int, courseId: Int, vote: VoteInputModel): Int

    fun createStagingCourseOnProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): CourseProgrammeStage

    fun createCourseProgrammeFromStaged(programmeId: Int, stageId: Int): Course

    fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, vote: VoteInputModel): Int

    fun voteOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int

    fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): List<CourseProgrammeStage>

    fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): Optional<CourseProgrammeStage>

    fun updateCourseProgrammeFromReport(programmeId: Int, courseId: Int, reportId: Int): Course

    fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int): Int

    fun deleteSpecificVersionOfCourseProgramme(programmeId: Int, courseId: Int, version: Int): Int

    fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun deleteSpecificStagedCourseProgramme(programmeId: Int, courseId: Int, stageId: Int): Int
}