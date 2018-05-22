package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.VoteInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Course
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion

interface ProgrammeDAO {
    /**
     * Main entities queries
     */

    fun getSpecificProgramme(programmeId: Int) : Programme

    fun getAllProgrammes() : List<Programme>

    fun deleteProgramme(programmeId: Int) : Int

    fun deleteAllProgrammes() : Int

    fun updateProgramme(programme: Programme) : Int

    fun createProgramme(programme: Programme)

    fun voteOnProgramme(programmeId: Int, vote: VoteInputModel)

    fun getCoursesOnSpecificProgramme(programmeId: Int): List<Course>

    fun addCourseToProgramme(programmeId: Int, course: Course)

    /**
     * Stage entities queries
     */
    fun getProgrammeStage(programmeId: Int) : ProgrammeStage

    fun getAllProgrammeStages() : List<ProgrammeStage>

    fun deleteProgrammeStage(stageId: Int) : Int

    fun deleteAllProgrammeStages() : Int

    fun createProgrammeStage(programmeStage: ProgrammeStage)

    fun voteOnProgrammeStage(programmeId: Int, vote: Vote)

    /**
     * Version entities queries
     */

    fun getVersionProgramme(versionProgrammeId: Int, version: Int) : ProgrammeVersion

    fun getAllVersionProgrammes() : List<ProgrammeVersion>

    fun deleteVersionProgramme(versionProgrammeId: Int, version: Int) : Int

    fun deleteAllVersionProgrammes() : Int

    fun createVersionProgramme(programmeVersion: ProgrammeVersion)

    /**
     * Report entity queries
     */

    fun getAllReportsOfProgramme(programmeId : Int) : List<ProgrammeReport>

    fun getSpecificReportOfProgramme(programmeId : Int, reportId: Int): ProgrammeReport

    fun reportProgramme(programmeId: Int, programmeReport: ProgrammeReport)

    fun deleteReportOnProgramme(programmeId: Int, reportId: Int) : Int

    fun deleteAllReportsOnProgramme(programmeId : Int) : Int

    fun deleteAllReports(): Int

    fun voteOnReportedProgramme(reportId: Int, vote: VoteInputModel)

    fun updateReportedProgramme(programme : Programme)

    fun addProgrammeVersion(programme: Programme)

}