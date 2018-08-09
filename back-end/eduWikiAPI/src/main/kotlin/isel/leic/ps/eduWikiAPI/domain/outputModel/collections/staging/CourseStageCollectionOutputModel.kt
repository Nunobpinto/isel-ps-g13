package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseStageOutputModel

data class CourseStageCollectionOutputModel (
        val courseStageList: List<CourseStageOutputModel>
)