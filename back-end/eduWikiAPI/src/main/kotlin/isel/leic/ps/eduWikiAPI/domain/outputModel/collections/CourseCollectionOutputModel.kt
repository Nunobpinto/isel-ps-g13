package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseOutputModel

data class CourseCollectionOutputModel (
        val courseList: List<CourseOutputModel>
)