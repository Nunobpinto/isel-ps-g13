package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ProgrammeVersionOutputModel

data class ProgrammeVersionCollectionOutputModel (
        val programmeVersionList: List<ProgrammeVersionOutputModel>
)