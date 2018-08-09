package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ExamVersionOutputModel

data class ExamVersionCollectionOutputModel (
        val examVersionList: List<ExamVersionOutputModel>
)