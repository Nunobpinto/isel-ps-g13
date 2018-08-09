package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.HomeworkStageOutputModel

data class HomeworkStageCollectionOutputModel (
     val homeworkStageList: List<HomeworkStageOutputModel>
)