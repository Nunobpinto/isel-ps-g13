package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.LectureOutputModel

data class LectureCollectionOutputModel (
        val lectureList: List<LectureOutputModel>
)