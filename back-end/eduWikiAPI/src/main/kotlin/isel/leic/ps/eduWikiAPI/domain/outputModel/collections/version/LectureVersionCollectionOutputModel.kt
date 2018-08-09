package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.LectureVersionOutputModel

data class LectureVersionCollectionOutputModel (
        val lectureVersionList: List<LectureVersionOutputModel>
)