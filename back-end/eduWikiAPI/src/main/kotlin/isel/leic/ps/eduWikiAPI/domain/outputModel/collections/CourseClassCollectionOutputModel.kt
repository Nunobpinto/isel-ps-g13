package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseClassOutputModel

data class CourseClassCollectionOutputModel (
        val courseClassList: List<CourseClassOutputModel>
)