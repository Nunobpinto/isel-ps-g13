package isel.leic.ps.eduWikiAPI.domain.outputModel.collections

import isel.leic.ps.eduWikiAPI.domain.outputModel.CourseOutputModel

data class CourseCollectionOutputModel (
        val courseList: List<CourseOutputModel>
)