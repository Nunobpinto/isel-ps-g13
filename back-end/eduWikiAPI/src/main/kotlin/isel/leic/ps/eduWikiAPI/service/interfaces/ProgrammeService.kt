package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.ProgrammeInputModel
import isel.leic.ps.eduWikiAPI.domain.model.Programme

interface ProgrammeService {
    fun createProgramme(input: ProgrammeInputModel)
    fun getSpecificProgramme(programmeId: Int): Programme
}