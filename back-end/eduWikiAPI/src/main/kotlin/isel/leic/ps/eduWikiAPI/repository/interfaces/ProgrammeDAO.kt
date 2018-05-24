package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion

interface ProgrammeDAO {
    /**
     * Main entities queries
     */

    fun getSpecificProgramme(programmeId: Int) : Programme

    fun getAllProgrammes() : List<Programme>

    fun deleteSpecificProgramme(programmeId: Int)

    fun deleteAllProgrammes()

    fun updateProgramme(programmeId: Int, programme: Programme)

    fun createProgramme(programme: Programme)

    fun voteOnProgramme(programmeId: Int, inputVote: VoteInputModel)

    fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course>

    fun addCourseToProgramme(programmeId: Int, course: Course)

    /**
     * Stage entities queries
     */

    fun getProgrammeStage(programmeId: Int) : ProgrammeStage

    fun getAllProgrammeStages() : List<ProgrammeStage>

    fun deleteAllStagedProgrammes()

    fun createProgrammeStage(programmeStage: ProgrammeStage)

    fun voteOnStagedProgramme(programmeId: Int, inputVote: VoteInputModel)

    fun updateStagedProgramme(programmeId: Int, programme: ProgrammeStage)

    fun deleteStagedProgramme(stageId: Int)

    /**
     * Version entities queries
     */

    fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion>

    fun getSpecificVersionOfProgramme(programmeId: Int, versionId: Int): ProgrammeVersion

    fun deleteVersionProgramme(versionProgrammeId: Int, version: Int) : Int

    fun deleteAllVersionsOfProgramme(programmeId: Int) : Int

    fun addToProgrammeVersion(programme: Programme)

    /**
     * Report entity queries
     */

    fun getAllReportsOfProgramme(programmeId : Int) : List<ProgrammeReport>

    fun getSpecificReportOfProgramme(programmeId : Int, reportId: Int): ProgrammeReport

    fun reportProgramme(programmeId: Int, programmeReport: ProgrammeReport)

    fun deleteReportOnProgramme(programmeId: Int, reportId: Int)

    fun deleteAllReportsOnProgramme(programmeId : Int)

    fun voteOnReportedProgramme(reportId: Int, inputVote: VoteInputModel)


}