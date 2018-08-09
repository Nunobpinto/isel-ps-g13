package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.LectureStageOutputModel

data class LectureStageCollectionOutputModel (
        val lectureStageList: List<LectureStageOutputModel>
)