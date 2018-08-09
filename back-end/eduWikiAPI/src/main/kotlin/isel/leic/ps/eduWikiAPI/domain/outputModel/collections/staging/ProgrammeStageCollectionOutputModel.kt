package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.ProgrammeStageOutputModel

data class ProgrammeStageCollectionOutputModel (
        val programmeStageList: List<ProgrammeStageOutputModel>
)