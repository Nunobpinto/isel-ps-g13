package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion

interface ProgrammeDAO {
    /**
     * Main entities queries
     */

    fun getProgramme(programmeId: Int) : Programme

    fun getAllProgrammes() : List<Programme>

    fun deleteProgramme(programmeId: Int) : Int

    fun deleteAllProgrammes() : Int

    fun updateProgramme(programme: Programme) : Int

    fun createProgramme(programme: Programme)

    fun voteOnProgramme(programmeId: Int, voteType: Int)

    /**
     * Stage entities queries
     */
    fun getProgrammeStage(programmeId: Int) : ProgrammeStage

    fun getAllProgrammeStages() : List<ProgrammeStage>

    fun deleteProgrammeStage(stageId: Int) : Int

    fun deleteAllProgrammeStages() : Int

    fun createProgrammeStage(programmeStage: ProgrammeStage)

    fun voteOnProgrammeStage(programmeId: Int, voteType: Int)

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

    fun reportProgramme(programmeReport: ProgrammeReport)

    fun deleteReportOnProgramme(reportId: Int) : Int

    fun deleteAllReportsOnProgramme(programmeId : Int) : Int

    fun deleteAllReports(): Int

}