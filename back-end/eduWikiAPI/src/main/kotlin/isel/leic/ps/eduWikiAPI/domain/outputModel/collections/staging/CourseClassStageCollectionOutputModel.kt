package isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseClassStageOutputModel

data class CourseClassStageCollectionOutputModel (
        val courseClassStageList: List<CourseClassStageOutputModel>
)