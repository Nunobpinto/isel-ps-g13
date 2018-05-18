package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.ProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Programme
import isel.leic.ps.eduWikiAPI.domain.model.staging.ProgrammeStage

interface ProgrammeService {
    fun createProgramme(input: ProgrammeInputModel)
    fun getSpecificProgramme(programmeId: Int): Programme
    fun createStagedProgramme(programme: ProgrammeInputModel)
    fun getSpecificStagedProgramme(stageId: Int) : ProgrammeStage
    fun createProgrammeFromStaged(stageId: Int)
    fun getStagedProgrammes() : List<ProgrammeStage>
}