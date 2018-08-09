package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.HomeworkVersionOutputModel

data class HomeworkVersionCollectionOutputModel(
        val homeworkVersionList: List<HomeworkVersionOutputModel>
)