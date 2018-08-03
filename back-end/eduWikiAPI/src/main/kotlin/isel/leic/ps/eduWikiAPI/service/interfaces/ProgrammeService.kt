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
import isel.leic.ps.eduWikiAPI.domain.outputModel.CourseProgrammeOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.ProgrammeOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.CourseProgrammeReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.reports.ProgrammeReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.CourseProgrammeStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.staging.ProgrammeStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.CourseProgrammeVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.version.ProgrammeVersionOutputModel
import java.util.*

interface ProgrammeService {

    fun getAllProgrammes(): List<ProgrammeOutputModel>

    fun getSpecificProgramme(programmeId: Int): ProgrammeOutputModel

    fun createProgramme(inputProgramme: ProgrammeInputModel): ProgrammeOutputModel

    fun createStagingProgramme(inputProgramme: ProgrammeInputModel): ProgrammeStageOutputModel

    fun getSpecificStageEntryOfProgramme(stageId: Int) : ProgrammeStageOutputModel

    fun createProgrammeFromStaged(stageId: Int): ProgrammeOutputModel

    fun getAllProgrammeStageEntries() : List<ProgrammeStageOutputModel>

    fun getAllReportsOfSpecificProgramme(programmeId: Int): List<ProgrammeReportOutputModel>

    fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReportOutputModel

    fun getAllCoursesOnSpecificProgramme(programmeId: Int): List<CourseProgrammeOutputModel>

    fun addCourseToProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): CourseProgrammeOutputModel

    fun voteOnProgramme(programmeId: Int, vote: VoteInputModel): Int

    fun reportProgramme(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel): ProgrammeReportOutputModel

    fun voteOnReportedProgramme(programmeId: Int, reportId: Int, vote: VoteInputModel): Int

    fun updateProgrammeFromReport(programmeId: Int, reportId: Int): ProgrammeOutputModel

    fun voteOnStagedProgramme(stageId: Int, vote: VoteInputModel): Int

    fun deleteAllProgrammes(): Int

    fun deleteSpecificProgramme(programmeId: Int): Int

    fun deleteAllStagedProgrammes(): Int

    fun deleteSpecificStagedProgramme(stageId : Int): Int

    fun deleteAllReportsOnProgramme(programmeId: Int): Int

    fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int) : Int

    fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel): ProgrammeOutputModel

    fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersionOutputModel>

    fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): ProgrammeVersionOutputModel

    fun deleteAllProgrammeVersions(programmeId: Int): Int

    fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int): Int

    fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel): CourseProgrammeReportOutputModel

    fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): CourseProgrammeOutputModel

    fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeVersionOutputModel>

    fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): CourseProgrammeVersionOutputModel

    fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): List<CourseProgrammeReportOutputModel>

    fun getSpecificReportOfCourseOnProgramme(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeReportOutputModel

    fun voteOnCourseProgramme(programmeId: Int, courseId: Int, vote: VoteInputModel): Int

    fun createStagingCourseOnProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel): CourseProgrammeStageOutputModel

    fun createCourseProgrammeFromStaged(programmeId: Int, stageId: Int): CourseProgrammeOutputModel

    fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, vote: VoteInputModel): Int

    fun voteOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, vote: VoteInputModel): Int

    fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): List<CourseProgrammeStageOutputModel>

    fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): CourseProgrammeStageOutputModel

    fun updateCourseProgrammeFromReport(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeOutputModel

    fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int): Int

    fun deleteSpecificVersionOfCourseProgramme(programmeId: Int, courseId: Int, version: Int): Int

    fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int): Int

    fun deleteSpecificStagedCourseProgramme(programmeId: Int, courseId: Int, stageId: Int): Int
}