package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseProgrammeOutputModel

data class CourseProgrammeCollectionOutputModel (
        val courseProgrammeList: List<CourseProgrammeOutputModel>
)