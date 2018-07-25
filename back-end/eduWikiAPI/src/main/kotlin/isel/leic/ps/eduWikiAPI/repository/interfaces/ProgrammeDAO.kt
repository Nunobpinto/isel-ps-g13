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

    fun deleteAllProgrammes(): Int

    fun deleteSpecificProgramme(programmeId: Int): Int

    fun updateProgramme(programmeId: Int, programme: Programme): Optional<Programme>

    fun createProgramme(programme: Programme): Optional<Programme>

    fun voteOnProgramme(programmeId: Int, vote: Vote): Int

    /**
     * Stage entities queries
     */

    fun getSpecificStageEntryOfProgramme(stageId: Int): Optional<ProgrammeStage>

    fun getAllProgrammeStageEntries(): List<ProgrammeStage>

    fun deleteAllStagedProgrammes(): Int

    fun createStagingProgramme(programmeStage: ProgrammeStage): Optional<ProgrammeStage>

    fun voteOnStagedProgramme(stageId: Int, vote: Vote): Int

    fun deleteSpecificStagedProgramme(stageId: Int): Int

    /**
     * Version entities queries
     */

    fun getAllVersionsOfProgramme(programmeId: Int): List<ProgrammeVersion>

    fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): Optional<ProgrammeVersion>

    fun deleteSpecificProgrammeVersion(programmeId: Int, version: Int) : Int

    fun deleteAllProgrammeVersions(programmeId: Int) : Int

    fun createProgrammeVersion(programme: Programme): Optional<ProgrammeVersion>

    /**
     * Report entity queries
     */

    fun getAllReportsOfSpecificProgramme(programmeId : Int) : List<ProgrammeReport>

    fun getSpecificReportOfProgramme(programmeId : Int, reportId: Int): Optional<ProgrammeReport>

    fun reportProgramme(programmeId: Int, programmeReport: ProgrammeReport): Optional<ProgrammeReport>

    fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int): Int

    fun deleteAllReportsOnProgramme(programmeId : Int): Int

    fun voteOnReportedProgramme(programmeId: Int, reportId: Int, vote: Vote): Int



}