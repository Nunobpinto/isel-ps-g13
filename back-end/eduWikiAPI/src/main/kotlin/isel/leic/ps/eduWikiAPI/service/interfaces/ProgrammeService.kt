package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
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

    fun addCourseToProgramme(programmeId: Int, course: Course)

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

    fun partialUpdateOnStagedProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel)

    fun getAllVersions(programmeId: Int): List<ProgrammeVersion>

    fun getVersion(programmeId: Int, versionId: Int): ProgrammeVersion

    fun deleteAllVersions(programmeId: Int): Int

    fun deleteSpecificVersion(programmeId: Int, versionId: Int): Int

}