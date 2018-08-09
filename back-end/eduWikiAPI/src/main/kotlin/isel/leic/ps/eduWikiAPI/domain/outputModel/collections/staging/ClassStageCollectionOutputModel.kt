package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.ClassStageOutputModel

data class ClassStageCollectionOutputModel (
        val classStageList: List<ClassStageOutputModel>
)