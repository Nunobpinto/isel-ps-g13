package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ProgrammeOutputModel

data class ProgrammeCollectionOutputModel (
        val programmeList: List<ProgrammeOutputModel>
)