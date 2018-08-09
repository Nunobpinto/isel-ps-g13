package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.HomeworkOutputModel

data class HomeworkCollectionOutputModel (
        val homeworkList: List<HomeworkOutputModel>
)