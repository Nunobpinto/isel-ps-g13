package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.CourseClassOutputModel

data class CourseClassCollectionOutputModel (
        val courseClasseList: List<CourseClassOutputModel>
)