package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.LectureOutputModel

data class LectureCollectionOutputModel (
        val lectureList: List<LectureOutputModel>
)