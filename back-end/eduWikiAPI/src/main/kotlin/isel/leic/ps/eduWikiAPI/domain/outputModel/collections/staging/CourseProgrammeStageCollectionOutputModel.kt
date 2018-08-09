package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseProgrammeStageOutputModel

data class CourseProgrammeStageCollectionOutputModel (
        val courseProgrammeStageList: List<CourseProgrammeStageOutputModel>
)