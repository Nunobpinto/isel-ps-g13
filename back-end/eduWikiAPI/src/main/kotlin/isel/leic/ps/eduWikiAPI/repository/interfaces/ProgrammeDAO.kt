package isel.leic.ps.eduWikiAPI.repository.interfaces

import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.Vote
import isel.leic.ps.eduWikiAPI.domain.model.report.ProgrammeReport
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage
import isel.leic.ps.eduWikiAPI.domain.model.version.ProgrammeVersion
import java.util.*

interface ProgrammeDAO {

    /**
     * Main entities queries
     */

    fun getAllProgrammes(): List<Programme>

    fun getSpecificProgramme(programmeId: Int): Optional<Programme>

    fun createProgramme(programme: Programme): Programme

    fun updateProgramme(programmeId: Int, programme: Programme): Programme

    fun updateVotesOnProgramme(programmeId: Int, votes: Int): Int

    fun deleteSpecificProgramme(programmeId: Int): Int

    /**
     * Stage entities queries
     */

    fun getSpecificStageEntryOfProgramme(stageId: Int): Optional<ProgrammeStage>

    fun getAllProgrammeStageEntries(): List<ProgrammeStage>

    fun createStagingProgramme(programmeStage: ProgrammeStage): ProgrammeStage

    fun updateVotesOnStagedProgramme(stageId: Int, votes: Int): Int

    fun deleteSpecificStagedProgramme(stageId: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion>

    fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): Optional<ProgrammeVersion>

    fun createProgrammeVersion(programmeVersion: ProgrammeVersion): ProgrammeVersion

    fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int) : Int

    /**
     * Report entity queries
     */

    fun getAllReportsOfSpecificProgramme(programmeId : Int) : List<ProgrammeReport>

    fun getSpecificReportOfProgramme(programmeId : Int, reportId: Int): Optional<ProgrammeReport>

    fun reportProgramme(programmeId: Int, programmeReport: ProgrammeReport): ProgrammeReport

    fun updateVotesOnReportedProgramme(programmeId: Int, reportId: Int, votes: Int): Int

    fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int): Int

}